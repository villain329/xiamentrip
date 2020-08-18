package com.example.bysj3.Raiders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.CommentAcyivity;
import com.example.bysj3.CommentRaidersAcyivity;
import com.example.bysj3.MyAPPlication;
import com.example.bysj3.R;
import com.example.bysj3.User.UserActivity;
import com.example.bysj3.Util.HttpAll;
import com.example.bysj3.Util.HttpUtilSearch;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

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

public class RaidersSecondActivity extends AppCompatActivity implements View.OnClickListener {

    public String user;
    public String user_id;
    public String password;
    public String user_image;
    public String title;
    public String content;
    public String like_id;
    public String collect_id;
    public String comment_id;
    public String comment_user;
    public String comment_user_id;
    public String comment_user_image;


    public String image;
    public String image2;
    public String image3;
    public String image4;
    public String image5;
    public String image6;
    public String image7;
    public String image8;
    public String image9;

    public String comment;

    private Button bt_raiders_second_back;
    private TextView tv_raiders_second_user;
    private TextView tv_raiders_second_title;
    private TextView tv_raiders_second_content;
    private TextView tv_raiders_second_like;
    private TextView tv_raiders_second_collect;
    private TextView tv_raiders_second_comment;
    private TextView tv_raiders_second_comment_send;
    private ImageView iv_raiders_second_image;
    private ImageView iv_raiders_second_like;
    private ImageView iv_raiders_second_collect;
    private ImageView iv_raiders_second_comment;
    private SwipeRefreshLayout refresh_raiders_second;

    private Banner banner;
    private RecyclerView commentRaidersRecycleView;
    private RecyclerView RaidersMainRecycleView;
    public List<Map<String, Object>> list = new ArrayList<>();
    private ArrayList<String> imagelist = new ArrayList<>();
    public List<Map<String, Object>> likelist = new ArrayList<>();
    public List<Map<String, Object>> collectlist = new ArrayList<>();
    public List<Map<String, Object>> commentnumberlist = new ArrayList<>();
    public List<Map<String, Object>> commentlist = new ArrayList<>();
    public List<Map<String, Object>> imageList = new ArrayList<>();
    private String url1 = "http://39.97.251.81:8080/test5/ArticleRaidersServlet";
    private String url = "http://39.97.251.81:8080/test5/CommentRaidersServlet";
    private String url3 = "http://39.97.251.81:8080/test5/LikeRaidersNumberServlet";
    private String url4 = "http://39.97.251.81:8080/test5/CollectRaidersNumberServlet";
    private String url5 = "http://39.97.251.81:8080/test5/CommentRaidersNumberServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_raiders_second);


//评论
        okhttpcomment();
        //图片，标题，内容
        okhttpData();

        //喜欢的数量
        okhttplikereplace();

        //收藏的数量
        okhttpcollectreplace();

        //评论的数量
        okhttpcommentreplace();

        bindView();


