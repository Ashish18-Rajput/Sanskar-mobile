package com.sanskar.tv.module.HomeModule.Adapter;

import static com.sanskar.tv.module.HomeModule.Activity.HomeActivityy.notifyTV;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.clicktype;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
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
import com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment;
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

/**
 * Created by appsquadz on 2/14/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack {
    public static LinearLayout ll_chckbox;
    public static CheckBox checkbox;
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
    public static Bhajan[] bhajanforImages;
    // Videos videos;
    String video_url;
    ArrayList<Videos> videosArrayList;
    boolean isFromNotificationTap;

    public NotificationAdapter(List<NotificationTypeBean> notificationTypeList, Context context,boolean isFromNotificationTap) {
        this.context = context;
        this.notificationTypeList = notificationTypeList;
        this.isFromNotificationTap = isFromNotificationTap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_notification_adapter, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        pos = position;

      /*  for(int i=0;i<notificationTypeList.size();i++){
        if (notificationTypeList.get(i).isSetcheckbox())*/
        if (notificationTypeList.get(holder.getAdapterPosition()).isSetcheckbox()) {
            holder.ll_chckbox.setVisibility(View.VISIBLE);
            // checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.ll_chckbox.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.GONE);
        }
        if (notificationTypeList.get(holder.getAdapterPosition()).isMarkCheckBox()) {

            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }


        if (notificationTypeList.get(position).isSelected()) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);

        }



        if (notificationTypeList.get(position).getNotificationType().equalsIgnoreCase("URL")) {
            if (notificationTypeList.get(position).getNotification_thumbnail() != null) {
                Ion.with(context).load(notificationTypeList.get(position).getNotification_thumbnail()).asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                //Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                                holder.artistImage.setImageBitmap(result);
                            }
                        });
            }
        } else {
            if (notificationTypeList.get(position).getImage() != null) {
                Ion.with(context).load(notificationTypeList.get(position).getImage()).asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                //Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                                holder.artistImage.setImageBitmap(result);
                            }
                        });
            }
        }
        if (notificationTypeList.get(position).getPostType().equalsIgnoreCase("URL")) {
            holder.channelName.setText("General");
        } else {

            String notification_type1 = notificationTypeList.get(position).getPostType();

            String fist_letter = notification_type1.substring(0, 1).toUpperCase();
            String second_letter = notification_type1.substring(1, notification_type1.length()).toLowerCase();
            String notification_type = fist_letter + second_letter;

            holder.channelName.setText(notification_type);
        }
        holder.description.setText(notificationTypeList.get(position).getText());
        //   holder.date.setText(getDate(Long.parseLong(notificationTypeList.get(position).getCreatedOn())));
        holder.date.setText(notificationTypeList.get(position).getCreatedOn());
        if (notificationTypeList.get(position).getIsView().equalsIgnoreCase("1")) {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.whiteBgColor));
        } else {

        }
        holder.rl_notification.setTag(notificationTypeList.get(position));

        notificationType = notificationTypeList.get(position).getPostType();
        PostId = notificationTypeList.get(position).getPostId();
        NotificationId = notificationTypeList.get(position).getId();
        networkCall = new NetworkCall(NotificationAdapter.this, context);

        if (position == 0 && isFromNotificationTap){
            if (notificationType.equalsIgnoreCase(Constants.BHAJAN)) {
                getBhajanNotification(true);
            } else if (notificationType.equalsIgnoreCase(Constants.NEWS)) {
                getNewsNotification(true);
            } else if (notificationType.equalsIgnoreCase(Constants.VIDEO)) {
                getVideosNotification(true);
            }
        }

        holder.rl_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notificationType = notificationTypeList.get(position).getPostType();
                PostId = notificationTypeList.get(position).getPostId();
                NotificationId = notificationTypeList.get(position).getId();
                networkCall = new NetworkCall(NotificationAdapter.this, context);
                Log.d("checkclicktype", clicktype);
                if (clicktype.equals("clicktypeFromEdit")) {
                    //    Toast.makeText(context, "EDit", Toast.LENGTH_SHORT).show();
                    //  checkboxsetChecked(true);
                    Log.d("checkclicktype", clicktype);

                    if (holder.checkbox.isChecked()) {
                        holder.checkbox.setChecked(false);
                        Log.d("check1", "test5");
                        //    notificationTypeBean.setSelected(cb.isChecked());
                        notificationTypeList.get(holder.getAdapterPosition()).setSelected(holder.checkbox.isChecked());
                        holder.rl_notification.setTag(notificationTypeList.get(position));

                        Log.d("check1", "test5");
                    } else {
                        holder.checkbox.setChecked(true);
                        Log.d("check1", "test6");
                        //notificationTypeBean.setSelected(cb.isChecked());
                        notificationTypeList.get(holder.getAdapterPosition()).setSelected(holder.checkbox.isChecked());
                        holder.rl_notification.setTag(notificationTypeList.get(position));

                        Log.d("check1", "test7");
                    }


                    if (holder.checkbox.isChecked() == true) {
                        Log.d("checked_id", notificationTypeList.get(position).getId());


                        Log.d("check1", "test8");
                        list_of_noti_id.add(notificationTypeList.get(position).getId());
                        list_of_noti_pos.add(String.valueOf(position));
                        Log.d("check1", "test9");

                    } else {
                        Log.d("checked_id2", notificationTypeList.get(position).getId());

                        pos_id = notificationTypeList.get(position).getId();
                        list_of_noti_id.remove(notificationTypeList.get(position).getId());
                        list_of_noti_pos.remove(String.valueOf(position));
                        Log.d("check1", "test10");
                    }

                    Log.d("listssss", String.valueOf(list_of_noti_id));
                    Log.d("listingssss", String.valueOf(list_of_noti_id.toArray()));
                    String[] valuues = String.valueOf(list_of_noti_id).split(",");

                    Log.d("list_1ssss", String.valueOf(list_of_noti_pos));
                    Log.d("listing_1ssss", String.valueOf(list_of_noti_pos.toArray()));
                    String[] valuues_1 = String.valueOf(list_of_noti_pos).split(",");
                    Log.d("check1", "test11");


                } else if (clicktype.equals("")) {
                    holder.layout.setBackgroundColor(context.getResources().getColor(R.color.whiteBgColor));

                    Checkviewed(false);
                    if (notificationType.equalsIgnoreCase(Constants.BHAJAN)) {
                        getBhajanNotification(true);
                    } else if (notificationType.equalsIgnoreCase(Constants.NEWS)) {
                        getNewsNotification(true);
                    } else if (notificationType.equalsIgnoreCase(Constants.VIDEO)) {
                        getVideosNotification(true);
                    } else {

                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return notificationTypeList.size();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.NOTIFICATION_DETAIL)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("post_id", PostId)
                    .setMultipartParameter("post_type", notificationType);
        } else if (apitype.equals(API.NOTIFICATION_VIEW)) {

            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("notification_id", NotificationId);

        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {

        if (apitype.equals(API.NOTIFICATION_VIEW)) {
            if (result.optBoolean("status")) {
                String dataString = result.optString("data");
                JSONObject jsonObject = new JSONObject(dataString);
                if (jsonObject != null) {
                    // activity.homevideourl = result.optString("home_video");
                    notiFicationViewData = new Gson().fromJson(jsonObject.toString(), NotiFicationViewData.class);
                    String notification_counter = String.valueOf(notiFicationViewData.getNotificationCounter());
                    Log.d("check_counter", String.valueOf(notification_counter));


                    SharedPreference.getInstance(context).putString(Constants.NOTIFICATION_COUNTER, notification_counter);

                    SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER);


                    notifyTV.setText(SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER));


                }

            } else {
                String dataString = result.optString("data");
                JSONObject jsonObject = new JSONObject(dataString);
                if (jsonObject != null) {
                    // activity.homevideourl = result.optString("home_video");
                    notiFicationViewData = new Gson().fromJson(jsonObject.toString(), NotiFicationViewData.class);
                    String notification_counter = String.valueOf(notiFicationViewData.getNotificationCounter());
                    Log.d("check_counter_after", String.valueOf(notification_counter));


                    SharedPreference.getInstance(context).putString(Constants.NOTIFICATION_COUNTER, notification_counter);

                    SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER);


                    notifyTV.setText(SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER));
                }
            }

        } else if (apitype.equals(API.NOTIFICATION_DETAIL)) {
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
                            if (!NotificationFragment.serviceBound) {
                                if (!BhajanViewAllFragment.serviceBound) {
                                    if (!HomeFragment.serviceBound) {
                                        if (!BhajansCategoryFragment.serviceBound) {

                                            Constants.AUDIO_ACTIVE_LIST = "notificationbhajan";
                                            Intent intentservice = new Intent(context, AudioPlayerService.class);
                                            intentservice.putExtra("bhajanarray", bhajans);
                                            intentservice.putExtra("status", "bhajan");
                                            intentservice.putExtra("redirect", Constants.AUDIO_ACTIVE_LIST);
                                            PreferencesHelper.getInstance().setValue("index", 0);
                                            PreferencesHelper.getInstance().setValue("audioplayindex", 0);

                                            context.startService(intentservice);
                                            context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                            Constants.IS_PLAYING_ON_CATEGORY = "false";
                                            // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                            Constants.LIST_INDEX = 0;
                                            bhajanforImages = bhajans;
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
                                            bhajanforImages = bhajans;
                                            Constants.LIST_INDEX = 0;
                                            PreferencesHelper.getInstance().setValue("index", 0);
                                            intent.putExtra("position", 0);
                                            context.sendBroadcast(intent);
                                            Constants.IS_PLAYING_ON_CATEGORY = "false";
                                            // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                            Constants.LIST_INDEX = 0;
                                            bhajanforImages = bhajans;
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
                                       /* Constants.AUDIO_ACTIVE_LIST = "notificationbhajan";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", bhajans);
                                        bundle.putSerializable("bhajanlist", bhajans);
                                        bhajanforImages =bhajans;
                                        Constants.LIST_INDEX = 0;
                                        PreferencesHelper.getInstance().setValue("index", 0);
                                        intent.putExtra("position", 0);
                                        context.sendBroadcast(intent);*/
                                        }

                                    } else {


                                        Constants.AUDIO_ACTIVE_LIST = "notificationbhajan";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", bhajans);
                                        bundle.putSerializable("bhajanlist", bhajans);
                                        bhajanforImages = bhajans;
                                        Constants.LIST_INDEX = 0;
                                        PreferencesHelper.getInstance().setValue("index", 0);
                                        intent.putExtra("position", 0);
                                        context.sendBroadcast(intent);
                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                        Constants.LIST_INDEX = 0;
                                        bhajanforImages = bhajans;
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
                                    bhajanforImages = bhajans;
                                    Constants.LIST_INDEX = 0;
                                    PreferencesHelper.getInstance().setValue("index", 0);
                                    intent.putExtra("position", 0);
                                    context.sendBroadcast(intent);
                                }
                            } else {
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist", bhajans);
                                bundle.putSerializable("bhajanlist", bhajans);
                                bhajanforImages = bhajans;
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
                            bhajanforImages = bhajans;
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
                    } else if (notificationType.equalsIgnoreCase(Constants.VIDEO)) {
                        videos[0] = new Gson().fromJson(jsonObject.toString(), Videos.class);

                        bundle.putInt("position", 0);
                        bundle.putSerializable("video_data", videos);


                        if (context instanceof HomeActivityy) {


                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("position", "0");
                            context.startActivity(intent);
                        }

                    }
                }
            }
        }
    }


    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        ToastUtil.showDialogBox(context, jsonstring);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView artistImage;
        TextView channelName, description, date;
        private ConstraintLayout layout;
        private TextView viewTV;
        CardView inside_card, outside_card;
        RelativeLayout rl_notification;
        LinearLayout ll_chckbox;
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item_notification);
            channelName = itemView.findViewById(R.id.channel_name_single_item_notification);
            description = itemView.findViewById(R.id.description_single_item_notification);
            date = itemView.findViewById(R.id.date_single_item_notification);
            layout = itemView.findViewById(R.id.single_item_layout_video);
            viewTV = itemView.findViewById(R.id.views_number_single_item);
            outside_card = itemView.findViewById(R.id.outside_card);
            rl_notification = itemView.findViewById(R.id.rl_notification);
            ll_chckbox = itemView.findViewById(R.id.ll_chckbox);
            checkbox = itemView.findViewById(R.id.checkbox);
            //  inside_card = itemView.findViewById(R.id.inside_card);

            viewTV.setVisibility(View.GONE);
        }
       /* public void updateView(){
            //Do whatever you need to do regarding the item view
            //this.rl_notification.setSelected(getAdapterPosition() == mSelectedIndex);

            for(int i=0;i<notificationTypeList.size();i++){
                ll_chckbox.setVisibility(View.VISIBLE);
            }
        }*/


    }


    private void getBhajanNotification(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_DETAIL, progress);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void Checkviewed(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_VIEW, progress);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void getNewsNotification(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_DETAIL, progress);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void getVideosNotification(boolean progress) {

        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_DETAIL, progress);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }
                return false;

            }
        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }

        return false;
    }

   /* @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.checkbox != null) {
            holder.checkbox.setOnClickListener(null);
        }
        super.onViewRecycled(holder);
    }*/
}

