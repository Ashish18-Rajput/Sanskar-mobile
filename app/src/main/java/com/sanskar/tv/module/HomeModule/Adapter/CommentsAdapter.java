package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.module.HomeModule.Pojo.Comments;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by appsquadz on 4/10/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    ArrayList<Comments> list;

    public CommentsAdapter(Context context, ArrayList<Comments> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getComment());
        holder.time.setText(Utils.getAgoTime(list.get(position).getTime()));
        if (!(TextUtils.isEmpty(list.get(position).getProfile_picture()))) {
            Ion.with(context).load(list.get(position).getProfile_picture()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    holder.image.setImageBitmap(result);
                }
            });
        } else {
            holder.image.setImageResource(R.mipmap.profile);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name, description, time;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.single_item_comment_image);
            name = itemView.findViewById(R.id.single_item_comment_name);
            description = itemView.findViewById(R.id.single_item_comment_detail);
            time = itemView.findViewById(R.id.single_item_comment_time);
        }
    }
}
