package com.faradaytrading.tennet.nomination;

import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.mmchub.*;
import com.faradaytrading.tennet.services.nomination.NominationService;
import com.faradaytrading.tennet.transformer.MessageTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
@Path("/schedulemarketmessage")
public class ScheduleMarketMessageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleMarketMessageResource.class);
    ApplicationConfiguration configuration;
    MessageTransformer transformer;

    NominationService nominationService;

    @Inject
    public ScheduleMarketMessageResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.transformer = new MessageTransformer();
        this.nominationService = new NominationService(configuration);
    }

    @GET
    @Path("/isalive")
    public Response isAlive(){
        try {
            IsAliveRequestMessage isAliveRequestMessage = transformer.createIsAliveRequestMessage();
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(isAliveRequestMessage, IsAliveRequestMessage.class));
            IsAliveResponseMessage isAliveResponseMessage = nominationService.isAlive(isAliveRequestMessage);
            return Response.ok(isAliveResponseMessage).build();
        } catch (Exception e){
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
    
    @POST
    @Path("/schedule")
    public Response sendSchedule(@RequestBody ScheduleMarketDocument scheduleMarketDocument,
                                 @QueryParam("carrierId") String carrierId,
                                 @QueryParam("senderId") String senderId,
                                 @QueryParam("receiverId") String receiverId) {
        try {
            String technicalMessageId = UUID.randomUUID().toString();
            String contentType = "AGGREGATED_IMBALANCE_INFORMATION";//TODO: get from wsdl
            String correlationId = UUID.randomUUID().toString();
            MessageAddressing messageAddressing = transformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);

            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(scheduleMarketDocument, ScheduleMarketDocument.class));
            ScheduleMarketDocumentResponse sendScheduleResponseMessage = nominationService.sendSchedule(scheduleMarketDocument, messageAddressing);
            return Response.ok(sendScheduleResponseMessage).build();

        } catch (Exception e) {
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
    
}
