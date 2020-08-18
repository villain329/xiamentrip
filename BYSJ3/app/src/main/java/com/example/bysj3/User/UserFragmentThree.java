package com.example.bysj3.User;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserFragmentThree extends Fragment {

    public String title;
    public String content;
    public String user;
    public String image;
    public String article_id;

    public List<Map<String, Object>> list = new ArrayList<>();

    private RecyclerView userOThreeRecycleView;

    private String url = "http://39.97.251.81:8080/test5/SearchNameLikeServlet";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_three, container, false);
        userOThreeRecycleView = view.findViewById(R.id.rv_user_fragmrnt_three);
        okhttp();


        return view;
    }


    private void okhttp() {
        list.clear();
        String anydata = getActivity().getIntent().getStringExtra("user_id");
        Log.e("anydata1", "anydata: "+anydata );
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
                            title = object.getString("title");
                            article_id = object.getString("article_id");
                            user = object.getString("user");
                            image = object.getString("image");
                            Log.e("title", "title: " + title );
                            Map<String, Object> map = new HashMap<>();
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
                            userOThreeRecycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            UserFragmentThreeAdapter adapter = new UserFragmentThreeAdapter(list, getActivity());
                            userOThreeRecycleView.setAdapter(adapter);
//                            mineOneRecycleView.setNestedScrollingEnabled(false);
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
    class UserFragmentThreeAdapter extends RecyclerView.Adapter<UserFragmentThreeAdapter.UserFragmentThreeHolder> {

        public List<Map<String, Object>> list;
        public Context con;
        public LayoutInflater inflater;

        public UserFragmentThreeAdapter(List<Map<String, Object>> list, Context con) {
            this.con = con;
            this.list = list;
            inflater = LayoutInflater.from(con);
        }

        @NonNull
        @Override
        public UserFragmentThreeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_three, parent, false);
            final UserFragmentThreeHolder holder = new UserFragmentThreeHolder(view);

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
        public void onBindViewHolder(@NonNull UserFragmentThreeAdapter.UserFragmentThreeHolder holder, final int position) {
            holder.tv_adapter_user_three_title.setText(list.get(position).get("title").toString());
            holder.tv_adapter_user_three_user.setText(list.get(position).get("user").toString());
            Glide.with(con).load(list.get(position).get("image").toString()).into(holder.iv_adapter_user_three_image);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class UserFragmentThreeHolder extends RecyclerView.ViewHolder {
            View itemView;
            public TextView tv_adapter_user_three_title;
            public TextView tv_adapter_user_three_user;
            public ImageView iv_adapter_user_three_image;

            public UserFragmentThreeHolder(View view) {
                super(view);
                itemView = view;
                tv_adapter_user_three_title = view.findViewById(R.id.tv_adapter_user_three_title);
                tv_adapter_user_three_user = view.findViewById(R.id.tv_adapter_user_three_user);
                iv_adapter_user_three_image = view.findViewById(R.id.iv_adapter_user_three_image);
            }
        }
    }
}