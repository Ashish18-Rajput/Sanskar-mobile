package com.sanskar.tv.module.HomeModule.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.WallPaperModel;

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

public class WallPaperAdapter3 extends RecyclerView.Adapter<WallPaperAdapter3.ViewHolder> {

    Context context;
    HomeActivityy homeActivityy;
    List<WallPaperModel> wallPaperModels;


    public WallPaperAdapter3(Context context, HomeActivityy homeActivityy, List<WallPaperModel> wallPaperModels) {
        this.context = context;
        this.homeActivityy = homeActivityy;
        this.wallPaperModels = wallPaperModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_all_wallpaper,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(wallPaperModels.get(position).getWallpaper())
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewWallPaper);

        holder.title.setText(wallPaperModels.get(position).getTitle());

        holder.main_rl.setOnLongClickListener(v -> {
            showDialogBox(wallPaperModels.get(position).getWallpaper());
            return false;
        });

        holder.download_rl.setOnClickListener(view -> beginDownload(position));
    }

    private void beginDownload(int i) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(wallPaperModels.get(i).getWallpaper());
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
                    String imagename = wallPaperModels.get(i).getTitle() + "_" + time + ".PNG";
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
        return wallPaperModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout download_rl,main_rl;
        TextView title;
        ImageView imageViewWallPaper;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            download_rl = itemView.findViewById(R.id.download_rl);
            main_rl = itemView.findViewById(R.id.main_rl);
            imageViewWallPaper = itemView.findViewById(R.id.imageViewWallPaper);
            title = itemView.findViewById(R.id.title);
        }
    }


    private void showDialogBox(String menuMasterListList) {
        final Dialog dialog = new Dialog(homeActivityy, R.style.BottomSheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.wallpaper_dialog);

        ImageView imageView, imageView1;

        imageView = dialog.findViewById(R.id.banner_img);

        Glide.with(context)
                .load(menuMasterListList)
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        imageView1 = dialog.findViewById(R.id.delete_banner_img);

        imageView1.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}
