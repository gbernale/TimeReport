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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewDataTimeFragment extends Fragment {

    public ViewDataTimeFragment() {
        // Required empty public constructor
    }
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private View view;
    private Button buttonLogout;
    private static final String TAG = "ViewDataTimeFragment";
    private FirebaseUser user;
    private TextView tvsalida;
    private ListView lv;
    final ArrayList<String> keyList = new ArrayList<>();
    final ArrayList<String> reportItems = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_view_data_time, container, false);
        view = inflater.inflate(R.layout.fragment_view_data_time, container, false);
        final ArrayAdapter<String> clientAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        TextView tv3 = (TextView) getActivity().findViewById(R.id.tv3);
        tv3.setVisibility(View.GONE);
        tvsalida = (TextView) view.findViewById(R.id.tvsalida);
        lv = (ListView) view.findViewById(R.id.lv);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        Query query = databaseReference.child("TimeReport").child(user.getUid()).orderByChild("date_in");
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TimeReport userTimeReport = snapshot.getValue(TimeReport.class);
                    String userdataString = userTimeReport.getDate_in() + "   " + userTimeReport.getTime_in() + "   " + userTimeReport.getTime_out() + "   " + userTimeReport.getTotal_hours() + "\n\n";
                    clientAdapter.add(userdataString);

                }
                lv.setAdapter(clientAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
// Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//System.out.println("The read failed: " );
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

}


