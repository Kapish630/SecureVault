package com.example.kapis.securevault;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {


    FirebaseAuth mAuth;

    @BindView(R.id.forgot_EmailInput)
    EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }


    @OnClick(R.id.forgot_Confirm)
    public void clickedConfirm() {
        String email = emailInput.getText().toString().trim();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this,"If you have an account, We will send you an email with a way to reset your password",Toast.LENGTH_LONG).show();
                            // brings you back to the SignInPage
                            startActivity(new Intent(ForgotPassword.this, LogInPage.class));
                        }
                        else
                            Toast.makeText(ForgotPassword.this,"Error",Toast.LENGTH_LONG).show();

                    }
                });
    }

}
