package com.sanskar.tv.module.HomeModule.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Pojo.ActiveDevicesModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveDevicesAdapter extends RecyclerView.Adapter<ActiveDevicesAdapter.ViewHolder> {

    Context context;
    Fragment fragment;
    List<ActiveDevicesModel> activeDevicesModelList;
    SelectDevicesForDetach selectDevicesForDetach;
    Map<Integer,String> brandMap = new HashMap<>();
    String[] colors = {"#F9AA31", "#F64A14", "#1164A6", "#F1754E", "#4CAF50"};

    Boolean selectMultipleDevices = false;

    public ActiveDevicesAdapter(Context context, Fragment fragment, List<ActiveDevicesModel> activeDevicesModelList,ActiveDevicesAdapter.SelectDevicesForDetach selectDevicesForDetach) {
        this.context = context;
        this.fragment = fragment;
        this.activeDevicesModelList = activeDevicesModelList;
        this.selectDevicesForDetach= selectDevicesForDetach;
    }

    public void updateSelectedDevicesList(List<ActiveDevicesModel> selectedDevices, Map<Integer,String> brandMap){
        this.activeDevicesModelList=selectedDevices;
        this.brandMap = brandMap;
        if(brandMap.isEmpty()) selectMultipleDevices= false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_active_devices, parent, false));
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String device_id = SharedPreference.getInstance(context).getString("android_id");
        if(activeDevicesModelList.get(position).getDevice_id().equalsIgnoreCase(device_id)){
            holder.main_rl.setAlpha(0.2f);
        }
        holder.mainTitle.setText(getInitials(position));
        holder.subTitle.setText(activeDevicesModelList.get(position).getDevice_name());
        holder.main_rl.setBackgroundColor(Color.parseColor(colors[position]));
        if(activeDevicesModelList.get(position).getIsSelected()){
            holder.selectDevice.setVisibility(View.VISIBLE);
        }
        else{
            holder.selectDevice.setVisibility(View.GONE);
        }
        holder.main_rl.setOnClickListener(view ->{
            if(activeDevicesModelList.get(position).getDevice_id().equalsIgnoreCase(device_id)){
                Toast.makeText(context, "You can not select this device ", Toast.LENGTH_SHORT).show();
                holder.main_rl.setClickable(false);
            }else {
                if (selectMultipleDevices)
                    selectDevicesForDetach.selectDevices(holder, holder.getAdapterPosition());
                else
                    ToastUtil.showDialogBoxForDetach(context, "<h1 >Are you sure you want to detach this device.</h1> <br> <h1>क्या आप वाकई इस डिवाइस को अलग करना चाहते हैं।</h1>", fragment, position);
            }
        });

        holder.main_rl.setOnLongClickListener(view -> {
            if(activeDevicesModelList.get(position).getDevice_id().equalsIgnoreCase(device_id)){
                Toast.makeText(context, "You can not select this device ", Toast.LENGTH_SHORT).show();
               return false;
            }
            else {
                selectDevicesForDetach.selectDevices(holder, holder.getAdapterPosition());
                selectMultipleDevices = true;
                return true;
            }
        });

        if(activeDevicesModelList.get(position).getDevice_id().equalsIgnoreCase(device_id)){
            holder.main_rl.setClickable(false);
        }
    }

    private String getInitials(int position) {
        String initials = "";

        String[] s = activeDevicesModelList.get(position).getDevice_name().split(" ");

        for (String s1 : s) {
            initials = initials.concat(String.valueOf(s1.charAt(0)));
        }

        return initials.toUpperCase().substring(0, Math.min(3, initials.length()));
    }

    @Override
    public int getItemCount() {
        return activeDevicesModelList.size();
    }

    public interface SelectDevicesForDetach{
        void selectDevices(RecyclerView.ViewHolder viewHolder,int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout main_rl;
        TextView mainTitle, subTitle;
        ImageView selectDevice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subTitle = itemView.findViewById(R.id.subTitle);
            main_rl = itemView.findViewById(R.id.main_rl);
            mainTitle = itemView.findViewById(R.id.mainTitle);
            selectDevice = itemView.findViewById(R.id.checkbox);
        }
    }
}
