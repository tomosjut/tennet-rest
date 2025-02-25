package com.faradaytrading.tennet;

import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.message.ErrorResponse;
import com.faradaytrading.tennet.services.ListMessageMetadataService;
import com.faradaytrading.tennet.transformer.IsAliveTransformer;
import com.faradaytrading.tennet.transformer.MessageAddressingTransformer;
import com.faradaytrading.tennet.transformer.MessageRequestTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveRequestMessage;
import eu.tennet.cdm.tennet.tennetservice.message.v2.IsAliveResponseMessage;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing;
import nl.tennet.svc.sys.mmchub.v1.ListMessageMetadataRequest;
import nl.tennet.svc.sys.mmchub.v1.ListMessageMetadataResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/listmessages")
public class ListMessageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListMessageResource.class);
    ApplicationConfiguration configuration;
    MessageAddressingTransformer messageAddressingTransformer;
    MessageRequestTransformer messageRequestTransformer;
    IsAliveTransformer isAliveTransformer;
    ListMessageMetadataService listMessageMetadataService;

    @Inject
    public ListMessageResource(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.messageAddressingTransformer = new MessageAddressingTransformer();
        this.messageRequestTransformer = new MessageRequestTransformer();
        this.isAliveTransformer = new IsAliveTransformer();
        this.listMessageMetadataService = new ListMessageMetadataService(configuration);
    }



    @GET
    @Path("/isalive")
    @Produces(MediaType.APPLICATION_XML)
    public Response isAlive(){
        try {
            IsAliveRequestMessage isAliveRequestMessage = isAliveTransformer.createIsAliveRequestMessage();
            LOGGER.info("RequestMessage: {}", XmlUtils.marshal(isAliveRequestMessage, IsAliveRequestMessage.class));
            String response = listMessageMetadataService.isAlive(isAliveRequestMessage);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e){
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }

    @GET
    @Path("/listmessagemetadata")
    @Produces(MediaType.APPLICATION_XML)
    public Response listMessageMetadata(@QueryParam("carrierId") String carrierId,
                                        @QueryParam("senderId") String senderId,
                                        @QueryParam("receiverId") String receiverId,
                                        @QueryParam("contentType") String contentType,
                                        @QueryParam("correlationId") String correlationId){
        try {
            String technicalMessageId = UUID.randomUUID().toString();

            if("null".equals(correlationId)){
                correlationId = null;
            }else {
                correlationId = StringUtils.defaultIfBlank(correlationId, UUID.randomUUID().toString());
            }
            MessageAddressing messageAddressing = messageAddressingTransformer.createMessageAddressing(carrierId,
                    technicalMessageId,
                    contentType,
                    senderId,
                    receiverId,
                    correlationId);

            List<String> contentTypeList = new ArrayList<>();
            contentTypeList.add(contentType);

            ListMessageMetadataRequest listMessageMetadataRequest = messageRequestTransformer.createListMessageMetadataRequest(correlationId,receiverId, contentTypeList, 10);
            String response = listMessageMetadataService.listMessageMetadata(listMessageMetadataRequest, messageAddressing);
            return Response.ok(XmlUtils.prettyPrintXml(response)).build();
        } catch (Exception e){
            LOGGER.error("Something went wrong: {}", e.getMessage(), e);
            return Response.status(400).entity(new ErrorResponse(400, e.getMessage())).build();
        }
    }
}
