package com.szs.prj.advice.exception;

public class CNotExistDataException extends RuntimeException {

    public CNotExistDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNotExistDataException(String msg) {
        super(msg);
    }

    public CNotExistDataException() {
        super();
    }

}
