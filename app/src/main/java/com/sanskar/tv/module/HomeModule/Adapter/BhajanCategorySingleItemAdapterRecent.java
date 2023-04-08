package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class BhajanCategorySingleItemAdapterRecent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Bhajan[] bhajans;
    ArrayList<Bhajan> bhajanArrayList;
    Context context;
    //private String categoryName;
    private Progress mprogress;
    private String directPlay;

    public BhajanCategorySingleItemAdapterRecent(Bhajan[] bhajans, int position, Context context) {
//        this.bhajanArrayList = bhajanArrayList;
        this.bhajans = bhajans;
        this.context = context;
        //this.categoryName = categoryName;
        //mprogress = new Progress(context);
        //mprogress.setCancelable(false);
//        this.directPlay = Integer.parseInt(directPlay);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      //  return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_bhajan_adapter,null));

        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_list3, null);
            return new RectangleViewHolder(view);
        } else if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_bhajan_adapter_circle, null);
            return new CircleViewHolder(view);
        }
        else if (viewType == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_bhajan_adapter_circle, null);
            return new CircleViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (bhajans[i].getDirect_play().equalsIgnoreCase("0")) {
           /* Ion.with(context)
                    .load(bhajans[i].getArtist_image())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
//                    Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);

                            if (result != null) {
                                (( CircleViewHolder)viewHolder).categoryImage2.setImageBitmap(result);
                            } else {
                                (( CircleViewHolder)viewHolder).categoryImage2.setImageResource(R.mipmap.thumbnail_placeholder);

                            }
                        }
                    });*/
            Glide.with(context)
                    .load(bhajans[i].getArtist_image())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .into((( CircleViewHolder)viewHolder).categoryImage2);


            (( CircleViewHolder)viewHolder).categoryName.setText(bhajans[i].getArtist_name());
            (( CircleViewHolder)viewHolder).categoryName.setMaxLines(2);
        }
        else if (bhajans[i].getDirect_play().equalsIgnoreCase("2")) {
           /* Ion.with(context)
                    .load(bhajans[i].getGod_image())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
//                    Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);

                            if (result != null) {
                                (( CircleViewHolder)viewHolder).categoryImage2.setImageBitmap(result);
                            } else {
                                (( CircleViewHolder)viewHolder).categoryImage2.setImageResource(R.mipmap.thumbnail_placeholder);

                            }
                        }
                    });
*/

            Glide.with(context)
                    .load(bhajans[i].getGod_image())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .into((( CircleViewHolder)viewHolder).categoryImage2);

            (( CircleViewHolder)viewHolder).categoryName.setText(bhajans[i].getGod_name());
            (( CircleViewHolder)viewHolder).categoryName.setMaxLines(2);
        }else {
            /*Ion.with(context)
                    .load(bhajans[i].getImage())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            // Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                                    if(result!=null){
                                    (( RectangleViewHolder)viewHolder).categoryImage.setImageBitmap(result);}
                                    else {
                                        (( RectangleViewHolder)viewHolder).categoryImage.setImageResource(R.mipmap.thumbnail_placeholder);

                                    }

                        }
                    });*/
            Glide.with(context)
                    .load(bhajans[i].getImage())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .into((( RectangleViewHolder)viewHolder).categoryImage);
            (( RectangleViewHolder)viewHolder).categoryName.setText(bhajans[i].getTitle());
//            (( RectangleViewHolder)viewHolder).desc.setText(bhajans[i].getArtist_name());

            String desc = Html.fromHtml(bhajans[i].getDescription()).toString();
//            (( RectangleViewHolder)viewHolder).desc.setVisibility(View.GONE);
//            (( RectangleViewHolder)viewHolder).desc.setText(desc);
        }
        /*(( RectangleViewHolder)viewHolder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if(bhajans[i].getDirect_play().equalsIgnoreCase("1")) {
                    bundle.putInt("position", i);
                    bundle.putSerializable("bhajans",bhajans);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }

                else{
                    bundle.putSerializable("bhajanViewAll",bhajans[i]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home,fragment).commit();
                }
            }
        });*/
    /*    (( CircleViewHolder)viewHolder).ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if(bhajans[i].getDirect_play().equalsIgnoreCase("1")) {
                    bundle.putInt("position", i);
                    bundle.putSerializable("bhajans",bhajans);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }

                else{
                    bundle.putSerializable("bhajanViewAll",bhajans[i]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home,fragment).commit();
                }
            }
        });*/
    }
    @Override
    public int getItemViewType(int position) {
        if (bhajans[position].getDirect_play().equalsIgnoreCase("0")){
            return 0;}
       else if (bhajans[position].getDirect_play().equalsIgnoreCase("2")){
            return 2;}
        else return 1;
    }
   /* @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (bhajans[position].getDirect_play().equalsIgnoreCase("0")) {
            Ion.with(context)
                    .load(bhajans[position].getArtist_image())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
//                    Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                    holder.categoryImage.setImageBitmap(result);

                }
            });
            holder.categoryName.setText(bhajans[position].getArtist_name());
            holder.categoryName.setMaxLines(2);
            holder.desc.setVisibility(View.GONE);
        } else {
            Ion.with(context)
                    .load(bhajans[position].getImage())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            // Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                            holder.categoryImage.setImageBitmap(result);
                        }
                    });
            holder.categoryName.setText(bhajans[position].getTitle());
            holder.desc.setText(bhajans[position].getArtist_name());

            String desc = Html.fromHtml(bhajans[position].getDescription()).toString();
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(desc);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if(bhajans[position].getDirect_play().equalsIgnoreCase("1")) {
                    bundle.putInt("position", position);
                    bundle.putSerializable("bhajans",bhajans);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }

                else{
                    bundle.putSerializable("bhajanViewAll",bhajans[position]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home,fragment).commit();
                }
            }
        });
    }*/

    @Override
    public int getItemCount() {
//        return bhajanArrayList.size();
        return bhajans.length;
    }

    class RectangleViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryName;
        RelativeLayout layout;
        ImageView mp3VideoIcon;
        private TextView desc;

        public RectangleViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.bhajan_single_category_single_item_image);

            categoryName = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
         //   desc = itemView.findViewById(R.id.desc_txt);
            layout = itemView.findViewById(R.id.bhajan_single_category_single_item_holder_layout);
            mp3VideoIcon = itemView.findViewById(R.id.mp3_video_icon);

            mp3VideoIcon.setImageResource(R.mipmap.mp3);
            mp3VideoIcon.setVisibility(View.VISIBLE);
        }
    }

    class CircleViewHolder extends RecyclerView.ViewHolder{
        CircleImageView categoryImage2;
        TextView categoryName;
        RelativeLayout ll;
        ImageView mp3VideoIcon;
        private TextView desc;

        public CircleViewHolder(View itemView) {
            super(itemView);
            categoryImage2 = itemView.findViewById(R.id.bhajan_single_category_single_item_image);

            categoryName = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            //desc = itemView.findViewById(R.id.desc_txt);
            ll = itemView.findViewById(R.id.bhajan_single_category_single_item_holder_circle_layout);

            /*mp3VideoIcon.setImageResource(R.mipmap.mp3);*/
          /*  mp3VideoIcon.setVisibility(View.VISIBLE);*/
        }
    }

}
