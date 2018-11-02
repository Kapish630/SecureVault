package com.example.kapis.securevault;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.kapis.securevault.UserContract.TABLE_NAME;
import static com.example.kapis.securevault.UserContract.UserEntry.*;



public class Register extends AppCompatActivity {

    @BindView(R.id.reigsterFullName)
    EditText nameInput;

    @BindView(R.id.registerEmail)
    EditText emailInput;

    @BindView(R.id.registerPassword1)
    EditText passwordInput;

    @BindView(R.id.registerPassword2)
    EditText password2Input;

    //DATABASE
    UserAccountDBHelper mDbHelper;

    // COLORS USED
    final int colorRed = R.color.colorRed;
    final int colorBrightGreen = R.color.colorBrightGreen;
    final int colorWhite = R.color.colorWhite;
    final int colorBlack = R.color.colorBlack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        mDbHelper = new UserAccountDBHelper(this);
    }

    // Method that checks to see if the input is valid
    // It will also turn the incorrect fields red
    private boolean isValid()
    {
        boolean valid = true;

        if(nameInput.getText().toString().trim().isEmpty() ||
                nameInput.getText().toString().trim().length() < 2 ||
                nameInput.getText().toString().trim().length() > 30)
        {
            valid = false;
            nameInput.setBackgroundColor(getResources().getColor(colorRed));
            nameInput.setTextColor(getResources().getColor(colorWhite));
        }

        if(TextUtils.isEmpty(emailInput.getText())
                || !Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches())
        {
            valid = false;
            emailInput.setBackgroundColor(getResources().getColor(colorRed));
            emailInput.setTextColor(getResources().getColor(colorWhite));
        }

        if(passwordInput.getText().toString().trim().isEmpty() ||
                passwordInput.getText().toString().equals("NOT FOUND") ||
                password2Input.getText().toString().trim().isEmpty() ||
                !passwordInput.getText().toString().trim().equals(password2Input.getText().toString().trim()))
        {
            valid = false;
            passwordInput.setBackgroundColor(getResources().getColor(colorRed));
            passwordInput.setTextColor(getResources().getColor(colorWhite));
            password2Input.setBackgroundColor(getResources().getColor(colorRed));
            password2Input.setTextColor(getResources().getColor(colorWhite));
        }

        return valid;
    }

    // This is the Register button
    // It calls the isValid Method to see if input is correct
    // Any field that is valid will turn back to original colors if it was red (incorrect) before.
    @OnClick(R.id.registerButton)
    public void submit_btn_func(View view) {
        if (!isValid()) {
            nameInput.getText().toString().trim();
            nameInput.setBackgroundColor(getResources().getColor(colorWhite));
            nameInput.setTextColor(getResources().getColor(colorBlack));

            emailInput.getText().toString().trim();
            emailInput.setBackgroundColor(getResources().getColor(colorWhite));
            emailInput.setTextColor(getResources().getColor(colorBlack));

            passwordInput.getText().clear();
            passwordInput.setBackgroundColor(getResources().getColor(colorWhite));
            passwordInput.setTextColor(getResources().getColor(colorBlack));

            password2Input.getText().clear();
            password2Input.setBackgroundColor(getResources().getColor(colorWhite));
            password2Input.setTextColor(getResources().getColor(colorBlack));

            isValid();
        }

        // If all of the fields are valid then it accepts the email and pass
        // Creates a new user with those values
        else {

            String EMAIL = emailInput.getText().toString().trim();
            String PASS = passwordInput.getText().toString().trim();
            String NAME = nameInput.getText().toString().trim();

            nameInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
            nameInput.setTextColor(getResources().getColor(colorWhite));

            emailInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
            emailInput.setTextColor(getResources().getColor(colorWhite));

            passwordInput.setBackgroundColor(getResources().getColor(colorBrightGreen));
            passwordInput.setTextColor(getResources().getColor(colorWhite));

            password2Input.setBackgroundColor(getResources().getColor(colorBrightGreen));
            password2Input.setTextColor(getResources().getColor(colorWhite));

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put( COL_FULL_NAME, NAME);
            values.put( COL_EMAIL, EMAIL);
            values.put( COL_PASS, PASS);


            // Add Values into the table
            db.insert(TABLE_NAME,null,values);

            // Passes new user information to the Main Activity screen for login validation
            Intent newUser = new Intent(Register.this, LogInPage.class);
            startActivity(newUser);


            // Tells the user that the registration was a success
            Toast.makeText(this, "REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
        }
    }
}
