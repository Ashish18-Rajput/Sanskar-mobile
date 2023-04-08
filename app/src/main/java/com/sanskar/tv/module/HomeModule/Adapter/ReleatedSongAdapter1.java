package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
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
import com.sanskar.tv.module.HomeModule.Fragment.PlayListFrag;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.util.List;

import static com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment.popupWindowsong;

public class ReleatedSongAdapter1 extends RecyclerView.Adapter<ReleatedSongAdapter1.ReleatedViewHolder> {
    List<MenuMasterList> bhajanList;
    Context context;
    private boolean isArtistBhajan;
    int Type;
    public static  MenuMasterList[] relatedbhajan1;
   // Bhajan[] bhajans;
    public ReleatedSongAdapter1(List<MenuMasterList> bhajanList, Context context) {
        this.bhajanList = bhajanList;
        this.context = context;

    }

    @NonNull
    @Override
    public ReleatedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReleatedSongAdapter1.ReleatedViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_related_song,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ReleatedViewHolder releatedViewHolder, int position) {
        try{
        MenuMasterList bhajanCat=bhajanList.get(position);
        if (bhajanCat.getImage() != null && !TextUtils.isEmpty(bhajanCat.getImage())) {
            Ion.with(context).load(bhajanCat.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    releatedViewHolder.artistImage.setImageBitmap(result);
                }
            });
        } else {
            releatedViewHolder.artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
        }
        if (isArtistBhajan) {
            releatedViewHolder.channelName.setText(bhajanCat.getTitle().trim());
        } else {
            releatedViewHolder.channelName.setText(bhajanCat.getTitle().trim());
        }
        String desc = Html.fromHtml(bhajanCat.getDescription().trim()).toString();
        releatedViewHolder.description.setText(desc);






        releatedViewHolder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MenuMasterList[] bhajans = (MenuMasterList[]) bhajanList.toArray(new MenuMasterList[bhajanList.size()]);

                Bundle bundle = new Bundle();
                if (bhajanCat.getDirectPlay().equalsIgnoreCase("1")) {
                    /*bundle.putInt("position", position);
                    bundle.putSerializable("bhajans", bhajans);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();*/
                    if (!PlayListFrag.serviceBound) {
                        if (!DownloadsFragment.serviceBound) {
                            if (!BhajanViewAllFragment.serviceBound) {
                                if (!HomeFragment.serviceBound) {
                                    if (!BhajansCategoryFragment.serviceBound) {
                                     //   Constants.AUDIO_ACTIVE_LIST = "bhajan";

                                        Constants.AUDIO_ACTIVE_LIST = "relatedbhajan1";

                                        Intent intentservice = new Intent(context, AudioPlayerService.class);
                                        intentservice.putExtra("bhajanarray1", bhajans);
                                        intentservice.putExtra("status", "bhajan");
                                        intentservice.putExtra("redirect",Constants.AUDIO_ACTIVE_LIST);
                                        PreferencesHelper.getInstance().setValue("index", position);
                                       // PreferencesHelper.getInstance().setValue("audioindex", position);

                                        context.startService(intentservice);
                                        context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                        Constants.LIST_INDEX = position;

                                        bundle.putInt("position", position);
                                        bundle.putSerializable("bhajans1", bhajans);
                                        Fragment fragment = new BhajanPlayFragment();
                                        fragment.setArguments(bundle);
                                        ((HomeActivityy) context)
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .addToBackStack("BHAJANS")
                                                .replace(R.id.container_layout_home, fragment)
                                                .commit();

                                        popupWindowsong.dismiss();
                                    } else {
                                     //
                                        //   Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                        Constants.AUDIO_ACTIVE_LIST = "relatedbhajan1";

                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist1", bhajans);
                                        bundle.putSerializable("bhajanlist1", bhajans);
                                        Constants.LIST_INDEX = position;
                                        PreferencesHelper.getInstance().setValue("index", position);
                                        //TODO by sumit
                                      //  PreferencesHelper.getInstance().setValue("index", position);
                                     //   PreferencesHelper.getInstance().setValue("audioindex", position);

                                        relatedbhajan1 =bhajans;

                                        intent.putExtra("position", position);
                                        context.sendBroadcast(intent);

                                        bundle.putInt("position", position);
                                        bundle.putSerializable("bhajans1", bhajans);
                                        Fragment fragment = new BhajanPlayFragment();
                                        fragment.setArguments(bundle);
                                        ((HomeActivityy) context)
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .addToBackStack("BHAJANS")
                                                .replace(R.id.container_layout_home, fragment)
                                                .commit();



                                    /*    Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                        Intent intentservice = new Intent(context, AudioPlayerService.class);
                                        intentservice.putExtra("bhajanarray", bhajans);
                                        intentservice.putExtra("status", "bhajan");
                                        PreferencesHelper.getInstance().setValue("index", position);

                                        context.startService(intentservice);
                                        context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                        Constants.LIST_INDEX = position;

                                        bundle.putInt("position", position);
                                        bundle.putSerializable("bhajans", bhajans);
                                        Fragment fragment = new BhajanPlayFragment();
                                        fragment.setArguments(bundle);
                                        ((HomeActivityy) context)
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .addToBackStack("BHAJANS")
                                                .replace(R.id.container_layout_home, fragment)
                                                .commit();*/


                                        popupWindowsong.dismiss();}

                                } else {
                                    //Constants.AUDIO_ACTIVE_LIST = "bhajan";

                                    Constants.AUDIO_ACTIVE_LIST = "relatedbhajan1";

                                   Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                    intent.putExtra("bhajanlist1", bhajans);
                                    bundle.putSerializable("bhajanlist1", bhajans);
                                    Constants.LIST_INDEX = position;
                                    PreferencesHelper.getInstance().setValue("index", position);
                                    PreferencesHelper.getInstance().setValue("audioindex", position);
                                    intent.putExtra("position", position);
                                    context.sendBroadcast(intent);
                                    relatedbhajan1 =bhajans;

                                    bundle.putInt("position", position);
                                    bundle.putSerializable("bhajans1", bhajans);
                                    Fragment fragment = new BhajanPlayFragment();
                                    fragment.setArguments(bundle);
                                    ((HomeActivityy) context)
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .addToBackStack("BHAJANS")
                                            .replace(R.id.container_layout_home, fragment)
                                            .commit();


                                    popupWindowsong.dismiss();
                                }
                            } else {
                                //Constants.AUDIO_ACTIVE_LIST = "bhajan";

                                //TODO by sumit
                                Constants.AUDIO_ACTIVE_LIST = "relatedbhajan1";

                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist1", bhajans);
                                bundle.putSerializable("bhajanlist1", bhajans);
                                Constants.LIST_INDEX = position;
                                PreferencesHelper.getInstance().setValue("index", position);
                                intent.putExtra("position", position);
                                context.sendBroadcast(intent);

                                //TODO by sumit
                                relatedbhajan1 =bhajans;

                                bundle.putInt("position", position);
                                bundle.putSerializable("bhajans1", bhajans);
                                Fragment fragment = new BhajanPlayFragment();
                                fragment.setArguments(bundle);
                                ((HomeActivityy) context)
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack("BHAJANS")
                                        .replace(R.id.container_layout_home, fragment)
                                        .commit();

                                popupWindowsong.dismiss();

                            }
                        } else {
                            //TODO by sumit
                            Constants.AUDIO_ACTIVE_LIST = "relatedbhajan1";

                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("bhajanlist1", bhajans);
                            bundle.putSerializable("bhajanlist1", bhajans);
                            Constants.LIST_INDEX = position;
                            PreferencesHelper.getInstance().setValue("index", position);
                            intent.putExtra("position1", position);
                            context.sendBroadcast(intent);



                            //TODO by sumit
                            relatedbhajan1 =bhajans;

                            bundle.putInt("position", position);
                            bundle.putSerializable("bhajans", bhajans);
                            Fragment fragment = new BhajanPlayFragment();
                            fragment.setArguments(bundle);
                            ((HomeActivityy) context)
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack("BHAJANS")
                                    .replace(R.id.container_layout_home, fragment)
                                    .commit();

                            popupWindowsong.dismiss();

                        }
                    } else {
                        //TODO by sumit
                        Constants.AUDIO_ACTIVE_LIST = "relatedbhajan1";
                  //......................................................

                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("bhajanlist1", bhajans);
                        bundle.putSerializable("bhajanlist1", bhajans);
                        Constants.LIST_INDEX = position;
                        PreferencesHelper.getInstance().setValue("index", position);
                        intent.putExtra("position", position);
                        context.sendBroadcast(intent);





                        //TODO by sumit
                        relatedbhajan1 =bhajans;

                        bundle.putInt("position", position);
                        bundle.putSerializable("bhajans1", bhajans);
                        Fragment fragment = new BhajanPlayFragment();
                        fragment.setArguments(bundle);
                        ((HomeActivityy) context)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack("BHAJANS")
                                .replace(R.id.container_layout_home, fragment)
                                .commit();


                        popupWindowsong.dismiss();    }


                } else {
                    bundle.putSerializable("bhajanViewAll1", bhajans[position]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home, fragment).commit();
                    popupWindowsong.dismiss();
                }
            }

        });
    }catch (Exception e)
    {
        e.printStackTrace();
    }




}

    @Override
    public int getItemCount() {
        return bhajanList.size();
    }

    public class ReleatedViewHolder extends RecyclerView.ViewHolder{

        private ImageView dotImg;
        ImageView artistImage;
        TextView channelName, description;
        CardView singleItem;


        public ReleatedViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_view_all_bhajan);
            channelName = itemView.findViewById(R.id.channel_name_view_all_bhajan);
            description = itemView.findViewById(R.id.description_view_all_bhajan);
            singleItem = itemView.findViewById(R.id.single_item_view_all_bhajan);
            dotImg = itemView.findViewById(R.id.imageView2);

            dotImg.setVisibility(View.GONE);
        }}
}
