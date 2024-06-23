package com.szs.prj.advice.exception;

public class CInvalidException extends RuntimeException {

    public CInvalidException(String msg, Throwable t) {
        super(msg, t);
    }

    public CInvalidException(String msg) {
        super(msg);
    }

    public CInvalidException() {
        super();
    }
}
