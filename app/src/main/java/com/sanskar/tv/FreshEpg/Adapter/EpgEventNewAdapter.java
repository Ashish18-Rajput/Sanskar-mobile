package com.sanskar.tv.FreshEpg.Adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sanskar.tv.EPG.Event;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.R;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;

import java.util.List;


public class EpgEventNewAdapter extends RecyclerView.Adapter<EpgEventNewAdapter.ViewHolder> {

    Context context;
    List<Event> eventList;
    int currentEventPosition;
    String url;
    String releasedDate;
    int currentPlaying = -1;
    boolean isToday;

    public EpgEventNewAdapter(Context context, List<Event> eventList, int currentEventPosition, String url, String releasedDate,boolean isToday) {
        this.context = context;
        this.eventList = eventList;
        this.currentEventPosition = currentEventPosition;
        this.url = url;
        this.releasedDate = releasedDate;
        this.isToday = isToday;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (currentEventPosition!=-1){
            if (currentEventPosition == position) {
                if (isToday){
                    holder.textView.setText("Live");
                }else{
                    holder.textView.setText("Now Playing");
                }
                holder.live_rl.setVisibility(View.VISIBLE);
                String newUrl = url + "?start=" + releasedDate + "T" + eventList.get(currentEventPosition).getStartTime() + "+05:30" + "&end=" + releasedDate + "T" + eventList.get(currentEventPosition).getEndTime() + "+05:30";
                Log.e("chaudhary", newUrl);
                Uri uri = Uri.parse(newUrl);
                if (url!=null && !TextUtils.isEmpty(url))
                ((LiveStreamJWActivity) context).playVideo1(uri);
                else ToastUtil.showDialogBox1(context,"Please Subscribe to our Premium");
                ((LiveStreamJWActivity) context).epg_event_name.setText(eventList.get(currentEventPosition).getProgramTitle());
            } else {
                holder.live_rl.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.live_rl.setVisibility(View.INVISIBLE);
        }


        Glide.with(context)
                .load(eventList.get(position).getThumbnail())
                .into(holder.imgView);

        holder.event_name.setText(eventList.get(position).getProgramTitle());
        holder.eventTime.setText(eventList.get(position).getStartTime() + " - " + eventList.get(position).getEndTime());
        holder.container_rl.setOnClickListener(v -> {
            ((LiveStreamJWActivity) context).linearLayoutManager1.scrollToPositionWithOffset(position, 0);
            currentPlaying = position;
            String newUrl = url + "?start=" + releasedDate + "T" + eventList.get(position).getStartTime() + "+05:30" + "&end=" + releasedDate + "T" + eventList.get(position).getEndTime() + "+05:30";
            Log.e("chaudhary", newUrl);
            Uri uri = Uri.parse(newUrl);
            if (url!=null && !TextUtils.isEmpty(url))
                ((LiveStreamJWActivity) context).playVideo1(uri);
            else ToastUtil.showDialogBox1(context,"Please Subscribe to our Premium");
            ((LiveStreamJWActivity) context).epg_event_name.setText(eventList.get(currentEventPosition).getProgramTitle());
            ((LiveStreamJWActivity) context).epg_event_name.setText(eventList.get(position).getProgramTitle());
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout now_playing_rl, container_rl, live_rl;
        TextView event_name, eventTime, textView;
        ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            now_playing_rl = itemView.findViewById(R.id.now_playing_rl);
            event_name = itemView.findViewById(R.id.event_name);
            eventTime = itemView.findViewById(R.id.eventTime);
            container_rl = itemView.findViewById(R.id.container_rl);
            textView = itemView.findViewById(R.id.textView);
            live_rl = itemView.findViewById(R.id.live_rl);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}
