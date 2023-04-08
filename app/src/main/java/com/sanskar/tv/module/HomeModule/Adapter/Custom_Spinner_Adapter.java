package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sanskar.tv.R;

import java.util.List;

public class Custom_Spinner_Adapter extends BaseAdapter {
    Context context;
    List<String> categories;
    LayoutInflater inflter;

    public Custom_Spinner_Adapter(Context applicationContext, List<String> categories) {
        this.context = applicationContext;
        this.categories = categories;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.simple_spinner_drop_down_item, null);
      //  TextView names = (TextView) view.findViewById(R.id.textView);
       // names.setText(categories.get(i));
        return view;
    }
}
