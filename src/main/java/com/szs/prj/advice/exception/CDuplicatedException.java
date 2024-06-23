package com.szs.prj.advice.exception;

public class CDuplicatedException extends RuntimeException {

    public CDuplicatedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CDuplicatedException(String msg) {
        super(msg);
    }

    public CDuplicatedException() {
        super();
    }
}
