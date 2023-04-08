package com.sanskar.tv;

import android.content.Context;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.module.HomeModule.Adapter.NotificationAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.NotificationTypeBean;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    Context context;
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<NotificationTypeBean> notificationTypeList;
    private NetworkCall networkCall;
    RelativeLayout toolbar, noDataFound;
    ImageView back,backNotification;
    TextView title;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }
}
