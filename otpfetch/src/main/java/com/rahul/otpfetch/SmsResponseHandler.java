package com.rahul.otpfetch;

public interface SmsResponseHandler {

    void failureToStartService(Exception e);

    void smsResponse(String message);

    void requestTimedOut();
}
