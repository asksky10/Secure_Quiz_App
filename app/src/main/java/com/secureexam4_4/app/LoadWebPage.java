package com.secureexam4_4.app;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


@SuppressLint("SetJavaScriptEnabled")
public class LoadWebPage extends Activity {

    private final String TAG = "MyActivity123";
    private String username;
    private String secret;
    private WebView browser;
    private ArrayList<String> addresses = new ArrayList<String>();
    private String password;
    private boolean wantExit;
    private String addr;
    private boolean connected;
    private int flag =0;
    private int onceConnectedFlag = 0;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //receiver that listens to network connections and disconnections
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
        setContentView(com.secureexam4_4.app.R.layout.activity_load_web_page);

//        set_up_button();

        //Setting up webview browser
        browser = (WebView) findViewById(com.secureexam4_4.app.R.id.webView1);
        browser.setWebViewClient(new MyBrowser());

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.getSettings().setUseWideViewPort(false);
        browser.setWebContentsDebuggingEnabled(true); //this feature doesn't work in versions < 4.3
        browser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //get data stored in application context (can be considered as global data)
        //used to pass username, password, urls and secret key b/w activities
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        MyApp appState = ((MyApp)getApplicationContext());
        int attendquiz = appState.getAttendorquiz();
//      str = intent.getStringExtra(MainActivity.EXAM_URL);
        username = b.getString("uname");
        password = b.getString("pword");
        secret = b.getString("secret");
        if(attendquiz ==1)
            addr = b.getString("src_addr");
        else
            addr = b.getString("attend_addr");
       
        //this is the position when the quiz url is opened for the first time
        // username is automatically filled and hidden
        Bundle d = appState.getbundle();
        int f = appState.getflag();
        if(savedInstanceState != null && f == 0){
            //System.out.println("savedinstance");
            this.onRestoreInstanceState(savedInstanceState);
            if(browser.getUrl().contains("viewform")){
                browser.loadUrl("javascript: { document.forms[0].elements[0].setAttribute('value','"+username+"'); };");
                browser.loadUrl("javascript: {document.forms[0].elements[0].setAttribute('type','hidden');};");
            }

        }

        else if(d != null && f == 0) {

            this.onRestoreInstanceState(d);
            //System.out.println("bundle bits " + browser.getUrl());
            if(browser.getUrl().contains("viewform")){
                //System.out.println("lele");
                browser.loadUrl("javascript: { document.forms[0].elements[0].setAttribute('value','"+username+"'); };");
                browser.loadUrl("javascript: {document.forms[0].elements[0].setAttribute('type','hidden');};");
            }
//            this.onRestoreInstanceState(d);
        }


