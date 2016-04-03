package com.secureexam4_4.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.secureexam4_4.app.R;

import static android.app.ActionBar.*;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity implements FragmentSymbolLogin.Communicator{
    /*CLASS VARIABLES*/
    private final String TAG = "URLLOG";
    private final String TAG2 = "HTTPSLOG";
    private boolean exit_ready = false;
    private String exit_code = "123456";
    private String response_str = "temp_response";
    private String exam_url = "http://www.google.com";
    public final static String EXAM_URL = "https://docs.google.com/forms/d/1yhitfFecYrUujXHU7ri90AOJ8spVVtGAhmLlvhm7-hc/viewform?c=0&w=1&usp=mail_form_link";
    public final static String URL_LIST = "https://docs.google.com/forms/d/1yhitfFecYrUujXHU7ri90AOJ8spVVtGAhmLlvhm7-hc/viewform?c=0&w=1&usp=mail_form_link";
    public final static String username1="";
    public final static String password1="";
    private String secret_key="";
    private boolean https = false;
    private boolean no_error = true;
    private HeadphoneReceiver receiver = new HeadphoneReceiver();
    private BluetoothReceiver bluetooth_receiver = new BluetoothReceiver();
    private int ff = 0;
    int flagbaba=0;

    //////////////////////////////////////////
    public static int[] keyboardButtonIds;
    public static int[] arr,arr2;
    public static int random;
    public static Button[] keyboardButtons;
    public static Typeface symbolFont;
    public static EditText passPrompt;
    public static EditText symbol_password;
    ViewPager viewPager = null;


    public void initFont() {

        symbolFont = Typeface
                .createFromAsset(getAssets(), "fonts/marvosym.ttf");
    }

    private void initButtons(View view) {
        keyboardButtonIds = new int[] { R.id.kbutton1, R.id.kbutton2,
                R.id.kbutton3, R.id.kbutton4, R.id.kbutton5, R.id.kbutton6,
                R.id.kbutton7, R.id.kbutton8, R.id.kbutton9, R.id.kbutton10,
                R.id.kbutton11, R.id.kbutton12, R.id.kbutton13, R.id.kbutton14,
                R.id.kbutton15, R.id.kbutton16, R.id.kbutton17, R.id.kbutton18,
                R.id.kbutton19, R.id.kbutton20, R.id.kbutton21, R.id.kbutton22,
                R.id.kbutton23, R.id.kbutton24, R.id.kbutton25, R.id.kbutton26,
                R.id.kbutton27, R.id.kbutton28, R.id.kbutton29, R.id.kbutton30,
                R.id.kbutton31, R.id.kbutton32, R.id.kbutton33, R.id.kbutton34,
                R.id.kbutton35, R.id.kbutton36, R.id.kbutton37, R.id.kbutton38,
                R.id.kbutton39, R.id.kbutton40, R.id.kbutton41, R.id.kbutton42,
                R.id.kbutton43, R.id.kbutton44, R.id.kbutton45, R.id.kbutton46,
                R.id.kbutton47, R.id.kbutton48, R.id.kbutton49, R.id.kbutton50 };

        int i = 0;
        symbol_password = (EditText)findViewById(R.id.symbol_password);
        arr = new int[50];
        for (i = 0; i < 50; i++)
            arr[i] = i;
        i = 0;
        int j=0;
        keyboardButtons = new Button[keyboardButtonIds.length];
        shuffleArray(keyboardButtonIds);

        for (int id : keyboardButtonIds) {
            keyboardButtons[arr[i++]] = (Button) view.findViewById(id);
            keyboardButtons[arr[i - 1]].setTypeface(symbolFont);
            if(j==12)
                j++;
            keyboardButtons[arr[i - 1]].setText("" + (char) (j + 65));
            keyboardButtons[arr[i - 1]]
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            passPrompt.setText(passPrompt.getText().toString()
                                    + b.getText());
                            symbol_password.setText(passPrompt.getText());

                        }
                    });
            j++;
        }
    }

    static void shuffleArray(int[] ar) {
        Random rnd = new Random();
        arr2 = new int[50];
        for (int i = 0; i < 50; i++)
            arr2[i] = 0;
        for (int i = ar.length - 1; i > 0; i--) {
            if (arr2[i] != 0)
                continue;
            else {
                int index = rnd.nextInt(i + 1);
                while (arr2[index] != 0) {
                    index = rnd.nextInt(i + 1);
                }
                arr2[index] = 1;
                System.out.println(index + "adadadada");
                // Simple swap
                int a = ar[index];
                ar[index] = ar[i];
                ar[i] = a;
                arr[index] = i;
            }
        }
    }


    @Override
    public void fragmentCreating(View v) {

        initFont();
        initButtons(v);

        passPrompt = (EditText) v.findViewById(R.id.password_box);
        passPrompt.setTypeface(symbolFont);

        // Clear button
        Button clearButton = (Button) v.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passPrompt.setText("");symbol_password.setText("");
            }
        });

        // Backspace button
        Button backButton = (Button) v.findViewById(R.id.backspace_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passString = passPrompt.getText().toString();
                if (passString.length() > 0) {
                    passString = passString.substring(0,
                            passString.length() - 1);

                    // System.out.println(secret_key + " adada ");
                    passPrompt.setText(passString);
                    symbol_password.setText(passPrompt.getText());
                }
            }
        });

    }
