package com.indyzalab.appversioncontrollersample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.indyzalab.appversioncontroller.version.AppVersionApplication;
import com.indyzalab.appversioncontroller.version.AppVersionController;
import com.indyzalab.appversioncontroller.version.log.CLog;
import com.indyzalab.appversioncontrollersample.Network.NetworkConstant;

public class MainActivity extends AppCompatActivity {

    AppVersionController appVersionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        // App Version Controller
        String pathUrl = "/indy1515/AppVersionController/master/app/example.json";
        appVersionController = new AppVersionController
                ((AppVersionApplication)getApplication(),this, NetworkConstant.API_URL,pathUrl
                        ,"1.0.0","com.package.example");
        appVersionController.start();
        CLog.setAllowLog(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        appVersionController.stop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
