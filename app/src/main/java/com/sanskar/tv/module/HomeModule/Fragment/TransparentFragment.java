package com.sanskar.tv.module.HomeModule.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanskar.tv.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransparentFragment extends Fragment {

    public TransparentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transparent, container, false);
    }
}
