package com.example.akshay.tipsatandroidchallenge.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.akshay.tipsatandroidchallenge.R;

/**
 * Created by akshay on 24/10/15.
 */
public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = new ProfileFragment();
        fragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().replace(R.id.layout, fragment).commit();
    }

}
