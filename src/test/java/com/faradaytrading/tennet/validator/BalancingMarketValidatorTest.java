package com.faradaytrading.tennet.validator;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.AcknowledgementMarketDocument;
import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.ESMPDateTimeInterval;
import com.faradaytrading.tennet.message.acknowledgement.AcknowledgementMessage;
import com.faradaytrading.tennet.transformer.SOAPTransformer;
import com.faradaytrading.tennet.utils.XmlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

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

        //clean target dif
        Path directoryPath = Paths.get("target/acknowledgement");

        try {
            // Use Files.walk() to get all files and subdirectories in reverse order
            Files.walk(directoryPath)
                    .sorted(Comparator.reverseOrder()) // Ensures subdirectories are deleted before their parents
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            System.out.println("Deleted: " + path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + path + " due to " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    void testCorrectPreliminaryNotice() throws Exception {
        String input = readFile("AcknowledgementTest/PreliminaryNotice/input.xml");

        AcknowledgementMessage acknowledgementMessage = validator.acknowledgeMarketDocument(input);

        assertTrue(acknowledgementMessage.isValid());
        assertEquals("Message fully accepted", acknowledgementMessage.acknowledgementMarketDocument().getReasons().get(0).getText());
    }

    @Test
    @DisplayName("Response to correct final notice of a DST transition day (both)")
    void testCorrectFinalNoticeDST() throws Exception {
        String inputSummerWinter = readFile("AcknowledgementTest/FinalNoticeDST/inputSummerWinter.xml");
        String inputWinterSummer = readFile("AcknowledgementTest/FinalNoticeDST/inputWinterSummer.xml");

        AcknowledgementMessage summerWinter = validator.acknowledgeMarketDocument(inputSummerWinter);
        AcknowledgementMessage winterSummer = validator.acknowledgeMarketDocument(inputWinterSummer);

        assertTrue(summerWinter.isValid());
        assertTrue(winterSummer.isValid());
    }

    @Test
    @DisplayName("Response to an erroneous message with version number lower or equal to the message of Scenario")
    void testEroneousMessageVersionNumberLowerOrEqual() throws Exception {
        String input1 = readFile("AcknowledgementTest/ValueLowerEqual/input.xml");
        String input2 = readFile("AcknowledgementTest/ValueLowerEqual/input-2.xml");
        String input3 = readFile("AcknowledgementTest/ValueLowerEqual/input-3.xml");

        AcknowledgementMessage output1 = validator.acknowledgeMarketDocument(input1);
        AcknowledgementMessage output2 = validator.acknowledgeMarketDocument(input2);
        AcknowledgementMessage output3 = validator.acknowledgeMarketDocument(input3);

        String actualOutput1 = XmlUtils.marshal(output1.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        String actualOutput2 = XmlUtils.marshal(output2.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        String actualOutput3 = XmlUtils.marshal(output3.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);

        System.out.println(actualOutput1);
        System.out.println(actualOutput2);
        System.out.println(actualOutput3);

        assertTrue(output1.isValid());
        assertTrue(output2.isValid());
        assertFalse(output3.isValid());
    }

    @Test
    @DisplayName("Response to an erroneous message with message period in the future")
    void testErroneousMessagePeriodInFuture() throws Exception {
        String input = readFile("AcknowledgementTest/MessagePeriodInFuture/input.xml");

        AcknowledgementMessage output = validator.acknowledgeMarketDocument(input);

        String actualOutput = XmlUtils.marshal(output.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        System.out.println(actualOutput);

        assertFalse(output.isValid());
    }

    @Test
    @DisplayName("Response to an erroneous message with negative values for a volume or price time series")
    void testErroneousMessageNegativeValues() throws Exception {
        String input = readFile("AcknowledgementTest/NegativeValues/input.xml");

        AcknowledgementMessage output = validator.acknowledgeMarketDocument(input);

        String actualOutput = XmlUtils.marshal(output.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        System.out.println(actualOutput);

        assertFalse(output.isValid());
    }

    @Test
    @DisplayName("Response to incorrect message with message period longer than one day")
    void testIncorrectMessagePeriodLongerThanOneDay() throws Exception {
        String input = readFile("AcknowledgementTest/IncorrectMessagePeriod/input.xml");

        AcknowledgementMessage output = validator.acknowledgeMarketDocument(input);

        String actualOutput = XmlUtils.marshal(output.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        System.out.println(actualOutput);

        assertFalse(output.isValid());
    }

    @Test
    @DisplayName("Response to incorrect message with incorrect number of positions")
    void testIncorrectMessageIncorrectNumberOfPoints() throws Exception {
        //Setup
        String inputStringDoublePoints = readFile("AcknowledgementTest/IncorrectPositions/inputDoublePoints.xml");
        String inputStringSummerWinter = readFile("AcknowledgementTest/IncorrectPositions/inputSummerWinter.xml");
        String inputTooHighPoints = readFile("AcknowledgementTest/IncorrectPositions/inputTooHighPoints.xml");
        String expectedOutput = readFile("AcknowledgementTest/IncorrectPositions/output.xml");

        AcknowledgementMessage output1 = validator.acknowledgeMarketDocument(inputStringDoublePoints);
        AcknowledgementMessage output2 = validator.acknowledgeMarketDocument(inputStringSummerWinter);
        AcknowledgementMessage output3 = validator.acknowledgeMarketDocument(inputTooHighPoints);

        String actualOutput1 = XmlUtils.marshal(output1.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        String actualOutput2 = XmlUtils.marshal(output2.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);
        String actualOutput3 = XmlUtils.marshal(output3.acknowledgementMarketDocument(), AcknowledgementMarketDocument.class);

        System.out.println(actualOutput1);
        System.out.println(actualOutput2);
        System.out.println(actualOutput3);

        assertFalse(output1.isValid());
        assertFalse(output2.isValid());
        assertFalse(output3.isValid());
    }

}
