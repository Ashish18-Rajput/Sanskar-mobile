package com.sanskar.tv.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;


public class AppButton extends AppCompatButton {

    public AppButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        try {
            Typeface myFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato_Heavy.ttf");

            setTypeface(myFont);
        } catch (Exception e) {
            Log.e("Tag", e.toString());
        }
    }

}