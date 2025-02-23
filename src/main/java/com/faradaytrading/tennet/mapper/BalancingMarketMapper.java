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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BalancingMarketMapper {

    ObjectFactory objectFactory = new ObjectFactory();

    public enum SETTLEMENT_TYPE{};

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
    private static final String QUANTITY_MEASUREMENT_UNIT_NAME = "KWH";
    private static final String CURVE_TYPE = "A01";

    //SeriesPeriod Defaults
    private static final String RESOLUTION = "PT15M";

    public BalancingMarketDocument map(BalancingMarketMessage input) throws UnrecoverableException{

        //Common fields
        BalancingMarketDocument output = objectFactory.createBalancingMarketDocument();

        output.setMRID(UUID.randomUUID().toString());
        //1..999
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

        output.setAllocationDecisionDateAndOrTimeDateTime(input.getAllocationDecisionDateAndOrTimeDateTime());
        ESMPDateTimeInterval periodTimeInterval = objectFactory.createESMPDateTimeInterval();
        periodTimeInterval.setStart(input.getPeriodTimeInterval().getStart());
        periodTimeInterval.setEnd(input.getPeriodTimeInterval().getEnd());
        output.setPeriodTimeInterval(periodTimeInterval);

        for(com.faradaytrading.tennet.message.balancingmarket.TimeSeries inputTS : input.getTimeSeries()){
            output.getTimeSeries().add(mapTimeSeries(inputTS));
        }

        output.getReasons().add(createReason("Aggregated imbalance information"));

        return output;
    }


    private @NotNull TimeSeries mapTimeSeries(com.faradaytrading.tennet.message.balancingmarket.TimeSeries input) throws UnrecoverableException {
        TimeSeries output = objectFactory.createTimeSeries();
        String businessType = input.getBusinessType();
        //Defaults across all TimeSeries
        output.setMRID(UUID.randomUUID().toString());
        output.setCurveType(CURVE_TYPE);
        //Specific Defaults per businessType

        //Create optional objects
        AreaIDString acquiringDomain = objectFactory.createAreaIDString();

        switch (businessType){
            case "A02" : //Settlement Program Domestic 8.1
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.getReasons().add(createReason("Settlement Program Domestic"));
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                output.setAuctionMRID(input.getAuctionMRID());
                break;
            case "A03" : // 8.2
                output.getReasons().add(createReason("Settlement Program Foreign"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                output.setAuctionMRID(input.getAuctionMRID());
                break;
            case "B22" : // 8.3
                output.getReasons().add(createReason("Regulation state"));
                break;
            case "B24" : // 8.4
                output.getReasons().add(createReason("Imbalance price TenneT sells"));
                output.setCurrencyUnitName("EUR");
                output.setPriceMeasurementUnitName("MWH");
                break;
            case "B25" : // 8.5
                output.getReasons().add(createReason("Imbalance price TenneT buys"));
                output.setCurrencyUnitName("EUR");
                output.setPriceMeasurementUnitName("MWH");
                break;
            case "A14" : // 8.6
                output.getReasons().add(createReason("Allocation per grid area"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setAuctionMRID(input.getAuctionMRID());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                //ConnectingDomain
                AreaIDString connectingDomain = objectFactory.createAreaIDString();
                connectingDomain.setCodingScheme(input.getConnectingDomainMRID().getCodingScheme());
                connectingDomain.setValue(input.getConnectingDomainMRID().getValue());
                output.setConnectingDomainMRID(connectingDomain);

                output.setAuctionMRID(input.getAuctionMRID());
                break;
            case "A20" : // 8.7
                output.getReasons().add(createReason("NL imbalance"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                output.setAuctionMRID(input.getAuctionMRID());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);

                output.setCurrencyUnitName("EUR");
                output.setPriceMeasurementUnitName("MWH");
                break;
            case "A19" : // 8.8
                output.getReasons().add(createReason("Total imbalance adjustment for all relevant products"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                break;
            case "A96" : // 8.9
                output.getReasons().add(createReason("Imbalance Adjustment aFRR"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                break;
            case "A97" : // 8.10, 8.11
                output.getReasons().add(createReason("Imbalance Adjustment mFRRda specific product"));//TODO: A97 is dubbel :(
                output.getReasons().add(createReason("Imbalance Adjustment mFRR standard product"));//TODO: A97 is dubbel :(
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                break;
            case "A85" : // 8.12
                output.getReasons().add(createReason("Imbalance Adjustment Redispatch (ROP, block bids)"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                break;
            case "A15" : // 8.13
                output.getReasons().add(createReason("Market Compensated Losses"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                break;
            case "C28" : // 8.14
                output.getReasons().add(createReason("Imbalance Adjustment SST"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                break;
            case "B03" : // 8.15
                output.getReasons().add(createReason("Imbalance Adjustment BRP4GOPACS"));
                output.setQuantityMeasurementUnitName(QUANTITY_MEASUREMENT_UNIT_NAME);
                output.setFlowDirectionDirection(input.getFlowDirectionDirection());
                //AcquiringDomain
                acquiringDomain.setCodingScheme(input.getAcquiringDomainMRID().getCodingScheme());
                acquiringDomain.setValue(input.getAcquiringDomainMRID().getValue());
                output.setAcquiringDomainMRID(acquiringDomain);
                break;
            default:
                throw new UnrecoverableException("Unexpected businessType");

        }
        //Mapping from input
        output.setBusinessType(businessType);

        for(com.faradaytrading.tennet.message.balancingmarket.SeriesPeriod inputSP : input.getPeriods()){
            output.getPeriods().add(mapSeriesPeriod(inputSP, businessType));
        }

        return output;
    }



    private SeriesPeriod mapSeriesPeriod(com.faradaytrading.tennet.message.balancingmarket.SeriesPeriod input, String businessType) throws UnrecoverableException {

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

        for(com.faradaytrading.tennet.message.balancingmarket.Point inputP : input.getPoints()){
            output.getPoints().add(mapPoint(inputP, businessType));
        }

        return output;
    }

    private Point mapPoint(com.faradaytrading.tennet.message.balancingmarket.Point input, String businessType){
        Point output = objectFactory.createPoint();
        output.setPosition(input.getPosition());

        //Quantity, doesn't apply to B22, B24 and B25
        List<String> nonQuantityTypes = Arrays.asList("B22", "B24", "B25");
        if(!nonQuantityTypes.contains(businessType)) {
            output.setQuantity(input.getQuantity());
        }

        //Amount, only applies to A20(Imbalance)
        if("A20".equals(businessType)){
            for (com.faradaytrading.tennet.message.balancingmarket.FinancialPrice inputFP : input.getFinancialPrices()){
                FinancialPrice outputFP = objectFactory.createFinancialPrice();
                outputFP.setAmount(inputFP.getAmount());
                outputFP.setDirection(inputFP.getDirection());
                output.getFinancialPrices().add(outputFP);
            }
        }
        //Flow direction
        if("B22".equals(businessType)){
            output.setFlowDirectionDirection(input.getFlowDirectionDirection());
        }
        return output;
    }

    private Reason createReason(String reasonText){
        Reason reason = objectFactory.createReason();
        reason.setCode("A95");
        reason.setText(reasonText);
        return reason;
    }
}
