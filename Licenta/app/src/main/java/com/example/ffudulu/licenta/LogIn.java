package com.example.ffudulu.licenta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends Activity {

    private Button mBtnLogin;
    private TextView mTxtSignUp;
    private EditText mEmailField;
    private EditText mPasswordField;
    private ProgressBar mProgressBarLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        mBtnLogin = (Button) findViewById(R.id.btnLogIn);
        mTxtSignUp = (TextView) findViewById(R.id.txtSignUp);

        mEmailField = (EditText) findViewById(R.id.txtUsername);
        mPasswordField = (EditText) findViewById(R.id.txtPassword);

        mTxtSignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
//                    Toast.makeText(LogIn.this, "You are allready logged in",
//                            Toast.LENGTH_LONG).show();
                    Intent mDrawer = new Intent(LogIn.this, MainDrawer.class);
                    startActivity(mDrawer);
                }
            }
        };

        mBtnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                trySignIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void trySignIn(){
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mProgressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogIn);
        mProgressBarLogin.setVisibility(View.VISIBLE);

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            mProgressBarLogin.setVisibility(View.GONE);
            Toast.makeText(LogIn.this, "One or both field is empty", Toast.LENGTH_LONG).show();

        }
        else{

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        mProgressBarLogin.setVisibility(View.GONE);
                        Toast.makeText(LogIn.this, "Username or password incorrect!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        mProgressBarLogin.setVisibility(View.GONE);
                        Intent mDrawer = new Intent(LogIn.this, MainDrawer.class);
                        startActivity(mDrawer);
                    }
                }
            });
        }
        mEmailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mEmailField.setHint("");
            }
        });
    }

    private void goToRegister(){
        Intent mRegister = new Intent(LogIn.this, Register.class);
        startActivity(mRegister);
    }
}
