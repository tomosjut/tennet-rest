package com.faradaytrading.tennet.mapper;

import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.*;
import com.faradaytrading.tennet.exception.UnrecoverableException;
import com.faradaytrading.tennet.message.balancingmarket.BalancingMarketMessage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class BalancingMarketMapper {

    ObjectFactory objectFactory = new ObjectFactory();

    //Defaults
    private static final String TYPE = "A12";
    private static final String PROCESS_PROCESS_TYPE = "A06";
    private static final String SENDER_MRID_VALUE = "8716867999983";
    private static final String SENDER_MRID_CODING_SCHEME = "A10";
    private static final String SENDER_MPM_ROLETYPE = "A05";
    private static final String RECEIVER_MRID_CODING_SCHEME = "A10";
    private static final String RECEIVER_MPM_ROLETYPE = "A08";
    private static final String AREA_DOMAIN_MRID_VALUE = "10YNL----------L";
    private static final String AREA_DOMAIN_CODING_SCHEME = "A01";

    //TimeSeries Defaults
    private static final String BUSINESS_TYPE = "A02";
    private static final String QUANTITY_MEASUREMENT_UNIT_NAME = "KWH";
    private static final String CURVE_TYPE = "A01";

    //SeriesPeriod Defaults
    private static final String RESOLUTION = "PT15M";

    public BalancingMarketDocument map(BalancingMarketMessage input) throws UnrecoverableException{
        BalancingMarketDocument output = objectFactory.createBalancingMarketDocument();

        output.setMRID(UUID.randomUUID().toString());

        output.setRevisionNumber(input.getRevisionNumber());
        output.setType(TYPE);
        output.setProcessProcessType(PROCESS_PROCESS_TYPE);

        PartyIDString senderMRID = objectFactory.createPartyIDString();
        senderMRID.setValue(SENDER_MRID_VALUE);
        senderMRID.setCodingScheme(SENDER_MRID_CODING_SCHEME);
        output.setSenderMarketParticipantMRID(senderMRID);
        output.setSenderMarketParticipantMarketRoleType(SENDER_MPM_ROLETYPE);

        PartyIDString receiverMRID = objectFactory.createPartyIDString();
        receiverMRID.setValue(input.getReceiverMarketParticipantMRID().getValue());
        receiverMRID.setCodingScheme(RECEIVER_MRID_CODING_SCHEME);
        output.setReceiverMarketParticipantMRID(receiverMRID);
        output.setReceiverMarketParticipantMarketRoleType(RECEIVER_MPM_ROLETYPE);

        output.setCreatedDateTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        ActionStatus docStatus = objectFactory.createActionStatus();
        docStatus.setValue(input.getDocStatus().getValue());
        output.setDocStatus(docStatus);

        AreaIDString areaDomainMRID = objectFactory.createAreaIDString();
        areaDomainMRID.setValue(AREA_DOMAIN_MRID_VALUE);
        areaDomainMRID.setCodingScheme(AREA_DOMAIN_CODING_SCHEME);
        output.setAreaDomainMRID(areaDomainMRID);

        //TODO: map from input
//        output.setAllocationDecisionDateAndOrTimeDateTime();
        ESMPDateTimeInterval periodTimeInterval = objectFactory.createESMPDateTimeInterval();
        periodTimeInterval.setStart(input.getPeriodTimeInterval().getStart());
        periodTimeInterval.setEnd(input.getPeriodTimeInterval().getEnd());
        output.setPeriodTimeInterval(periodTimeInterval);

        for(com.faradaytrading.tennet.message.common.Reason inputR : input.getReasons()){
            output.getReasons().add(mapReason(inputR));
        }

        return output;
    }


    private @NotNull TimeSeries mapTimeSeries(com.faradaytrading.tennet.message.balancingmarket.TimeSeries input) throws UnrecoverableException {
        TimeSeries output = objectFactory.createTimeSeries();

        output.setMRID(UUID.randomUUID().toString());
        output.setBusinessType(BUSINESS_TYPE);
        output.setFlowDirectionDirection(input.getFlowDirectionDirection());
        output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
        output.setCurveType(CURVE_TYPE);
        output.setAuctionMRID(input.getAuctionMRID());

        for(com.faradaytrading.tennet.message.common.Reason inputR : input.getReasons()){
            output.getReasons().add(mapReason(inputR));
        }

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

    private Reason mapReason(com.faradaytrading.tennet.message.common.Reason input){
        Reason reason = objectFactory.createReason();
        reason.setCode(input.getCode());
        reason.setText(input.getText());
        return reason;
    }
}
