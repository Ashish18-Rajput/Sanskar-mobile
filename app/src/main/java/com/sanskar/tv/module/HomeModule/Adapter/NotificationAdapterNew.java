package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanViewAllFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajansCategoryFragment;
import com.sanskar.tv.module.HomeModule.Fragment.DownloadsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Fragment.NewsDetailFrag;

import com.sanskar.tv.module.HomeModule.Fragment.NotificationFragmentNew;
import com.sanskar.tv.module.HomeModule.NotiFicationViewData;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.News;
import com.sanskar.tv.module.HomeModule.Pojo.NotificationTypeBean;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sanskar.tv.module.HomeModule.Activity.HomeActivityy.notifyTV;


/**
 * Created by appsquadz on 2/14/2018.
 */

public class NotificationAdapterNew extends ArrayAdapter<NotificationTypeBean> implements NetworkCall.MyNetworkCallBack {
    public  static LinearLayout ll_chckbox_new;
    public  static CheckBox checkBox_new;
    public  static  int Position_pos;
    public static List<String> list_of_noti_pos = new ArrayList<String>();
    public static List<String> list_of_noti_id = new ArrayList<String>();
    public static int pos;
    public static String pos_id;
    List<NotificationTypeBean> notificationTypeList;
    Context context;
    private int focusedItem = 0;
    NotiFicationViewData notiFicationViewData;
    CheckBox cb1;
    private NetworkCall networkCall;
    private String notificationType;
    private String PostId;
    private String NotificationId;
    private Bhajan[] bhajans = new Bhajan[1];
    private Videos[] videos = new Videos[1];
    public static  Bhajan[] bhajanforImages;
   // Videos videos;
    String video_url;
    ArrayList<Videos>videosArrayList;
    int resource;

    public NotificationAdapterNew(List<NotificationTypeBean> notificationTypeList,Context context) {
        super(context,R.layout.layout_notification_adapter, notificationTypeList);
        this.context = context;
        this.resource = resource;
        this.notificationTypeList = notificationTypeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      //  return super.getView(position, convertView, parent);

      //  return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_notification_adapter,null));

      //  LayoutInflater inflater=context.getL;
        View rowView=convertView;
        Position_pos=position;

        CircleImageView artistImage;
        TextView channelName, description, date;
       ConstraintLayout layout;
         TextView viewTV;
        CardView inside_card,outside_card;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
      //  LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         rowView=layoutInflater.inflate(R.layout.layout_notification_adapter, parent,false);

        artistImage = rowView.findViewById(R.id.artist_image_single_item_notification);
        channelName = rowView.findViewById(R.id.channel_name_single_item_notification);
        description = rowView.findViewById(R.id.description_single_item_notification);
        date = rowView.findViewById(R.id.date_single_item_notification);
        layout = rowView.findViewById(R.id.single_item_layout_video);
        viewTV = rowView.findViewById(R.id.views_number_single_item);
        outside_card = rowView.findViewById(R.id.outside_card);
        ll_chckbox_new = rowView.findViewById(R.id.ll_chckbox);
        checkBox_new = rowView.findViewById(R.id.checkbox);




        return rowView;

    }


    @Override
    public int getCount() {
        return 10;
    }