        refresh_raiders_second = findViewById(R.id.refresh_raiders_second);
        refresh_raiders_second.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                okhttpcomment();
                                okhttpData();
                                refresh_raiders_second.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });


    }

    private void okhttpcommentreplace() {

        String data = getIntent().getStringExtra("article_id");
        HttpUtilSearch.sendOkHttpRequest(url5, data, new Callback() {
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
                            comment_id = object.getString("comment_id");
                            Map<String, Object> map = new HashMap<>();
                            map.put("collect_id", comment_id);
                            commentnumberlist.add(map);
                        }
                        Message msg = new Message();
                        msg.what = 7;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //收藏的数量
    private void okhttpcollectreplace() {

        String data = getIntent().getStringExtra("article_id");
        HttpUtilSearch.sendOkHttpRequest(url4, data, new Callback() {
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
                            collect_id = object.getString("collect_id");
                            Map<String, Object> map = new HashMap<>();
                            map.put("collect_id", collect_id);
                            collectlist.add(map);
                        }
                        Message msg = new Message();
                        msg.what = 6;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    //喜欢数量
    private void okhttplikereplace() {
        String data = getIntent().getStringExtra("article_id");
        Log.e(String.valueOf(88324), "okhttpData: " + data);
        HttpUtilSearch.sendOkHttpRequest(url3, data, new Callback() {
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
                            like_id = object.getString("like_id");
                            Map<String, Object> map = new HashMap<>();
                            map.put("like_id", like_id);
                            likelist.add(map);
                        }
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //评论
    private void okhttpcomment() {
        commentlist.clear();
        String anydata = getIntent().getStringExtra("article_id");
        Log.e(String.valueOf(88), "okhttpData: " + anydata);
        HttpUtilSearch.sendOkHttpRequest(url, anydata, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                jsonJXDate(responseData);
            }

            private void jsonJXDate(String responseData) {
                if (responseData != null) {
                    try {
                        JSONArray array = new JSONArray(responseData);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            comment_user = object.getString("user");
                            comment_user_id = object.getString("user_id");
                            Log.e(String.valueOf(14), "okhttpData: " + comment_user);
                            comment = object.getString("comment");
                            comment_user_image = object.getString("user_image");
                            Map<String, Object> map = new HashMap<>();
                            map.put("user", comment_user);
                            map.put("user_id", comment_user_id);
                            map.put("comment", comment);
                            map.put("user_image", comment_user_image);
                            commentlist.add(map);
                        }

                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }


    //图片，标题，内容
    private void okhttpData() {
        String anydata = getIntent().getStringExtra("address");
        Log.e(String.valueOf(1), "okhttpData: " + anydata);
        HttpUtilSearch.sendOkHttpRequest(url1, anydata, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                jsonJXDate(responseData);
            }

            private void jsonJXDate(String responseData) {
                if (responseData != null) {
                    try {
                        JSONArray array = new JSONArray(responseData);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            title = object.getString("title");
//                            Log.e(String.valueOf(2), "okhttpData: " + title);
                            content = object.getString("content");
                            user = object.getString("user");
                            user_id = object.getString("user_id");
                            user_image = object.getString("user_image");
                            image = object.getString("image");
                            image2 = object.getString("image2");
                            image3 = object.getString("image3");
                            image4 = object.getString("image4");
                            image5 = object.getString("image5");
                            image6 = object.getString("image6");
                            image7 = object.getString("image7");
                            image8 = object.getString("image8");
                            image9 = object.getString("image9");
//
//                            Log.e("TAG", "image: " + image);
//                            Log.e("TAG", "image3: " + image3);
//                            Log.e("TAG", "image4: " + image4);
//                            Log.e("TAG", "image5: " + image5);

//                            判断image是否有图片
//                            if (image == "null") {
//                            } else {
//                                imagelist.add(image);
//                            }
//
//                            //判断image2是否有图片
//                            if (image2 == "null") {
//                            } else {
//                                imagelist.add(image2);
//                            }
//
//                            //判断image3是否有图片
//                            if (image3 == "null") {
//
//                            } else {
//                                imagelist.add(image3);
//                            }
//
//                            //判断image4是否有图片
//                            if (image4 == "null") {
//                            } else {
//                                imagelist.add(image4);
//                            }
//
//                            //判断image5是否有图片
//                            if (image5 == "null") {
//                            } else {
//                                imagelist.add(image5);
//                            }
//
//                            //判断image6是否有图片
//                            if (image6 == "null") {
//                            } else {
//                                imagelist.add(image6);
//                            }
//
//                            //判断image7是否有图片
//                            if (image7 == "null") {
//                            } else {
//                                imagelist.add(image7);
//                            }
//
//                            //判断image8是否有图片
//                            if (image8 == "null") {
//                            } else {
//                                imagelist.add(image8);
//                            }
//
//                            //判断image9是否有图片
//                            if (image9 == "null") {
//                            } else {
//                                imagelist.add(image9);
//                            }





                            Map<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("content", content);
                            map.put("user", user);
                            map.put("user_id", user_id);
                            map.put("user_image", user_image);
                                map.put("image",image);
                                map.put("image2",image2);
                            map.put("image3",image3);
                            map.put("image4",image4);
                            map.put("image5",image5);
                            map.put("image6",image6);
                            map.put("image7",image7);
                            map.put("image8",image8);
                            map.put("image9",image9);
                            list.add(map);
//                            Map<String, Object> imagemap = new HashMap<>();
//                            if (image == "null") {
//                            } else {
//                                imagemap.put("image",image);
//                            }
//                            if (image2 == "null") {
//                            } else {
//                                imagemap.put("image2",image2);
//                            }
//                            imageList.add(imagemap);
                            Log.e("image", "image: "+image );
                            Log.e("image2", "image2: "+image2 );
//                            Log.e("imageList", "imageList: "+imageList );
//imagelist.clear();
//                            Log.e("imagelist", "imagelist: "+imagelist );
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }


    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {

                case 1:
                    RaidersMainRecycleView.setLayoutManager(new LinearLayoutManager(RaidersSecondActivity.this));


                    RaidersMainSecondAdapter adapter1 = new RaidersMainSecondAdapter(list,imageList,RaidersSecondActivity.this);
                    RaidersMainRecycleView.setAdapter(adapter1);

                    tv_raiders_second_user.setText(user);
                    Glide.with(RaidersSecondActivity.this)
                            .load(user_image)
                            .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0, 0)))
                            .into(iv_raiders_second_image);

                case 2:

                    commentRaidersRecycleView.setLayoutManager(new LinearLayoutManager(RaidersSecondActivity.this));

                    RaidersSecondAdapter adapter = new RaidersSecondAdapter(commentlist, RaidersSecondActivity.this);
                    commentRaidersRecycleView.setAdapter(adapter);

                case 3:
//                    Log.e("likelist.size()", "likelist.size(): " + likelist.size());
                    tv_raiders_second_like.setText(String.valueOf(likelist.size()));

                case 4:
                    Bundle bundle;
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    try {
                        if (result.equals("success")) {
                            result = "点赞成功";
                            Toast.makeText(RaidersSecondActivity.this, result, Toast.LENGTH_SHORT).show();
                        } else {
                            result = "点赞失败";
                            Toast.makeText(RaidersSecondActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                case 6:
                    tv_raiders_second_collect.setText(String.valueOf(collectlist.size()));

                case 5:
                    Bundle bundle1;
                    bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    try {
                        if (result1.equals("success")) {
                            result1 = "收藏成功";
                            Toast.makeText(RaidersSecondActivity.this, result1, Toast.LENGTH_SHORT).show();
                        } else {
                            result1 = "收藏失败";
                            Toast.makeText(RaidersSecondActivity.this, result1, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                case 7:
                    tv_raiders_second_comment.setText(String.valueOf(commentnumberlist.size()));

            }
            return false;
        }
    });

    private void bindView() {
        bt_raiders_second_back = findViewById(R.id.bt_raiders_second_back);
        tv_raiders_second_user = findViewById(R.id.tv_raiders_second_user);
//        tv_raiders_second_title = findViewById(R.id.tv_raiders_second_title);
//        tv_home_second_content = findViewById(R.id.tv_home_second_content);
        tv_raiders_second_like = findViewById(R.id.tv_raiders_second_like);
        tv_raiders_second_collect = findViewById(R.id.tv_raiders_second_collect);
        tv_raiders_second_comment = findViewById(R.id.tv_raiders_second_comment);
        tv_raiders_second_comment_send = findViewById(R.id.tv_raiders_second_comment_send);
        iv_raiders_second_image = findViewById(R.id.iv_raiders_second_image);
        iv_raiders_second_like = findViewById(R.id.iv_raiders_second_like);
        iv_raiders_second_collect = findViewById(R.id.iv_raiders_second_collect);
        iv_raiders_second_comment = findViewById(R.id.iv_raiders_second_comment);

        commentRaidersRecycleView = findViewById(R.id.rv_raiders_second_comment);
        RaidersMainRecycleView = findViewById(R.id.rv_raiders_second_content);
//        banner = findViewById(R.id.banner);
//        banner.setIndicatorGravity(BannerConfig.CENTER);


        bt_raiders_second_back.setOnClickListener(this);
//        tv_raiders_second_title.setOnClickListener(this);
//        tv_raiders_second_content.setOnClickListener(this);
        tv_raiders_second_like.setOnClickListener(this);
        tv_raiders_second_collect.setOnClickListener(this);
        tv_raiders_second_comment.setOnClickListener(this);
        tv_raiders_second_user.setOnClickListener(this);
        iv_raiders_second_image.setOnClickListener(this);
        iv_raiders_second_like.setOnClickListener(this);
        iv_raiders_second_collect.setOnClickListener(this);
        iv_raiders_second_comment.setOnClickListener(this);
        tv_raiders_second_comment_send.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_raiders_second_back:
                finish();
                break;
            case R.id.iv_raiders_second_image:
                Intent intent = new Intent(RaidersSecondActivity.this, UserActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                break;

            case R.id.tv_raiders_second_comment_send:
                Intent intent1 = new Intent(RaidersSecondActivity.this, CommentRaidersAcyivity.class);
                intent1.putExtra("article_id_second", getIntent().getStringExtra("article_id").trim());
                startActivity(intent1);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                break;

            case R.id.iv_raiders_second_like:
                okhttplike();
                int sum = Integer.parseInt(tv_raiders_second_like.getText().toString());
                tv_raiders_second_like.setText(String.valueOf(++sum));
                break;

            case R.id.iv_raiders_second_collect:
                okhttpcollect();
                int summ = Integer.parseInt(tv_raiders_second_collect.getText().toString());
                tv_raiders_second_collect.setText(String.valueOf(++summ));
                break;
        }
    }


    private void okhttplike() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpAll.AddRaidersLike(
                        getIntent().getStringExtra("article_id").trim(),
                        MyAPPlication.getGlobalvariable().trim()
                );
                Bundle bundle1 = new Bundle();
                bundle1.putString("result", result);
//                Log.e("4", "addlike: " + bundle1);
                Message msg = new Message();
                msg.what = 4;
                msg.setData(bundle1);
                handler.sendMessage(msg);
            }
        }).start();
    }


    private void okhttpcollect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpAll.AddRaidersCollect(
                        getIntent().getStringExtra("article_id").trim(),
                        MyAPPlication.getGlobalvariable().trim()
                );
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                Message msg = new Message();
                msg.what = 5;
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }).start();
    }
}


//内置适配器
class RaidersSecondAdapter extends RecyclerView.Adapter<RaidersSecondAdapter.RaidersSecondHolder> {

    public List<Map<String, Object>> commentlist;

    public Context con;
    public LayoutInflater inflater;

    public RaidersSecondAdapter(List<Map<String, Object>> commentlist, Context con) {
        this.con = con;
        this.commentlist = commentlist;
        inflater = LayoutInflater.from(con);
    }

    @NonNull
    @Override
    public RaidersSecondHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_raiders_second_comment, parent, false);
        final RaidersSecondHolder holder = new RaidersSecondHolder(view);


        holder.iv_adapter_raiders_second_comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Map map = commentlist.get(position);
                String user_id = (String) map.get("user_id");
                Intent intent = new Intent(con, UserActivity.class);
                intent.putExtra("user_id", user_id);
                con.startActivity(intent);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RaidersSecondAdapter.RaidersSecondHolder holder, final int position) {
        holder.tv__adapter_raiders_second_comment_user.setText(commentlist.get(position).get("user").toString());
        holder.tv__adapter_raiders_second_comment_comment.setText(commentlist.get(position).get("comment").toString());
        Glide.with(con).load(commentlist.get(position).get("user_image").toString())
                .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0, 0)))
                .into(holder.iv_adapter_raiders_second_comment_image);
