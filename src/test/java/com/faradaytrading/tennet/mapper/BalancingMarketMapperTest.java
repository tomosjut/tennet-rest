package com.faradaytrading.tennet.mapper;

import _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.BalancingMarketDocument;
import com.faradaytrading.tennet.message.balancingmarket.BalancingMarketMessage;
import com.faradaytrading.tennet.utils.XmlUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEvaluators;

import static com.faradaytrading.tennet.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BalancingMarketMapperTest {

    BalancingMarketMapper mapper = new BalancingMarketMapper();
    ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    @DisplayName("Imbalance Volume (A20)")
    void testImbalanceVolume() throws Exception {
        String inputString = readFile("BalancingMarketMapperTest/ImbalanceVolume(A20)/input.json");
        String expectedOutputString = readFile("BalancingMarketMapperTest/ImbalanceVolume(A20)/output.xml");

        BalancingMarketMessage input = jsonMapper.readValue(inputString, BalancingMarketMessage.class);

        BalancingMarketDocument output = mapper.map(input);

        String actualOutputString = XmlUtils.marshal(output, BalancingMarketDocument.class);

        Diff diff = DiffBuilder.compare(Input.fromString(expectedOutputString))
                .withTest(Input.fromString(actualOutputString))
                .withDifferenceEvaluator(DifferenceEvaluators.Default)
                .ignoreWhitespace()
                .build();

        diff.getDifferences().forEach(difference -> System.out.println(difference.toString()));

        assertFalse(diff.hasDifferences());
    }



//    private static DifferenceEvaluator ignoringNamespaces() {
//        return (comparison, outcome) -> {
//            if (comparison.getControlDetails().getTarget().getNodeType() == Node.ATTRIBUTE_NODE
//                    && comparison.getControlDetails().getXPath().contains("@xmlns")) {
//                return ComparisonResult.EQUAL;
//            }
//            return DifferenceEvaluators.Default.evaluate(comparison, outcome);
//        };
//    }
}
