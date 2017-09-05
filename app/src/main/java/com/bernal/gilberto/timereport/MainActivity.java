package com.bernal.gilberto.timereport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null)  // if current user already registered
        {
            //  profile will be here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }


    private void registerUser() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //  empty email
            Toast.makeText(this, " Please Enter your email ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {

            //  empty password
            Toast.makeText(this, " Please Enter your Password ", Toast.LENGTH_SHORT).show();
            return;
        }
        // if validation is ok we will register the user
        progressDialog.setMessage(" Registering user .... ");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create user", e);
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // user is sucessful registered
                            Toast.makeText(MainActivity.this, " User registered Successfuly ", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                        } else {
                            Toast.makeText(MainActivity.this, " Could not register, Please try again ", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();

                    }
                });
    }

    @Override
    public void onClick(View view) {

        if (view == buttonRegister) {
            registerUser();
        }
        if (view == textViewSignIn) {
            //  We will open login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}