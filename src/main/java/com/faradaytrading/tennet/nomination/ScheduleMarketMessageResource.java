package com.faradaytrading.tennet.nomination;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.AcknowledgementMarketDocument;
import _351.iec62325.tc57wg16._451_2.scheduledocument._5._0.ScheduleMarketDocument;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.exception.UnrecoverableException;
import com.faradaytrading.tennet.mapper.ScheduleMarketMapper;
import com.faradaytrading.tennet.mapper.ScheduleMarketMapperForeign;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.message.common.*;
import com.faradaytrading.tennet.message.schedulemarket.ScheduleMarketMessage;
import com.faradaytrading.tennet.message.schedulemarket.TimeSeries;
import com.faradaytrading.tennet.services.nomination.GetReportingInformationService;
import com.faradaytrading.tennet.services.nomination.SendScheduleService;
import com.faradaytrading.tennet.transformer.IsAliveTransformer;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.transformer.MessageRequestTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    ScheduleMarketMapperForeign scheduleMarketMapperForeign;

    SendScheduleService sendScheduleService;
    GetReportingInformationService getReportingInformationService;

    public static void main(String[] args) {
        ScheduleMarketMessage scheduleMarketMessage = new ScheduleMarketMessage();
        scheduleMarketMessage.setRevisionNumber("1");
        scheduleMarketMessage.setMRID(UUID.randomUUID().toString().replace("-", ""));
        PartyIDString partyIDString = new PartyIDString();
        partyIDString.setValue("8716867111163");
        scheduleMarketMessage.setReceiverMarketParticipantMRID(partyIDString);
        ESMPDateTimeInterval timeInterval = new ESMPDateTimeInterval();
        timeInterval.setStart("2024-03-30T23:00Z");
        timeInterval.setEnd("2024-03-31T22:00Z");
        scheduleMarketMessage.setScheduleTimePeriodTimeInterval(timeInterval);

        TimeSeries ts = new TimeSeries();

        ts.setBusinessType("A20");

        ts.setMRID(UUID.randomUUID().toString().replace("-", ""));
        AreaIDString in = new AreaIDString();
        in.setValue("8720844058549");
        ts.setInDomainMRID(in);

        AreaIDString out = new AreaIDString();
        out.setValue("8716867444223");
        ts.setOutDomainMRID(out);

        SeriesPeriod sp = new SeriesPeriod();
        sp.setTimeInterval(timeInterval);

        for(int i = 1; i <= 92; i++){
            Point point = new Point();
            point.setPosition(i);
            point.setQuantity(BigDecimal.valueOf((int) ((Math.random() * (30 - 5)) + 5)).setScale(0));
            sp.getPoints().add(point);
        }

        ts.getPeriods().add(sp);

        scheduleMarketMessage.getTimeSeries().add(ts);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(scheduleMarketMessage);
            System.out.println(s);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Inject
    public ScheduleMarketMessageResource(ApplicationConfiguration configuration){
        this.configuration = configuration;

        this.isAliveTransformer = new IsAliveTransformer();
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();

        this.scheduleMarketMapper = new ScheduleMarketMapper(configuration);
        this.scheduleMarketMapperForeign = new ScheduleMarketMapperForeign(configuration);

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
            String response = sendScheduleService.isAlive(isAliveRequestMessage);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
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

            writeFile(XmlUtils.marshal(document, ScheduleMarketDocument.class), "schedule", document.getMRID(), document.getRevisionNumber());

            String response = sendScheduleService.sendSchedule(document, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
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
            ObjectMapper mapper = new ObjectMapper();
            writeJSON(mapper.writeValueAsString(scheduleMarketMessage), "schedule/foreign", technicalMessageId, "");
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
            ScheduleMarketDocument document = scheduleMarketMapperForeign.map(scheduleMarketMessage);
            writeFile(XmlUtils.marshal(document, ScheduleMarketDocument.class), "schedule/foreign", document.getMRID(), document.getRevisionNumber());

            String response = sendScheduleService.sendSchedule(document, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();

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
            String response = getReportingInformationService.getAcknowledgementMarketDocument(getMessageRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
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
            String response = getReportingInformationService.getAcknowledgementMarketDocument(getMessageRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }



    private void writeFile(String file, String archiveKey, String documentName, String revision) throws UnrecoverableException {
        try {
            String path = "%s/%s".formatted(configuration.fileArchiveBaseDir(), archiveKey);
            String fileName = "%s-%s.xml".formatted(documentName, revision);
            java.nio.file.Path filePath = Paths.get(path, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (Exception e){
            throw new UnrecoverableException(e);
        }
    }

    private void writeJSON(String file, String archiveKey, String documentName, String revision) throws UnrecoverableException {
        try {
            String path = "%s/%s".formatted(configuration.fileArchiveBaseDir(), archiveKey);
            String fileName = "%s-%s.json".formatted(documentName, revision);
            java.nio.file.Path filePath = Paths.get(path, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (Exception e){
            throw new UnrecoverableException(e);
        }
    }
    
}
