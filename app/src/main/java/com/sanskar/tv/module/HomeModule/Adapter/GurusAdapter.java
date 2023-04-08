package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.GuruDetailsFragment;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;
import com.sanskar.tv.R;

import java.util.List;
import java.util.Locale;

import static com.sanskar.tv.Others.Helper.Utils.getDate;

public class GurusAdapter extends RecyclerView.Adapter<GurusAdapter.ViewHolder>{

    List<GuruPojo> guruResponses;
    List<GuruPojo> alphabeticalcharlist;
    Context context;

    public GurusAdapter(List<GuruPojo> guruResponses, Context context) {
        this.guruResponses = guruResponses;
        this.context = context;
     /*   this.alphabeticalcharlist = new ArrayList<>();
        this.alphabeticalcharlist.addAll(guruResponses);*/
    }

    @Override
    public GurusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // if (viewType == 0){
        //    return new ViewHolder2(LayoutInflater.from(context).inflate(R.layout.top_title_layout,null));
        //}else if (viewType == 1){
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.guru_layout, null));
        //}
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      //  alphabeticalcharlist=new ArrayList<>();
            final ViewHolder viewHolder = (ViewHolder)holder;



        Glide.with(context)
                .load(guruResponses.get(position).getProfile_image())

                .into(viewHolder.firstGuruIV);

            /*Ion.with(context).load(guruResponses.get(position).getProfile_image()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (result != null) {
                        viewHolder.firstGuruIV.setImageBitmap(result);
                    } else {
                        viewHolder.firstGuruIV.setImageResource(R.mipmap.thumbnail_placeholder);
                    }
                }
            });
*/
            //Picasso.with(context).load(guruResponses.get(position).getProfile_image()).placeholder(R.mipmap.profile_pic_default).into(holder.artistImage);
            viewHolder.firstGuruTitle.setText(guruResponses.get(position).getName());
            String firstDescString = Html.fromHtml(guruResponses.get(position).getDescription()).toString();
            viewHolder.firstDesc.setText(firstDescString);

//        if (guruResponses.get(position).length > 1) {

            Ion.with(context).load(guruResponses.get(position).getProfile_image()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (result != null) {
                        viewHolder.secGuruIV.setImageBitmap(result);
                    } else {
                        viewHolder.secGuruIV.setImageResource(R.mipmap.thumbnail_placeholder);
                    }
                }
            });

            //Picasso.with(context).load(guruResponses.get(position).getProfile_image()).placeholder(R.mipmap.profile_pic_default).into(holder.artistImage);
            viewHolder.secGuruTitle.setText(guruResponses.get(position).getName());
            String secDescString = Html.fromHtml(guruResponses.get(position).getDescription()).toString();
            viewHolder.secDesc.setText(secDescString);
//        }

            viewHolder.firstParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showGuruDetailFrag(0, position);
                }
            });


    }

    /*@Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }else{
            return 1;
        }
    }*/

    @Override
    public int getItemCount() {
        return guruResponses.size();
    }



    public void filter(String charText) {
        guruResponses.clear();
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.length() == 0) {
            guruResponses.addAll(alphabeticalcharlist);
        } else {
            for (GuruPojo wp : alphabeticalcharlist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    guruResponses.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        public ViewHolder2(View itemView) {
            super(itemView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView firstGuruIV;
        private TextView firstGuruTitle;
        private TextView firstDesc;
        private RelativeLayout firstParentLayout;

        private ImageView secGuruIV;
        private TextView secGuruTitle;
        private TextView secDesc;
        private RelativeLayout secParentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
//            artistImage = itemView.findViewById(R.id.artist_image_single_item_guru);
//            channelName = itemView.findViewById(R.id.channel_name_single_item_guru);
//            //description = itemView.findViewById(R.id.description_single_item_guru);
//            singleItem = itemView.findViewById(R.id.single_item_guru);

            firstGuruIV = itemView.findViewById(R.id.first_guru_image);
            firstGuruTitle = itemView.findViewById(R.id.first_guru_title_text);
            firstDesc = itemView.findViewById(R.id.first_desc_txt);
            firstParentLayout = itemView.findViewById(R.id.first_parent_card_guru);

            secGuruIV = itemView.findViewById(R.id.second_guru_image);
            secGuruTitle = itemView.findViewById(R.id.second_guru_title_text);
            secDesc = itemView.findViewById(R.id.second_desc_txt);
            secParentLayout = itemView.findViewById(R.id.sec_parent_card_guru);
        }

    }

    private void showGuruDetailFrag(int arrayPos, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",guruResponses.get(position));
        bundle.putInt("type",1);
        GuruDetailsFragment fragment = new GuruDetailsFragment();
        fragment.setArguments(bundle);
        ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("Guru_Details").replace(R.id.container_layout_home, fragment).commit();
    }
}


