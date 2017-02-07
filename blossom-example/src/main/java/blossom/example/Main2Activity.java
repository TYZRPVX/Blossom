package blossom.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import blossom.annotations.TieString;
import blossom.core.Blossom;

public class Main2Activity extends AppCompatActivity {

    @TieString(R.string.text_view2)
    String textview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Blossom.tie(this);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(textview2);
    }
}
