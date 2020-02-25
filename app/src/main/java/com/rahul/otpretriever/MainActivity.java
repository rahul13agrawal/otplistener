package com.rahul.otpretriever;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rahul.otpfetch.ResponseHandler;
import com.rahul.otpfetch.SmsRetriever;

public class MainActivity extends AppCompatActivity implements ResponseHandler {

    com.rahul.otpfetch.SmsRetriever smsRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsRetriever = new SmsRetriever(this, this);
        smsRetriever.startService();
    }

    @Override
    public void failureToStartService(Exception e) {
        Log.d("MainActivity", "failureToStartService: " + e.getMessage());
    }

    @Override
    public void completeSMS(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (smsRetriever != null) {
            smsRetriever.stopService();
        }
    }

    @Override
    public void timedOut() {
        Toast.makeText(this, "TimedOut", Toast.LENGTH_SHORT).show();
    }
}
