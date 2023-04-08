package com.sanskar.tv.Premium.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sanskar.tv.R;


public class SlidingupFragment extends Fragment{


    LinearLayout mainLayout;
    RelativeLayout secondLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_slidingup, container, false);

        initViews(view);

        return  view;
    }

    private void initViews(View view) {
        mainLayout=view.findViewById(R.id.mainLayout);
        secondLayout=view.findViewById(R.id.SecondLayout);
    }
}