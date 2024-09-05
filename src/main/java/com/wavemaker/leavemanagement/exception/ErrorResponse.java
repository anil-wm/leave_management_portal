package com.wavemaker.leavemanagement.exception;


public class ErrorResponse extends RuntimeException {
    private int statusCode;
    private String errorMessage;

    public ErrorResponse(String errorMessage, int statusCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public ErrorResponse(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}