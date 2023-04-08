package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.BhajanInterface;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajansCategoryFragment;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.squareup.picasso.Picasso;

/**
 * Created by appsquadz on 4/10/2018.
 */

public class BhajanAdapter1 extends RecyclerView.Adapter<BhajanAdapter1.ViewHolder> {

    Context context;
    Integer[] images = new Integer[]{};
    String[] imgs = new String[]{};
    LayoutInflater layoutInflater;
    BhajanInterface bhajanInterface;
    public MediaPlayer mediaPlayer;
    public Bhajan bhajan;
    Fragment fragment;
    MenuMasterList[] bhajans;
    boolean is_active;
    public  static int selectedposition2;

    public BhajanAdapter1(Context context, MenuMasterList[] bhajans, String[] imgs, BhajanInterface bhajanInterface, Fragment fragment) {
        this.context = context;
        this.bhajans=bhajans;
        this.imgs = imgs;
        this.is_active=is_active;
        this.bhajanInterface = bhajanInterface;
        this.fragment = fragment;
        //  layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bhajan_player_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (images.length > 0)
            Picasso.with(context).load(images[position]).placeholder(R.mipmap.landscape_placeholder).into(holder.imageView);
        else {
            Picasso.with(context).load(Uri.parse(imgs[position])).placeholder(R.mipmap.landscape_placeholder).into(holder.imageView);

        }
        holder.imageView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (fragment instanceof BhajanPlayFragment) {
                    //is_active=false;

                    ((BhajanPlayFragment) fragment).scrollPlay(position);
                    Constants.DONT_SHOW="false";
                    Intent intent=new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                    intent.putExtra("bhajanlist1",bhajans);
                    PreferencesHelper.getInstance().setValue("index",position);
                    intent.putExtra("position",position);

                //    selectedposition2=position;

                    context.sendBroadcast(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (images.length > 0)
            return images.length;
        return imgs.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView,imageView1;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bhajan_img);
            imageView1 = itemView.findViewById(R.id.bhajan_img1);
        }
    }
}
