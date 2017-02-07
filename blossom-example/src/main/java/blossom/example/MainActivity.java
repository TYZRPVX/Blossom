package blossom.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import blossom.annotations.TieString;
import blossom.core.Blossom;

public class MainActivity extends AppCompatActivity {

    @TieString(R.string.app_name)
    public String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Blossom.tie(this);
//        String string = this.getResources().getString(2131099668);

        TextView textView = (TextView) findViewById(R.id.textview);
        textView.setText(appName);
    }
}
