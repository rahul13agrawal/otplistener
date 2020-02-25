package com.rahul.otpfetch;

public interface ResponseHandler {
    void failureToStartService(Exception e);

    void completeSMS(String message);

    void timedOut();
}
