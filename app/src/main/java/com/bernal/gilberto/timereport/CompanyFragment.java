package com.bernal.gilberto.timereport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CompanyFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private DatabaseReference databaseReference;
    private EditText  editTextName, editTextAddress, editTextPhone;
    private EditText editTextCompanyId, editTextEmailAddress, editTextPeriod, editTextperiodBegin;
    private Button buttonSaveData;
    private TextView textViewPersons;
    private ValueEventListener mPostListener;
    private View view;
    private static final String TAG = "CompanyFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_company, container, false);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        TextView tv3 = (TextView) getActivity().findViewById(R.id.tv3);
        tv3.setVisibility(View.GONE);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        buttonSaveData = (Button) view.findViewById(R.id.buttonSaveData);

        // loadUserdata(user);

        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {

                editTextCompanyId = (EditText) view.findViewById(R.id.editTextCompanyId);
                editTextName = (EditText) view.findViewById(R.id.editTextCompanyName);
                editTextAddress = (EditText) view.findViewById(R.id.editTextAddress);
                editTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
                editTextEmailAddress = (EditText) view.findViewById(R.id.editTextEmailAddress);
                editTextPeriod = (EditText) view.findViewById(R.id.editTextPeriod);
                editTextperiodBegin = (EditText) view.findViewById(R.id.editTextperiodBegin);
                textViewPersons = (TextView) view.findViewById(R.id.textViewPersons);

                String Id = editTextCompanyId.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String email = editTextEmailAddress.getText().toString().trim();
                String period = editTextPeriod.getText().toString().trim();
                String periodBegin = editTextperiodBegin.getText().toString().trim();
                String status = "A";
                Company company = new Company(Id,name, address, phone,email,period,periodBegin,status);
                //FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child("Company").push().setValue(company);
//                databaseReference.child("Company").setValue(company);
                Toast.makeText(getContext(), "Profile saved  .....", Toast.LENGTH_LONG).show();
                //loadCompanyData(user);
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

   /* private void loadCompanyData(final FirebaseUser comp){

        databaseReference.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                DataSnapshot company = dataSnapshot.child("Company");
                for (DataSnapshot postSnapshot : company.getChildren()){
                    if (postSnapshot.getKey().equals (comp.getUid())) {
                        User user = postSnapshot.getValue(User.class);
                        String string = "Name: " + user.getName() + "\nAddress: " + user.getAddress() + "\nTelephone : " + user.getAddress() + "\n\n";

                        //Displaying it on textview
                        textViewPersons.setText(string);
                    }
                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                //System.out.println("The read failed: " );
            }

        });  */


    }

