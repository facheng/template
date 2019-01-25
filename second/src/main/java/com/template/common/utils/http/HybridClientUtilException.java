package com.template.common.utils.http;

/**
 * Created by facheng on 19-1-22.
 */
public class HybridClientUtilException extends RuntimeException {

    public HybridClientUtilException() {
        super();
    }

    public HybridClientUtilException(String message) {
        super(message);
    }

    public HybridClientUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public HybridClientUtilException(Throwable cause) {
        super(cause);
    }

    protected HybridClientUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