        else
            open();

//        if(browser.getUrl().endsWith("login/index.php")){
//            super.onoy();
//        }
    }

    @Override
    protected void onResume(){
        super.onResume();
//        MyApp appState = ((MyApp)getApplicationContext());
//        if(appState.getflag2() == 1)
//            finish();
    }

    //save app context and options filled in the quiz
    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        browser.saveState(outState);
        MyApp appState = ((MyApp)getApplicationContext());
        appState.setbundle(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        //restore app context and options filled in the quiz
        super.onRestoreInstanceState(savedInstanceState);
        browser.restoreState(savedInstanceState);
    }


    //standard function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //backpress should give a message if pressed by chance
    //if still wants to exit then clear everything
    //else go back to normal(do nothing)

    @Override
    public void onBackPressed()
    {
        if(!connected) {
            wantExit = false;
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit")
                    .setMessage("Discard all Quiz Data and Exit?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyApp appState = ((MyApp)getApplicationContext());
                            appState.setbundle(null);
                            appState.setflag(1);
                            appState.setflag2(1);
                            appState.setGpo(0);
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("EXIT",true);
                            onceConnectedFlag=0;
                            startActivity(i);
                            getPackageManager().clearPackagePreferredActivities(getPackageName());
                            finish();
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
    }

    //this happens when focus is being taken by some other application
    //cancel all notifications and bring back focus to the application  


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


        //Log.d("Focus debug", "Focus changed !");
        if(!hasFocus) {
            //Log.d("Focus debug", "Lost focus !");
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);     //NIXIT
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME );
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            Intent notifi = new Intent(NOTIFICATION_SERVICE);
            sendBroadcast(notifi);

            sendBroadcast(closeDialog);
        }
    }

    //open gpo
    public void open() {
        String url = "";
        MyApp appState = ((MyApp)getApplicationContext());
        appState.setflag(0);

            browser.loadUrl(addr);
    }

    //check connection by pinging packets to non-local site
    //and then only allow to proceed
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                connected = true;
//                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                if(onceConnectedFlag > 0) {
                    new AlertDialog.Builder(LoadWebPage.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Internet Connected")
                            .setMessage("YOU CAN NOW SUBMIT")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }

                            })
                            .show();
                }
                onceConnectedFlag=0;
            }else{
                connected = false;
                onceConnectedFlag+=1;
//                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(LoadWebPage.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("No Internet")
                        .setMessage("Please Wait. DO NOT SUBMIT")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }

                        })
                        .show();
            }
        }
    };


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //this handles the redirection of urls
        //needless to explain each and every part
        @Override
        public  void onPageFinished(WebView view, String url){


            MyApp appStateOuter = ((MyApp)getApplicationContext());
            appStateOuter.setflag(1);
            appStateOuter.setGpo(1);

            if(url.contains("formResponse")){
                MyApp appState = ((MyApp)getApplicationContext());
                int attendquiz = appState.getAttendorquiz();
                if(attendquiz ==0){
                    Toast.makeText(LoadWebPage.this, "Your attendance has been recorded", Toast.LENGTH_LONG).show();
                    appState.setAttendorquiz(1);
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    appState.setflag(1);
                    appState.setflag2(1);
                    appState.setStart_entry(0);
                    appState.setGpo(1);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
                else {
                    int start = appState.getStart_entry();
                    if (start != 1) {
                        appState.setStart_entry(1);
                        browser.loadUrl(addr);
                    } else {
                        appState.setbundle(null);
                        appState.setflag(1);
                        appState.setflag2(1);
                        appState.setStart_entry(0);
                        appState.setGpo(1);
                        appState.setAttendorquiz(0);
                        if (connected)
                            Toast.makeText(LoadWebPage.this, "Your response has been recorded", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(LoadWebPage.this, "No Network: Response NOT recorded", Toast.LENGTH_LONG).show();


                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);
                        onceConnectedFlag = 0;
                        startActivity(i);
                        getPackageManager().clearPackagePreferredActivities(getPackageName());
                        finish();
                    }
                }
            }else if(url.contains("viewform")){

                MyApp appState = ((MyApp)getApplicationContext());
                int start = appState.getStart_entry();
                if(start == 0 ){
                    browser.loadUrl("javascript: { document.forms[0].elements[0].setAttribute('value','" + "start_"+ username + "'); };");
                    browser.loadUrl("javascript: {" + "var f1 = document.getElementsByTagName('input');" +
                            "f1[f1.length-1].click();};");
                }
                else {
                    browser.loadUrl("javascript: { document.forms[0].elements[0].setAttribute('value','" + username + "'); };");
                    browser.loadUrl("javascript: {document.forms[0].elements[0].setAttribute('type','hidden');};");

                    browser.loadUrl("javascript: { document.forms[0].elements[1].setAttribute('value','" + secret + "'); };");
                    browser.loadUrl("javascript: {document.forms[0].elements[1].setAttribute('type','hidden');};");
                }
            }
        }

        private boolean is_permitted_url(String url) throws URISyntaxException {

            Log.v(TAG, "index=" + addresses + "\t" + getDomainName(url));
            for (String s : addresses) {
                Log.v(TAG, "index=" + getDomainName(s) + "\t"
                        + getDomainName(url));
                if (getDomainName(url).endsWith(getDomainName(s))) {
                    Log.v(TAG, "index=" + url);
                    return true;
                }
            }
            return false;
        }

        public String getDomainName(String url) throws URISyntaxException {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
    }

    //uncomment this code to add a menu to the current activity
//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//      // Inflate the menu; this adds items to the action bar if it is present.
//      getMenuInflater().inflate(R.menu.load_web_page, menu);
//      return true;
//  }


    @Override
    protected void onPause(){
        super.onPause();
        MyApp appState = ((MyApp)getApplicationContext());
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);     //NIXIT
        am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME );
    }


}