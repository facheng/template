package com.template.common.utils.http;

/**
 * Created by facheng on 19-1-22.
 */
public class HttpClientUtilException extends RuntimeException {

    public HttpClientUtilException() {
        super();
    }

    public HttpClientUtilException(String message) {
        super(message);
    }

    public HttpClientUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpClientUtilException(Throwable cause) {
        super(cause);
    }

    protected HttpClientUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
