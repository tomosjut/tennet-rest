package com.faradaytrading.tennet.message;

public record ErrorResponse(int code, String reason) {
}