//        Log.e("Raiders", "commentlist: "+commentlist);
    }

    @Override
    public int getItemCount() {
        return commentlist.size();
    }

    class RaidersSecondHolder extends RecyclerView.ViewHolder {
        View itemView;
        public TextView tv__adapter_raiders_second_comment_user;
        public TextView tv__adapter_raiders_second_comment_comment;
        public ImageView iv_adapter_raiders_second_comment_image;

        public RaidersSecondHolder(View view) {
            super(view);
            itemView = view;
            tv__adapter_raiders_second_comment_user = view.findViewById(R.id.tv__adapter_raiders_second_comment_user);
            tv__adapter_raiders_second_comment_comment = view.findViewById(R.id.tv__adapter_raiders_second_comment_comment);
            iv_adapter_raiders_second_comment_image = view.findViewById(R.id.iv_adapter_raiders_second_comment_image);


        }
    }


}

//内置适配器
class RaidersMainSecondAdapter extends RecyclerView.Adapter<RaidersMainSecondAdapter.RaidersMainSecondHolder> {

    public List<Map<String, Object>> list;
    public List<Map<String, Object>> imageList;
    private ArrayList<String> imagelist = new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;

    public RaidersMainSecondAdapter( List<Map<String, Object>> list,List<Map<String, Object>> imageList,Context con) {
        this.con = con;
        this.list = list;
        this.imageList = imageList;
        inflater = LayoutInflater.from(con);
    }

