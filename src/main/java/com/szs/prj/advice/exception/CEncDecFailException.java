package com.szs.prj.advice.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serial;

public class CEncDecFailException extends RuntimeException {

    public CEncDecFailException(String msg, Throwable t) {
        super(msg, t);
    }

    public CEncDecFailException(String msg) {
        super(msg);
    }

    public CEncDecFailException() {
        super();
    }
}
