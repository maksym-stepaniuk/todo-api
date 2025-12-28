package com.maksym.todoapi.exception;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> details;
    private Instant timestamp = Instant.now();

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message, Map<String, String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public String getCode() { return code; };
    public String getMessage() { return message; }
    public Map<String, String> getDetails() { return details; }
    public Instant getTimestamp() { return timestamp; }

}
