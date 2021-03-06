package com.hq.CloudPlatform.ProxyServer.exception;

public class ServiceException extends Exception {

    private static final long serialVersionUID = 819257588855553664L;

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String msg, Exception e) {
        super(msg, e);
    }

    public ServiceException(String msg) {
        super(msg);
    }
}
