package com.sanskar.tv.Premium.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.Plans;

import java.util.List;

public class PremiumPlanAdapter extends RecyclerView.Adapter<PremiumPlanAdapter.ViewHolder> {

    Context context;
    List<Plans> plansList;
    RadioButton lastCheckedRB=null;
    boolean isTap=false;
    int previousSelectedPosition = -1;

    public PremiumPlanAdapter(Context context, List<Plans> plansList) {
        this.context = context;
        this.plansList = plansList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.planlistlayout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        Plans plans=plansList.get(i);
        holder.firstTextView.setText(plans.getPlanName());
        if (plans.getCurrency().equalsIgnoreCase("INR")){
            holder.first_rupee.setText("\u20B9 " + plans.getAmount());
        }

        if (plans.getCurrency().equalsIgnoreCase("USD")){
            holder.first_rupee.setText("$ " + plans.getAmount());
        }

        holder.first_radioButton.setChecked(plans.isChecked());

        holder.first_radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(previousSelectedPosition >= 0)
                        plansList.get(previousSelectedPosition).setChecked(false);
                    previousSelectedPosition = i;
                    plans.checked = !plans.checked;
                    if(lastCheckedRB!=null)
                        lastCheckedRB.setChecked(false);
                    lastCheckedRB = holder.first_radioButton;

                    PaymentActivity.isChecked=true;
                    PaymentActivity.amount=plansList.get(i).getAmount();
                    PaymentActivity.plan_id=plansList.get(i).getPlanId();
                    PaymentActivity.validity=plansList.get(i).getValidity();
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstTextView,first_rupee;
        RadioButton first_radioButton;

        RelativeLayout relativeLayout_first;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstTextView=itemView.findViewById(R.id.firstTextView);

            first_rupee=itemView.findViewById(R.id.first_rupee);
            first_radioButton=itemView.findViewById(R.id.first_radioButton);
            relativeLayout_first=itemView.findViewById(R.id.relativeLayout_first);
        }
    }
}
