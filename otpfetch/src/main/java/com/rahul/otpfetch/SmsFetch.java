package com.rahul.otpfetch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class SmsFetch {

    static String SMS_INTENT_ACTION = "SMS_INTENT_ACTION";
    static String TAG_MESSAGE = "TAG_MESSAGE";
    static String TAG_SUCCESS = "TAG_SUCCESS";
    private Context context;
    private SmsResponseListener listener;
    private BroadcastReceiver receiver;

    public SmsFetch(Context context, SmsResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void startListeningService() {

        SmsRetrieverClient client = com.google.android.gms.auth.api.phone.SmsRetriever.getClient(context /* context */);

        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SmsRetriever", "onSuccess: ");
                registerBroadcastReceiver();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SmsRetriever", "onFailure: ");
                listener.failureToStartService(e);
            }
        });
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(SMS_INTENT_ACTION);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(TAG_SUCCESS, false)) {
                    String stringExtra = intent.getStringExtra(TAG_MESSAGE);

                    handleResponse(stringExtra);
                    return;
                }

                listener.requestTimedOut();
            }
        };

        context.registerReceiver(receiver, intentFilter);
    }

    private void handleResponse(String stringExtra) {
        listener.smsResponse(stringExtra);
    }

    public void stopListeningService() {
        context.unregisterReceiver(receiver);
    }
}
