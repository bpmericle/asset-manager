package com.bpmericle.assetmanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This type of exception is thrown when an issue is encountered while executing
 * functionality in the {@link AssetManagementService}.
 *
 * @author Brian Mericle
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class AssetManagerServiceException extends RuntimeException {

    /**
     * Constructs an instance of <code>NewException</code> with the specified
     * detail message.
     *
     * @param message the detail message
     */
    public AssetManagerServiceException(final String message) {
        super(message);
    }

    /**
     * Constructs an instance of <code>NewException</code> with the specified
     * detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause of the exception
     */
    public AssetManagerServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
