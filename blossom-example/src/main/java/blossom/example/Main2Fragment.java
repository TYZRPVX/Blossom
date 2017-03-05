package blossom.example;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import blossom.annotations.TieView;
import blossom.core.Blossom;

public class Main2Fragment extends Fragment {

    @TieView(R.id.fragment_main2_textview)
    TextView fragment_main2_textview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main2, container, false);
//        Blossom.tie(this, view);
        return view;
    }
}