    @Override
    public int getViewTypeCount() {
        return 10;
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion =null;
        if(apitype.equals(API.NOTIFICATION_DETAIL)){
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id",((HomeActivityy)context).signupResponse.getId())
                    .setMultipartParameter("post_id", PostId)
                    .setMultipartParameter("post_type",notificationType);
        }
        else if(apitype.equals(API.NOTIFICATION_VIEW)){

            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id",((HomeActivityy)context).signupResponse.getId())
                    .setMultipartParameter("notification_id", NotificationId);

        }
        return ion;
    }
    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {

        if (apitype.equals(API.NOTIFICATION_VIEW)) {
            if(result.optBoolean("status")   ){
                String dataString = result.optString("data");
                JSONObject jsonObject = new JSONObject(dataString);
                if(jsonObject!=null){
                    // activity.homevideourl = result.optString("home_video");
                    notiFicationViewData= new Gson().fromJson(jsonObject.toString(), NotiFicationViewData.class);
                    String notification_counter=String.valueOf(notiFicationViewData.getNotificationCounter());
                    Log.d("check_counter",String.valueOf(notification_counter));


                    SharedPreference.getInstance(context).putString(Constants.NOTIFICATION_COUNTER, notification_counter );

                    SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER);


                    notifyTV.setText(SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER));


                }

            }
            else {
                String dataString = result.optString("data");
                JSONObject jsonObject = new JSONObject(dataString);
                if(jsonObject!=null){
                    // activity.homevideourl = result.optString("home_video");
                    notiFicationViewData= new Gson().fromJson(jsonObject.toString(), NotiFicationViewData.class);
                    String notification_counter=String.valueOf(notiFicationViewData.getNotificationCounter());
                    Log.d("check_counter_after",String.valueOf(notification_counter));


                    SharedPreference.getInstance(context).putString(Constants.NOTIFICATION_COUNTER, notification_counter );

                    SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER);


                    notifyTV.setText(SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER));}
            }

        }
        else if(apitype.equals(API.NOTIFICATION_DETAIL)){
            if (result.optBoolean("status")) {
                Bundle bundle = new Bundle();
                String dataString = result.optString("data");
                JSONObject jsonObject = new JSONObject(dataString);
                if (jsonObject != null) {
                    if (notificationType.equalsIgnoreCase(Constants.BHAJAN)) {
                        bhajans[0] = new Gson().fromJson(jsonObject.toString(), Bhajan.class);

                        if (AudioPlayerService.mediaPlayer != null) {
                            Constants.IS_PAUSEDFROMHOME = "true";
                            Constants.CALL_RESUME = "false";
                            AudioPlayerService.mediaPlayer.stop();
                        }
                        ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);
                        if (!DownloadsFragment.serviceBound) {
                            if (!NotificationFragmentNew.serviceBound) {
                                if (!BhajanViewAllFragment.serviceBound) {
                                    if (!HomeFragment.serviceBound) {
                                        if (!BhajansCategoryFragment.serviceBound) {

                                            Constants.AUDIO_ACTIVE_LIST = "notificationbhajan";
                                            Intent intentservice = new Intent(context, AudioPlayerService.class);
                                            intentservice.putExtra("bhajanarray", bhajans);
                                            intentservice.putExtra("status", "bhajan");
                                            intentservice.putExtra("redirect",Constants.AUDIO_ACTIVE_LIST);
                                            PreferencesHelper.getInstance().setValue("index", 0);
                                            PreferencesHelper.getInstance().setValue("audioplayindex",0);

                                            context.startService(intentservice);
                                            context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                            Constants.IS_PLAYING_ON_CATEGORY = "false";
                                            // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                            Constants.LIST_INDEX = 0;
                                            bhajanforImages =bhajans;
                                            bundle.putInt("position", 0);
                                            bundle.putSerializable("bhajans", bhajans);
                                            Fragment fragment = new BhajanPlayFragment();
                                            fragment.setArguments(bundle);
                                            ((HomeActivityy) context)
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .addToBackStack("BHAJANS")
                                                    .replace(R.id.container_layout_home, fragment)
                                                    .commit();
                                        } else {


                                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                            intent.putExtra("bhajanlist", bhajans);
                                            bundle.putSerializable("bhajanlist", bhajans);
                                            bhajanforImages =bhajans;
                                            Constants.LIST_INDEX = 0;
                                            PreferencesHelper.getInstance().setValue("index", 0);
                                            intent.putExtra("position", 0);
                                            context.sendBroadcast(intent);
                                            Constants.IS_PLAYING_ON_CATEGORY = "false";
                                            // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                            Constants.LIST_INDEX = 0;
                                            bhajanforImages =bhajans;
                                            bundle.putInt("position", 0);
                                            bundle.putSerializable("bhajans", bhajans);
                                            Fragment fragment = new BhajanPlayFragment();
                                            fragment.setArguments(bundle);
                                            ((HomeActivityy) context)
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .addToBackStack("BHAJANS")
                                                    .replace(R.id.container_layout_home, fragment)
                                                    .commit();

                                        }

                                    } else {

                                        Constants.AUDIO_ACTIVE_LIST = "notificationbhajan";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", bhajans);
                                        bundle.putSerializable("bhajanlist", bhajans);
                                        bhajanforImages =bhajans;
                                        Constants.LIST_INDEX = 0;
                                        PreferencesHelper.getInstance().setValue("index", 0);
                                        intent.putExtra("position", 0);
                                        context.sendBroadcast(intent);
                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                        Constants.LIST_INDEX = 0;
                                        bhajanforImages =bhajans;
                                        bundle.putInt("position", 0);
                                        bundle.putSerializable("bhajans", bhajans);
                                        Fragment fragment = new BhajanPlayFragment();
                                        fragment.setArguments(bundle);
                                        ((HomeActivityy) context)
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .addToBackStack("BHAJANS")
                                                .replace(R.id.container_layout_home, fragment)
                                                .commit();


                                    }
                                } else {
                                    //Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                    intent.putExtra("bhajanlist", bhajans);
                                    bundle.putSerializable("bhajanlist", bhajans);
                                    bhajanforImages =bhajans;
                                    Constants.LIST_INDEX = 0;
                                    PreferencesHelper.getInstance().setValue("index", 0);
                                    intent.putExtra("position", 0);
                                    context.sendBroadcast(intent);
                                }
                            } else {
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist", bhajans);
                                bundle.putSerializable("bhajanlist", bhajans);
                                bhajanforImages =bhajans;
                                Constants.LIST_INDEX = 0;
                                PreferencesHelper.getInstance().setValue("index", 0);
                                intent.putExtra("position", 0);
                                context.sendBroadcast(intent);
                            }
                        } else {
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("bhajanlist", bhajans);
                            bundle.putSerializable("bhajanlist", bhajans);
                            Constants.LIST_INDEX = 0;
                            bhajanforImages =bhajans;
                            PreferencesHelper.getInstance().setValue("index", 0);
                            intent.putExtra("position", 0);
                            context.sendBroadcast(intent);
                        }


                    } else if (notificationType.equalsIgnoreCase(Constants.NEWS)) {
                        News news = new Gson().fromJson(jsonObject.toString(), News.class);
                        bundle.putSerializable(Constants.NEWS_DATA, news);
                        NewsDetailFrag fragment = new NewsDetailFrag();
                        fragment.setArguments(bundle);
                        ((HomeActivityy) context)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack(Constants.READ_NEWS)
                                .replace(R.id.container_layout_home, fragment)
                                .commit();
                    }
                    else if (notificationType.equalsIgnoreCase(Constants.VIDEO)) {
                        videos[0] = new Gson().fromJson(jsonObject.toString(), Videos.class);

                        bundle.putInt("position", 0);
                        bundle.putSerializable("video_data",videos);


                        if (context instanceof HomeActivityy) {

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("position", "0");
                            context.startActivity(intent);
                        }


                    }
                }
            }}
    }
    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        ToastUtil.showDialogBox(context, jsonstring);
    }




    private void getBhajanNotification(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_DETAIL, progress);
        }
        else{
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    private void Checkviewed(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_VIEW, progress);
        }
        else{
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void getNewsNotification(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_DETAIL, progress);
        }
        else{
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    private void getVideosNotification(boolean progress) {

        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_DETAIL, progress);
        }
        else{
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


}

