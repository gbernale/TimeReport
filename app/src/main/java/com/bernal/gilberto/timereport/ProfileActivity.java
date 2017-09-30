package com.bernal.gilberto.timereport;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private TextView textViewUserMail;
    private DatabaseReference databaseReference;
    private EditText editTextName, editTextAddress, editTextPhone;
    private Button buttonSaveData;
    private TextView textViewPersons;
    private ValueEventListener mPostListener;
    private static final String TAG = "ProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) // if current user already registered
        {
// profile will be here
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        buttonSaveData = (Button) findViewById(R.id.buttonSaveData);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textViewUserMail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserMail.setText(" Welcome "+ user.getEmail());
        buttonLogout.setOnClickListener(this);
    }
    // added to include menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case R.id.add_profile:
                callFragmentProfile();
                return true;
            case R.id.add_datetime: // add_datetime;
                callFragmentAddDateTime(); // callFragmentAddDateTime();
                return true;
            case R.id.view_datetime:    // view_datetime;
                callFragmentViewDataTime(); // callFragmentViewDataTime();
                return true;
            case R.id.add_company:
                callFragmentAddCompany();
                return true;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void  callFragmentAddDateTime ()
    {
        Intent i = new Intent(getBaseContext(), TimePicker.class);
        startActivity(i);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ViewDataTimeFragment viewDataTimeFragment = new ViewDataTimeFragment();
        transaction.replace(R.id.fragment_container,viewDataTimeFragment,"DataTime Detail");
        transaction.commit();
    }
    private void callFragmentProfile()
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        transaction.replace(R.id.fragment_container,profileFragment,"Customer Profile");
        transaction.commit();
    }
    private void callFragmentViewDataTime()
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ViewDataTimeFragment viewDataTimeFragment = new ViewDataTimeFragment();
        transaction.replace(R.id.fragment_container,viewDataTimeFragment,"DataTime Detail");
        transaction.commit();
    }

    private void callFragmentAddCompany()
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CompanyFragment companyFragment = new CompanyFragment();
        transaction.replace(R.id.fragment_container,companyFragment,"Company Profile");
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (view == buttonSaveData){
// saveUserData();
        }
    }
}

