package com.szs.prj.advice.exception;

public class CNotExistUserException extends RuntimeException {

    public CNotExistUserException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNotExistUserException(String msg) {
        super(msg);
    }

    public CNotExistUserException() {
        super();
    }

}
