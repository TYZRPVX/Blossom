package blossom.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import blossom.annotations.TieString;

public class MainActivity extends AppCompatActivity {

    @TieString(R.string.app_name)
    String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
