package com.faradaytrading.tennet.message.acknowledgement;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._8._1.AcknowledgementMarketDocument;

public record AcknowledgementMessage(AcknowledgementMarketDocument acknowledgementMarketDocument, boolean isValid) {

}
