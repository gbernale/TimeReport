package com.bernal.gilberto.timereport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;


import static com.bernal.gilberto.timereport.TimePicker.DatePickerDialogFragment.FLAG_END_DATE;
import static com.bernal.gilberto.timereport.TimePicker.DatePickerDialogFragment.FLAG_START_DATE;

public class TimePicker extends AppCompatActivity implements View.OnClickListener{

    private static   Button startDateButton, finishDateButton, buttonBack;
    private  static  TextView startDateTextView, finishDateTextView;
    private DatePickerDialogFragment mDatePickerDialogFragment;
    private WorkLocationDialogFragment wkFragment;
    private  static  FirebaseAuth firebaseAuth;
    private static DatabaseReference databaseReference;
    private static String syearin, syearout, stimein, stimeout, swklocation, location ;
    private static int tTimeIn, tTimeOut;
    public  static int  flag;
    public static int lunchTime = 30;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        startDateButton = (Button) findViewById(R.id.b_pick);
        finishDateButton = (Button) findViewById(R.id.b_pick1);
        buttonBack = (Button) findViewById(R.id.back);
        startDateTextView = (TextView) findViewById(R.id.tv_datetimeInput);
        finishDateTextView = (TextView) findViewById(R.id.tv_datetimeOutput);
        mDatePickerDialogFragment = new DatePickerDialogFragment();

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        startDateButton.setOnClickListener(this);
        finishDateButton.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        wkFragment = WorkLocationDialogFragment.newInstance("Location Picker");

        }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.b_pick) {
            mDatePickerDialogFragment.setFlag(FLAG_START_DATE);
            mDatePickerDialogFragment.show(getFragmentManager(), "datePicker");
            wkFragment.show(getFragmentManager(),"Location Picker");
        } else if (id == R.id.b_pick1) {
            mDatePickerDialogFragment.setFlag(FLAG_END_DATE);
            mDatePickerDialogFragment.show(getFragmentManager(), "datePicker");
        } else if ( id == R.id.back){

            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    public static  class DatePickerDialogFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        public static final int FLAG_START_DATE = 0;
        public static final int FLAG_END_DATE = 1;

       // private int flag = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setFlag(int i) {
            flag = i;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (flag == FLAG_START_DATE) {

                startDateButton.setText(format.format(calendar.getTime()));
                syearin = format.format(calendar.getTime());
                syearout=syearin;

            } else if (flag == FLAG_END_DATE) {
                finishDateButton.setText(format.format(calendar.getTime()));
                syearout = format.format(calendar.getTime());
                syearin=syearout;
            }


            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(),"Time Picker");

        }
    }

    public static  class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public static final int FLAG_START_DATE = 0;
        public static final int FLAG_END_DATE = 1;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            //java.util.Calendar c = java.util.Calendar.getInstance();
            int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
            int minute = c.get(java.util.Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if (flag == FLAG_START_DATE) {
                String m = Integer.toString(minute);
                String h = Integer.toString(hourOfDay);
                stimein = h + ":" + m + ":" + "00";
                tTimeIn = hourOfDay*60 + minute;
                stimeout ="00:00:00";
            }  else if (flag == FLAG_END_DATE)
            {
                String m = Integer.toString(minute);
                String h = Integer.toString(hourOfDay);
                stimeout = h + ":" + m + ":" + "00";
                tTimeOut = (hourOfDay  )*60 + minute;
                stimein="00:00:00";
            }

            //startDateTextView.setText( "Time : " + m + " " +" " + h + "\n ");
            //final  String location = swklocation;
            final double total_hours;
            total_hours = 0;
            final double total_hours_value = 400;
            final int week_number = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            final String hour_status =  "Pending";
            final String user_status = "Active";
            final String comments = "Regular activities";

            // I added on Sept 24 to check and update records - time out -  if already exist

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int checkif = 0;
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    DataSnapshot currentUserTimeReports = dataSnapshot.child("TimeReport").child(user.getUid());

                    for (DataSnapshot currentUserTimeReport : currentUserTimeReports.getChildren()) {
                        TimeReport currentUserTimeReportValue = currentUserTimeReport.getValue(TimeReport.class);
                        if (currentUserTimeReportValue.getDate_out().equals(syearout) && stimeout != "00:00:00") {
                            try {
                                currentUserTimeReportValue.time_out = stimeout;
                                int memTotalTimeIn = currentUserTimeReportValue.getTotal_minutes_in();
                                currentUserTimeReportValue.total_hours = ((tTimeOut-memTotalTimeIn-lunchTime)/60.0d);
                                currentUserTimeReportValue.total_minutes_out = tTimeOut;
                                currentUserTimeReport.getRef().setValue(currentUserTimeReportValue);
                                checkif = 1;
                                Toast.makeText(getActivity().getApplication(), "TimeReport Record Updated  .....", Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if ( currentUserTimeReportValue.getDate_in().equals(syearin) && stimein != "00:00:00") {
                            try {
                                currentUserTimeReportValue.time_in = stimein;
                                int memTotalTimeOut = currentUserTimeReportValue.getTotal_minutes_out();
                                currentUserTimeReportValue.total_hours = Math.round(((memTotalTimeOut- tTimeIn-lunchTime)/60.0d)*100/100);
                                currentUserTimeReportValue.location = location;
                                currentUserTimeReportValue.total_minutes_in = tTimeIn;
                                currentUserTimeReport.getRef().setValue(currentUserTimeReportValue);
                                checkif = 1;
                                Toast.makeText(getActivity().getApplicationContext(), "TimeReport Record Updated  .....", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                   if(checkif ==0)  {
                    TimeReport userdata = new TimeReport(location, week_number, syearin, stimein, syearout, stimeout, tTimeIn, tTimeOut, total_hours, total_hours_value, hour_status, user_status, comments);
                    DatabaseReference timeReport = databaseReference.child("TimeReport").child(user.getUid()).push();
                    timeReport.setValue(userdata);
                    Toast.makeText(getActivity().getApplicationContext(), "TimeReport Record saved  .....", Toast.LENGTH_LONG).show();
                }

            }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

         public void showTimePickerDialog(View v) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "timePicker");
        }
    }

       // fragment to get work location
    public static class WorkLocationDialogFragment extends DialogFragment  {

        private EditText mEditText;

        public WorkLocationDialogFragment() {
            // Empty constructor is required for DialogFragment
            // Make sure not to add arguments to the constructor
            // Use `newInstance` instead as shown below
        }

        public static WorkLocationDialogFragment newInstance(String workLocation) {
            WorkLocationDialogFragment frag = new WorkLocationDialogFragment();
            Bundle args = new Bundle();
            args.putString("Work Location", workLocation);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.work_location, container);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // Get field from view
            mEditText = (EditText) view.findViewById(R.id.etWorkLocation);
            swklocation = mEditText.getText().toString().trim();
           Button  buttonCancel = (Button) view.findViewById(R.id.btnBack);
           Button buttonOK = (Button) view.findViewById(R.id.btnOK);
            // Fetch arguments from bundle and set title
            final String workLocation = getArguments().getString("Work_Location", "Enter WorkLocation");
            getDialog().setTitle(workLocation);
            // Show soft keyboard automatically and request focus to field
            mEditText.requestFocus();
            swklocation = mEditText.getText().toString().trim();
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swklocation = mEditText.getText().toString().trim();
                    location = mEditText.getText().toString().trim();
                    dismiss();

                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

    }}}