// Methods to change page when user clicks on certain views

    public void symbolPasswordClicked(View view) {
        viewPager.setCurrentItem(1);
    }

    public void onDoneClicked(View v) {
        viewPager.setCurrentItem(0);
    }

    public void onExitClicked(View view) {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Discard all Quiz Data and Exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       validate_exit_code(findViewById(which));
                    }

                })
                .setNegativeButton("WAIT", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // request a app screen with no TITLE (FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // Log.d("get", "aad");
        super.onCreate(savedInstanceState);

        MyApp appState = ((MyApp)getApplicationContext());
        if(getIntent().getBooleanExtra("EXIT",false)){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            stopService(i);
            finish();
        }else{
                        // checking if app set as default launcher

            int ff =0;
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            final ResolveInfo res = getPackageManager().resolveActivity(intent, 0);
            if (res.activityInfo == null){

            }
            if ("android".equals(res.activityInfo.packageName)) {

            }
            else {
                if(getPackageName().equals(res.activityInfo.packageName)) {
                    ff = 1;
                }
            }
            if(ff == 0){
                // NOT THE DEFAULT LAUNCHER
                Toast.makeText(this, "SET THIS APP AS DEFAULT LAUNCHER IN SETTINGS", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        appState.setflag2(0);


        ContentResolver contentResolver = this.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains("com.secureexam4_4.app/com.secureexam4_44_4.app.MyService"))
        {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            // in this situation we know that the user has not granted the app the Notification access permission
//            throw new Exception();
        }
        else{
//            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);     //NIXIT
//            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME );
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.disable();

        //set up notitle 

        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //*** end

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        setContentView(com.secureexam4_4.app.R.layout.activity_main);


        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(manager,this));

       // PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //tabs.setViewPager(pager);
                        // Intent to warn in case of headset connectivity

        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);

        // Intent to warn in case of headset connectivity
        IntentFilter bluetoothFilter = new IntentFilter(String.valueOf(BluetoothAdapter.ACTION_STATE_CHANGED));

//        IntentFilter notFilter = new IntentFilter(NOTIFICATION_SERVICE);
        receiver.setMainActivity(this);
        bluetooth_receiver.setMainActivity(this);
//        nt_receiver.setMainActivity(this);
        registerReceiver(receiver, receiverFilter);
        registerReceiver( bluetooth_receiver, bluetoothFilter );
    }

    // This function locks the focus of phone on our app. It gets called whenever focus changes and restores
    // it back to the app.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        View decorView = getWindow().getDecorView();                                           //NIXIT
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        //-------------------------------------------------------------------------------------------


//        //Log.d("Focus debug", "Focus changed !");
        if(!hasFocus) {
//            //Log.d("Focus debug", "Lost focus !");
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);     //NIXIT
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME );
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            Intent notifi = new Intent(NOTIFICATION_SERVICE);
            sendBroadcast(notifi);

            sendBroadcast(closeDialog);
        }
    }

    // This is called when "Submit attendance" is clicked
    public void connect2attend(View view) {
        flagbaba=1;
        MyApp appState = (MyApp)getApplicationContext();
        int attend = appState.getAttendorquiz();
        if(attend == 1){
            Toast.makeText(this, "Attendance already recorded", Toast.LENGTH_LONG).show();
            return;
        }

        EditText editText = (EditText) findViewById(R.id.attend_text);
        String url = editText.getText().toString();
/* StrictMode is a developer tool which detects things you might be doing by accident and brings them to your attention so you can fix them.
 StrictMode is most commonly used to catch accidental disk or network access on the application's main thread,
 where UI operations are received and animations take place*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //permitAll() disables all detection

        StrictMode.setThreadPolicy(policy);


        //check if the inputs are  valid
        String uname = ((EditText) findViewById(com.secureexam4_4.app.R.id.username)).getText().toString();//get the user name
        String pword = ((EditText) findViewById(com.secureexam4_4.app.R.id.password)).getText().toString();//get the password
        String ccode = ((EditText) findViewById(com.secureexam4_4.app.R.id.coursecode)).getText().toString();//get the coursecode
        String attendance = ((EditText) findViewById(R.id.attend_text)).getText().toString();
        if(attendance.equals("")){
            Toast.makeText(this, "Please enter the attendance url", Toast.LENGTH_LONG).show();
            return;
        }
        if(uname.equals("")){
            Toast.makeText(this, "Please enter the username", Toast.LENGTH_LONG).show();
            return;
        }
        if(pword.equals("")){
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_LONG).show();
            return;
        }
        if(ccode.equals("")){
            Toast.makeText(this, "Please enter the course code", Toast.LENGTH_LONG).show();
            return;
        }

        else{
            https=true;
        }
        URL target_url = null;
        try {
            target_url = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        int ff =0;
        try{
            final URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            new getInfo().execute(target_url);
            ff=1;

        } catch(final MalformedURLException e){
            throw new IllegalStateException("Bad URL: " + url, e);
        } catch(final IOException e){

        }
        if(ff != 1){
            Toast.makeText(this, "no connection (enable wifi and login)", Toast.LENGTH_LONG).show();
//            MyApp appState = (MyApp)getApplicationContext();
//            appState.setflag2(1);
            finish();
        }
    }

    public void connect2server(View view) {
        // checking for attendance first
        flagbaba = 1;
        MyApp appState = (MyApp)getApplicationContext();
        int attend = appState.getAttendorquiz();
        if(attend == 0){
            Toast.makeText(this, "Mark Attendance first", Toast.LENGTH_LONG).show();
            return;
        }
        EditText editText = (EditText) findViewById(com.secureexam4_4.app.R.id.source_address);
        String url = editText.getText().toString();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        //check if the inputs are  valid
        String uname = ((EditText) findViewById(com.secureexam4_4.app.R.id.username)).getText().toString();//get the user name
        String pword = ((EditText) findViewById(com.secureexam4_4.app.R.id.password)).getText().toString();//get the password
        String ccode = ((EditText) findViewById(com.secureexam4_4.app.R.id.coursecode)).getText().toString();//get the coursecode
        String attendance = ((EditText) findViewById(R.id.attend_text)).getText().toString();
        if(attendance.equals("")){
            Toast.makeText(this, "Please enter the attendance url", Toast.LENGTH_LONG).show();
            return;
        }
        if(uname.equals("")){
            Toast.makeText(this, "Please enter the username", Toast.LENGTH_LONG).show();
            return;
        }
        if(pword.equals("")){
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_LONG).show();
            return;
        }
        if(ccode.equals("")){
            Toast.makeText(this, "Please enter the course code", Toast.LENGTH_LONG).show();
            return;
        }
        if(url.equals("")){
            Toast.makeText(this, "Please enter source url", Toast.LENGTH_LONG).show();
            return;
        }
        if(!url.startsWith("http")){
            Toast.makeText(this, "Please add HTTP to the source URL", Toast.LENGTH_LONG).show();
            return;
        }
        if(url.startsWith("http")){
            https=false;
        }
        else{
            https=true;
        }
        URL target_url = null;
        try {
            target_url = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int ff =0;
        try{
            final URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            new getInfo().execute(target_url);
            ff=1;

        } catch(final MalformedURLException e){
            throw new IllegalStateException("Bad URL: " + url, e);
        } catch(final IOException e){

        }

        // WIFI CHECK
        if(ff != 1){
            Toast.makeText(this, "no connection (enable wifi and login)", Toast.LENGTH_LONG).show();
//            MyApp appState = (MyApp)getApplicationContext();
//            appState.setflag2(1);
            finish();
        }
        passPrompt = (EditText) view.findViewById(R.id.password_box);

    }




    // ASSIGNS VALUES TO CLASS VARIABLES FROM THOSE FILLED BY USER
    private class getInfo extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            if(https==true){
                long totalSize = 1;
                String httpsURL = urls[0].toString();

                String uname = ((EditText) findViewById(com.secureexam4_4.app.R.id.username)).getText().toString();//get the user name
                String pword = ((EditText) findViewById(com.secureexam4_4.app.R.id.password)).getText().toString();//get the password
                String ccode = ((EditText) findViewById(com.secureexam4_4.app.R.id.coursecode)).getText().toString();//get the coursecode
//                //Log.d("course code", ccode);

                String query="";
                try {
                    // Construct data
                    String data = URLEncoder.encode("ldapuser", "UTF-8") + "=" + URLEncoder.encode(uname, "UTF-8");
                    data += "&" + URLEncoder.encode("ldappass", "UTF-8") + "=" + URLEncoder.encode(pword, "UTF-8");
                    data += "&" + URLEncoder.encode("coursecode", "UTF-8") + "=" + URLEncoder.encode(ccode, "UTF-8");

                    // Send data
                    URL url = new URL(httpsURL);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    // Get the response
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response_str += line + '\n';
                    }
                    Log.d("Utkarsh",response_str);
                    wr.close();
                    rd.close();

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//                    //Log.d(TAG2, "error in url encoding");
                    no_error = false;
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//                    //Log.d(TAG2, "error with url");
                    no_error = false;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//                    //Log.d(TAG2, "error with sending/receiving data");
                    no_error = false;
                }

                return totalSize;
            }
            else{
                long totalSize = 1;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urls[0].toString());

                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                String uname = ((EditText) findViewById(com.secureexam4_4.app.R.id.username)).getText().toString();//get the user name
                String pword = ((EditText) findViewById(com.secureexam4_4.app.R.id.password)).getText().toString();//get the password
                String ccode = ((EditText) findViewById(com.secureexam4_4.app.R.id.coursecode)).getText().toString();//get the coursecode
//                //Log.d("course code", ccode);
                postParameters.add(new BasicNameValuePair("ldapuser", uname));
                postParameters.add(new BasicNameValuePair("ldappass", pword));
                postParameters.add(new BasicNameValuePair("coursecode", ccode));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                HttpResponse response;
                try {
                    response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    response_str = EntityUtils.toString(entity);
                    Log.d("Utkarsh",response_str);
                }
                catch (Exception e) {}
                return totalSize;
            }
        }



        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPreExecute() {
            response_str = "temp_response";
        }

        // Executed after execute
        // creates cache file if not present
        // saved text field data except password
        protected void onPostExecute(Long result) {
//

            Intent intent = new Intent(MainActivity.this, LoadWebPage.class);
            String coursecode = ((EditText) findViewById(com.secureexam4_4.app.R.id.coursecode)).getText().toString();
            String user = ((EditText) findViewById(com.secureexam4_4.app.R.id.username)).getText().toString();
            String pass = ((EditText) findViewById(R.id.password)).getText().toString();
            String res = ((EditText) findViewById(R.id.source_address)).getText().toString();
            String attend = ((EditText) findViewById(R.id.attend_text)).getText().toString();
            secret_key = ((EditText) findViewById(R.id.password_box)).getText().toString();
            // save the user data code

            File myFile = new File(getCacheDir(), "cachefile");
            try {
                myFile.delete();
                myFile.createNewFile();
              //  System.out.println("CACHE FILE  CREATED");
            } catch (IOException e) {
            //    System.out.println("CACHE FILE  COULDN'T BE CREATED");
            }
            if(myFile.exists()) {
          //      System.out.println("CACHE FILE FOUND WHILE WRITING");
                try {
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(attend+"\n");
                    myOutWriter.append(user+"\n");
                    myOutWriter.append(res+"\n");
                    myOutWriter.close();
                    fOut.close();
                } catch (IOException e) {

                }
            }else{
        //        System.out.println("CACHE FILE NOT FOUND WHILE WRITING");
            }


            Bundle b = new Bundle();
            b.putString("uname",user);
            b.putString("pword",pass);
            b.putString("src_addr",res);
            b.putString("attend_addr",attend);
            b.putString("secret",secret_key);
            System.out.println(secret_key + " abra ");
            intent.putExtras(b);
//            intent.putExtra(username1,user);
//            intent.putExtra(password1,pass);
            //System.out.println(user + " " + pass);
            startActivity(intent);
//            finish();
        }
    }


    // called when "EXIT" pressed. exits the app
    public void validate_exit_code(View view){
        MyApp appState = ((MyApp)getApplicationContext());
        appState.setbundle(null);
        appState.setflag(1);
        appState.setflag2(1);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("EXIT",true);
        startActivity(i);
        getPackageManager().clearPackagePreferredActivities(getPackageName());
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
//        super.onResume();
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);     //NIXIT
        am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME );


        MyApp appState = (MyApp)getApplicationContext();
        if(appState.getflag2() == 1){
            finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApp appState = (MyApp)getApplicationContext();
        if(appState.getflag2() == 1){
            finish();
        }
        if(appState.getGpo() == 1 && flagbaba==1){
            System.out.println("CALLING LOADWEBPAGe");
            Intent intent = new Intent(MainActivity.this, LoadWebPage.class);
            String user =((EditText) findViewById(com.secureexam4_4.app.R.id.username)).getText().toString();
            String pass = ((EditText) findViewById(R.id.password)).getText().toString();
            String res = ((EditText) findViewById(R.id.source_address)).getText().toString();
            secret_key = ((EditText) findViewById(R.id.password_box)).getText().toString();
            Bundle b = new Bundle();
            b.putString("uname",user);
            b.putString("pword",pass);
            b.putString("src_addr",res);
            b.putString("secret",secret_key);
            intent.putExtras(b);
//            intent.putExtra(username1,user);
//            intent.putExtra(password1,pass);
            //System.out.println(user + " " + pass);
            startActivity(intent);
        }
        //*** end

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(bluetooth_receiver);
//        unregisterReceiver(nt_receiver);
    }


    // override "BACK" and "HOME" key input to nothing
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(exit_ready){
                MyApp appState = ((MyApp)getApplicationContext());
                appState.setbundle(null);
                appState.setflag(1);
                appState.setflag2(1);

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                startActivity(i);
                finish();
                super.onBackPressed();
            }
            else{
                return false;  //false is returned when we want the keydown event to be handled by next receiver
            }
            
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if(exit_ready){
               return super.onKeyDown(keyCode, event);
            }

            else{
                //Log.d("KeyPressed", "menu button pressed!!");
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            if(exit_ready){
                return super.onKeyDown(keyCode, event);
            }

            else{
                //Log.d("KeyPressed", "home button pressed!!");
                return false;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            if(exit_ready){
                return super.onKeyDown(keyCode, event);
            }

            else{
                //Log.d("KeyPressed", "search button pressed!!");
                return false;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            if(exit_ready){
                return super.onKeyDown(keyCode, event);
            }
            else{
                //Log.d("KeyPressed", "settings button pressed!!");
                return false;
            }
        }
        return false;
        //return super.onKeyDown(keyCode, event);
    }


}

class MyAdapter extends FragmentPagerAdapter {

    FragmentSymbolLogin fragment2 = null;
    FragmentEnter fragment1 = null;
    ActionBarActivity activity;
    public MyAdapter(android.support.v4.app.FragmentManager fm , ActionBarActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            fragment1 = new FragmentEnter();
            return fragment1;
        }
        else {
            fragment2 = new FragmentSymbolLogin();
            fragment2.setCommunicator((FragmentSymbolLogin.Communicator) activity);
            return fragment2;
        }
    }

    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Home";
        if (position == 1)
            return "Symbol Password";
        return null;
    }

}