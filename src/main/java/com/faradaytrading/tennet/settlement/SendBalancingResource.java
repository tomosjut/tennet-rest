package com.faradaytrading.tennet.settlement;

import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.mmchub.BalancingMarketDocument;
import com.faradaytrading.tennet.mmchub.MessageAddressing;
import com.faradaytrading.tennet.services.settlement.SendBalancingService;
import com.faradaytrading.tennet.transformer.MessageTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
@Path("/sendbalancing")
public class SendBalancingResource {


    private static final Logger LOGGER = LoggerFactory.getLogger(SendBalancingResource.class);
    ApplicationConfiguration configuration;
    MessageTransformer transformer;
    SendBalancingService sendBalancingService;

    @POST
    @Path("/send")
    public Response sendBalancing(@RequestBody BalancingMarketDocument balancingMarketDocument,
                                  @QueryParam("carrierId") String carrierId,
                                  @QueryParam("senderId") String senderId,
                                  @QueryParam("receiverId") String receiverId) {

        try {
            String technicalMessageId = UUID.randomUUID().toString();
            String contentType = "AGGREGATED_IMBALANCE_INFORMATION"; //TODO: Wat moet hier??
            String correlationId = UUID.randomUUID().toString();
            MessageAddressing messageAddressing = transformer.createMessageAddressing(carrierId, technicalMessageId, contentType, senderId, receiverId, correlationId);
            sendBalancingService.sendBalancingMarketDocument(balancingMarketDocument, messageAddressing);

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(500).entity(new ErrorResponse(500, e.getMessage())).build();
        }

    }

}
