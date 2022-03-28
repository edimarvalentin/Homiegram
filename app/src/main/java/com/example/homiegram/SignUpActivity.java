package com.example.homiegram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SingUpActivity";

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword1;
    private EditText etPassword2;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etAddEmail);
        etPassword1 = findViewById(R.id.etAddPassword1);
        etPassword2 = findViewById(R.id.etAddPassword2);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);

        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(etPassword2.getText().toString().equals(etPassword1.getText().toString())){
                    etPassword2.setTextColor(getResources().getColor(R.color.white));
                }else{
                    etPassword2.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                if(etFirstName.getText().toString().isEmpty()){
                    Log.e(TAG, "No first name");
                    etFirstName.setHint("First Name Missing");
                    isValid = false;
                }
                if(etLastName.getText().toString().isEmpty()){
                    Log.e(TAG, "No last name");
                    etLastName.setHint("Last Name Missing");
                    isValid = false;
                }
                if(etUsername.getText().toString().isEmpty()){
                    Log.e(TAG, "No username");
                    etUsername.setHint("User name missing");
                    isValid = false;
                }
                if(etEmail.getText().toString().isEmpty()){
                    Log.e(TAG, "No email");
                    etEmail.setHint("Email is missing");
                    isValid = false;
                }
                if(etPassword1.getText().toString().isEmpty()){
                    Log.e(TAG, "No Password");
                    etPassword1.setHint("Password is missing");
                    isValid = false;
                }
                if(!etPassword2.getText().toString().equals(etPassword1.getText().toString())){
                    Log.e(TAG, "Passwords don't match");
                    etPassword2.setText("");
                    etPassword2.setHint("Password doesn't match");
                    isValid = false;
                }
                if(isValid){
                    ParseUser newUser = new ParseUser();
                    newUser.put("firstName", etFirstName.getText().toString());
                    newUser.put("lastName", etLastName.getText().toString());
                    newUser.setUsername(etUsername.getText().toString());
                    newUser.setEmail(etEmail.getText().toString());
                    newUser.setPassword(etPassword1.getText().toString());
                    newUser.signUpInBackground(e->{
                        if(e!=null){
                            Log.e(TAG, "Issue with posting new user", e);
                        }
                    });
                    finish();
                }else{
                    return;
                }

            }
        });
    }
}