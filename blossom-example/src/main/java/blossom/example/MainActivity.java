package blossom.example;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import blossom.annotations.*;
import blossom.core.Blossom;

public class MainActivity extends AppCompatActivity {

    @TieString(R.string.app_name)
    String appName1;

    @TieString(R.string.button_name)
    String buttonName;

    @TieView(R.id.textview)
    TextView textView;

    @TieDrawable(R.drawable.blue_circle)
    Drawable blueCircleDrawable;

    @TieView(R.id.blue_circle)
    ImageView blueCircleView;

    @TieView(R.id.main_checkbox)
    CheckBox main_checkbox;

    @TieColor(R.color.colorPrimaryDark)
    int colorPrimaryDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Blossom.tie(this);

        textView.setText(appName1);
        textView.setTextColor(colorPrimaryDark);
        Button button = (Button) findViewById(R.id.button);
        button.setText(buttonName);
        blueCircleView.setImageDrawable(blueCircleDrawable);
    }

    @OnCheckedChanged(R.id.main_checkbox)
    void doOnCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, "isChecked: " + isChecked, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button)
    void startActivity(View v) {
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
    }

    @OnTouch(R.id.button)
    boolean doOnTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(this, "doOnTouch MotionEvent.ACTION_DOWN", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

//
//    @OnClick(R.id.button)
//    void startActivity1(View v) {
//        startActivity(new Intent(MainActivity.this, Main2Activity.class));
//    }

    @OnLongClick(R.id.button)
    boolean showToast(View v) {
        Toast.makeText(this, "long click", Toast.LENGTH_SHORT).show();
        return true;
    }
}
