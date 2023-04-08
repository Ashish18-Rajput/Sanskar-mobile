package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Adapter.ReleatedSongAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment.Relatedbhajanlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends Fragment {
    RecyclerView releted_song_recycler;
    ReleatedSongAdapter releatedSongAdapter;
    List<Bhajan> bhajanList = new ArrayList<>();

    Context context;
    private LinearLayoutManager linearLayoutManager;

    public SongListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    /*    bhajanAdapter = new BhajanAdapter(context, bhajans, imgString, bhajanInterface, this);
        itemPicker.setAdapter(bhajanAdapter);*/
        context = getActivity();
        bhajanList = Relatedbhajanlist;
        releted_song_recycler = view.findViewById(R.id.releted_song_recycler);

        releatedSongAdapter = new ReleatedSongAdapter(bhajanList, context);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        releted_song_recycler.setLayoutManager(linearLayoutManager);

        releted_song_recycler.setAdapter(releatedSongAdapter);


    }
}
