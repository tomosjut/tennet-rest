package com.faradaytrading.tennet.validator;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.*;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.BalancingMarketDocument;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.FinancialPrice;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.Point;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.SeriesPeriod;
import com.faradaytrading.tennet.config.ApplicationConfiguration;
import com.faradaytrading.tennet.config.SoapApplicationConfiguration;
import com.faradaytrading.tennet.exception.UnrecoverableException;
import com.faradaytrading.tennet.message.acknowledgement.AcknowledgementMessage;
import com.faradaytrading.tennet.message.acknowledgement.ArchiveEntry;
import com.faradaytrading.tennet.transformer.SOAPTransformer;
import nl.tennet.svc.sys.mmchub.header.v1.MessageAddressing;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class BalancingMarketValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalancingMarketValidator.class);

    ApplicationConfiguration configuration;

    ObjectFactory objectFactory = new ObjectFactory();

    List<String> technicalMessageIdCache = new ArrayList<>(); //TODO: Database

    SOAPTransformer soapTransformer;

    public BalancingMarketValidator(ApplicationConfiguration configuration){
        this.configuration = configuration;
        this.soapTransformer = new SOAPTransformer(configuration);
    }

    public AcknowledgementMessage acknowledgeMarketDocument(String soapMessage) throws UnrecoverableException {
        LocalDateTime receivedDateTime = LocalDateTime.now();
        BalancingMarketDocument balancingMarketDocument = soapTransformer.getSoapBodyContent(soapMessage, BalancingMarketDocument.class);
        MessageAddressing messageAddressing = soapTransformer.getSoapMessageAddressing(soapMessage);

        boolean valid = true;
        AcknowledgementMarketDocument output = objectFactory.createAcknowledgementMarketDocument();

        List<Reason> reasons = output.getReasons();

        //Technical validations
        String technicalMessageId = messageAddressing.getTechnicalMessageId();
        if(technicalMessageIdCache.contains(technicalMessageId)){
            reasons.add(createReason("999", "TEN-500023 Unique identifier not unique or already processed"));
        } else {
            technicalMessageIdCache.add(technicalMessageId);
        }

        String senderMRID = messageAddressing.getSenderId();
        String senderMarketParticipantMRID = balancingMarketDocument.getSenderMarketParticipantMRID().getValue();

        if(!StringUtils.equals(senderMRID, senderMarketParticipantMRID)){
            reasons.add(createReason("999", "TEN-500021 – Incorrect message signing"));
        }

        if(!isValidEAN13(senderMRID)){
            reasons.add(createReason("999", "TEN-100025: Invalid (EAN13) code [code] for SenderIdentification"));
        }
        if(!"A10".equals(balancingMarketDocument.getSenderMarketParticipantMRID().getCodingScheme())){
            reasons.add(createReason("999", "TEN-100026: Invalid Coding Scheme for SenderIdentification"));
        }
        if(!configuration.knownEans().contains(senderMRID)){
            reasons.add(createReason("999", "TEN-100027: SenderIdentification unknown"));
        }

        if(!"AGGREGATED_IMBALANCE_INFORMATION".equalsIgnoreCase(messageAddressing.getContentType())){
            reasons.add(createReason("999", "TEN-500024 – Invalid content type"));
        }

        if(!"A01".equals(balancingMarketDocument.getDocStatus().getValue()) && !"A02".equals(balancingMarketDocument.getDocStatus().getValue())){
            reasons.add(createReason("999", "TEN-100050:  Incorrect Status"));
        }

        if(!"A01".equals(balancingMarketDocument.getAreaDomainMRID().getCodingScheme())){
            reasons.add(createReason("999", "TEN-100344: Invalid Coding Scheme for area_Domain.mRID"));
        }
        if(!"10YNL----------L".equals(balancingMarketDocument.getAreaDomainMRID().getValue())){
            reasons.add(createReason("999", "TEN-100345:  Invalid area_domain.mRID [area_domain.mRID"));
        }

        //Semantical validations
        String MRID = balancingMarketDocument.getMRID();
        String revisionNumber = balancingMarketDocument.getRevisionNumber();
        String archiveKey = "%s-%s".formatted(MRID, senderMarketParticipantMRID);

        //Check filesystem for previous revisions
        List<String> revisions = listFilesInDirectory(archiveKey);
        if(revisions.isEmpty()){
            writeFile(soapMessage, archiveKey, "BalancingMarketDocument", revisionNumber);
            if(!"1".equals(revisionNumber)){
                reasons.add(createReason("999", "TEN-100242: Initial Document does not have RevisionNumber 1"));
            }
        } else if(!isRevisionNumberValid(revisions, revisionNumber)) {
            reasons.add(createReason("999", "TEN-100243: RevisionNumber received  is lower than or equal to the RevisionNumber received earlier"));
        }

        if(!"A12".equals(balancingMarketDocument.getType())){
            reasons.add(createReason("999", "TEN-100024 DocumentType incorrect"));
        }

        if(!"A06".equals(balancingMarketDocument.getProcessProcessType())){
            reasons.add(createReason("A06", "ProcessType invalid"));
        }

        Reason periodTimeIntervalReason = isValidPeriodTimeInterval(balancingMarketDocument.getPeriodTimeInterval());
        if(periodTimeIntervalReason != null){
            reasons.add(periodTimeIntervalReason);
        }

        if(timeintervalInFuture(balancingMarketDocument.getPeriodTimeInterval())){
            reasons.add(createReason("999", "TEN-100122 Business day too far into the future/Gate not open"));
        }

        if(balancingMarketDocument.getReasons() != null && !balancingMarketDocument.getReasons().isEmpty()){
            _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.Reason reason = balancingMarketDocument.getReasons().get(0);
            if(!"A95".equals(reason.getCode())){
                reasons.add(createReason("999", "TEN-100038: Invalid ReasonCode"));
            }
            if(!"Aggregated imbalance information".equalsIgnoreCase(reason.getText())){
                reasons.add(createReason("999", "TEN-100109: Aggregated imbalance information is mandatory with <reason.code> A95"));
            }
        }

        output.setMRID(UUID.randomUUID().toString().replace("-", ""));
        output.setCreatedDateTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        PartyIDString senderPartyIDString = objectFactory.createPartyIDString();
        senderPartyIDString.setValue(balancingMarketDocument.getReceiverMarketParticipantMRID().getValue());
        senderPartyIDString.setCodingScheme(balancingMarketDocument.getReceiverMarketParticipantMRID().getCodingScheme());
        output.setSenderMarketParticipantMRID(senderPartyIDString);
        output.setSenderMarketParticipantMarketRoleType(balancingMarketDocument.getReceiverMarketParticipantMarketRoleType());

        PartyIDString receiverPartyIDString = objectFactory.createPartyIDString();
        receiverPartyIDString.setValue(balancingMarketDocument.getSenderMarketParticipantMRID().getValue());
        receiverPartyIDString.setCodingScheme(balancingMarketDocument.getSenderMarketParticipantMRID().getCodingScheme());
        output.setReceiverMarketParticipantMRID(receiverPartyIDString);
        output.setReceiverMarketParticipantMarketRoleType(balancingMarketDocument.getSenderMarketParticipantMarketRoleType());

        output.setReceivedMarketDocumentMRID(balancingMarketDocument.getMRID());
        output.setReceivedMarketDocumentType(balancingMarketDocument.getType());
        output.setReceivedMarketDocumentCreatedDateTime(balancingMarketDocument.getCreatedDateTime());
        output.setReceivedMarketDocumentRevisionNumber(revisionNumber);

        for(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.TimeSeries inputTS : balancingMarketDocument.getTimeSeries()){
            TimeSeries outputTS = validateTimeSeries(inputTS, balancingMarketDocument.getPeriodTimeInterval());
            if(outputTS != null){
                output.getRejectedTimeSeries().add(outputTS);
            }
        }
        if(reasons.isEmpty() && output.getRejectedTimeSeries().isEmpty()){
            reasons.add(createReason("A01", "Message fully accepted"));
        } else {
            reasons.add(createReason("A02", "Message fully rejected"));
            valid = false;
        }

        //Put in archive
        AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage(output, valid);

        return new AcknowledgementMessage(output, valid);
    }

    protected TimeSeries validateTimeSeries(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.TimeSeries input, _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval mainInterval){
        TimeSeries output = null;

        output = objectFactory.createTimeSeries();
        output.setMRID(input.getMRID());

        SeriesPeriod inputSP = input.getPeriods().get(0);
        //TEN-100150:  Invalid timeInterval in timeSeries
        String start = inputSP.getTimeInterval().getStart();
        String end = inputSP.getTimeInterval().getEnd();

        long pointsSize = calculatePeriodPoints(start, end);

        if(input.getFlowDirectionDirection() != null && !"A01".equals(input.getFlowDirectionDirection()) && !"A02".equals(input.getFlowDirectionDirection())){
            output.getReasons().add(createReason("999", "TEN-100233: Invalid flowDirection.direction %s".formatted(input.getFlowDirectionDirection())));
        }

        if(input.getQuantityMeasurementUnitName() != null && !"KWH".equals(input.getQuantityMeasurementUnitName())){
            output.getReasons().add(createReason("999", "TEN-100048: Incorrect MeasureUnit"));
        }

        if(input.getCurveType() != null && !"A01".equals(input.getCurveType())){
            output.getReasons().add(createReason("999", "TEN-100119: Invalid curve type"));
        }

        if(inputSP.getTimeInterval() != null && !areIntervalsEqual(mainInterval, inputSP.getTimeInterval())){
            output.getReasons().add(createReason("999", "TEN-100150: Invalid timeInterval in timeSeries %s".formatted(input.getMRID())));
        }

        if(input.getReasons() != null && !"PT15M".equals(inputSP.getResolution().toString())){
            output.getReasons().add(createReason("999", "TEN-100062: Incorrect resolution"));
        }

        List<Reason> pointsReasons = checkPositionPoints(inputSP, pointsSize, input.getMRID());
        output.getReasons().addAll(pointsReasons);

        if(pointsSize == 0){
            Reason reason = createReason("999", "TEN-100150: Invalid timeInterval in timeSeries %s".formatted(input.getMRID()));
            output.getReasons().add(reason);
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

    protected void validateMessageAddressing(MessageAddressing messageAddressing, List<Reason> reasons){

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

    private Reason isValidPeriodTimeInterval(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval interval){
        boolean isStartDST = isInDST(interval.getStart());
        boolean isEndDST = isInDST(interval.getEnd());

        if(isStartDST && isEndDST){
            if(!interval.getStart().endsWith("22:00Z") || !interval.getEnd().endsWith("22:00Z")){
                return createReason("999", "Invalid period.timeInterval");
            } else if(calculatePeriodPoints(interval.getStart(), interval.getEnd()) != 96){
                return createReason("999", "Period.timeInterval must cover one complete day in UTC respecting DST");
            }
        } else if (isStartDST){ //Summer to Winter
            if(!interval.getStart().endsWith("22:00Z") || !interval.getEnd().endsWith("23:00Z")){
                return createReason("999", "Invalid period.timeInterval");
            } else if(calculatePeriodPoints(interval.getStart(), interval.getEnd()) != 100){
                return createReason("999", "Period.timeInterval must cover one complete day in UTC respecting DST");
            }
        } else if(isEndDST){ //Winter to summer
            if(!interval.getStart().endsWith("23:00Z") || !interval.getEnd().endsWith("22:00Z")){
                return createReason("999", "Invalid period.timeInterval");
            } else if(calculatePeriodPoints(interval.getStart(), interval.getEnd()) != 92){
                return createReason("999", "Period.timeInterval must cover one complete day in UTC respecting DST");
            }
        } else {
            if(!interval.getStart().endsWith("23:00Z") || !interval.getEnd().endsWith("23:00Z")){
                return createReason("999", "Invalid period.timeInterval");
            } else if(calculatePeriodPoints(interval.getStart(), interval.getEnd()) != 96){
                return createReason("999", "Period.timeInterval must cover one complete day in UTC respecting DST");
            }
        }
        return null;
    }


    private boolean isInDST(String input){
        ZoneId zoneId = ZoneId.of("Europe/Amsterdam");
        // Define the formatter to parse date strings
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        // Parse date strings to ZonedDateTime
        ZonedDateTime dateTime1 = ZonedDateTime.parse(input, formatter);
        return zoneId.getRules().isDaylightSavings(dateTime1.toInstant());
    }

    private boolean isMessageIdemPotent(LocalDateTime first, LocalDateTime second){
        if(first.isBefore(second) && Duration.between(first, second).toMinutes() <= 15){
            return true;
        }
        return false;
    }

    private boolean isValidEAN13(String ean) {
        if (ean == null || ean.length() != 13 || !ean.matches("\\d+")) {
            return false;
        }

        int sum = 0;

        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(ean.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        int lastDigit = Character.getNumericValue(ean.charAt(12));

        return checkDigit == lastDigit;
    }

    protected boolean timeintervalInFuture(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval interval){
        ZonedDateTime startDateTime = ZonedDateTime.parse(interval.getStart());
        return startDateTime.isAfter(ZonedDateTime.now());
    }

    private boolean areIntervalsEqual(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval first, _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval second){
        return first.getStart().equals(second.getStart()) && first.getEnd().equals(second.getEnd());
    }

    private boolean hasTimeSeriesReasons(List<_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.TimeSeries> timeSeries){
        for(_351.iec62325.tc57wg16._451_6.balancingdocument._4._5.TimeSeries ts : timeSeries) {
            if (!ts.getReasons().isEmpty()) {
                return true;
            }
            for (SeriesPeriod sp : ts.getPeriods()) {
                for (Point point : sp.getPoints()) {
                    if (!point.getReasons().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<Reason> checkPositionPoints(SeriesPeriod seriesPeriod, long max, String timeSeriesMRID){
        List<Reason> reasons = new ArrayList<>();
        List<Integer> positions = new ArrayList<>();
        for(Point point : seriesPeriod.getPoints()){
            if(point.getPosition() > max){
                reasons.add(createReason("999", "Position %s is outside timeInterval in timeSeries %s".formatted(point.getPosition(), timeSeriesMRID)));
            }
            if(positions.contains(point.getPosition())){
                reasons.add(createReason("999", "TEN-100141:  Position %s occurs more than once in a timeInterval in timeSeries %s".formatted(point.getPosition(), timeSeriesMRID)));
            }
            //check quantities
            if(point.getQuantity() != null && point.getQuantity().compareTo(BigDecimal.ZERO) < 0){
                reasons.add(createReason("999", "TEN-100065: Incorrect Qty (format)"));
            }

            if(point.getFinancialPrices() != null && !point.getFinancialPrices().isEmpty()){
                FinancialPrice fp = point.getFinancialPrices().get(0);
                if(fp.getAmount() != null && fp.getAmount().compareTo(BigDecimal.ZERO) < 0){
                    reasons.add(createReason("999", "TEN-100274: Invalid price amount"));
                }
            }

            positions.add(point.getPosition());
        }
        //Check for missing points
        for(int i = 1; i <= max; i++){
            if (!positions.contains(i)){
                reasons.add(createReason("999", "Position %s missing in timeSeries %s".formatted(i, timeSeriesMRID)));
            }
        }
        return reasons;
    }

    private boolean isRevisionNumberValid(List<String> files, String revisionString) throws UnrecoverableException {
        Integer inputRevisionNumber = Integer.parseInt(revisionString);
        for(String s : files){
            String revision = s.replace("BalancingMarketDocument", "").replace("-", "").replace(".xml", "");
            if(inputRevisionNumber <= Integer.parseInt(revision)){
                LOGGER.warn("Revision number lower or equal to previous revision");
                return false;
            }
        }
        return true;
    }

    private List<String> listFilesInDirectory(String archiveKey) throws UnrecoverableException {
        String path = "%s/%s".formatted(configuration.fileArchiveBaseDir(), archiveKey);
        Path dirPath = Paths.get(path);
        List<String> fileNames = new ArrayList<>();
        if(!Files.exists(dirPath)){
            LOGGER.info("No directory found for {}", archiveKey);
            return fileNames;
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)){
            for(Path p : stream){
                if(Files.isRegularFile(p)){
                    fileNames.add(p.getFileName().toString());
                }
            }
        } catch (IOException e) {
            throw new UnrecoverableException(e);
        }
        return fileNames;
    }

    private String readFile(String archiveKey, String documentName, String revision){
        String path = "%s/%s".formatted(configuration.fileArchiveBaseDir(), archiveKey);
        String fileName = "%s-%s.xml".formatted(documentName, revision);
        Path filePath = Paths.get(path, fileName);
        try {
            return Files.readString(filePath);
        } catch (IOException e){
            LOGGER.info("File {}/{} not found", path, fileName);
            return null;
        }
    }

    private void writeFile(String file, String archiveKey, String documentName, String revision) throws UnrecoverableException {
        try {
            String path = "%s/%s".formatted(configuration.fileArchiveBaseDir(), archiveKey);
            String fileName = "%s-%s.xml".formatted(documentName, revision);
            Path filePath = Paths.get(path, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (Exception e){
            throw new UnrecoverableException(e);
        }
    }
}
