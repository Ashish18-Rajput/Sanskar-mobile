package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.ReccycleItemTouch;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.NotificationAdapterNew;
import com.sanskar.tv.module.HomeModule.Adapter.NotificationInsideAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.NotificationTypeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.module.HomeModule.Activity.HomeActivityy.frag_type;
import static com.sanskar.tv.module.HomeModule.Adapter.NotificationAdapter.list_of_noti_id;
import static com.sanskar.tv.module.HomeModule.Adapter.NotificationAdapter.list_of_noti_pos;

/**
 * Created by appsquadz on 2/14/2018.
 */

public class NotificationFragmentNew extends Fragment implements NetworkCall.MyNetworkCallBack {
    public static AudioPlayerService player;
    Boolean flag;
    RelativeLayout rl_noti;
    public static Snackbar snackbar;
    public  static   String final_list_of_id;
    public  static   String final_list_of_pos;
    public static boolean serviceBound = false;
    public static int notification_position;
    String checkedsnackbar="";
    public static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AudioPlayerService.LocalBinder binder = (AudioPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;

        }
    };
    Context context;
    public static  String clicktype="";
  // public static RecyclerView notification_recyclerView, recycler_view_notification2;
   public static RecyclerView recycler_view_notification2;
   public static ListView listView;
  //  NotificationAdapter adapter;
    NotificationAdapterNew adapter;
    NotificationInsideAdapter insideadapter;
    LinearLayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager2;
    List<NotificationTypeBean> notificationTypeList;
    List<NotificationTypeBean> notificationTypeInsideList;
    public static AppTextView clear_notify,edit,done;
    RelativeLayout toolbar, noDataFound;
    ImageView back, backNotification;
    TextView title;
    int type;
    String notification_id;
    Boolean checkClearStatus = false;
    private int focusedItem = 0;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;
    private NetworkCall networkCall;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String removetype="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_new, null);
        context = getActivity();
        frag_type = "";

        if (((HomeActivityy) getActivity()).playerlayout1.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);

        }
        if (((HomeActivityy) getActivity()).playerlayout2.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);

        }
        initView(view);
        toolbar.setVisibility(View.GONE);
        return view;
    }

    private void initView(View view) {
        //    backNotification= view.findViewById(R.id.back1);
        listView = view.findViewById(R.id.recycler_view_notificationabc);
        rl_noti = view.findViewById(R.id.rl_noti);
        recycler_view_notification2 = view.findViewById(R.id.recycler_view_notification2);
        toolbar = view.findViewById(R.id.toolbar_notification);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
        noDataFound = view.findViewById(R.id.no_data_found_notification);
        back = toolbar.findViewById(R.id.back);
        title = toolbar.findViewById(R.id.toolbar_title);
        notificationTypeList = new ArrayList<>();
        notificationTypeList = new ArrayList<>();
        notificationTypeInsideList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(context);
        layoutManager2 = new LinearLayoutManager(context);
        clear_notify = ((HomeActivityy) context).clear_notification;
        edit = ((HomeActivityy) context).editTV;
        done = ((HomeActivityy) context).cancle_noti;

         adapter = new NotificationAdapterNew(notificationTypeList,context);

        //attaching adapter to the listview

      //  adapter = new NotificationAdapter(notificationTypeList, context);
        insideadapter = new NotificationInsideAdapter(notificationTypeInsideList, context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setText(getResources().getString(R.string.notification));
        title.setVisibility(View.VISIBLE);
        edit.setVisibility(View.VISIBLE);
        clear_notify.setVisibility(View.GONE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicktype="clicktypeFromEdit";
                edit.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);
                list_of_noti_pos.clear();
                list_of_noti_id.clear();

                for (int i = 0; i <10;i++) {

                  View v = listView.getChildAt(i);

                    LinearLayout ll_chk = v.findViewById(R.id.ll_chckbox);
                    CheckBox cb = v.findViewById(R.id.checkbox);
                    ll_chk.setVisibility(View.VISIBLE);
                    cb.setVisibility(View.VISIBLE);
                    cb.setChecked(false);
                }

                showSnackbar();


            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicktype="";

                edit.setVisibility(View.VISIBLE);

                for (int i = 0; i < notificationTypeList.size(); i++) {
                    View v = listView.getChildAt(i);
                    CheckBox cb = v.findViewById(R.id.checkbox);
                    LinearLayout ll_chk = v.findViewById(R.id.ll_chckbox);
                    ll_chk.setVisibility(View.GONE);
                    cb.setVisibility(View.GONE);
                    cb.setChecked(false);
                }
                listView.setVisibility(View.VISIBLE);
              //  adapter.notifyDataSetChanged();
                list_of_noti_pos.clear();
                list_of_noti_id.clear();
                snackbar.dismiss();
                done.setVisibility(View.GONE);
                // ll_chckbox.setVisibility(View.GONE);
            }
        });
        clear_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clear_notification();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();

                ((HomeActivityy) context).onBackPressed();
            }
        });
        ((HomeActivityy) context).drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ((HomeActivityy) context).getSupportActionBar().hide();

      //  notification_recyclerView.setLayoutManager(layoutManager);
        recycler_view_notification2.setLayoutManager(layoutManager2);
        recycler_view_notification2.setAdapter(insideadapter);
        listView.setAdapter(adapter);

        ReccycleItemTouch swipeToDeleteCallback = new ReccycleItemTouch(context) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                type = 2;
                //  adapter.remove(viewHolder.getAdapterPosition());
                //recycler_view_notification2.setVisibility(View.VISIBLE);
                notification_id = notificationTypeList.get(viewHolder.getAdapterPosition()).getId();
                Log.d("noti_id", notification_id);
                notification_position = viewHolder.getAdapterPosition();
                Log.d("noti_pos", String.valueOf(notification_position));


                getclearNotification(false);
            }};
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotificationList(false);
            }
        });

        getNotificationList(true);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            serviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).handleToolbar();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.NOTIFICATION_LIST)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("device_type", "1");
        } else if (apitype.equals(API.NOTIFICATION_CLEAR)) {
            if (type == 1) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", "2");
            } else if (type == 2) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", "1")
                        .setMultipartParameter("notification_id", notification_id);

            }
            else if (type == 3) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", "1")
                        .setMultipartParameter("notification_id", final_list_of_id);

            }
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
        if (apitype.equals(API.NOTIFICATION_LIST)) {
            if (result.optBoolean("status")) {
                JSONArray jsonArray = result.optJSONArray("data");
                Log.d("Response", "" + jsonArray);
                if (jsonArray.length() > 0) {
                    noDataFound.setVisibility(View.GONE);
                    if (notificationTypeList.size() > 0) {
                        notificationTypeList.clear();
                    }
                    if (notificationTypeInsideList.size() > 0) {
                        notificationTypeInsideList.clear();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        NotificationTypeBean notificationTypeBean = new Gson()
                                .fromJson(jsonArray.opt(i).toString(), NotificationTypeBean.class);
                        notificationTypeList.add(notificationTypeBean);
                        notificationTypeInsideList.add(notificationTypeBean);
                    }
                    adapter.notifyDataSetChanged();
/*
                    insideadapter.notifyDataSetChanged();
*/
                } /*else {
                    edit.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }*/
            }
        } else if (apitype.equals(API.NOTIFICATION_CLEAR)) {
            if (result.optBoolean("status")) {
                if (type == 2) {
                    checkClearStatus = true;

                    notificationTypeList.remove(notification_position);

                    Log.d("noti_size", String.valueOf(notificationTypeList.size()));
                    adapter.notifyDataSetChanged();
                    getNotificationList(true);


                    Toast.makeText(context, "Notification Removed", Toast.LENGTH_SHORT).show();

/*
                  Toast.makeText(context, "Notification Removed success", Toast.LENGTH_SHORT).show();
*/

                    //adapter.notifyDataSetChanged();
                } else if (type == 1) {
                    list_of_noti_pos.clear();
                    list_of_noti_id.clear();
                    getNotificationList(true);
                   /* notificationTypeList.clear();
                    adapter.notifyDataSetChanged();*/
                    Toast.makeText(context, "All Notification Removed", Toast.LENGTH_SHORT).show();

                }

                else if (type == 3) {
                    checkClearStatus = true;
                   /* for(int i=0;i<list_of_noti_pos.size();i++){
                        notificationTypeList.remove(list_of_noti_pos);
                        adapter.notifyDataSetChanged();

                    }
                   // notificationTypeList.remove(notification_position);
                    Log.d("noti_size", String.valueOf(notificationTypeList.size()));
                    adapter.notifyDataSetChanged();*/
                    list_of_noti_pos.clear();
                    list_of_noti_id.clear();
                   getNotificationList(true);
                    edit.setVisibility(View.VISIBLE);
                    for (int i = 0; i < notificationTypeList.size(); i++) {
                        View v = listView.getChildAt(i);
                        CheckBox cb = v.findViewById(R.id.checkbox);
                        LinearLayout ll_chk = v.findViewById(R.id.ll_chckbox);
                        ll_chk.setVisibility(View.GONE);
                        cb.setVisibility(View.VISIBLE);
                        cb.setChecked(false);
                    }
                    listView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();

                    snackbar.dismiss();

                    done.setVisibility(View.GONE);
                    removetype="";

                    Toast.makeText(context, "Notification Removed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show();
           //     getNotificationList(true);

            }

        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
        //ToastUtil.showDialogBox(context, jsonstring);//----by sumit
    }

    private void getNotificationList(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall = new NetworkCall(NotificationFragmentNew.this, context);
            networkCall.NetworkAPICall(API.NOTIFICATION_LIST, progress);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void Clear_notification() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(R.string.app_name)
                .setMessage(R.string.clear_notifiaction)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "clear", Toast.LENGTH_SHORT).show();
                        type = 1;
                        getclearNotification(true);

                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setCancelable(false)
                .show();
    }

    private void getclearNotification(boolean progress) {

        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.NOTIFICATION_CLEAR, progress);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    private void showSnackbar() {

        // RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.sss);
        View view =  ((HomeActivityy) context).findViewById(android.R.id.content);
       snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
     /*   Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        LayoutInflater objLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
        LayoutInflater objLayoutInflater = (LayoutInflater)((HomeActivityy) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.findViewById(R.id.snackbar_text).setVisibility(View.INVISIBLE);

        View snackView = objLayoutInflater.inflate(R.layout.bottomsheet_dialog, null);
        //  final Snackbar s = Snackbar.make(v, message, duration);
        // layout.findViewById(android.support.design.R.id.snackbar_text).setVisibility(View.INVISIBLE);
        // Set an action on it, and a handler
        TextView remove=snackView.findViewById(R.id.remove);
        TextView mark=snackView.findViewById(R.id.mark);
        TextView unmark=snackView.findViewById(R.id.unmark);
        checkedsnackbar="snackbar";
       /* s.setAction("MarkAll", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });*/


        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removetype="all";
                list_of_noti_id.clear();


              /*  for(int i=0;i<notificationTypeInsideList.size();i++){

                    checkbox.getTag(i);
                    checkbox.setSelected(true);
                }*/

              /*  for (int i = 0; i < notificationTypeInsideList.size(); i++) {
                    View v = recycler_view_notification2.getChildAt(i);
                    CheckBox cb = v.findViewById(R.id.checkbox);
                    cb.setChecked(true);
                }*/
                list_of_noti_id.clear();

                for (int i = 0; i < notificationTypeList.size(); i++) {
                    View v = listView.getChildAt(i);
                    CheckBox cb = v.findViewById(R.id.checkbox);
                    list_of_noti_id.add(notificationTypeList.get(i).getId());
                    list_of_noti_pos.add(String.valueOf(i));
                    Log.d("mark_all_id", String.valueOf(list_of_noti_id));
                    cb.setChecked(true);
                }

                Log.d("mark_all_id", String.valueOf(list_of_noti_id));
                Log.d("mark_list_of_noti_id", String.valueOf(list_of_noti_id));

                mark.setVisibility(View.GONE);
                unmark.setVisibility(View.VISIBLE);
                Toast.makeText(context, "mark done", Toast.LENGTH_SHORT).show();
            }
        });
        unmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removetype="unmark";
              /*  for (int i = 0; i < notificationTypeInsideList.size(); i++) {
                    View v = recycler_view_notification2.getChildAt(i);
                    CheckBox cb = v.findViewById(R.id.checkbox);
                    cb.setChecked(false);
                }*/

                for (int i = 0; i < notificationTypeList.size(); i++) {
                    View v = listView.getChildAt(i);
                    CheckBox cb = v.findViewById(R.id.checkbox);
                    cb.setChecked(false);
                }
                list_of_noti_id.clear();
                list_of_noti_pos.clear();
                Log.d("un_mark_all_id", String.valueOf(list_of_noti_id));
                Log.d("unmark_list_of_noti_id", String.valueOf(list_of_noti_id));

                unmark.setVisibility(View.GONE);
                mark.setVisibility(View.VISIBLE);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                snackbar.dismiss();
*/


                    if(list_of_noti_id.size()==0){
                        Toast.makeText(context, "Select Atleast One", Toast.LENGTH_LONG).show();

                    }else {

                type=3;

                String list_of_id=String.valueOf(list_of_noti_id);
                Log.d("ragex",list_of_id);
                final_list_of_id=list_of_id.replaceAll("[\\[\\]\\(\\)]", "");



                String list_of_pos=String.valueOf(list_of_noti_pos);
                Log.d("ragex1",list_of_pos);
                final_list_of_pos=list_of_pos.replaceAll("[\\[\\]\\(\\)]", "");

                getclearNotification(false);

               /* for(int i=0;i<list_of_noti_pos.size();i++){
                    Log.d("llll", String.valueOf(list_of_noti_pos));
                    notificationTypeList.remove(list_of_noti_pos.get(i));
                    adapter.notifyDataSetChanged();

                }
                adapter.notifyDataSetChanged();*/

                Toast.makeText(context, "remove done", Toast.LENGTH_SHORT).show(); }  }
        });
        // snackbar.show();
        layout.setPadding(0,0,0,0);

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        snackbar.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        edit.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        if (checkedsnackbar.equalsIgnoreCase("snackbar")) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
    }





    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition =firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
