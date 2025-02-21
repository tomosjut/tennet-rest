package com.faradaytrading.tennet.nomination;

import _351.iec62325.tc57wg16._451_2.anomalydocument._5._0.AnomalyReportMarketDocument;
import _351.iec62325.tc57wg16._451_2.confirmationdocument._5._0.ConfirmationMarketDocument;
import _351.iec62325.tc57wg16._451_n.reportinginformationdocument._2._1.ReportingInformationMarketDocument;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.services.nomination.GetReportingInformationService;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.transformer.MessageRequestTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing;
import nl.tennet.svc.sys.mmchub.v1.GetMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Path("/reportinginformation")
@ApplicationScoped
public class GetReportingInformationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetReportingInformationResource.class);
    ApplicationConfiguration configuration;
    MessageAddressingTransformer messageAddressingTransformer;
    MessageRequestTransformer messageRequestTransformer;
    GetReportingInformationService getReportingInformationService;

    @Inject
    public GetReportingInformationResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();
        this.getReportingInformationService = new GetReportingInformationService(configuration);
    }


    @GET
    @Path("/reportinginformation")
    @Produces(MediaType.APPLICATION_XML)
    public Response getReportingInformation(@QueryParam("technicalMessageId") String technicalMessageId,
                                            @QueryParam("carrierId") String carrierId,
                                            @QueryParam("senderId") String senderId,
                                            @QueryParam("receiverId") String receiverId) {
        try {
            String contentType = "REPORTING_INFORMATION_DOMESTIC_ENERGY_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            String response = getReportingInformationService.getReportingInformationMarketDocument(getMessageRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @GET
    @Path("/anomalyreporting")
    @Produces(MediaType.APPLICATION_XML)
    public Response getAnomalyReportingDocument(@QueryParam("technicalMessageId") String technicalMessageId,
                                               @QueryParam("carrierId") String carrierId,
                                               @QueryParam("senderId") String senderId,
                                               @QueryParam("receiverId") String receiverId) {
        try {
            String contentType = "ANOMALY_DOMESTIC_ENERGY_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            String response = getReportingInformationService.getAnomalyReportMarketDocument(getMessageRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @GET
    @Path("/confirmationdocument/domestic")
    @Produces(MediaType.APPLICATION_XML)
    public Response getConfirmationDocumentDomestic(@QueryParam("technicalMessageId") String technicalMessageId,
                                                    @QueryParam("carrierId") String carrierId,
                                                    @QueryParam("senderId") String senderId,
                                                    @QueryParam("receiverId") String receiverId) {
        try {
            String contentType = "DOMESTIC_SETTLEMENT_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            String response = getReportingInformationService.getConfirmationMarketDocument(getMessageRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @GET
    @Path("/confirmationdocument/foreign")
    @Produces(MediaType.APPLICATION_XML)
    public Response getConfirmationDocumentForeign(@QueryParam("technicalMessageId") String technicalMessageId,
                                                    @QueryParam("carrierId") String carrierId,
                                                    @QueryParam("senderId") String senderId,
                                                    @QueryParam("receiverId") String receiverId) {
        try {
            String contentType = "FOREIGN_SETTLEMENT_PROGRAM";
            String correlationId = UUID.randomUUID().toString();
            GetMessageRequest getMessageRequest = messageRequestTransformer.createGetMessageRequest(technicalMessageId);
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(getMessageRequest, GetMessageRequest.class));
            String response = getReportingInformationService.getConfirmationMarketDocument(getMessageRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
}
