package com.sanskar.tv.Others.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.sanskar.tv.R;


/**
 * Created by surya on 28/11/17.
 */

public class Progress extends Dialog {
    private AnimationDrawable animationDrawable;
    private ImageView mProgressBar;

    public Progress(Context context) {
        super(context/*, android.R.style.Theme_Translucent_NoTitleBar*/);
        super.setCancelable(false);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.progressbar);

        mProgressBar = findViewById(R.id.progress_image);
       // GlideDr imageViewTarget = new GlideDrawableImageViewTarget(mProgressBar);
        Glide.with(context).load(R.mipmap.loader).into(mProgressBar);

//        mProgressBar.setBackgroundResource(R.drawable.loading_progress);
//        animationDrawable = (AnimationDrawable)mProgressBar.getBackground();
    }

    @Override
    public void show() {
        try {
            super.show();
            mProgressBar.setVisibility(View.VISIBLE);
        }catch (Exception e){}
//        animationDrawable.start();
    }

    @Override
    public void hide() {
        super.hide();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
