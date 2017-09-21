package com.hq.CloudPlatform.ProxyServer.sys.proxy;

import java.io.Serializable;

/**
 * 可序列化的响应消息
 */
public class SerializableResponse implements Serializable{

    private int statusCode;

    private String statusMessage;

    private String errorMessage;

    private String version;

    private String header;

    private byte[] bodyContentBytes;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public byte[] getBodyContentBytes() {
        return bodyContentBytes;
    }

    public void setBodyContentBytes(byte[] bodyContentBytes) {
        this.bodyContentBytes = bodyContentBytes;
    }
}
