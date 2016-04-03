package com.secureexam4_4.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by a on 6/24/15.
 */
public class FragmentSymbolLogin extends android.support.v4.app.Fragment {

    Communicator comm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment2, container, false);
        comm.fragmentCreating(v);
        LinearLayout l = (LinearLayout)v.findViewById(R.id.fragment_2);
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

        return v;
    }


    public void setCommunicator(Communicator a) {

        comm = a;

    }

    public interface Communicator {

        void fragmentCreating(View v);
    }

}
