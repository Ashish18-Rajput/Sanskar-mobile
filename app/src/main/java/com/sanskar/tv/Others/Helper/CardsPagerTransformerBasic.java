package com.sanskar.tv.Others.Helper;

import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

public class CardsPagerTransformerBasic implements ViewPager.PageTransformer {
  
  private int baseElevation;
  private int raisingElevation;
  private float smallerScale;
  
  public CardsPagerTransformerBasic(int baseElevation, int raisingElevation, float smallerScale) {
    this.baseElevation = baseElevation;
    this.raisingElevation = raisingElevation;
    this.smallerScale = smallerScale;
  }
  
  @Override
  public void transformPage(View page, float position) {
    float absPosition = Math.abs(position);

    if (absPosition == 0) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        page.setElevation(baseElevation + raisingElevation);
      }
//      page.setScaleY(smallerScale);
//
// } else if(absPosition>0) {
//      // This will be during transformation
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        page.setElevation(((1 - absPosition) * raisingElevation + baseElevation));
//      }
//      page.setScaleY((smallerScale - 1) * absPosition + 1);
    }
  }
  
}