package com.sanskar.tv.Others.Helper;

import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.R;


public class NotificationDialog implements View.OnClickListener {

    Context ctx;
    Fragment frag;
    BottomSheetDialog dialog;
    private RelativeLayout shareLay;
    private RelativeLayout addPlayListLay;
    private RelativeLayout cancelLay;

    public NotificationDialog(Context ctx, Fragment frag) {
        this.ctx = ctx;
        this.frag = frag;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_lay:
                if (frag instanceof BhajanPlayFragment) {
                    ((BhajanPlayFragment) frag).shareTextUrl();
                }
                dialog.cancel();
                break;
            case R.id.add_play_list_lay:
                if (frag instanceof BhajanPlayFragment) {
                    ((BhajanPlayFragment) frag).addPlayListData();
                    ToastUtil.showDialogBox(ctx, "BhajanCat added in my playlist successfully");
                }
                dialog.cancel();
                break;
            case R.id.cancel_lay:
                dialog.cancel();
                break;
        }
    }

    public void showPopup() {
        dialog = new BottomSheetDialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lay);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        shareLay  =  dialog.findViewById(R.id.share_lay);
        addPlayListLay  =  dialog.findViewById(R.id.add_play_list_lay);
        cancelLay  =  dialog.findViewById(R.id.cancel_lay);

        shareLay.setOnClickListener(this);
        addPlayListLay.setOnClickListener(this);
        cancelLay.setOnClickListener(this);

        dialog.show();
    }
}
