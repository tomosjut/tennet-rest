package com.faradaytrading.tennet.settlement;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._8._1.AcknowledgementMarketDocument;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.BalancingMarketDocument;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.exception.RecoverableException;
import com.faradaytrading.tennet.exception.RequestException;
import com.faradaytrading.tennet.exception.UnrecoverableException;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.message.acknowledgement.AcknowledgementMessage;
import com.faradaytrading.tennet.services.acknowledgement.SendAcknowledgementService;
import com.faradaytrading.tennet.services.settlement.GetBalancingService;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.transformer.MessageRequestTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import com.faradaytrading.tennet.validator.BalancingMarketValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.tennet.svc.sys.mmchub.common.v1.AcknowledgementMarketDocumentResponse;
import nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing;
import nl.tennet.svc.sys.mmchub.v1.GetMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Path("/getbalancing")
@ApplicationScoped
public class GetBalancingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetBalancingResource.class);
    ApplicationConfiguration configuration;
    MessageAddressingTransformer messageAddressingTransformer;
    MessageRequestTransformer messageRequestTransformer;
    BalancingMarketValidator balancingMarketValidator;
    GetBalancingService getBalancingService;
    SendAcknowledgementService sendAcknowledgementService;

    @Inject
    public GetBalancingResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();
        this.balancingMarketValidator = new BalancingMarketValidator();
        this.getBalancingService = new GetBalancingService(configuration);
        this.sendAcknowledgementService = new SendAcknowledgementService(configuration);
    }

    @GET
    @Path("/balancing")
    @Produces(MediaType.APPLICATION_XML)
    public Response getBalancingMarketDocument(@QueryParam("technicalMessageId") String technicalMessageId,
                                               @QueryParam("carrierId") String carrierId,
                                               @QueryParam("senderId") String senderId,
                                               @QueryParam("receiverId") String receiverId) {
        try {
            String contentType = "AGGREGATED_IMBALANCE_INFORMATION";
            String correlationId = UUID.randomUUID().toString();
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            String response = getBalancingService.getBalancingMarketDocument(getMessageRequest, messageAddressing);

            BalancingMarketDocument balancingMarketDocument = XmlUtils.unmarshal(response, BalancingMarketDocument.class);

            AcknowledgementMessage acknowledgementMessage = acknowledgeBalancingMarketDocument(balancingMarketDocument,
                    "",
                    "",
                    "",
                    "",
                    "");

            String acknowledgement = XmlUtils.marshal(acknowledgementMessage.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);

            if(acknowledgementMessage.isValid()){
                LOGGER.info("Acknowledgement ok: {}", acknowledgement);
                return Response.ok(XmlUtils.prettyPrintXml(response)).build();
            } else {
                LOGGER.warn("Acknowledgement not ok: {}", acknowledgement);
                return Response.status(202).entity(XmlUtils.prettyPrintXml(response)).build();
            }

        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    public AcknowledgementMessage acknowledgeBalancingMarketDocument(BalancingMarketDocument balancingMarketDocument,
                                                                     String technicalMessageId,
                                                                     String correlationId,
                                                                     String carrierId,
                                                                     String senderId,
                                                                     String receiverId) throws UnrecoverableException, RequestException, RecoverableException {
        String contentType = "ACK_AGGREGATED_IMBALANCE_INFORMATION";
        MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
        AcknowledgementMessage acknowledgementMessage = balancingMarketValidator.acknowledgeMarketDocument(balancingMarketDocument);

        String acknowledgementResponse = sendAcknowledgementService.sendAcknowledgement(acknowledgementMessage.acknowledgementMarketDocument(), messageAddressing);
        try {
            AcknowledgementMarketDocumentResponse acknowledgementMarketDocumentResponse = XmlUtils.unmarshal(acknowledgementResponse, AcknowledgementMarketDocumentResponse.class);
        } catch (Exception e){
            LOGGER.error("Sending acknowledgement failed: {}", acknowledgementResponse, e);
            throw new UnrecoverableException("Sending acknowledgement failed:");
        }
        return acknowledgementMessage;
    }
}
