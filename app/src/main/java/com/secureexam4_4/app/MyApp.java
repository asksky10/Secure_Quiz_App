package com.secureexam4_4.app;

import android.app.Application;
import android.os.Bundle;

/**
 * Created by nishit on 9/24/14.
 */
/*USED TO PASS GLOBAL VARIABLES BETWEEN MAINACTIVITY AND LOADWEBPAGE*/
public class MyApp extends Application {

    private Bundle mybundle = null;

    private int flag =0;

    private int onetime=0;

    private int start_entry=0;

    private int seen_exam = 0;

    private int gpo =0;


    private int attendorquiz=0; // attend 0 quiz 1

    public Bundle getbundle(){
        return mybundle;
    }
    public void setbundle(Bundle b){
        mybundle = b;
    }

    public int getAttendorquiz(){
        return attendorquiz;
    }
    public void setAttendorquiz(int b){
        attendorquiz = b;
    }

    public int getflag(){
        return flag;
    }
    public void setflag(int b){
        flag = b;
    }

    public int getSeen_exam(){
        return seen_exam;
    }

    public void setSeen_exam(int i){
        seen_exam = i;
    }

    public int getflag2(){
        return onetime;
    }
    public void setflag2(int b){
        onetime = b;
    }

    public int getStart_entry(){
        return start_entry;
    }
    public void setStart_entry(int b){
        start_entry = b;
    }
    public int getGpo(){return gpo;}
    public void setGpo(int b){
        gpo = b;
    }
}

