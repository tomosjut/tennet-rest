package com.faradaytrading.tennet.nomination;

import com.faradaytrading.tennet.TennetSOAPClient;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.mmchub.nominations.nominations.IsAliveRequestMessage;
import com.faradaytrading.tennet.mmchub.nominations.nominations.IsAliveResponseMessage;
import com.faradaytrading.tennet.services.nomination.NominationService;
import com.faradaytrading.tennet.transformer.nomination.NominationTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Path("/schedulemarketmessage")
public class ScheduleMarketMessageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleMarketMessageResource.class);
    ApplicationConfiguration configuration;
    NominationTransformer transformer;

    NominationService nominationService;

    @Inject
    public ScheduleMarketMessageResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.transformer = new NominationTransformer();
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
}
