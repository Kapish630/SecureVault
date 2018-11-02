package com.example.kapis.securevault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInPage extends AppCompatActivity {

    @BindView(R.id.login_Email)
    EditText emailInput;

    @BindView(R.id.login_Password)
    EditText passwordInput;

    UserAccountDBHelper mDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        ButterKnife.bind(this);

        // UserAccoutDBHelper instance
        mDbHelper = new UserAccountDBHelper(this);

        }



    @OnClick(R.id.login_ForgotPassword)
    public void forgotScreen() {
        // Bring you to the Forgot Password page
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_NewAccount)
    public void registerScreen() {
        // Brings you to the Registration Page
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_SignIn)
    public void fingerprintScreen() {
        // Brings you to the Fingerprint Screen
        Intent intent = new Intent(this, SecondAuthentication.class);
        startActivity(intent);
    }
}
