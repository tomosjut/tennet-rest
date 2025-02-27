package com.faradaytrading.tennet.validator;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.AcknowledgementMarketDocument;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.BalancingMarketDocument;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval;
import com.faradaytrading.tennet.message.acknowledgement.AcknowledgementMessage;
import com.faradaytrading.tennet.transformer.SOAPTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.faradaytrading.tennet.utils.TestUtils.readConfig;
import static com.faradaytrading.tennet.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.*;

public class BalancingMarketValidatorTest {

    BalancingMarketValidator validator;
    SOAPTransformer transformer;

    @BeforeEach
    void before(){
        validator = new BalancingMarketValidator(readConfig());
        transformer = new SOAPTransformer(null);
    }

    @Test
    void testCalculatePeriodPoints(){
        String start = "2025-02-21T23:00Z";
        String end = "2025-02-22T23:00Z";

        String startMinus1 = "2025-02-21T22:00Z";
        String endMinus1 = "2025-02-22T22:00+01:00";

        String startPlus1 = "2025-02-21T22:00+01:00";
        String endPlus1 = "2025-02-22T22:00Z";

        long points = validator.calculatePeriodPoints(start, end);
        long pointsMinus1 = validator.calculatePeriodPoints(startMinus1, endMinus1);
        long pointsPlus1 = validator.calculatePeriodPoints(startPlus1, endPlus1);

        assertEquals(96, points);
        assertEquals(100, pointsPlus1);
        assertEquals(92, pointsMinus1);
    }

    @Test
    void testTimeintervalInFuture(){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        ZonedDateTime future = ZonedDateTime.now().plusDays(1);
        ESMPDateTimeInterval timeIntervalFuture = new ESMPDateTimeInterval();
        timeIntervalFuture.setStart(formatter.format(ZonedDateTime.now().plusDays(1)));
        timeIntervalFuture.setEnd(formatter.format(ZonedDateTime.now().plusDays(2)));

        ESMPDateTimeInterval timeIntervalPast = new ESMPDateTimeInterval();
        timeIntervalPast.setStart(formatter.format(ZonedDateTime.now()));
        timeIntervalPast.setEnd(formatter.format(ZonedDateTime.now().plusDays(1)));

        assertTrue(validator.timeintervalInFuture(timeIntervalFuture));
        assertFalse(validator.timeintervalInFuture(timeIntervalPast));
    }

    @Test
    @DisplayName("Response to correct preliminary notice")
    void testCorrectPreliminaryNotice(){

    }

    @Test
    @DisplayName("Response to correct final notice of a DST transition day (both)")
    void testCorrectFinalNoticeDST(){

    }

    @Test
    @DisplayName("Response to correct final notice with version number higher than a previously received final notice for the same day (i.e. request for postponement of allocation dates or correction of imbalance after deadline)")
    void testCorrectFinalNoticeVersionNumberHigherThanPreviously(){

    }

    @Test
    @DisplayName("Response to an erroneous message with version number lower or equal to the message of Scenario")
    void testEroneousMessageVersionNumberLowerOrEqual(){

    }

    @Test
    @DisplayName("Response to an erroneous message with message period in the future")
    void testErroneousMessagePeriodInFuture(){

    }

    @Test
    @DisplayName("Response to an erroneous message with negative values for a volume or price time series")
    void testErroneousMessageNegativeValues(){

    }

    @Test
    @DisplayName("Response to incorrect message with message period longer than one day")
    void testIncorrectMessagePeriodLongerThanOneDay(){

    }

    @Test
    @DisplayName("Response to incorrect message with incorrect number of positions")
    void testIncorrectMessageIncorrectNumberOfPositions() throws Exception {
        //Setup
        String inputString = readFile("AcknowledgementTest/IncorrectPositions/input.xml");
        String expectedOutput = readFile("AcknowledgementTest/IncorrectPositions/output.xml");

        AcknowledgementMessage output = validator.acknowledgeMarketDocument(inputString);

        String actualOutput = XmlUtils.marshal(output.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);

        System.out.println(actualOutput);
    }

}
