package com.example.kapis.securevault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInPage extends AppCompatActivity {

    Button signin;
    Button forgot;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        signin = (Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInScreen();
            }
        });

        forgot = (Button)findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotScreen();
            }
        });

        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerScreen();
            }
        });

    }

    public void signInScreen() {
        // brings you back to the SignInPage
        Intent intent = new Intent(this, SecondAuthentication.class);
        startActivity(intent);
    }
    public void forgotScreen() {
        // brings you back to the SignInPage
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }
    public void registerScreen() {
        // brings you back to the SignInPage
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
