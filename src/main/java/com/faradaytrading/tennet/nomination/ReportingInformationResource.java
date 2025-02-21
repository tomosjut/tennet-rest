package com.faradaytrading.tennet.nomination;

import _351.iec62325.tc57wg16._451_n.reportinginformationdocument._2._1.ReportingInformationMarketDocument;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.message.reportinginformation.ReportingInformationMarketMessage;
import com.faradaytrading.tennet.services.nomination.SendReportingInformationService;
import com.faradaytrading.tennet.transformer.IsAliveTransformer;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveRequestMessage;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveResponseMessage;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.tennet.svc.sys.mmchub.common.v1.ReportingInformationMarketDocumentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

//@Path("/reportinginformation")
//@ApplicationScoped
public class ReportingInformationResource {


    private static final Logger LOGGER = LoggerFactory.getLogger(ReportingInformationResource.class);
    ApplicationConfiguration configuration;
    IsAliveTransformer isAliveTransformer;
    MessageAddressingTransformer messageAddressingTransformer;
    SendReportingInformationService sendReportingInformationService;

    @Inject
    public ReportingInformationResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.isAliveTransformer = new IsAliveTransformer();
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.sendReportingInformationService = new SendReportingInformationService(configuration);
    }

    @GET
    @Path("/isalive")
    @Produces(MediaType.APPLICATION_XML)
    public Response isAlive(){
        try {
            IsAliveRequestMessage isAliveRequestMessage = isAliveTransformer.createIsAliveRequestMessage();
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(isAliveRequestMessage, IsAliveRequestMessage.class));
            String response = sendReportingInformationService.isAlive(isAliveRequestMessage);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e){
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @POST
    @Path("/reportinginformation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response sendReportingInformation(ReportingInformationMarketMessage reportingInformationMarketMessage,
                                  @QueryParam("carrierId") String carrierId,
                                  @QueryParam("senderId") String senderId,
                                  @QueryParam("receiverId") String receiverId) {

        try {
            //Validate input
//            validateBalancingMarketDocument(balancingMarketDocument);

            ReportingInformationMarketDocument document = new ReportingInformationMarketDocument();//TODO mapping

            String technicalMessageId = UUID.randomUUID().toString();
            String contentType = "AGGREGATED_IMBALANCE_INFORMATION";//TODO?/???
            String correlationId = UUID.randomUUID().toString();
            nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
            String response = sendReportingInformationService.sendReportingInformation(document, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e) {
            return Response.status(500).entity(new ErrorResponse(500, e.getMessage())).build();
        }

    }

}
