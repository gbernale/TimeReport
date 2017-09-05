package com.bernal.gilberto.timereport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSingIn;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private TextView textViewSignUp;
    private TextView textViewResetPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) // if current user already registered
        {
// profile will be here
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSingIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        textViewResetPassword = (TextView) findViewById(R.id.textViewResetPassword);
        progressDialog = new ProgressDialog(this);
        buttonSingIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        textViewResetPassword.setOnClickListener(this);
    }
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
// empty email
            Toast.makeText(this, " Please Enter your email ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
// empty password
            Toast.makeText(this, " Please Enter your Password ", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage(" Registering Please wait ...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if(view == buttonSingIn){
            userLogin();
        }
        if ( view == textViewSignUp){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if(view == textViewResetPassword){
            finish();
            startActivity(new Intent(this,ResetPasswordActivity.class));
        }
    }}
