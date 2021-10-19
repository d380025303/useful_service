
package com.daixinmini.base.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

public class BizException extends RuntimeException {
    public static final String UNKNOWN = "UNKNOWN";

    private final String errorCode;

    public BizException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public BizException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }

    public BizException(String errorMessage) {
        super(errorMessage);
        this.errorCode = UNKNOWN;
    }

    public BizException(String errorMessagePattern, Object... args) {
        super(MessageFormat.format(errorMessagePattern, args));
        this.errorCode = UNKNOWN;
    }

    public BizException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = UNKNOWN;
    }

    public String detailMessage() {
        Throwable cause = this.getCause();
        if (cause == null) {
            cause = this;
        }
        StringWriter stringWriter = new StringWriter();
        cause.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public String errorMessage() {
        return this.getMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }
}
