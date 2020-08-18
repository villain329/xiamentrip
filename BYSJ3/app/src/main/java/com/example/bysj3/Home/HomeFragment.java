package com.example.bysj3.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.R;
import com.example.bysj3.Util.HttpUtil;
import com.example.refreshview.CustomRefreshView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {


    public String article_id;
    public String title;
    public String content;
    public String user;
    public String image;
    public String user_image;
    private int pagerSize = 5;
    public List<Map<String, Object>> list = new ArrayList<>();
    private List<String> data = new ArrayList<>();

    private CustomRefreshView refreshView;
    private RecyclerView homeRecycleView;

    private String url = "http://39.97.251.81:8080/test5/Data";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeRecycleView = view.findViewById(R.id.rv_home_content);


        okhttpData();
        refreshView = view.findViewById(R.id.refresh_view);


        final HomeAdapter adapter = new HomeAdapter(list, getActivity());
        refreshView.setAdapter(adapter);

        //下拉刷新
        refreshView.setOnLoadListener(new CustomRefreshView.OnLoadListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        okhttpData();

                        refreshView.complete();
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //设置自动下拉刷新，切记要在recyclerView.setOnLoadListener()之后调用
        //因为在没有设置监听接口的情况下，setRefreshing(true),调用不到OnLoadListener
        refreshView.setRefreshing(true);


        return view;
    }


    private void okhttpData() {
        list.clear();
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                jsonJXDate(responseData);
            }

            private void jsonJXDate(String resposeData) {
                if (resposeData != null) {
                    try {
                        JSONArray array = new JSONArray(resposeData);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            article_id = object.getString("article_id");
                            title = object.getString("title");
                            content = object.getString("content");
                            user = object.getString("user");
                            image = object.getString("image");
                            user_image = object.getString("user_image");
                            Map<String, Object> map = new HashMap<>();
                            map.put("article_id", article_id);
                            map.put("title", title);
                            map.put("content", content);
                            map.put("user", user);
                            map.put("image", image);
                            map.put("user_image",user_image);
                            list.add(map);
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            public Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    switch (msg.what) {
                        case 1:
                            refreshView.getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            HomeAdapter adapter = new HomeAdapter(list, getActivity());
                            refreshView.setAdapter(adapter);
                            break;
                    }
                    return false;
                }
            });
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancaState) {
        super.onViewCreated(view, savedInstancaState);

        final TextView tv_home_search = getActivity().findViewById(R.id.tv_home_search);
        tv_home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().addOnBackStackChangedListener(null);
                Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
                startActivity(intent);
            }
        });

    }

    //内置适配器
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

        public List<Map<String, Object>> list;
        public Context con;
        public LayoutInflater inflater;


        public HomeAdapter(List<Map<String, Object>> list, Context con) {
            this.con = con;
            this.list = list;
            inflater = LayoutInflater.from(con);
        }

        @NonNull
        @Override
        public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home, parent, false);


            final HomeHolder holder = new HomeHolder(view);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Map map = list.get(position);
                    String article_id = (String) map.get("article_id");
                    Intent intent = new Intent(getActivity(), HomeSecondActivity.class);
                    intent.putExtra("article_id", article_id);
                    startActivity(intent);

                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeAdapter.HomeHolder holder, final int position) {
            holder.tv_adapter_home_title.setText(list.get(position).get("title").toString());
            holder.tv_adapter_home_user.setText(list.get(position).get("user").toString());
            Glide.with(con)
                    .load(list.get(position).get("image").toString())
                    .into(holder.iv_adapter_home_image);
            Glide.with(con)
                    .load(list.get(position).get("user_image").toString())
                    .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0,0)))
                    .into(holder.iv_adapter_home_user_image);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }


        class HomeHolder extends RecyclerView.ViewHolder {
            View itemView;
            public TextView tv_adapter_home_title;
            public TextView tv_adapter_home_user;
            public ImageView iv_adapter_home_image;
            public ImageView iv_adapter_home_user_image;

            public HomeHolder(View view) {
                super(view);
                itemView = view;
                tv_adapter_home_title = view.findViewById(R.id.tv_adapter_home_title);
                tv_adapter_home_user = view.findViewById(R.id.tv_adapter_home_user);
                iv_adapter_home_image = view.findViewById(R.id.iv_adapter_home_image);
                iv_adapter_home_user_image = view.findViewById(R.id.iv_adapter_home_user_image);
            }
        }
    }
}