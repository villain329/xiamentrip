package com.example.bysj3.Message;

import android.annotation.SuppressLint;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class MessageFragmentOne extends Fragment {

    public String title;
    public String content;
    public String user;
    public String image;
    public String user_image;
    public String article_id;

    public List<Map<String, Object>> list = new ArrayList<>();

    private RecyclerView messageOneRecycleView;
    private SwipeRefreshLayout refresh_message;
    private String url = "http://39.97.251.81:8080/test5/SearchNameServlet";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_one, container, false);

        messageOneRecycleView = view.findViewById(R.id.rv_message_fragmrnt_one);
        okhttp();

        refresh_message = view.findViewById(R.id.refresh_message);
        refresh_message.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                                okhttp();
                                refresh_message.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });

        return view;
    }


    private void okhttp() {
        list.clear();
        String anydata = MyAPPlication.getGlobalvariable();
        Log.e("anydata123", "anydata: "+anydata );
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
                            user_image = object.getString("user_image");
                            Log.e("title", "title: " + title );
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("article_id", article_id);
                            map.put("user", user);
                            map.put("user_image",user_image);
                            map.put("image", image);
                            list.add(map);

                        }
                        Log.e("messlist", "list: "+list );
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
                            messageOneRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                            MessageFragmentOneAdapter adapter = new MessageFragmentOneAdapter(list, getActivity());
                            messageOneRecycleView.setAdapter(adapter);
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
    class MessageFragmentOneAdapter extends RecyclerView.Adapter<MessageFragmentOneAdapter.MessageFragmentOneHolder> {

        public List<Map<String, Object>> list;
        public Context con;
        public LayoutInflater inflater;

        public MessageFragmentOneAdapter(List<Map<String, Object>> list, Context con) {
            this.con = con;
            this.list = list;
            inflater = LayoutInflater.from(con);
        }

        @NonNull
        @Override
        public MessageFragmentOneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message_one, parent, false);
            final MessageFragmentOneHolder holder = new MessageFragmentOneHolder(view);

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

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MessageFragmentOneAdapter.MessageFragmentOneHolder holder, final int position) {
            holder.tv_adapter_message_one_what.setText(list.get(position).get("user").toString());
            Glide.with(con)
                    .load(list.get(position).get("user_image").toString())
                    .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0, 0)))
                    .into(holder.iv_adapter_message_one_image_user);
            Glide.with(con).load(list.get(position).get("image").toString()).into(holder.iv_adapter_message_one_image_article);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MessageFragmentOneHolder extends RecyclerView.ViewHolder {
            View itemView;
            public TextView tv_adapter_message_one_what;
            public ImageView iv_adapter_message_one_image_user;
            public ImageView iv_adapter_message_one_image_article;
            public MessageFragmentOneHolder(View view) {
                super(view);
                itemView = view;
                tv_adapter_message_one_what = view.findViewById(R.id.tv_adapter_message_one_what);
                iv_adapter_message_one_image_user = view.findViewById(R.id.iv_adapter_message_one_image_user);
                iv_adapter_message_one_image_article = view.findViewById(R.id.iv_adapter_message_one_image_article);
            }
        }
    }
}