package com.faradaytrading.tennet.settlement;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.AcknowledgementMarketDocument;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.BalancingMarketDocument;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.message.balancingmarket.BalancingMarketMessage;
import com.faradaytrading.tennet.services.nomination.GetReportingInformationService;
import com.faradaytrading.tennet.services.settlement.SendBalancingService;
import com.faradaytrading.tennet.transformer.IsAliveTransformer;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.transformer.MessageRequestTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveRequestMessage;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveResponseMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.tennet.svc.sys.mmchub.common.v1.BalancingMarketDocumentResponse;
import nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing;
import nl.tennet.svc.sys.mmchub.v1.GetMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
@Path("/settlement")
public class BalancingMarketDocumentResource {


    private static final Logger LOGGER = LoggerFactory.getLogger(BalancingMarketDocumentResource.class);
    ApplicationConfiguration configuration;
    IsAliveTransformer isAliveTransformer;
    MessageAddressingTransformer messageAddressingTransformer;
    MessageRequestTransformer messageRequestTransformer;
    SendBalancingService sendBalancingService;
    GetReportingInformationService getReportingInformationService;

    @Inject
    public BalancingMarketDocumentResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.isAliveTransformer = new IsAliveTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.sendBalancingService = new SendBalancingService(configuration);
        this.getReportingInformationService = new GetReportingInformationService(configuration);
    }

    @GET
    @Path("/isalive")
    @Produces(MediaType.APPLICATION_XML)
    public Response isAlive(){
        try {
            IsAliveRequestMessage isAliveRequestMessage = isAliveTransformer.createIsAliveRequestMessage();
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(isAliveRequestMessage, IsAliveRequestMessage.class));
            String response = sendBalancingService.isAlive(isAliveRequestMessage);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e){
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @POST
    @Path("/sendBalancing")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response sendBalancing(@Valid BalancingMarketMessage balancingMarketMessage,
                                  @QueryParam("carrierId") String carrierId,
                                  @QueryParam("senderId") String senderId,
                                  @QueryParam("receiverId") String receiverId) {

        try {

            String technicalMessageId = UUID.randomUUID().toString();
            String contentType = "AGGREGATED_IMBALANCE_INFORMATION";
            String correlationId = UUID.randomUUID().toString();
            BalancingMarketDocument document = new BalancingMarketDocument();//todo mapping
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
            String response = sendBalancingService.sendBalancingMarketDocument(document, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();

        } catch (Exception e) {
            return Response.status(500).entity(new ErrorResponse(500, e.getMessage())).build();
        }

    }

    @GET
    @Path("/acknowledgement")
    @Produces(MediaType.APPLICATION_XML)
    public Response getAcknowledgementMarketDocumentForeign(@QueryParam("technicalMessageId") String technicalMessageId,
                                                            @QueryParam("carrierId") String carrierId,
                                                            @QueryParam("senderId") String senderId,
                                                            @QueryParam("receiverId") String receiverId,
                                                            @QueryParam("correlationId") String correlationId) {
        try {
            String contentType = "ACK_AGGREGATED_IMBALANCE_INFORMATION";
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            String response = getReportingInformationService.getAcknowledgementMarketDocument(getMessageRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

}
