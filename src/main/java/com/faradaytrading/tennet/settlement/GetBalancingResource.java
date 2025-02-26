package com.faradaytrading.tennet.settlement;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.AcknowledgementMarketDocument;
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
import com.faradaytrading.tennet.transformer.SOAPTransformer;
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
import org.apache.commons.lang3.StringUtils;
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
    SOAPTransformer soapTransformer;

    @Inject
    public GetBalancingResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();
        this.balancingMarketValidator = new BalancingMarketValidator();
        this.getBalancingService = new GetBalancingService(configuration);
        this.sendAcknowledgementService = new SendAcknowledgementService(configuration);
        this.soapTransformer = new SOAPTransformer(configuration);
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

            return Response.ok(XmlUtils.prettyPrintXml(response)).build();

        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
    @POST
    @Path("/acknowledge")
    @Produces(MediaType.APPLICATION_XML)
    public Response acknowledgeBalancingMarketDocument(String balancingMarketDocumentSoapMessage,
                                                       @QueryParam("technicalMessageId") String technicalMessageId,
                                                       @QueryParam("carrierId") String carrierId,
                                                       @QueryParam("senderId") String senderId,
                                                       @QueryParam("receiverId") String receiverId,
                                                       @QueryParam("correlationId") String correlationId) throws UnrecoverableException, RequestException, RecoverableException {
        String contentType = "ACK_AGGREGATED_IMBALANCE_INFORMATION";
        correlationId = StringUtils.defaultIfBlank(correlationId, UUID.randomUUID().toString());
        MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
        BalancingMarketDocument balancingMarketDocument = soapTransformer.getSoapBodyContent(balancingMarketDocumentSoapMessage, BalancingMarketDocument.class);
        AcknowledgementMessage acknowledgementMessage = balancingMarketValidator.acknowledgeMarketDocument(balancingMarketDocument);

        String acknowledgement = XmlUtils.marshal(acknowledgementMessage.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        LOGGER.info("Acknowledgement Request: {}", acknowledgement);

        String acknowledgementResponse = sendAcknowledgementService.sendAcknowledgement(acknowledgementMessage.acknowledgementMarketDocument(), messageAddressing);
        LOGGER.info("Acknowledgement Response: {}", acknowledgementResponse);
        try {
            AcknowledgementMarketDocumentResponse acknowledgementMarketDocumentResponse = soapTransformer.getSoapBodyContent(acknowledgementResponse, AcknowledgementMarketDocumentResponse.class);
            //We return the Acknowledgement we have sent.
            return Response.ok(XmlUtils.prettyPrintXml(acknowledgement)).build();
        } catch (Exception e){
            LOGGER.error("Sending acknowledgement failed: {}", acknowledgementResponse, e);
            //We return the Acknowledgement we have failed to send.
            return Response.status(500).build();
        }
    }
}
