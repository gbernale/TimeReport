package com.bernal.gilberto.timereport;

import java.sql.Time;

/**
 * Created by Gilberto Bernal   on 9/3/2017.
   January 2018  Added variable to save total_minutes_in and total_minutes_out to help me calculate total_hours
 */

public class TimeReport {

    public String location;
    public int week_number;
    public String date_in;
    public String time_in;
    public String date_out;
    public String time_out;
    public int total_minutes_in;
    public int total_minutes_out;
    public double total_hours;
    public double total_hours_value;
    public String hour_status ;
    public String user_status;
    public String comments;

    public TimeReport(){}

    public TimeReport(String location, int week_number, String date_in, String time_in, String date_out, String time_out, int total_minutes_in, int  total_minutes_out, double total_hours, double total_hours_value, String hour_status, String user_status, String comments) {
        this.location = location;
        this.week_number = week_number;
        this.date_in = date_in;
        this.time_in = time_in;
        this.date_out = date_out;
        this.time_out = time_out;
        this.total_minutes_in = total_minutes_in;
        this.total_minutes_out = total_minutes_out;
        this.total_hours = total_hours;
        this.total_hours_value = total_hours_value;
        this.hour_status = hour_status;
        this.user_status = user_status;
        this.comments = comments;
    }

    public String getLocation() {
        return location;
    }

    public int getWeek_number() {
        return week_number;
    }

    public String getDate_in() {
        return date_in;
    }

    public String getTime_in() {
        return time_in;
    }

    public String getDate_out() {
        return date_out;
    }

    public String getTime_out() {
        return time_out;
    }

    public int getTotal_minutes_in() {
        return total_minutes_in;
    }

    public int getTotal_minutes_out() {
        return total_minutes_out;
    }

    public double getTotal_hours() {
        return total_hours;
    }

    public double getTotal_hours_value() {
        return total_hours_value;
    }

    public String getHour_status() {
        return hour_status;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getComments() {
        return comments;
    }
}
