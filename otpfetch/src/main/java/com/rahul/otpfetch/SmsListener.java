package com.rahul.otpfetch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class SmsListener {

    static String SMS_INTENT_ACTION = "SMS_INTENT_ACTION";
    static String TAG_MESSAGE = "TAG_MESSAGE";
    static String TAG_SUCCESS = "TAG_SUCCESS";

    private Context context;
    private SmsResponseHandler handler;
    private BroadcastReceiver receiver;

    public SmsListener(Context context, SmsResponseHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void startService() {

        SmsRetrieverClient client = com.google.android.gms.auth.api.phone.SmsRetriever.getClient(context);

        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Registering the Broadcast receiver once the Task has started.
                registerBroadcastReceiver();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //If the Task could not start, will throw error.
                handler.failureToStartService(e);
            }
        });
    }

    private void registerBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter(SMS_INTENT_ACTION);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getBooleanExtra(TAG_SUCCESS, false)) {
                    String retrievedText = intent.getStringExtra(TAG_MESSAGE);

                    handleResponse(retrievedText);
                    return;
                }

                handler.requestTimedOut();
            }
        };

        context.registerReceiver(receiver, intentFilter);
    }

    private void handleResponse(String retrievedText) {
        handler.smsResponse(retrievedText);
    }

    /**
     * Call this method on onDestroy so that Broadcast Receiver can be unregistered
     */
    public void stopService() {
        context.unregisterReceiver(receiver);
    }
}
