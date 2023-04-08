package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomTouchListener;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanViewAllFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajansCategoryFragment;
import com.sanskar.tv.module.HomeModule.Fragment.DownloadsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Fragment.PlayListFrag;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.BhajanResponse;
import com.sanskar.tv.R;
import com.sanskar.tv.onItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class BhajanCategoryListAdapter extends RecyclerView.Adapter<BhajanCategoryListAdapter.ViewHolder> {

    List<BhajanResponse> bhajanResponseArrayList;
    Context context;
    public static RecyclerView recyclerView;
    public static Bhajan[] bhajanlist;
    public  static int selectedposition;
    int scrollCount = 0;
    private final static int FADE_DURATION = 1000;
   // public  static LinearLayoutManager layoutManager;
  //  public static Bhajan[]abc= new Bhajan[20];

    public BhajanCategoryListAdapter(ArrayList<BhajanResponse> bhajanResponseArrayList, Context context) {
        this.bhajanResponseArrayList = bhajanResponseArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_category_bhajan,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(bhajanResponseArrayList.get(position).getBhajan().length>0) {
            holder.categoryName.setText(bhajanResponseArrayList.get(position).getCategory_name());
            holder.viewAll.setTag(position);
            //holder.bhajanCategorySingleItemAdapter = new BhajanCategorySingleItemAdapter(bhajanResponseArrayList.get(position).getBhajan(), position, context);
            holder.bhajanCategorySingleItemAdapter = new BhajanCategorySingleItemAdapterRecent(bhajanResponseArrayList.get(position).getBhajan(), position, context);
            bhajanlist = bhajanResponseArrayList.get(position).getBhajan();
            recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);
            holder.bhajanCategorySingleItemAdapter.notifyDataSetChanged();

         /*   if(position==0){
                holder.bhajanCategorySingleItemAdapter = new BhajanCategorySingleItemAdapterRecent(bhajanResponseArrayList.get(position).getBhajan(), position, context);
                bhajanlist = bhajanResponseArrayList.get(position).getBhajan();
                recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);
                holder.bhajanCategorySingleItemAdapter.notifyDataSetChanged();
                MovableRecycler(recyclerView,layoutManager);

            }
            else if(position==1){
                holder.bhajanCategorySingleItemAdapter = new BhajanCategorySingleItemAdapterRecent(bhajanResponseArrayList.get(position).getBhajan(), position, context);
                bhajanlist = bhajanResponseArrayList.get(position).getBhajan();
                recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);
                holder.bhajanCategorySingleItemAdapter.notifyDataSetChanged();
                MovableRecycler(recyclerView,layoutManager);


            } else if(position==2){
                holder.bhajanCategorySingleItemAdapter = new BhajanCategorySingleItemAdapterRecent(bhajanResponseArrayList.get(position).getBhajan(), position, context);
                bhajanlist = bhajanResponseArrayList.get(position).getBhajan();
                recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);
                holder.bhajanCategorySingleItemAdapter.notifyDataSetChanged();
                MovableRecycler(recyclerView,layoutManager);


            }else if(position==3){
                holder.bhajanCategorySingleItemAdapter = new BhajanCategorySingleItemAdapterRecent(bhajanResponseArrayList.get(position).getBhajan(), position, context);
                bhajanlist = bhajanResponseArrayList.get(position).getBhajan();
                recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);
                holder.bhajanCategorySingleItemAdapter.notifyDataSetChanged();
                MovableRecycler(recyclerView,layoutManager);


            }else if(position==4){
                holder.bhajanCategorySingleItemAdapter = new BhajanCategorySingleItemAdapterRecent(bhajanResponseArrayList.get(position).getBhajan(), position, context);
                bhajanlist = bhajanResponseArrayList.get(position).getBhajan();
                recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);
                holder.bhajanCategorySingleItemAdapter.notifyDataSetChanged();
                MovableRecycler(recyclerView,layoutManager);

            }*/


            //TODO by SUMIT -31-7-20
        Log.d("checking","1");






            holder.viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", bhajanResponseArrayList.get(position));
                    Fragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }
            });
        }
        else{
            holder.categoryName.setVisibility(View.GONE);
            holder.viewAll.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        recyclerView.addOnItemTouchListener(new CustomTouchListener(context, new onItemClickListener() {
            @Override
            public void onClick(View view, int audioindex) {



                if (HomeActivityy.playerlayout1.getVisibility() == View.VISIBLE)
                    HomeActivityy.playerlayout1.setVisibility(View.GONE);

                if (HomeActivityy.playerlayout2.getVisibility() == View.VISIBLE)
                    HomeActivityy.playerlayout2.setVisibility(View.GONE);

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
                if (bhajanResponseArrayList.get(position).bhajan[audioindex].getDirect_play().equals("1")) {

                    Bundle bundle = new Bundle();
                    //changes on 17-04-19
                    try {
                        if (AudioPlayerService.mediaPlayer != null) {
                            Constants.IS_PAUSEDFROMHOME = "true";
                            Constants.CALL_RESUME = "false";
                            AudioPlayerService.mediaPlayer.stop();
                        }
                        ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);

                        if (!PlayListFrag.serviceBound) {
                            if (!DownloadsFragment.serviceBound) {
                                if (!BhajanViewAllFragment.serviceBound) {

                                    if (!BhajansCategoryFragment.serviceBound) {
                                        if (!HomeFragment.serviceBound) {
                                            Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                            Intent intentservice = new Intent(context, AudioPlayerService.class);
                                            intentservice.putExtra("status", "bhajan");
                                            intentservice.putExtra("redirect",Constants.AUDIO_ACTIVE_LIST);
                                            intentservice.putExtra("bhajanarray", bhajanResponseArrayList.get(position).getBhajan());

                                            // bundle.putSerializable("bhajanlist",bhajanResponseArrayList.get(position).getBhajan());
                                            //Constants.AUDIO_INDEX=audioindex;
                                            PreferencesHelper.getInstance().setValue("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                            PreferencesHelper.getInstance().setValue("bhajan", bhajanResponseArrayList.get(position).getBhajan());
                                          //  abc= bhajanResponseArrayList.get(position).getBhajan();
                                            PreferencesHelper.getInstance().setValue("audioplayindex",position);

                                            PreferencesHelper.getInstance().setValue("index", audioindex);
                                        //    Log.d("songarray2","songArray2:"+PreferencesHelper.getInstance().get);

                                            context.startService(intentservice);
                                            context.bindService(intentservice, BhajansCategoryFragment.serviceConnection, Context.BIND_AUTO_CREATE);

                                            Constants.IS_PLAYING_ON_CATEGORY = "false";
                                            // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                            Constants.LIST_INDEX = position;

                                            Bundle bundle1 = new Bundle();
                                         //   bundle1.putInt("position", position);
                                            bundle1.putInt("position", audioindex);
                                            bundle1.putSerializable("bhajans", bhajanResponseArrayList.get(position).getBhajan());

                                           selectedposition=position;
                                            ((HomeActivityy) context).back1="";

                                         //   ((HomeActivityy)context).loadsong(bhajanResponseArrayList.get(position).getBhajan(),audioindex);
                                            Fragment fragment = new BhajanPlayFragment();
                                            fragment.setArguments(bundle1);
                                            ((HomeActivityy) context)
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .addToBackStack("BHAJANS")
                                                    .replace(R.id.container_layout_home, fragment)
                                                    .commit();


                                            // getActivity().bindService(intentservice,)
                                        } else {
                                            Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                            intent.putExtra("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                            PreferencesHelper.getInstance().setValue("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                            bundle.putSerializable("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                            Constants.LIST_INDEX = position;
                                            PreferencesHelper.getInstance().setValue("index", audioindex);
                                            //TODO by sumit
                                            PreferencesHelper.getInstance().setValue("audioplayindex",position);

                                            intent.putExtra("position", audioindex);
                                            context.sendBroadcast(intent);

                                            Bundle bundle1 = new Bundle();
                                            //   bundle1.putInt("position", position);
                                            bundle1.putInt("position", audioindex);
                                            bundle1.putSerializable("bhajans", bhajanResponseArrayList.get(position).getBhajan());

                                            selectedposition=position;
                                            ((HomeActivityy) context).back1="";

                                            //   ((HomeActivityy)context).loadsong(bhajanResponseArrayList.get(position).getBhajan(),audioindex);
                                            Fragment fragment = new BhajanPlayFragment();
                                            fragment.setArguments(bundle1);
                                            ((HomeActivityy) context)
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .addToBackStack("BHAJANS")
                                                    .replace(R.id.container_layout_home, fragment)
                                                    .commit();
                                        }
                                    } else {
                                        Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                        PreferencesHelper.getInstance().setValue("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                        bundle.putSerializable("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                        Constants.LIST_INDEX = position;
                                        PreferencesHelper.getInstance().setValue("index", audioindex);
                                        //TODO by sumit
                                        PreferencesHelper.getInstance().setValue("audioplayindex",position);

                                        intent.putExtra("position", audioindex);
                                        context.sendBroadcast(intent);

                                        Bundle bundle1 = new Bundle();
                                        //   bundle1.putInt("position", position);
                                        bundle1.putInt("position", audioindex);
                                        bundle1.putSerializable("bhajans", bhajanResponseArrayList.get(position).getBhajan());

                                        selectedposition=position;
                                        ((HomeActivityy) context).back1="";

                                        //   ((HomeActivityy)context).loadsong(bhajanResponseArrayList.get(position).getBhajan(),audioindex);
                                        Fragment fragment = new BhajanPlayFragment();
                                        fragment.setArguments(bundle1);
                                        ((HomeActivityy) context)
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .addToBackStack("BHAJANS")
                                                .replace(R.id.container_layout_home, fragment)
                                                .commit();
                                    }
                                } else {
                                    Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                    intent.putExtra("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                    PreferencesHelper.getInstance().setValue("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                    bundle.putSerializable("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                    Constants.LIST_INDEX = position;
                                    PreferencesHelper.getInstance().setValue("index", audioindex);
                                    //TODO by sumit
                                    PreferencesHelper.getInstance().setValue("audioplayindex",position);
                                    intent.putExtra("position", audioindex);
                                    context.sendBroadcast(intent);

                                    Bundle bundle1 = new Bundle();
                                    //   bundle1.putInt("position", position);
                                    bundle1.putInt("position", audioindex);
                                    bundle1.putSerializable("bhajans", bhajanResponseArrayList.get(position).getBhajan());

                                    selectedposition=position;
                                    ((HomeActivityy) context).back1="";

                                    //   ((HomeActivityy)context).loadsong(bhajanResponseArrayList.get(position).getBhajan(),audioindex);
                                    Fragment fragment = new BhajanPlayFragment();
                                    fragment.setArguments(bundle1);
                                    ((HomeActivityy) context)
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .addToBackStack("BHAJANS")
                                            .replace(R.id.container_layout_home, fragment)
                                            .commit();
                                }
                            } else {
                                Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                PreferencesHelper.getInstance().setValue("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                bundle.putSerializable("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                                Constants.LIST_INDEX = position;

                                //TODO by sumit

                                PreferencesHelper.getInstance().setValue("audioplayindex",position);
                                intent.putExtra("position", audioindex);
                                context.sendBroadcast(intent);

                                Bundle bundle1 = new Bundle();
                                //   bundle1.putInt("position", position);
                                bundle1.putInt("position", audioindex);
                                bundle1.putSerializable("bhajans", bhajanResponseArrayList.get(position).getBhajan());

                                selectedposition=position;
                                ((HomeActivityy) context).back1="";

                                //   ((HomeActivityy)context).loadsong(bhajanResponseArrayList.get(position).getBhajan(),audioindex);
                                Fragment fragment = new BhajanPlayFragment();
                                fragment.setArguments(bundle1);
                                ((HomeActivityy) context)
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack("BHAJANS")
                                        .replace(R.id.container_layout_home, fragment)
                                        .commit();
                            }
                        }else{
                            Constants.AUDIO_ACTIVE_LIST = "bhajan";
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                            PreferencesHelper.getInstance().setValue("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                            bundle.putSerializable("bhajanlist", bhajanResponseArrayList.get(position).getBhajan());
                            Constants.LIST_INDEX = position;
                            PreferencesHelper.getInstance().setValue("index", audioindex);

                            //TODO by sumit
                            PreferencesHelper.getInstance().setValue("audioplayindex",position);


                            intent.putExtra("position", audioindex);
                            context.sendBroadcast(intent);

                            Bundle bundle1 = new Bundle();
                            //   bundle1.putInt("position", position);
                            bundle1.putInt("position", audioindex);
                            bundle1.putSerializable("bhajans", bhajanResponseArrayList.get(position).getBhajan());

                            selectedposition=position;
                            ((HomeActivityy) context).back1="";

                            //   ((HomeActivityy)context).loadsong(bhajanResponseArrayList.get(position).getBhajan(),audioindex);
                            Fragment fragment = new BhajanPlayFragment();
                            fragment.setArguments(bundle1);
                            ((HomeActivityy) context)
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack("BHAJANS")
                                    .replace(R.id.container_layout_home, fragment)
                                    .commit();
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
                else if (bhajanResponseArrayList.get(position).bhajan[audioindex].getDirect_play().equals("2")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bhajanViewAllByGod", bhajanResponseArrayList.get(position).bhajan[audioindex]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment).commit();

                    }

                else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bhajanViewAll", bhajanResponseArrayList.get(position).bhajan[audioindex]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment).commit();
                }
            }

        }));
        setFadeAnimation(holder.itemView);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    private void playAudio(int audioindex){


    }

    @Override
    public int getItemCount() {
        return bhajanResponseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;
        RelativeLayout viewAll;
        RecyclerView.LayoutManager layoutManager;
       // BhajanCategorySingleItemAdapter bhajanCategorySingleItemAdapter;
       BhajanCategorySingleItemAdapterRecent bhajanCategorySingleItemAdapter;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.bhajan_single_category_recycler_view);
            categoryName = itemView.findViewById(R.id.bhajan_single_category_name);
            viewAll = itemView.findViewById(R.id.bhajan_single_category_view_all);
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
       /*     RecyclerView.LayoutManager layoutManager*/


        /*   layoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false){
                @Override
                public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                    Log.d("checking","2");
                    super.smoothScrollToPosition(recyclerView, state, position);
                    Log.d("checking","3");
                    try {
                        LinearSmoothScroller smoothScroller=new LinearSmoothScroller(Objects.requireNonNull(context)){

                            private static final float SPEED = 3500f;// Change this value (default=25f)

                            @Override
                            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                return  SPEED / displayMetrics.densityDpi;
                            }
                        };
                        smoothScroller.setTargetPosition(position);
                        Log.d("checking","4");
                        startSmoothScroll(smoothScroller);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            scrollCount = 0;
            final Handler handler = new Handler();
            Log.d("checking","5");
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition((scrollCount++));
                 *//*   if (scrollCount == bhajanResponseArrayList.get(position).getBhajan().length -2 ) {
                        stockListModels.addAll(stockListModels);
                        scrollStockAdapter.notifyDataSetChanged();
                    }*//*
                    handler.postDelayed(this, 2000);
                    Log.d("checking","6");
                }
            };
            handler.postDelayed(runnable, 2000);

            //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(1000);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //    recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);

            Log.d("checking","7");
*/
        }}




    private void MovableRecycler(RecyclerView rl, LinearLayoutManager llmanager) {



        llmanager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false){
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                Log.d("checking","2");
                super.smoothScrollToPosition(recyclerView, state, position);
                Log.d("checking","3");
                try {
                    LinearSmoothScroller smoothScroller=new LinearSmoothScroller(Objects.requireNonNull(context)){

                        private static final float SPEED = 3500f;// Change this value (default=25f)

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return  SPEED / displayMetrics.densityDpi;
                        }
                    };
                    smoothScroller.setTargetPosition(position);
                    Log.d("checking","4");
                    startSmoothScroll(smoothScroller);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        scrollCount = 0;
        final Handler handler = new Handler();
        Log.d("checking","5");
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rl.smoothScrollToPosition((scrollCount++));
                  /*  if (scrollCount == onTopHomeLiveChannelAdapter.getItemCount() -2 ) {
                        activity.homeChannelList.addAll(activity.homeChannelList);
                        onTopHomeLiveChannelAdapter.notifyDataSetChanged();
                    }*/
                handler.postDelayed(this, 2000);
                Log.d("checking","6");
            }
        };
        handler.postDelayed(runnable, 2000);

        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rl.setLayoutManager(llmanager);
        rl.setHasFixedSize(true);
        rl.setItemViewCacheSize(1000);
        rl.setDrawingCacheEnabled(true);
        rl.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //    recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);

        Log.d("checking","7");

    }
}
