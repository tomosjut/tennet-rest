package com.faradaytrading.tennet.nomination;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.AcknowledgementMarketDocument;
import _351.iec62325.tc57wg16._451_2.scheduledocument._5._0.ScheduleMarketDocument;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.mapper.ScheduleMarketMapper;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.message.schedulemarket.ScheduleMarketMessage;
import com.faradaytrading.tennet.services.nomination.GetReportingInformationService;
import com.faradaytrading.tennet.services.nomination.SendScheduleService;
import com.faradaytrading.tennet.transformer.IsAliveTransformer;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.transformer.MessageRequestTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveRequestMessage;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveResponseMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.tennet.svc.sys.mmchub.common.v1.ScheduleMarketDocumentResponse;
import nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing;
import nl.tennet.svc.sys.mmchub.v1.GetMessageRequest;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
@Path("/schedulemarketmessage")
public class ScheduleMarketMessageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleMarketMessageResource.class);
    ApplicationConfiguration configuration;
    IsAliveTransformer isAliveTransformer;
    MessageAddressingTransformer messageAddressingTransformer;
    MessageRequestTransformer messageRequestTransformer;

    ScheduleMarketMapper scheduleMarketMapper;

    SendScheduleService sendScheduleService;
    GetReportingInformationService getReportingInformationService;

    @Inject
    public ScheduleMarketMessageResource(ApplicationConfiguration configuration){
        this.configuration = configuration;

        this.isAliveTransformer = new IsAliveTransformer();
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();

        this.scheduleMarketMapper = new ScheduleMarketMapper();

        this.sendScheduleService = new SendScheduleService(configuration);
        this.getReportingInformationService = new GetReportingInformationService(configuration);
    }

    @GET
    @Path("/isalive")
    @Produces(MediaType.APPLICATION_XML)
    public Response isAlive(){
        try {
            IsAliveRequestMessage isAliveRequestMessage = isAliveTransformer.createIsAliveRequestMessage();
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(isAliveRequestMessage, IsAliveRequestMessage.class));
            IsAliveResponseMessage isAliveResponseMessage = sendScheduleService.isAlive(isAliveRequestMessage);
            String xml = XmlUtils.marshal(isAliveResponseMessage, IsAliveResponseMessage.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();
        } catch (Exception e){
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @POST
    @Path("/schedulemarketdocument/domestic")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response sendScheduleMarketDocumentDomestic(@RequestBody ScheduleMarketMessage scheduleMarketMessage,
                                                       @QueryParam("carrierId") String carrierId,
                                                       @QueryParam("senderId") String senderId,
                                                       @QueryParam("receiverId") String receiverId) {
        try {
            String technicalMessageId = UUID.randomUUID().toString();
            String contentType = "DOMESTIC_ENERGY_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
            ScheduleMarketDocument document = scheduleMarketMapper.map(scheduleMarketMessage);
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(document, ScheduleMarketDocument.class));
            ScheduleMarketDocumentResponse response = sendScheduleService.sendSchedule(document, messageAddressing);
            LOGGER.info("ResponseMessage: " + response);
            String xml = XmlUtils.marshal(response, ScheduleMarketDocumentResponse.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @POST
    @Path("/schedulemarketdocument/foreign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response sendScheduleMarketDocumentForeign(@RequestBody ScheduleMarketMessage scheduleMarketMessage,
                                                       @QueryParam("carrierId") String carrierId,
                                                       @QueryParam("senderId") String senderId,
                                                       @QueryParam("receiverId") String receiverId) {
        try {
            String technicalMessageId = UUID.randomUUID().toString();
            String contentType = "FOREIGN_ENERGY_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
            ScheduleMarketDocument document = scheduleMarketMapper.map(scheduleMarketMessage);
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(document, ScheduleMarketDocument.class));
            ScheduleMarketDocumentResponse response = sendScheduleService.sendSchedule(document, messageAddressing);

            String xml = XmlUtils.marshal(response, ScheduleMarketDocumentResponse.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();

        } catch (Exception e) {
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @GET
    @Path("/acknowledgement/domestic")
    @Produces(MediaType.APPLICATION_XML)
    public Response getAcknowledgementMarketDocumentDomestic(@QueryParam("technicalMessageId") String technicalMessageId,
                                                             @QueryParam("carrierId") String carrierId,
                                                             @QueryParam("senderId") String senderId,
                                                             @QueryParam("receiverId") String receiverId) {
        try {
            String contentType = "ACK_DOMESTIC_ENERGY_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            AcknowledgementMarketDocument response = getReportingInformationService.getAcknowledgementMarketDocument(getMessageRequest, messageAddressing);

            String xml = XmlUtils.marshal(response, AcknowledgementMarketDocument.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @GET
    @Path("/acknowledgement/foreign")
    @Produces(MediaType.APPLICATION_XML)
    public Response getAcknowledgementMarketDocumentForeign(@QueryParam("technicalMessageId") String technicalMessageId,
                                                            @QueryParam("carrierId") String carrierId,
                                                            @QueryParam("senderId") String senderId,
                                                            @QueryParam("receiverId") String receiverId) {
        try {
            String contentType = "ACK_FOREIGN_ENERGY_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            AcknowledgementMarketDocument response = getReportingInformationService.getAcknowledgementMarketDocument(getMessageRequest, messageAddressing);

            String xml = XmlUtils.marshal(response, AcknowledgementMarketDocument.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
    
}
