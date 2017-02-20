package blossom.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import blossom.annotations.OnClick;
import blossom.annotations.OnLongClick;
import blossom.annotations.TieString;
import blossom.annotations.TieView;
import blossom.core.Blossom;

public class MainActivity extends AppCompatActivity {

    @TieString(R.string.app_name)
    String appName1;

    @TieString(R.string.button_name)
    String buttonName;

    @TieView(R.id.textview)
    TextView textView;

    @TieView(R.id.textview) // TODO: 17/02/19 应该直接抛异常
    TextView duplicatedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Blossom.tie(this);

        textView.setText(appName1);
        Button button = (Button) findViewById(R.id.button);
        button.setText(buttonName);
    }

    @OnClick(R.id.button)
    void startActivity(View v) {
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
    }

    @OnLongClick(R.id.button)
    void showToast(View v) {
        Toast.makeText(this, "long click", Toast.LENGTH_SHORT).show();
    }
}
