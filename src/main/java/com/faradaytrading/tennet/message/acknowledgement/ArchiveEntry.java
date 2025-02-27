package com.faradaytrading.tennet.message.acknowledgement;

import _351.iec62325.tc57wg16._451_1.acknowledgementdocument._7._0.AcknowledgementMarketDocument;

import java.time.LocalDateTime;

public record ArchiveEntry (String soapMessage, AcknowledgementMessage acknowledgementMessage, LocalDateTime receivedDateTime) {

}
