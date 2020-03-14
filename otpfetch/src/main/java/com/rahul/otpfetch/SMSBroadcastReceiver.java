package com.rahul.otpfetch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            Status status = null;
            if (extras != null) {
                status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            }

            if (status != null) {
                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        createBroadcastIntent(context, message, true);
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        createBroadcastIntent(context, null, false);
                        break;
                }
            }
        }
    }

    /**
     * Sending a broadcast to capture the SMS received
     */
    private void createBroadcastIntent(Context context, @Nullable String data, boolean isSuccess) {

        Intent intent = new Intent(SmsListener.SMS_INTENT_ACTION);
        intent.putExtra(SmsListener.TAG_MESSAGE, data);
        intent.putExtra(SmsListener.TAG_SUCCESS, isSuccess);
        context.sendBroadcast(intent);
    }
}
