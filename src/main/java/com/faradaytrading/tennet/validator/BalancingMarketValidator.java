package com.faradaytrading.tennet.validator;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.*;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.BalancingMarketDocument;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.Point;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.SeriesPeriod;
import com.faradaytrading.tennet.message.acknowledgement.AcknowledgementMessage;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BalancingMarketValidator {

    ObjectFactory objectFactory = new ObjectFactory();

    Map<String, List<String>> versionCache = new HashMap<>();

    public AcknowledgementMessage acknowledgeMarketDocument(BalancingMarketDocument input){
        boolean valid = true;
        AcknowledgementMarketDocument output = objectFactory.createAcknowledgementMarketDocument();

        String MRID = input.getMRID();
        String revisionNumber = input.getRevisionNumber();

        List<String> revisions = versionCache.get(MRID);
        if(revisions == null){
            revisions = new ArrayList<>();
            versionCache.put(MRID, revisions);
        }
        if(revisions.isEmpty() || !revisions.contains(revisionNumber)){
            revisions.add(revisionNumber);
        } else {
            Reason reason = createReason("999", "TEN-100023 DocumentVersion Received  is lower than DocumentVersion received earlier");
            output.getReasons().add(reason);
        }

        output.setMRID(MRID);
        output.setCreatedDateTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        PartyIDString senderPartyIDString = objectFactory.createPartyIDString();
        senderPartyIDString.setValue(input.getReceiverMarketParticipantMRID().getValue());
        senderPartyIDString.setCodingScheme(input.getReceiverMarketParticipantMRID().getCodingScheme());
        output.setSenderMarketParticipantMRID(senderPartyIDString);
        output.setSenderMarketParticipantMarketRoleType(input.getReceiverMarketParticipantMarketRoleType());

        PartyIDString receiverPartyIDString = objectFactory.createPartyIDString();
        receiverPartyIDString.setValue(input.getSenderMarketParticipantMRID().getValue());
        receiverPartyIDString.setCodingScheme(input.getSenderMarketParticipantMRID().getCodingScheme());
        output.setReceiverMarketParticipantMRID(receiverPartyIDString);
        output.setReceiverMarketParticipantMarketRoleType(input.getSenderMarketParticipantMarketRoleType());

        output.setReceivedMarketDocumentMRID(input.getMRID());
        output.setReceivedMarketDocumentType(input.getType());
        output.setReceivedMarketDocumentCreatedDateTime(input.getCreatedDateTime());

        List<Reason> reasons = output.getReasons();

        for(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.TimeSeries inputTS : input.getTimeSeries()){
            TimeSeries outputTS = validateTimeSeries(inputTS);
            if(outputTS != null){
                output.getRejectedTimeSeries().add(outputTS);
                outputTS.getReasons().forEach(reason -> reasons.add(reason));
            }
        }
        if(reasons.isEmpty()){
            reasons.add(createReason("A01", "Message fully accepted"));
        } else {
            reasons.add(createReason("A02", "Message fully rejected"));
        }

        return new AcknowledgementMessage(output, valid);
    }

    protected TimeSeries validateTimeSeries(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.TimeSeries input){
        TimeSeries output = null;

        output = objectFactory.createTimeSeries();
        output.setMRID(input.getMRID());

        SeriesPeriod inputSP = input.getPeriods().get(0);
        //TEN-100150:  Invalid timeInterval in timeSeries
        String start = inputSP.getTimeInterval().getStart();
        String end = inputSP.getTimeInterval().getEnd();

        long pointsSize = calculatePeriodPoints(start, end);

        if(pointsSize == 0){
            Reason reason = createReason("999", "TEN-100150:  Invalid timeInterval in timeSeries %s".formatted(input.getMRID()));
            output.getReasons().add(reason);
        }

        //TEN-100122 Business day too far into the future/Gate not open
        if(timeintervalInFuture(inputSP.getTimeInterval())){
            Reason reason = createReason("999", "TEN-100122 Business day too far into the future/Gate not open");
            output.getReasons().add(reason);
        }

        //Size should be 96 +/- 4 (depending on summer/winter time
        if(inputSP.getPoints().size() != pointsSize){
            Reason reason = createReason("A49", "Position inconsistency");
            output.getReasons().add(reason);
        } else {
            for(int i = 0; i < pointsSize; i++){
                Point point = inputSP.getPoints().get(i);
                if(point.getPosition() != i + 1){
                    Reason reason = createReason("A49", "Position inconsistency");
                    output.getReasons().add(reason);
                    break;
                }
            }
        }
        if(output.getReasons().size() == 0){
            //No errors in TimeSeries, returning null
            return null;
        }
        return output;
    }

    private Reason createReason(String code, String reasonText){
        Reason reason = objectFactory.createReason();
        reason.setCode(code);
        reason.setText(reasonText);
        return reason;
    }

    protected long calculatePeriodPoints(String start, String end){
        ZoneId zoneId = ZoneId.of("Europe/Amsterdam");
        // Define the formatter to parse date strings
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        // Parse date strings to ZonedDateTime
        ZonedDateTime dateTime1 = ZonedDateTime.parse(start, formatter);
        ZonedDateTime dateTime2 = ZonedDateTime.parse(end, formatter);

        boolean startInDST = zoneId.getRules().isDaylightSavings(dateTime1.toInstant());
        boolean endInDST = zoneId.getRules().isDaylightSavings(dateTime2.toInstant());

        long expectedPeriodPoints = 96;
        if(startInDST && !endInDST){
            expectedPeriodPoints = 100;
        } else if (!startInDST && endInDST){
            expectedPeriodPoints = 92;
        }

        // Calculate the difference in hours and multiply by 4 (one period per quarter hour)
        long periodPoints = ChronoUnit.HOURS.between(dateTime1, dateTime2) * 4;

        if(periodPoints != expectedPeriodPoints){
            //TimeInterval is niet correct, return 0;
            return 0;
        }

        return periodPoints;

    }

    protected boolean timeintervalInFuture(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval interval){
        ZonedDateTime startDateTime = ZonedDateTime.parse(interval.getStart());
        return startDateTime.isAfter(ZonedDateTime.now());
    }
}
