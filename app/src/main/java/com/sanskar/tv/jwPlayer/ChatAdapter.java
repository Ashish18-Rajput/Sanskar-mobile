package com.sanskar.tv.jwPlayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sanskar.tv.jwPlayer.LiveStreamJWActivity.channelID;
import static com.sanskar.tv.jwPlayer.LiveStreamJWActivity.channel_database;
import static com.sanskar.tv.jwPlayer.LiveStreamJWActivity.checkfordeletechat;
import static com.sanskar.tv.jwPlayer.LiveStreamJWActivity.current_user_ref;
import static com.sanskar.tv.jwPlayer.LiveStreamJWActivity.deletechat;

public class ChatAdapter extends  RecyclerView.Adapter<ChatAdapter.MessageHolder> implements NetworkCall.MyNetworkCallBack{String s;
    Context context;
    List<MessageModel>messageModelList;
    List<Message>messages;
    SignupResponse signupResponse;
    Activity activity;
    NetworkCall networkCall;
   public static String deleted_node_id;
  // public  static  String dele;
    String message;
    String channel_id,channel_name,repoted_by_id;
String node_for_report;
    public ChatAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        this.activity=activity;
    }



    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
     /*   View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_message,viewGroup,false);
        return new MessageHolder(view);*/
        return new MessageHolder(LayoutInflater.from(context).inflate(R.layout.my_message,null));

    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {

        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
        if(signupResponse!=null)
        {/*if(messages.get(i).getStatus().equals("1")){*/
            if(signupResponse.getId().equals(messages.get(i).getFrom()))
            {    /* messageHolder.rl_sender_msg.setVisibility(View.VISIBLE);*/
                /* messageHolder.rl_reciever.setVisibility(View.GONE);*/
                messageHolder.more.setVisibility(View.GONE);

                messageHolder.tv_receiver_body.setText(messages.get(i).getMessage());

                if(messages.get(i).getName().equals(""))
                {
                    messageHolder.reciever_name.setText("Guest User");
                }
                else {
                    messageHolder.reciever_name.setText(messages.get(i).getName());
                }
             /*   GetTimeAgo getTimeAgo=new GetTimeAgo();
                String time = getTimeAgo.getTimeAgo(messages.get(i).getTime(), getApplicationContext());
                messageHolder.sender_time.setText(time);*/
                messageHolder.reciever_time.setText(Utils.getAgoTime( String.valueOf(messages.get(i).getTime())));

                Ion.with(context)
                        .load(messages.get(i).getImg())
                        .asBitmap().setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if(e==null)
                            messageHolder.reciever_profile_img.setImageBitmap(result);
                        else
                            messageHolder.reciever_profile_img.setImageResource(R.mipmap.default_pic);
                    }
                });
               // messageHolder.sender_profile_img.getResources().geti
                messageHolder.delete_chat.setVisibility(View.VISIBLE);

            messageHolder.delete_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  String msg=messages.get(i).getId();
                    String mm=messages.get(i).getPushid();*/
                   // String msgtimestamp=
                  //  DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("sanskarliveChannels/").;
                  // ((LiveStreamJWActivity)activity).removemsg(msg,mm,i,messages);
                 //   String msg=keyList.get(i);
                    deleted_node_id=messages.get(i).getPushid();
                     //   messages.get(i).setStatus("2");
                    deletechat=false;
                    checkfordeletechat=false;
                   // checkdeletechat="1";
             /*       networkCall = new NetworkCall(ChatAdapter.this, context);

                    networkCall.NetworkAPICall(API.INFORM_DELETE_CHAT, true);*/

                 FirebaseDatabase.getInstance().getReference().child(current_user_ref)
                            .child(deleted_node_id)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         /*   messages.remove(i);
                            notifyItemRemoved(i);

                            notifyItemRangeChanged(i, messages.size());*/

                        //    notifyDataSetChanged();
                       //     notifyItemRemoved(i);
                          //  notifyItemChanged(i);
                           /* networkCall = new NetworkCall(ChatAdapter.this, context);

                          */
                            Log.d("delete","check for pre_check_complete");
                         /*   networkCall = new NetworkCall(ChatAdapter.this, context);

                            networkCall.NetworkAPICall(API.INFORM_DELETE_CHAT, true);*/
                            Log.d("delete","check for oncomplete");
                            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            }
            else {
             /*   messageHolder.rl_reciever.setVisibility(View.VISIBLE);
                 messageHolder.rl_sender_msg.setVisibility(View.GONE);*/
                messageHolder.tv_receiver_body.setText(messages.get(i).getMessage());

                if(messages.get(i).getName().equals(""))
                {
                    messageHolder.reciever_name.setText("Guest User");
                }
                else {
                    messageHolder.reciever_name.setText(messages.get(i).getName());
                }
             //   messageHolder..setText(messages.get(i).getName());
              /*  GetTimeAgo getTimeAgo=new GetTimeAgo();
                String time = getTimeAgo.getTimeAgo(messages.get(i).getTime(), getApplicationContext());
                messageHolder.reciever_time.setText(time);
*/
                messageHolder.reciever_time.setText(Utils.getAgoTime( String.valueOf(messages.get(i).getTime())));

                Ion.with(context)
                        .load(messages.get(i).getImg())
                        .asBitmap().setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if(e==null)
                            messageHolder.reciever_profile_img.setImageBitmap(result);
                        else
                            messageHolder.reciever_profile_img.setImageResource(R.mipmap.default_pic);
                    }
                });
                messageHolder.delete_chat.setVisibility(View.GONE);
                messageHolder.more.setVisibility(View.VISIBLE);

                messageHolder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  messageHolder.report.setVisibility(View.VISIBLE);
                        //  pop_dialog();
                        PopupMenu popup = new PopupMenu(context,messageHolder.more );
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.report_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                node_for_report=messages.get(i).getKey();
                                message=messages.get(i).getMessage();
                                channel_id=messages.get(i).getChannel_id();
                                channel_name=messages.get(i).getChannel_name();
                                repoted_by_id=messages.get(i).getFrom();

                                networkCall = new NetworkCall(ChatAdapter.this, context);
                                networkCall.NetworkAPICall(API.REPORT_MESSAGE, true);
                                // Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });

                        popup.show();//showing popup menu

                    }
                });

            }}
       // }
