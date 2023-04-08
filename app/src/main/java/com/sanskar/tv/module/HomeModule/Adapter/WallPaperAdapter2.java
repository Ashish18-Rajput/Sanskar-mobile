package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class WallPaperAdapter2 extends RecyclerView.Adapter<WallPaperAdapter2.ViewHolder> {

    Context context;
    HomeActivityy homeActivityy;
    List<WallPaperModel> wallPaperModels;

    public WallPaperAdapter2(Context context, HomeActivityy homeActivityy, List<WallPaperModel> wallPaperModels) {
        this.context = context;
        this.homeActivityy = homeActivityy;
        this.wallPaperModels = wallPaperModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wallpaper_still, parent, false);
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

        holder.main_rl.setOnLongClickListener(v ->{
            showDialogBox(context,wallPaperModels.get(position).getTitle(),position);
            return false;
        });


    }

    public  void showDialogBox(Context context, String message,int position) {

        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language_5);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView=dialog1.findViewById(R.id.message);
        ImageView imageViewWallPaper = dialog1.findViewById(R.id.imageViewWallPaper);

        Glide.with(context)
                .load(wallPaperModels.get(position).getWallpaper())
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewWallPaper);

        textView.setText(message);

        AppCompatButton btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
        AppCompatButton btn_proceed2 = dialog1.findViewById(R.id.btn_premium);
        btn_proceed1.setOnClickListener(v -> dialog1.dismiss());

        btn_proceed2.setOnClickListener(v -> {
            beginDownload(position);
            dialog1.dismiss();
        });

        dialog1.show();
    }

    private void beginDownload(int i) {

        Thread thread = new Thread(() -> {
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
        });

        thread.start();

    }

    @Override
    public int getItemCount() {
        return wallPaperModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewWallPaper;
        TextView title;
        RelativeLayout main_rl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_rl = itemView.findViewById(R.id.main_rl);
            imageViewWallPaper = itemView.findViewById(R.id.imageViewWallPaper);
            title = itemView.findViewById(R.id.title);
        }
    }
}
