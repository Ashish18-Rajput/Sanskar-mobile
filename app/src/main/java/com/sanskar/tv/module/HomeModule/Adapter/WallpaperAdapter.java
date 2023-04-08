package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;

import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.getDate;

/**
 * Created by kapil on 30/4/18.
 */

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack{

    private List<Bhajan> list;
    Context context;
    private NetworkCall networkCall;
    String video_id;

    public WallpaperAdapter(List<Bhajan> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public WallpaperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WallpaperAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_home,null));
    }

    @Override
    public void onBindViewHolder(final WallpaperAdapter.ViewHolder holder, final int position) {

        final Bhajan audioBean = list.get(position);
        Ion.with(context).load(audioBean.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                holder.artistImage.setImageBitmap(result);
            }
        });
        holder.channelName.setText(audioBean.getArtist_name());

        String desc = Html.fromHtml(audioBean.getDescription()).toString();

        holder.description.setText(desc);
        holder.date.setText(getDate(Long.parseLong(audioBean.getCreation_time())));
        holder.viewsNumber.setText(audioBean.getLikes()+" likes");
        holder.layout.setTag(audioBean.getId());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
//                    bundle.putSerializable("guruAudioData", (Serializable) audioBean);
                bundle.putInt("guruAudioData", position);
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle);
                ((HomeActivityy) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.GURU_RELATED_VIDEOS)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("type", "2")
                    .setMultipartParameter("media_id", video_id);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        Log.d("RECENT_VIEW Response",jsonstring.toString());
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage;
        TextView channelName, description, date, viewsNumber;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item);
            channelName = itemView.findViewById(R.id.channel_name_single_item);
            description = itemView.findViewById(R.id.description_single_item);
            date = itemView.findViewById(R.id.date_single_item);
            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
            layout = itemView.findViewById(R.id.single_item_layout_video);
        }
    }

}
