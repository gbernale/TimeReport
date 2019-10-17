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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


public class ViewDataUserFragment extends Fragment {


    public ViewDataUserFragment() {
        // Required empty public constructor
    }

    private View view;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private  ListView lv;
    private Button buttonBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view_data_user, container, false);
        lv = (ListView) view.findViewById(R.id.lv2);
        final ArrayAdapter<String> userAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1);
        buttonBack = (Button) view.findViewById(R.id.buttonBack);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();

        //Query query = databaseReference.child("TimeReport").child("Company").orderByChild("name");
       // Query query2 = databaseReference.child("TimeReport").child(user.getUid()).orderByChild("name");
        Query query = databaseReference.child("UserProfileData").orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userTimeReport = snapshot.getValue(User.class);
                    String userdataString = userTimeReport.getName()+ "\n";
                    userAdapter.add(userdataString);
                }
                lv.setAdapter(userAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        //MyClass selItem = (MyClass) adapter.getItem(position);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
// Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//System.out.println("The read failed: " );
            }
        });
            buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
         });
         return view;
        //return inflater.inflate(R.layout.fragment_view_data_user, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

   }
