package com.faradaytrading.tennet.mapper;

import _351.iec62325.tc57wg16._451_2.scheduledocument._5._0.*;
import com.faradaytrading.tennet.exception.UnrecoverableException;
import com.faradaytrading.tennet.message.schedulemarket.ScheduleMarketMessage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class ScheduleMarketMapper {

    ObjectFactory objectFactory = new ObjectFactory();

    //Defaults
    private static final String TYPE = "A01";
    private static final String PROCESS_PROCESS_TYPE = "A17";
    private static final String PROCESS_CLASSIFICATION_TYPE = "A02";
    private static final String SENDER_MARKET_ROLE_TYPE = "A08";
    private static final String SENDER_MARKET_CODING_SHEME = "A10";
    private static final String RECEIVER_MARKET_ROLE_MRID = "8716867999983";
    private static final String RECEIVER_MARKET_CODING_SHEME = "A10";
    private static final String RECEIVER_MARKET_ROLE_TYPE = "A04";
    private static final String DOMAIN_MRID = "10YNL----------L";

    //TimeSeries Defaults
    private static final String BUSINESS_TYPE = "A02";
    private static final String OBJECT_AGGREGATION = "A03";
    private static final String MEASURE_UNIT = "KWH";
    private static final String CURVE_TYPE = "A01";
    private static final String PRODUCT = "8716867000030";

    //SeriesPeriod Defaults
    private static final String RESOLUTION = "PT15M";

    public ScheduleMarketDocument map(ScheduleMarketMessage input) throws UnrecoverableException {
        ScheduleMarketDocument output = objectFactory.createScheduleMarketDocument();

        //Set defaults
        output.setType(TYPE);
        output.setProcessProcessType(PROCESS_PROCESS_TYPE);
        output.setProcessClassificationType(PROCESS_CLASSIFICATION_TYPE);
        output.setSenderMarketParticipantMarketRoleType(SENDER_MARKET_ROLE_TYPE);

        PartyIDString receiverPID = objectFactory.createPartyIDString();
        receiverPID.setValue(RECEIVER_MARKET_ROLE_MRID);
        receiverPID.setCodingScheme(RECEIVER_MARKET_CODING_SHEME);
        output.setReceiverMarketParticipantMRID(receiverPID);
        output.setReceiverMarketParticipantMarketRoleType(RECEIVER_MARKET_ROLE_TYPE);

        AreaIDString domainMRID = objectFactory.createAreaIDString();
        domainMRID.setValue(DOMAIN_MRID);
        domainMRID.setCodingScheme(input.getDomainMRID().getCodingScheme());
        output.setDomainMRID(domainMRID);

        //Set variables
        output.setMRID(input.getMRID());
        output.setRevisionNumber(input.getRevisionNumber());

        PartyIDString senderMRID = objectFactory.createPartyIDString();
        senderMRID.setValue(input.getSenderMarketParticipantMRID().getValue());
        senderMRID.setCodingScheme(input.getSenderMarketParticipantMRID().getCodingScheme());
        output.setSenderMarketParticipantMRID(senderMRID);

        output.setCreatedDateTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        ESMPDateTimeInterval scheduleTimePeriodTimeInterval = objectFactory.createESMPDateTimeInterval();
        scheduleTimePeriodTimeInterval.setStart(input.getScheduleTimePeriodTimeInterval().getStart());
        scheduleTimePeriodTimeInterval.setEnd(input.getScheduleTimePeriodTimeInterval().getEnd());
        output.setScheduleTimePeriodTimeInterval(scheduleTimePeriodTimeInterval);

        for(com.faradaytrading.tennet.message.schedulemarket.TimeSeries inputTS : input.getTimeSeries()){
            output.getTimeSeries().add(mapTimeSeries(inputTS));
        }
        
        return output;
    }

    private @NotNull TimeSeries mapTimeSeries(com.faradaytrading.tennet.message.schedulemarket.TimeSeries input) throws UnrecoverableException {
        TimeSeries output = objectFactory.createTimeSeries();

        //Map Defaults
        output.setBusinessType(BUSINESS_TYPE);
        output.setObjectAggregation(OBJECT_AGGREGATION);
        output.setMeasurementUnitName(MEASURE_UNIT);
        output.setCurveType(CURVE_TYPE);
        output.setProduct(PRODUCT);

        //Map variables
        output.setMRID(input.getMRID());
        //InDomain & OutDomain optional
        if(input.getInDomainMRID() != null && StringUtils.isNotBlank(input.getInDomainMRID().getValue())){
            AreaIDString inDomain = objectFactory.createAreaIDString();
            inDomain.setValue(input.getInDomainMRID().getValue());
            inDomain.setCodingScheme(input.getInDomainMRID().getCodingScheme());
            output.setInDomainMRID(inDomain);
        }
        if(input.getOutDomainMRID() != null && StringUtils.isNotBlank(input.getOutDomainMRID().getValue())){
            AreaIDString outDomain = objectFactory.createAreaIDString();
            outDomain.setValue(input.getOutDomainMRID().getValue());
            outDomain.setCodingScheme(input.getOutDomainMRID().getCodingScheme());
            output.setOutDomainMRID(outDomain);
        }
        PartyIDString inMarketParticipant = objectFactory.createPartyIDString();
        inMarketParticipant.setCodingScheme(input.getInMarketParticipantMRID().getCodingScheme());
        inMarketParticipant.setValue(input.getInMarketParticipantMRID().getValue());
        output.setInMarketParticipantMRID(inMarketParticipant);

        PartyIDString outMarketParticipant = objectFactory.createPartyIDString();
        outMarketParticipant.setCodingScheme(input.getOutMarketParticipantMRID().getCodingScheme());
        outMarketParticipant.setValue(input.getOutMarketParticipantMRID().getValue());
        output.setOutMarketParticipantMRID(outMarketParticipant);

        //MarketAgreement is optional
        output.setMarketAgreementMRID(input.getMarketAgreementMRID());

        output.setVersion(input.getVersion());

        for(com.faradaytrading.tennet.message.common.SeriesPeriod inputSP : input.getPeriods()){
            output.getPeriods().add(mapSeriesPeriod(inputSP));
        }

        return output;
    }



    private SeriesPeriod mapSeriesPeriod(com.faradaytrading.tennet.message.common.SeriesPeriod input) throws UnrecoverableException {

        SeriesPeriod output = objectFactory.createSeriesPeriod();
        try {
            DatatypeFactory dft = DatatypeFactory.newInstance();
            Duration resolution = dft.newDuration(RESOLUTION);
            output.setResolution(resolution);
        } catch (Exception e){
            throw new UnrecoverableException(e);
        }

        ESMPDateTimeInterval timeInterval = objectFactory.createESMPDateTimeInterval();
        timeInterval.setStart(input.getTimeInterval().getStart());
        timeInterval.setEnd(input.getTimeInterval().getEnd());
        output.setTimeInterval(timeInterval);

        for(com.faradaytrading.tennet.message.common.Point inputP : input.getPoints()){
            output.getPoints().add(mapPoint(inputP));
        }

        return output;
    }

    private Point mapPoint(com.faradaytrading.tennet.message.common.Point input){
        Point point = objectFactory.createPoint();
        point.setPosition(input.getPosition());
        point.setQuantity(input.getQuantity());
        return point;
    }
}
