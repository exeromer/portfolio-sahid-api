package org.portfolio.domain;

public record ContactResponse (
    String messageId,
    String status,
    String message
){}
