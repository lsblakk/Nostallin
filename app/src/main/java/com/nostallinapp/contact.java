package com.nostallinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
