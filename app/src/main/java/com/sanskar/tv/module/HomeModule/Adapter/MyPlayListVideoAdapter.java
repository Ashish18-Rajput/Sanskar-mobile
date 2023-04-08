package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.R;
import com.sanskar.tv.jwPlayer.OfflineVideoPlayer;
import com.sanskar.tv.model.PlayListModel;
import com.sanskar.tv.model.VideoPlaylistModel;
import com.sanskar.tv.sqlite.DatabaseManager;

import java.util.List;

public class MyPlayListVideoAdapter extends RecyclerView.Adapter<MyPlayListVideoAdapter.VideoPlylistModel> {
    Context context;
    List<VideoPlaylistModel> videolist;
    public VideoPlaylistModel[] videolistarry;
    String path1;
    Bitmap bitmapaudio;
    public static int videoplaylisttype;
    DatabaseManager databaseManager;



    public static int pos,playedposition;
    public static MediaPlayer downloadmediaplayer;

    public MyPlayListVideoAdapter(Context context, List<VideoPlaylistModel> videolist) {
        this.context = context;
        this.videolist = videolist;
    }

    @NonNull
    @Override
    public VideoPlylistModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_item_guru_list,null);
        return new MyPlayListVideoAdapter.VideoPlylistModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPlylistModel videoPlylistModel, int i) {
        Ion.with(context).load(videolist.get(i).getImg_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                //Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                videoPlylistModel.artistImage.setImageBitmap(result);
            }
        });

        videoPlylistModel.channelName.setText(videolist.get(i).get_name());
        String desc = Html.fromHtml(videolist.get(i).get_name()).toString();
        videoPlylistModel.description.setText(desc);

        videoPlylistModel.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoplaylisttype=1;
             //   playAudio(position);
                Intent intent = new Intent(context, OfflineVideoPlayer.class);
                intent.putExtra("absolutepath", videolist.get(i).get_url());
                context.startActivity(intent);
              /* videolistarry= videolist.toArray(new VideoPlaylistModel[0]);

                Bundle bundle = new Bundle();
                bundle.putSerializable("playlist", videolistarry);
                bundle.putInt("position", i);

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("playlist",videolistarry);
                intent.putExtra("position", i);
                context.startActivity(intent);*/
            }
        });

        videoPlylistModel.delete_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseManager=new DatabaseManager(context);
                databaseManager.deleteContact(new PlayListModel(videolist.get(i).get_name(),videolist.get(i).getImg_url(),
                        videolist.get(i).get_url()));

                videolist.remove(i);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videolist.size();
    }

    public  class VideoPlylistModel extends RecyclerView.ViewHolder {
        ImageView artistImage,delete_song;
        TextView channelName, description;
        ConstraintLayout singleItem;
        public VideoPlylistModel(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item_guru);
            channelName = itemView.findViewById(R.id.channel_name_single_item_guru);
            description = itemView.findViewById(R.id.description_single_item_guru);
            singleItem = itemView.findViewById(R.id.single_item_guru);
            delete_song=itemView.findViewById(R.id.delete_song);
        }
    }
}
