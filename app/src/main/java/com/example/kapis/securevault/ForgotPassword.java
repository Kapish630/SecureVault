package com.example.kapis.securevault;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.forgotNoAcc)
    TextView noAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

    }




    @OnClick(R.id.registerButton)
    public void backToLogin() {
        Toast.makeText(this,"If you have an account, We will send you an email with your password",Toast.LENGTH_LONG).show();
        // brings you back to the SignInPage
        startActivity(new Intent(this, LogInPage.class));
    }


    @OnClick(R.id.forgotNoAcc)
    public void backToRegister() {
        // brings you back to the Register Page
        startActivity(new Intent(this, Register.class));
    }

}
