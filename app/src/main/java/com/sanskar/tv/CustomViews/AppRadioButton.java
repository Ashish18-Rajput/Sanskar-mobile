package com.sanskar.tv.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by surya on 16/1/18.
 */

public class AppRadioButton extends AppCompatRadioButton {

    public AppRadioButton(Context context) {
        super(context);
        init(context);
    }

    public AppRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init(Context context) {
        try {
            Typeface myFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato_Regular.ttf");

            setTypeface(myFont);
        } catch (Exception e) {
            Log.e("Tag", e.toString());
        }
    }
}
