package blossom.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import blossom.annotations.TieString;
import blossom.core.Blossom;

public class MainActivity extends AppCompatActivity {

    @TieString(R.string.app_name)
    String appName1;

    @TieString(R.string.button_name)
    String buttonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Blossom.tie(this);

        TextView textView = (TextView) findViewById(R.id.textview);
        textView.setText(appName1);

        Button button = (Button) findViewById(R.id.button);
        button.setText(buttonName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }
}
