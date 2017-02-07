package blossom.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import blossom.annotations.TieString;

public class Main2Activity extends AppCompatActivity {

    @TieString(R.string.button_name)
    String buttonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
