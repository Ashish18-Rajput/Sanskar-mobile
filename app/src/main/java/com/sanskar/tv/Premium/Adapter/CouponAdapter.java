package com.sanskar.tv.Premium.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.Coupon;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    Context context;
    List<Coupon> couponList;

    public CouponAdapter(Context context, List<Coupon> couponList) {
        this.context = context;
        this.couponList = couponList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.couponlistlayout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.firstTextView.setText(couponList.get(i).getCouponTilte());
        if (couponList.get(i).getCouponType().equalsIgnoreCase("1")){
            holder.first_rupee.setText("Rs "+couponList.get(i).getCouponValue()+" off");
        }
        if (couponList.get(i).getCouponType().equalsIgnoreCase("2")){
            holder.first_rupee.setText(couponList.get(i).getCouponValue()+"% off");
        }

        holder.relativeLayout_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (couponList.get(i).getCouponType().equalsIgnoreCase("1")){
                    if (Integer.parseInt(PaymentActivity.amount)<Integer.parseInt(couponList.get(i).getCouponValue())){
                        ToastUtil.showDialogBox(context,"Can't apply this coupon");
                    }else{
                        //holder.first_rupee.setText("FLAT "+couponList.get(i).getCouponValue()+" off");
                        int amount=Integer.parseInt(PaymentActivity.amount)-Integer.parseInt(couponList.get(i).getCouponValue());
                        ((PaymentActivity)context).UpdateTotalAmount(amount);
                        PaymentActivity.coupon_applied=couponList.get(i).getId();
                        PaymentActivity.promo_code_applied="";
                        ((PaymentActivity)context).coupon.setText(couponList.get(i).getCouponTilte());
                    }

                }
                if (couponList.get(i).getCouponType().equalsIgnoreCase("2")){
                   // holder.first_rupee.setText(couponList.get(i).getCouponValue()+"% off");
                    int amount1=Integer.parseInt(PaymentActivity.amount)*Integer.parseInt(couponList.get(i).getCouponValue())/100;
                    int amount=Integer.parseInt(PaymentActivity.amount)-amount1;
                    ((PaymentActivity)context).UpdateTotalAmount(amount);
                    PaymentActivity.coupon_applied=couponList.get(i).getId();
                    PaymentActivity.promo_code_applied="";
                    ((PaymentActivity)context).coupon.setText(couponList.get(i).getCouponTilte());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstTextView,first_rupee;
        RelativeLayout relativeLayout_first;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            first_rupee=itemView.findViewById(R.id.first_rupee);
            firstTextView=itemView.findViewById(R.id.firstTextView);
            relativeLayout_first=itemView.findViewById(R.id.relativeLayout_first);
        }
    }
}
