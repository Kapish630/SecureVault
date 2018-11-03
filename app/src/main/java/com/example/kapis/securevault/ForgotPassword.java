package com.example.kapis.securevault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }




    @OnClick(R.id.registerButton)
    public void backToLogin() {
        Toast.makeText(this,"If you have an account, We will send you an email with your password",Toast.LENGTH_LONG).show();
        // brings you back to the SignInPage
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }
}
