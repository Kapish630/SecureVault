package com.example.kapis.securevault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class ForgotPassword extends AppCompatActivity {

    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void backToLogin() {
        // brings you back to the SignInPage
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }
}
