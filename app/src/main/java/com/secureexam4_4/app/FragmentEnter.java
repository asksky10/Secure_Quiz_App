package com.secureexam4_4.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by a on 6/24/15.
 */
public class FragmentEnter extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment1, container, false);
        LinearLayout l = (LinearLayout)v.findViewById(com.secureexam4_4.app.R.id.fragment_1);
        l.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View decorView, MotionEvent e) {

                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                ////Log.d("Focus Debug", "Navigation Gone");
                decorView.setSystemUiVisibility(uiOptions);
                return true;
                //-------------------------------------------------------------------------------------------
            }
        });
        return  v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Restoring cached data from cache
        File myFile = new File(getActivity().getCacheDir(), "cachefile");
        FileInputStream fIn = null;
        if(myFile.exists()) {
            try {

                fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
//                System.out.println("LOLNESS" + myReader.readLine());
                Log.d("Utkarsh", "File exist karti hai");
                EditText src = (EditText) getView().findViewById(R.id.attend_text);
                src.setText(myReader.readLine());
                src = (EditText) getView().findViewById(com.secureexam4_4.app.R.id.username);
                src.setText(myReader.readLine());
                src = (EditText) getView().findViewById(com.secureexam4_4.app.R.id.source_address);
                src.setText(myReader.readLine());
                fIn.close();
                myReader.close();
                //System.out.println("DATA RESTORED FROM CACHE");
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }else{

        }
    }
}

