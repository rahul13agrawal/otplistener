package com.rahul.otpretriever;

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
import com.rahul.otpfetch.ResponseHandler;

public class SmsRetriever {

    public static String SMS_RETRIEVED_ACTION = "122";
    public static String MESSAGE = "12222";
    public static String SUCCESS = "122111";
    private Context context;
    private com.rahul.otpfetch.ResponseHandler handler;
    private BroadcastReceiver receiver;

    public SmsRetriever(Context context, ResponseHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void startService() {

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
                handler.failureToStartService(e);
            }
        });
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(SMS_RETRIEVED_ACTION);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(SUCCESS, false)) {
                    String stringExtra = intent.getStringExtra(MESSAGE);
                    handler.completeSMS(stringExtra);
                    return;
                }

                handler.timedOut();
            }
        };

        context.registerReceiver(receiver, intentFilter);
    }

    public void stopService() {
        context.unregisterReceiver(receiver);
    }
}
