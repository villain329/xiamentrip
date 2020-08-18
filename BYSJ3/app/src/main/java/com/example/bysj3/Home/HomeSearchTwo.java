package com.example.bysj3.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.bysj3.R;
import com.example.bysj3.Util.HttpUtil;
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

public class HomeSearchTwo extends Fragment {

    public String title;
    public String content;
    public String user;
    public String image;
public String article_id;
    public List<Map<String, Object>> list = new ArrayList<>();

    private SwipeRefreshLayout refresh_home_search;
    private RecyclerView homesearchRecycleView;
    private EditText et_home_search_search;
    private TextView tv_home_search_search;
    private String url = "http://39.97.251.81:8080/test5/SearchServlet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_search_two, container, false);

        homesearchRecycleView = view.findViewById(R.id.rv_home_search_content);
        tv_home_search_search = getActivity().findViewById(R.id.tv_home_search_search);
        tv_home_search_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okhttpData();
            }
        });

        et_home_search_search = getActivity().findViewById(R.id.et_home_search_search);
        et_home_search_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    okhttpData();
                }
                return false;

            }
        });
        okhttpData();

        refresh_home_search = view.findViewById(R.id.refresh_home_search);
        refresh_home_search.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                                refresh_home_search.setRefreshing(false);
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
        String anydata = et_home_search_search.getText().toString();
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
                            homesearchRecycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            HomeSearchAdapter adapter = new HomeSearchAdapter(list, getActivity());
                            homesearchRecycleView.setAdapter(adapter);
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
    class HomeSearchAdapter extends RecyclerView.Adapter<HomeSearchAdapter.HomeSearchHolder> {

        public List<Map<String, Object>> list;
        public Context con;
        public LayoutInflater inflater;

        public HomeSearchAdapter(List<Map<String, Object>> list, Context con) {
            this.con = con;
            this.list = list;
            inflater = LayoutInflater.from(con);
        }

        @NonNull
        @Override
        public HomeSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_search, parent, false);
            final HomeSearchHolder holder = new HomeSearchHolder(view);

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
        public void onBindViewHolder(@NonNull HomeSearchAdapter.HomeSearchHolder holder, final int position) {
            holder.tv_adapter_home_search_title.setText(list.get(position).get("title").toString());
            holder.tv_adapter_home_search_user.setText(list.get(position).get("user").toString());
            Glide.with(con).load(list.get(position).get("image").toString()).into(holder.iv_adapter_home_search_image);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class HomeSearchHolder extends RecyclerView.ViewHolder {
            View itemView;
            public TextView tv_adapter_home_search_title;
            public TextView tv_adapter_home_search_user;
            public ImageView iv_adapter_home_search_image;

            public HomeSearchHolder(View view) {
                super(view);
                itemView = view;
                tv_adapter_home_search_title = view.findViewById(R.id.tv_adapter_home_search_title);
                tv_adapter_home_search_user = view.findViewById(R.id.tv_adapter_home_search_user);
                iv_adapter_home_search_image = view.findViewById(R.id.iv_adapter_home_search_image);
            }
        }
    }
}