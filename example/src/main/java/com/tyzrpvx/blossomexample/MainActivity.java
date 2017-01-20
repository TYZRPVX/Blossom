package com.tyzrpvx.blossomexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tyzrpvx.blossom.Test;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("test", Test.test);

        TextView viewById = (TextView) findViewById(R.id.text);
        viewById.setText(Test.test);

    }
}
