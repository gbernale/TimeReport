package com.bernal.gilberto.timereport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.bernal.gilberto.timereport.R.id.parent;



public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private TextView textViewUserMail, textViewCompanyId;
    private DatabaseReference databaseReference;
    private EditText editTextName, editTextAddress, editTextPhone, editTextHourValue, editTextCompanyId;
    private Button buttonSaveData;
    private TextView textViewPersons;
    private ValueEventListener mPostListener;
    private View view;
    private static final String TAG = "ProfileFragment";
    private List <String> companies = new ArrayList<String>();
    private String company;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        TextView tv3 = (TextView) getActivity().findViewById(R.id.tv3);
        tv3.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(view.getContext());
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        buttonSaveData = (Button) view.findViewById(R.id.buttonSaveData);
  //   functionality added to include cpmpany spinner
        final DatabaseReference companyDbReference = databaseReference.child("Company");
        companyDbReference.addListenerForSingleValueEvent(new ValueEventListener() {

           @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               FirebaseUser user = firebaseAuth.getCurrentUser();
               for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                   String companyName = areaSnapshot.child("name").getValue(String.class);
                   String companyemailAddress = areaSnapshot.child("emailAddress").getValue(String.class);
                   String companyId = areaSnapshot.child("name").getKey();
                   if (companyName != null) {
                       companies.add(companyName);
                   }
               }
               final Spinner companySpinner = (Spinner) view.findViewById(R.id.my_spinner);
               ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, companies);
               companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               companySpinner.setAdapter(companyAdapter);
               companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                       companySpinner.setSelection(position);
                       company = (String) companySpinner.getSelectedItem();
                       //selVersion.setText("Selected Android OS:" + selState);
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> adapterView) {

                   }
               });
           }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // loadUserdata(user);

        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {

                editTextAddress = (EditText) view.findViewById(R.id.editTextAddress);
                editTextName = (EditText) view.findViewById(R.id.editTextName);
                editTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
                editTextHourValue = (EditText) view.findViewById(R.id.editTextHourValue);
                textViewPersons = (TextView) view.findViewById(R.id.textViewPersons);

                String name = editTextName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String value = editTextHourValue.getText().toString().trim();
                boolean isAdmin = true;
                int hourvalue = Integer.parseInt(value);
                User datauser = new User(name, address, phone,hourvalue,isAdmin,company);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child("UserProfileData").child(user.getUid()).setValue(datauser);
               // databaseReference.child("UserProfileData").child(company.companyId).child(user.getUid()).setValue(datauser);
                Toast.makeText(getContext(), "Profile saved  .....", Toast.LENGTH_LONG).show();
                loadUserdata(user);
            }
        });


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void loadUserdata(final FirebaseUser user){

        databaseReference.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                DataSnapshot customerData = dataSnapshot.child("UserProfileData");
                for (DataSnapshot postSnapshot : customerData.getChildren()){
                    if (postSnapshot.getKey().equals (user.getUid())) {
                        User user = postSnapshot.getValue(User.class);

                        progressDialog.setMessage(" Creating User Profile  Please wait ...");
                        progressDialog.show();
                        String string = "Name: " + user.getName() + "\nAddress: " + user.getAddress() + "\nTelephone : " + user.getAddress() + "\n\n";

                        //Displaying it on textview
                        textViewPersons.setText(string);
                        progressDialog.dismiss();

                        getActivity().finish();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), ProfileActivity.class);
                        startActivity(intent);

                    }
                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                //System.out.println("The read failed: " );
            }

        });


    }

}