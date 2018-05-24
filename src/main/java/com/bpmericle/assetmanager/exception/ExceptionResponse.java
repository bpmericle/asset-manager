package com.bpmericle.assetmanager.exception;

import java.util.Date;

/**
 * Simple wrapper response to return when an exception occurs.
 *
 * @author Brian Mericle
 */
public class ExceptionResponse {

    private final Date timestamp;
    private final String message;
    private final String details;

    public ExceptionResponse(final Date timestamp, final String message, final String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
