package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanViewAllFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajansCategoryFragment;
import com.sanskar.tv.module.HomeModule.Fragment.DownloadsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment1;
import com.sanskar.tv.module.HomeModule.Fragment.PlayListFrag;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;

import java.util.List;

import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

public class BhajanListByMenuMasterAdapter extends RecyclerView.Adapter<BhajanListByMenuMasterAdapter.ViewHolder> {

    Context context;
    List<Bhajan> bhajans;
    Bhajan[] bhajan;
    public static  Bhajan[] bhajanforImagefromHome;
    public static  int newspos1;
    private final static int FADE_DURATION = 1000;
    public BhajanListByMenuMasterAdapter(Context context, List<Bhajan> bhajans) {
        this.context = context;
        this.bhajans = bhajans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_recyclerview_list4,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        if (bhajans!=null){
            bhajan = new Bhajan[bhajans.size()];
            for (int k = 0; k < bhajans.size(); k++) {
                bhajan[k] = bhajans.get(k);
            }
        }
        Glide.with(context)
                .load(bhajans.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .into(viewHolder.imageView);
        viewHolder.textView.setVisibility(View.GONE);
        viewHolder.textView.setText(bhajans.get(position).getTitle());

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivityy)context).homeBhajanList.size() > 0) {
                    ((HomeActivityy)context).homeBhajanList.clear();
                }
                ((HomeActivityy)context).homeBhajanList.addAll(bhajans);
                if (HomeActivityy.playerlayout1.getVisibility() == View.VISIBLE)
                    HomeActivityy.playerlayout1.setVisibility(View.GONE);

                if (HomeActivityy.playerlayout2.getVisibility() == View.VISIBLE)
                    HomeActivityy.playerlayout2.setVisibility(View.GONE);


                Bundle bundle = new Bundle();
                try{

                    if (downloadmediaplayer != null) {
                        if (downloadmediaplayer.isPlaying()){
                            downloadmediaplayer.pause();}}
                    else{

                    }

                    if(playlistmediaplayer!=null){
                        if (playlistmediaplayer.isPlaying()){
                            playlistmediaplayer.pause();}}
                    else{

                    }
                    if (bhajan.length>1){
                        if(bhajans.get(position).getDirect_play().equalsIgnoreCase("1")) {
                            if (!PlayListFrag.serviceBound) {
                                if (!DownloadsFragment.serviceBound) {
                                    if (!BhajanViewAllFragment.serviceBound) {
                                        if (!HomeFragment1.serviceBound) {
                                            if (!BhajansCategoryFragment.serviceBound) {
                                                Constants.AUDIO_ACTIVE_LIST = "home";
                                                Intent intentservice = new Intent(context, AudioPlayerService.class);
                                                intentservice.putExtra("bhajanarray", bhajan);
                                                intentservice.putExtra("status", "bhajan");
                                                intentservice.putExtra("redirect",Constants.AUDIO_ACTIVE_LIST);
                                                PreferencesHelper.getInstance().setValue("index", position);

                                                context.startService(intentservice);
                                                context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                                Constants.IS_PLAYING_ON_CATEGORY = "false";
                                                // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                                Constants.LIST_INDEX = position;
                                                bhajanforImagefromHome=bhajan;
                                                bundle.putInt("position", position);
                                                bundle.putSerializable("bhajans", bhajan);
                                                bundle.putString("BackPressed","BackPressed");
                                                Fragment fragment = new BhajanPlayFragment();
                                                fragment.setArguments(bundle);
                                                ((HomeActivityy) context)
                                                        .getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .addToBackStack("BHAJANS")
                                                        .replace(R.id.container_layout_home, fragment)
                                                        .commit();
                                            } else {
                                                Constants.AUDIO_ACTIVE_LIST = "home";
                                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                                intent.putExtra("bhajanlist", bhajan);
                                                bundle.putSerializable("bhajanlist", bhajan);
                                                Constants.LIST_INDEX = position;

                                                bhajanforImagefromHome=bhajan;

                                                PreferencesHelper.getInstance().setValue("index", position);
                                                intent.putExtra("position", position);
                                                context.sendBroadcast(intent);
                                            }

                                        } else {
                                            Constants.AUDIO_ACTIVE_LIST = "home";
                                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                            intent.putExtra("bhajanlist", bhajan);
                                            bundle.putSerializable("bhajanlist", bhajan);
                                            Constants.LIST_INDEX = position;
                                            bhajanforImagefromHome=bhajan;
                                            PreferencesHelper.getInstance().setValue("index", position);
                                            intent.putExtra("position", position);
                                            context.sendBroadcast(intent);

                                        }
                                    } else {
                                        Constants.AUDIO_ACTIVE_LIST = "home";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", bhajan);
                                        bundle.putSerializable("bhajanlist", bhajan);
                                        Constants.LIST_INDEX = position;
                                        bhajanforImagefromHome=bhajan;
                                        PreferencesHelper.getInstance().setValue("index", position);
                                        intent.putExtra("position", position);
                                        context.sendBroadcast(intent);
                                    }
                                } else {
                                    Constants.AUDIO_ACTIVE_LIST = "home";
                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                    intent.putExtra("bhajanlist", bhajan);
                                    bundle.putSerializable("bhajanlist", bhajan);
                                    Constants.LIST_INDEX = position;
                                    bhajanforImagefromHome=bhajan;
                                    PreferencesHelper.getInstance().setValue("index", position);
                                    intent.putExtra("position", position);
                                    context.sendBroadcast(intent);
                                }
                            }else{
                                Constants.AUDIO_ACTIVE_LIST = "home";
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist", bhajan);
                                bundle.putSerializable("bhajanlist", bhajan);
                                Constants.LIST_INDEX = position;
                                bhajanforImagefromHome=bhajan;
                                PreferencesHelper.getInstance().setValue("index", position);
                                intent.putExtra("position", position);
                                context.sendBroadcast(intent);
                            }
                        }
                        else{
                            bundle.putSerializable("bhajanViewAll",bhajans.get(position));
                            BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                            fragment.setArguments(bundle);
                            ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home,fragment).commit();
                        }
                    }else{
                        Toast.makeText(context, "Please Wait!!!...Still Loading!!!", Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });
       // setFadeAnimation(viewHolder.itemView);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    @Override
    public int getItemCount() {
        return bhajans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            textView=itemView.findViewById(R.id.bhajan_single_category_single_item_text);
        }
    }
}
