package com.rahul.otpretriever;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rahul.otpfetch.SmsResponseListener;
import com.rahul.otpfetch.SmsFetch;

public class MainActivity extends AppCompatActivity implements SmsResponseListener {

    SmsFetch smsFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsFetch = new SmsFetch(this, this);
        smsFetch.startListeningService();
    }

    @Override
    public void failureToStartService(Exception e) {
        Log.d("MainActivity", "failureToStartService: " + e.getMessage());
    }

    @Override
    public void smsResponse(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (smsFetch != null) {
            smsFetch.stopListeningService();
        }
    }

    @Override
    public void requestTimedOut() {
        Toast.makeText(this, "TimedOut", Toast.LENGTH_SHORT).show();
    }
}
