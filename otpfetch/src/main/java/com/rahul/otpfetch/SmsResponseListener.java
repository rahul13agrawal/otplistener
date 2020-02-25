package com.rahul.otpfetch;

public interface SmsResponseListener {

    void failureToStartService(Exception e);

    void smsResponse(String message);

    void requestTimedOut();
}
