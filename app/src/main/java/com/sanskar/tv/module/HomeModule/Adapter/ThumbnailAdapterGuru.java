package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.Thumbnails;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ThumbnailAdapterGuru extends RecyclerView.Adapter<ThumbnailAdapterGuru.ViewHolder> {
    Context context;
    List<Thumbnails> thumbnails;
    BitmapDrawable bitmapDrawable;
    Bitmap[] bitmap;
    boolean isDownloaded = false;

    public ThumbnailAdapterGuru(Context context, List<Thumbnails> thumbnails) {
        this.context = context;
        this.thumbnails = thumbnails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.thumbnail_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        bitmap = new Bitmap[thumbnails.size()];
        /*Ion.with(context).load(thumbnails.get(i).getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null) {
                    viewHolder.imageView.setImageBitmap(result);
                    bitmap[i] = result;
                } else {
                    viewHolder.imageView.setImageResource(R.mipmap.thumbnail_placeholder);
                }
            }
        });*/
        Glide.with(context)
                .load(thumbnails.get(i).getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .into(viewHolder.imageView);


        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginDownload(i);
            }
        });
    }

    private void beginDownload(int i) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(thumbnails.get(i).getImage());
                    InputStream in = new BufferedInputStream(url.openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n = in.read(buf);
                    while (n != -1) {
                        out.write(buf, 0, n);
                        n = in.read(buf);
                    }
                    out.close();
                    in.close();
                    byte[] response = out.toByteArray();
                    String time = new SimpleDateFormat("ss", Locale.getDefault())
                            .format(System.currentTimeMillis());
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File dir = new File(path + "/DCIM" + "/Sanskar Image");
                    dir.mkdir();
                    String imagename = thumbnails.get(i).getGuru_name() + "_" + time + ".PNG";
                    File file = new File(dir, imagename);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(response);
                    ToastUtil.showThreadShortToast(context, "Image saved in DCIM/Sanskar Image");
                    fos.close();

                } catch (IOException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }


    @Override
    public int getItemCount() {
        return thumbnails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_thumbnails);
            download = itemView.findViewById(R.id.download);
        }
    }
}