    @NonNull
    @Override
    public RaidersMainSecondHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_raiders_second, parent, false);
        final RaidersMainSecondHolder holder = new RaidersMainSecondHolder(view);


//        holder.iv_adapter_raiders_second_comment_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Map map = commentlist.get(position);
//                String user_id = (String) map.get("user_id");
//                Intent intent = new Intent(con, UserActivity.class);
//                intent.putExtra("user_id", user_id);
//                con.startActivity(intent);
//
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RaidersMainSecondAdapter.RaidersMainSecondHolder holder, final int position) {
        holder.tv_raiders_second_title.setText(list.get(position).get("title").toString());
        holder.tv_raiders_second_content.setText(list.get(position).get("content").toString());

        imagelist.add((String) list.get(position).get("image"));
        imagelist.add((String) list.get(position).get("image2"));
        imagelist.add((String) list.get(position).get("image3"));
        imagelist.add((String) list.get(position).get("image4"));
        imagelist.add((String) list.get(position).get("image5"));
        imagelist.add((String) list.get(position).get("image6"));
        imagelist.add((String) list.get(position).get("image7"));
        imagelist.add((String) list.get(position).get("image8"));
        imagelist.add((String) list.get(position).get("image9"));
        imagelist.removeIf(s -> s.contains("null"));
        Log.e("imagelist9", "imagelist9: "+imagelist );
//        Map<String, Object> imagelist =  imageList.get(position);
//        List image = (List) imagelist;

    holder.banner_raiders.setImageLoader(new GlideImageLoaderRaiders())
            .setImages(imagelist)
            .isAutoPlay(false)
            .start();
    imagelist.clear();
}

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RaidersMainSecondHolder extends RecyclerView.ViewHolder {
        View itemView;
        public TextView tv_raiders_second_title;
        public TextView tv_raiders_second_content;
        public Banner banner_raiders;

        public RaidersMainSecondHolder(View view) {
            super(view);
            itemView = view;
            tv_raiders_second_title = view.findViewById(R.id.tv_raiders_second_title);
            tv_raiders_second_content = view.findViewById(R.id.tv_raiders_second_content);
            banner_raiders = view.findViewById(R.id.banner_raiders);
            banner_raiders.setIndicatorGravity(BannerConfig.CENTER);
        }
    }


}

class GlideImageLoaderRaiders extends ImageLoader {
    public void displayImage(Context context, Object path, ImageView imageView) {


        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);

    }


}