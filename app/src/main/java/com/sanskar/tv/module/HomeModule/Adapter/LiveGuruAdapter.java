package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanskar.tv.R;
import com.sanskar.tv.module.goLiveModule.controller.LiveGuru;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kapil on 23/11/18.
 */

public class LiveGuruAdapter  extends RecyclerView.Adapter<LiveGuruAdapter.ViewHolder> {


    Context context;
    private ArrayList<LiveGuru> liveGuruArrayList;

    public LiveGuruAdapter(Context context, ArrayList<LiveGuru> liveGuruArrayList) {
        this.context = context;
        this.liveGuruArrayList = liveGuruArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.live_guru_list, null));
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // holder.itemView.setBackgroundResource(R.drawable.sample_background);
        LiveGuru liveGuru =liveGuruArrayList.get(position);
        holder.channelName.setText(liveGuru.getUsername());

if(!liveGuru.getProfilePicture().isEmpty())
        Picasso.with(context).
                load(liveGuru.getProfilePicture()).into(holder.artistImage);
    }

    @Override
    public int getItemCount() {
        return liveGuruArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage;
        TextView channelName, description;
        CardView singleItem;
        private View itemView;


        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.img_guru);
            channelName = itemView.findViewById(R.id.tv_guru_name);
            this.itemView    = itemView.findViewById(R.id.item_view);
//            description = itemView.findViewById(R.id.description_single_item_guru);
//            singleItem = itemView.findViewById(R.id.single_item_guru);

        }

    }
}
