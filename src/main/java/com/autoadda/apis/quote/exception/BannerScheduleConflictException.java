package com.autoadda.apis.quote.exception;

public class BannerScheduleConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BannerScheduleConflictException() {
        super();
    }

    public BannerScheduleConflictException(String message) {
        super(message);
    }

    public BannerScheduleConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
