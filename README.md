# OtpListener
This will make auto detecting OTP through SMS easier to implement.

Latest version : [![](https://jitpack.io/v/rahul13agrawal/OtpListener.svg)](https://jitpack.io/#rahul13agrawal/OtpListener)


## Setup:
Add below in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and Add the dependency:

```gradle
dependencies {
    implementation 'com.github.rahul13agrawal:OtpListener:1.1'
}
```

## Implementation

```
SmsListener smsListener = new SmsListener(this /*Context*/, this /*Listener*/);
//This will start SMSRetrieverClient and Broadcast Receiver.
smsListener.startService();
```

### Callbacks:
```
    @Override
    public void failureToStartService(Exception e) {
        //If error to start SMSRetrieverClient, will get it here.
    }

    @Override
    public void smsResponse(String message) {
        //The complete SMS will be provided here. Write logic to fetch OTP.
    }

    @Override
    public void otpResponse(String otp) {
        //Fetch otp from the SMS
    }
    
    @Override
    public void requestTimedOut() {
        //If OTP not fetched and Timeout response received in Broadcast receiver, This will be called.
    }
```
Once you receive the SMS, do not forget to ***stopService***
```
   smsListener.stopService();
```
