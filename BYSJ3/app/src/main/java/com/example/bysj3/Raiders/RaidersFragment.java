package com.example.bysj3.Raiders;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.Raiders.RaidersSecondActivity;
import com.example.bysj3.R;
import com.example.bysj3.Util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RaidersFragment extends Fragment {

    public String article_id;
    public String title;
    public String content;
    public String user;
    public String address;
    public String image;
    public String user_image;
    public List<Map<String, Object>> list = new ArrayList<>();

    private SwipeRefreshLayout refresh_raiders;
    private RecyclerView raidersRecycleView;

    private String url = "http://39.97.251.81:8080/test5/DataRaiders";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raiders,container,false);

        raidersRecycleView = view.findViewById(R.id.rv_raiders_content);

        okhttpData();

        refresh_raiders = view.findViewById(R.id.refresh_raiders);
        refresh_raiders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                okhttpData();
                                refresh_raiders.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
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
//                Log.e("Raiders", "responseData: " + responseData);
                jsonJXDate(responseData);
            }

            private void jsonJXDate(String resposeData) {
                if (resposeData != null) {
                    try {
                        JSONArray array = new JSONArray(resposeData);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            article_id = object.getString("article_id");
                            address = object.getString("address");
                            user = object.getString("user");
                            image = object.getString("image");
                            user_image = object.getString("user_image");
                            Map<String, Object> map = new HashMap<>();
                            map.put("article_id",article_id);
                            map.put("address", address);
                            map.put("user", user);
                            map.put("image", image);
                            map.put("user_image", user_image);
                            list.add(map);

                        }
//                        Log.e("Raiders", "adress: " + adress);
//                        Log.e("Raiders", "list: " + list);
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
                            raidersRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                            RaidersFragment.RaidersAdapter adapter = new RaidersFragment.RaidersAdapter(list, getActivity());
                            raidersRecycleView.setAdapter(adapter);
                            break;
                    }
                    return false;
                }
            });
        });
    }



    //内置适配器
    class RaidersAdapter extends RecyclerView.Adapter<RaidersFragment.RaidersAdapter.RaidersHolder> {

        public List<Map<String, Object>> list;
        public Context con;
        public LayoutInflater inflater;

        public RaidersAdapter(List<Map<String, Object>> list, Context con) {
            this.con = con;
            this.list = list;
            inflater = LayoutInflater.from(con);
        }

        @NonNull
        @Override
        public RaidersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_raiders, parent, false);
            final RaidersHolder holder = new RaidersHolder(view);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Map map = list.get(position);
                    String address = (String) map.get("address");
                    String article_id = (String) map.get("article_id");
                    Intent intent = new Intent(getActivity(), RaidersSecondActivity.class);
                    intent.putExtra("address", address);
                    intent.putExtra("article_id", article_id);
                    startActivity(intent);

                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RaidersFragment.RaidersAdapter.RaidersHolder holder, final int position) {
            holder.tv_adapter_raiders_title.setText(list.get(position).get("address").toString());
            holder.tv_adapter_raiders_user.setText(list.get(position).get("user").toString());
            Glide.with(con).load(list.get(position).get("image").toString()).into(holder.iv_adapter_raiders_image);
            Glide.with(con)
                    .load(list.get(position).get("user_image").toString())
                    .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0, 0)))
                    .into(holder.iv_adapter_raiders_user_image);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class RaidersHolder extends RecyclerView.ViewHolder {
            View itemView;
            public TextView tv_adapter_raiders_title;
            public TextView tv_adapter_raiders_user;
            public ImageView iv_adapter_raiders_image;
            public ImageView iv_adapter_raiders_user_image;
            public RaidersHolder(View view) {
                super(view);
                itemView = view;
                tv_adapter_raiders_title = view.findViewById(R.id.tv_adapter_raiders_title);
                tv_adapter_raiders_user = view.findViewById(R.id.tv_adapter_raiders_user);
                iv_adapter_raiders_image= view.findViewById(R.id.iv_adapter_raiders_image);
                iv_adapter_raiders_user_image = view.findViewById(R.id.iv_adapter_raiders_user_image);
            }
        }
    }
}