/*
messageHolder.more.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      //  messageHolder.report.setVisibility(View.VISIBLE);
      //  pop_dialog();
        PopupMenu popup = new PopupMenu(context,messageHolder.more );
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.report_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                node_for_report=messages.get(i).getPushid();
                message=messages.get(i).getMessage();
                channel_id=messages.get(i).getChannel_id();
                channel_name=messages.get(i).getChannel_name();

                networkCall = new NetworkCall(ChatAdapter.this, context);
                networkCall.NetworkAPICall(API.REPORT_MESSAGE, true);
               // Toast.makeText(context,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu

}
});*/

        /*messageHolder.like_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  messageHolder.like_chat.setBackgroundResource(R.mipmap.audio_liked);




                  Glide.with(context).load(R.mipmap.audio_liked).into(messageHolder.like_chat);


       *//* Glide.with(activity)
                .load(channels.get(position).getImage())
                .override(100, 100)
                .fitCenter()
                .crop()
                .into(holder.channel_image);*//*
            }
        });*/

     /*   messageHolder.rl_reciever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageHolder.report.setVisibility(View.GONE);

            }
        });*/

      /*  private void pop_dialog() {


            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.popup_report);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));  // backgroun transperent hua
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);// set height width of dialog
            dialog.show();

                }
            });
        }*/

       /* if(signupResponse.getId().equals(messages.get(i).getFrom())){
*/
        /*}
        else {*/
      /*  }*/
    }

  /*  private void pop_dialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_report);
       *//* dialog.setCancelable(false);*//*
       // dialog.showAsDropDown(Report,-40, 18);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));  // backgroun transperent hua
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);// set height width of dialog
        dialog.show();



    }*/

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.REPORT_MESSAGE)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",signupResponse.getId())
                    .setMultipartParameter("reported_by", signupResponse.getId())
                    .setMultipartParameter("channel_id",channelID )
                .setMultipartParameter("node_id",node_for_report)
                    .setMultipartParameter("message", message)
                    .setMultipartParameter("fb_channel_id", channel_database)
                    .setMultipartParameter("reported_to", repoted_by_id);
        }
        else if(apitype.equals(API.INFORM_DELETE_CHAT)){
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",signupResponse.getId())
                    .setMultipartParameter("node_id",deleted_node_id);

        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (jsonstring.optBoolean("status")) {
            if (apitype.equals(API.REPORT_MESSAGE)) {
                Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show();
               /* LikeDislike(true);
                getHomeData();*/}
        else if(apitype.equals(API.INFORM_DELETE_CHAT)){
                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();


            }}
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    public  class  MessageHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_reciever,rl_sender_msg;
        CardView report;
        ImageView more,delete_chat;
        CircleImageView reciever_profile_img,sender_profile_img;
        TextView reciever_name,reciever_time,tv_receiver_body,sender_name,sender_time,tv_sender_body;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
           rl_reciever=itemView.findViewById(R.id.rl_reciever);
            report=itemView.findViewById(R.id.Report);
            more=itemView.findViewById(R.id.more);
            delete_chat=itemView.findViewById(R.id.delete_chat);
      /*     rl_sender_msg=itemView.findViewById(R.id.rl_sender_msg);*/
            reciever_profile_img=itemView.findViewById(R.id.reciever_profile_img);
       /*     sender_profile_img=itemView.findViewById(R.id.sender_profile_img);*/
            reciever_name=itemView.findViewById(R.id.reciever_name);
            reciever_time=itemView.findViewById(R.id.reciever_time);
         /*  sender_name=itemView.findViewById(R.id.sender_name);
            sender_time=itemView.findViewById(R.id.sender_time);*/
           /* tv_sender_body=itemView.findViewById(R.id.tv_sender_body);*/
            tv_receiver_body=itemView.findViewById(R.id.tv_receiver_body);
           /* tv_sender_body=itemView.findViewById(R.id.tv_sender_body);*/
        }
    }

}