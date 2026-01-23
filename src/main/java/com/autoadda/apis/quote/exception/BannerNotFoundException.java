package com.autoadda.apis.quote.exception;

public class BannerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BannerNotFoundException() {
        super();
    }

    public BannerNotFoundException(String message) {
        super(message);
    }

    public BannerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
