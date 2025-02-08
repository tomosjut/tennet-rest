package com.faradaytrading.tennet.settlement;

import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.mmchub.settlement.*;
import com.faradaytrading.tennet.nomination.ScheduleMarketMessageResource;
import com.faradaytrading.tennet.services.settlement.GetBalancingService;
import com.faradaytrading.tennet.transformer.settlement.GetBalancingTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/getbalancing")
public class GetBalancingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleMarketMessageResource.class);
    ApplicationConfiguration configuration;
    GetBalancingTransformer transformer;
    GetBalancingService getBalancingService;

    @Inject
    public GetBalancingResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.transformer = new GetBalancingTransformer();
        this.getBalancingService = new GetBalancingService(configuration);
    }

    @GET
    @Path("/isalive")
    public Response isAlive(){
        try {
            IsAliveRequestMessage isAliveRequestMessage = transformer.createIsAliveRequestMessage();
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(isAliveRequestMessage, IsAliveRequestMessage.class));
            IsAliveResponseMessage isAliveResponseMessage = getBalancingService.isAlive(isAliveRequestMessage);
            String xml = XmlUtils.marshal(isAliveResponseMessage, IsAliveResponseMessage.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();
        } catch (Exception e){
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @GET
    @Path("/listmessagemetadata")
    @Produces(MediaType.APPLICATION_XML)
    public Response listMessageMetadata(@QueryParam("carrierId") String carrierId, @QueryParam("senderId") String senderId, @QueryParam("receiverId") String receiverId){
        try {
            String technicalMessageId = UUID.randomUUID().toString();
            String contentType = "AGGREGATED_IMBALANCE_INFORMATION";
            String correlationId = UUID.randomUUID().toString();
            MessageAddressing messageAddressing = transformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            List<String> contentTypeList = new ArrayList<>();
            contentTypeList.add(contentType);

            ListMessageMetadataRequest listMessageMetadataRequest = transformer.createListMessageMetadataRequest(correlationId,receiverId, contentTypeList, 10);
            ListMessageMetadataResponse listMessageMetadataResponse = getBalancingService.listMessageMetadata(listMessageMetadataRequest, messageAddressing);
            String xml = XmlUtils.marshal(listMessageMetadataResponse, ListMessageMetadataResponse.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();
        } catch (Exception e){
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
}
