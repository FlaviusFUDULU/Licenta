package com.example.ffudulu.licenta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends Activity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mRepeatePasswordField;
    private Button mRegisterBtn;
    private Button mBackBtn;
    private ProgressBar mProgressBarLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        mEmailField = (EditText) findViewById(R.id.txtEmail);
        mPasswordField = (EditText) findViewById(R.id.txtPassword);
        mRepeatePasswordField = (EditText) findViewById(R.id.txtRepeatPassword);
        mRegisterBtn = (Button) findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                tryregister();
            }
        });
    }

    private void tryregister(){

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        String repeatPassword = mRepeatePasswordField.getText().toString();

        mProgressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogIn);
        mProgressBarLogin.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(repeatPassword)){
            mProgressBarLogin.setVisibility(View.GONE);
            Toast.makeText(Register.this, "One or more fields empty!!", Toast.LENGTH_LONG).show();
        }
        else {

            if (password.equals(repeatPassword)) {

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    mProgressBarLogin.setVisibility(View.GONE);
                                    Toast.makeText(Register.this, "Registration failed!\n " +
                                                    "Please try again!",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    mProgressBarLogin.setVisibility(View.GONE);
                                    Toast.makeText(Register.this, "Registration succesfull!" ,
                                            Toast.LENGTH_SHORT).show();
                                    Intent mSubmit = new Intent(Register.this, SubmitPersonalData.class);
                                    startActivity(mSubmit);
                                }
                            }
                        });
            } else {
                mProgressBarLogin.setVisibility(View.GONE);
                Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void disableAll(){
        mRepeatePasswordField.setEnabled(false);
        mPasswordField.setEnabled(false);
        mEmailField.setEnabled(false);
        mRegisterBtn.setEnabled(false);
    }

    private void enableAll(){
        mRepeatePasswordField.setEnabled(true);
        mPasswordField.setEnabled(true);
        mEmailField.setEnabled(true);
        mRegisterBtn.setEnabled(true);
    }
}
