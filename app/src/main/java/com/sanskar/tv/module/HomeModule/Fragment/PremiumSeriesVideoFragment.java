package com.sanskar.tv.module.HomeModule.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanskar.tv.R;

public class PremiumSeriesVideoFragment extends Fragment {



    public static PremiumSeriesVideoFragment newInstance(String param1) {
        PremiumSeriesVideoFragment fragment = new PremiumSeriesVideoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_premium_series_video, container, false);
    }
}