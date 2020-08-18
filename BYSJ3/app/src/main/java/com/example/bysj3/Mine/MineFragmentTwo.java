package com.example.bysj3.Mine;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.Home.HomeSecondActivity;
import com.example.bysj3.MyAPPlication;
import com.example.bysj3.R;
import com.example.bysj3.Util.HttpUtilSearch;

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

public class MineFragmentTwo extends Fragment {
    public String user_image;
    public String title;
    public String content;
    public String user;
    public String image;
    public String article_id;

    public List<Map<String, Object>> list = new ArrayList<>();

    private RecyclerView mineTwoRecycleView;

    private String url = "http://39.97.251.81:8080/test5/SearchNameCollectServlet";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_two, container, false);
        mineTwoRecycleView = view.findViewById(R.id.rv_mine_fragmrnt_two);
        okhttp();




        return view;
    }

    private void okhttp() {
        list.clear();
        String anydata = MyAPPlication.getGlobalvariable();
        Log.e("anydata2", "anydata: "+anydata );
        HttpUtilSearch.sendOkHttpRequest(url, anydata, new Callback() {
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
                            user_image = object.getString("user_image");
                            title = object.getString("title");
                            article_id = object.getString("article_id");
                            user = object.getString("user");
                            image = object.getString("image");
                            Log.e("title", "title: " + title );
                            Map<String, Object> map = new HashMap<>();
                            map.put("user_image",user_image);
                            map.put("title", title);
                            map.put("article_id", article_id);
                            map.put("user", user);
                            map.put("image", image);
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
                            mineTwoRecycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            MineFragmentTwoAdapter adapter = new MineFragmentTwoAdapter(list, getActivity());
                            mineTwoRecycleView.setAdapter(adapter);
//                            mineTwoRecycleView.setNestedScrollingEnabled(false);
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
    }

    //内置适配器
    class MineFragmentTwoAdapter extends RecyclerView.Adapter<MineFragmentTwoAdapter.MineFragmentTwoHolder> {

        public List<Map<String, Object>> list;
        public Context con;
        public LayoutInflater inflater;

        public MineFragmentTwoAdapter(List<Map<String, Object>> list, Context con) {
            this.con = con;
            this.list = list;
            inflater = LayoutInflater.from(con);
        }

        @NonNull
        @Override
        public MineFragmentTwoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mine_two, parent, false);
            final MineFragmentTwoHolder holder = new MineFragmentTwoHolder(view);

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
        public void onBindViewHolder(@NonNull MineFragmentTwoAdapter.MineFragmentTwoHolder holder, final int position) {
            holder.tv_adapter_mine_two_title.setText(list.get(position).get("title").toString());
            holder.tv_adapter_mine_two_user.setText(list.get(position).get("user").toString());
            Glide.with(con).load(list.get(position).get("image").toString()).into(holder.iv_adapter_mine_two_image);
            Glide.with(con).load(list.get(position).get("user_image").toString())
                    .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0,0)))
                    .into(holder.iv_adapter_mine_two_user_image);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MineFragmentTwoHolder extends RecyclerView.ViewHolder {
            View itemView;
            public TextView tv_adapter_mine_two_title;
            public TextView tv_adapter_mine_two_user;
            public ImageView iv_adapter_mine_two_image;
            public ImageView iv_adapter_mine_two_user_image;

            public MineFragmentTwoHolder(View view) {
                super(view);
                itemView = view;
                tv_adapter_mine_two_title = view.findViewById(R.id.tv_adapter_mine_two_title);
                tv_adapter_mine_two_user = view.findViewById(R.id.tv_adapter_mine_two_user);
                iv_adapter_mine_two_image = view.findViewById(R.id.iv_adapter_mine_two_image);
                iv_adapter_mine_two_user_image = view.findViewById(R.id.iv_adapter_mine_two_user_image);
            }
        }
    }
}