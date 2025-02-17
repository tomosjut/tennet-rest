package com.faradaytrading.tennet.settlement;

import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.BalancingMarketDocument;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.services.settlement.GetBalancingService;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.transformer.MessageRequestTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing;
import nl.tennet.svc.sys.mmchub.v1.GetMessageRequest;
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
    GetBalancingService getBalancingService;

    @Inject
    public GetBalancingResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();
        this.getBalancingService = new GetBalancingService(configuration);
    }

    @GET
    @Path("/getbalancing")
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
            BalancingMarketDocument response = getBalancingService.getBalancingMarketDocument(getMessageRequest, messageAddressing);

            String xml = XmlUtils.marshal(response, BalancingMarketDocument.class);
            return Response.ok(XmlUtils.prettyPrintXml(xml)).build();
        } catch (Exception e) {
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
}
