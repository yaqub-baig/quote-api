package com.autoadda.apis.quote.exception;

public class BannerRequestNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BannerRequestNotFoundException() {
        super();
    }

    public BannerRequestNotFoundException(String message) {
        super(message);
    }

    public BannerRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
