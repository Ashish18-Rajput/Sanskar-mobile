package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanListByMenuMasterAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BhajanListByMenumaster extends Fragment implements NetworkCall.MyNetworkCallBack {

    RecyclerView recyclerView;
    private String lastGuruId = "";
    Bhajan bhajan;
    List<Bhajan> bhajans=new ArrayList<>();
    String Video_id="";
    String Category="";
    String Category_name = "";
    Context context;
    HomeActivityy activityy;
    boolean loading = false;
    int pastVisiblesItems , visibleItemCount, totalItemCount ;

    BhajanListByMenuMasterAdapter bhajanListByMenuMasterAdapter;

    GridLayoutManager gridLayoutManager;
    RelativeLayout relativeLayout;
    NetworkCall networkCall;
    int page_no=1;
    TextView no_data_found_text,title_textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        activityy=(HomeActivityy)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_bhajan_list_by_menumaster, container, false);
        getBundleData();
        initViews(view);

        fetchData(true);


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    loading = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = gridLayoutManager.getChildCount();
                Log.d("Shantanu", "visibleItemCount " + visibleItemCount);
                totalItemCount = gridLayoutManager.getItemCount();
                Log.d("Shantanu", "totalItemCount " + totalItemCount);
                pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                Log.d("Shantanu", "pastVisiblesItems " + pastVisiblesItems);

                if (loading && visibleItemCount+pastVisiblesItems==totalItemCount) {
                    loading = false;
                    page_no++;
                    //progress_layout.setVisibility(View.VISIBLE);
                   //lastGuruId = ((HomeActivityy) context).homeGuruList.get(totalItemCount - 1).getId();
                    relativeLayout.setVisibility(View.VISIBLE);
                    Log.d("Shantanu", "shantanu");

                    fetchData(false);
                }
            }
        });

        bhajanListByMenuMasterAdapter=new BhajanListByMenuMasterAdapter(context,bhajans);
        recyclerView.setAdapter(bhajanListByMenuMasterAdapter);
      //  recyclerView.scheduleLayoutAnimation();

        return view;
    }
    private void fetchData(boolean load) {

        networkCall=new NetworkCall(this,context);
        networkCall.NetworkAPICall(API.GET_BHAJAN_LIST_BY_MENUMASTER,load);

    }

    private void getBundleData() {

        Bundle bundle=getArguments();
        if (bundle.containsKey("Id")){
            Video_id=bundle.getString("Id");
        }
        if (bundle.containsKey("Category")){
            Category=bundle.getString("Category");
        }
        if (bundle.containsKey("Category_name")) {
            Category_name = bundle.getString("Category_name");
        }
    }

    private void initViews(View view) {

        recyclerView=view.findViewById(R.id.recyclerView);
        no_data_found_text=view.findViewById(R.id.no_data_found_text);
        gridLayoutManager=new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        title_textView=view.findViewById(R.id.title_textView);

        title_textView.setText(Category_name);
        relativeLayout=view.findViewById(R.id.progress_layout);

    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {

        Builders.Any.B ion=null;
        if (apitype.equals(API.GET_BHAJAN_LIST_BY_MENUMASTER)){
            ion = (Builders.Any.B) Ion.with(getContext()).load(apitype)
                    
                    .setMultipartParameter("user_id", activityy.signupResponse.getId())
                    .setMultipartParameter("menu_master_id",Video_id)
                    .setMultipartParameter("limit",String.valueOf(10))
                    .setMultipartParameter("category",Category)
                    .setMultipartParameter("page_no", String.valueOf(page_no));

        }


        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {

        relativeLayout.setVisibility(View.GONE);
        if (jsonstring.optBoolean("status")){
            recyclerView.setVisibility(View.VISIBLE);
            no_data_found_text.setVisibility(View.GONE);

            JSONArray jsonArray=jsonstring.optJSONArray("data");
            if (jsonArray.length()>0){

                for(int i=0;i<jsonArray.length();i++){
                    bhajan=new Gson().fromJson(jsonArray.opt(i).toString(),Bhajan.class);
                    bhajans.add(bhajan);

                  //  activityy.bhajanList.add(bhajan);
                }
               /* HashSet<Bhajan> bhajanHashSet=new HashSet<>();
                bhajanHashSet.addAll(activityy.homeBhajanList);
*/
                /*activityy.homeBhajanList=new ArrayList<>();
                activityy.homeBhajanList.addAll(bhajans);*/


                bhajanListByMenuMasterAdapter.notifyDataSetChanged();

                loading=true;
            }else{
                loading=false;
               /* recyclerView.setVisibility(View.GONE);
                no_data_found_text.setVisibility(View.VISIBLE);*/
            }

        }else{
            recyclerView.setVisibility(View.GONE);
            no_data_found_text.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}