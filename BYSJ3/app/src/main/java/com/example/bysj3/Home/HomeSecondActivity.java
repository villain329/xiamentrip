package com.example.bysj3.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.CommentAcyivity;
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

public class HomeSecondActivity extends AppCompatActivity implements View.OnClickListener {

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

    private Button bt_homesecond_back;
    private TextView tv_home_second_user;
    private TextView tv_home_second_title;
    private TextView tv_home_second_content;
    private TextView tv_home_second_like;
    private TextView tv_home_second_collect;
    private TextView tv_home_second_comment;
    private TextView tv_home_second_comment_send;
    private ImageView iv_home_second_image;
    private ImageView iv_home_second_like;
    private ImageView iv_home_second_collect;
    private ImageView iv_home_second_comment;
    private SwipeRefreshLayout refresh_home_second;

    private Banner banner;
    private RecyclerView commentRecycleView;
    public List<Map<String, Object>> list = new ArrayList<>();
    private ArrayList<String> imagelist = new ArrayList<>();
    public List<Map<String, Object>> likelist = new ArrayList<>();
    public List<Map<String, Object>> collectlist = new ArrayList<>();
    public List<Map<String, Object>> commentnumberlist = new ArrayList<>();
    public List<Map<String, Object>> commentlist = new ArrayList<>();

    private String url1 = "http://39.97.251.81:8080/test5/ArticleServlet";
    private String url = "http://39.97.251.81:8080/test5/CommentServlet";
    private String url3 = "http://39.97.251.81:8080/test5/LikeNumberServlet";
    private String url4 = "http://39.97.251.81:8080/test5/CollectNumberServlet";
    private String url5 = "http://39.97.251.81:8080/test5/CommentNumberServlet";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_home_second);


//评论
        okhttpcomment();
        //图片，标题，内容
        okhttpData();
        //用户
//        okhttpuser();

        //喜欢的数量
        okhttplikereplace();

        //收藏的数量
        okhttpcollectreplace();

        //评论的数量
        okhttpcommentreplace();

        bindView();


        refresh_home_second = findViewById(R.id.refresh_home_second);
        refresh_home_second.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                                imagelist.clear();
                                okhttpcomment();
                                okhttpData();
                                refresh_home_second.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });




    }

    private void okhttpcommentreplace() {
        commentnumberlist.clear();
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
        collectlist.clear();
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
        likelist.clear();
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
        list.clear();
        String anydata = getIntent().getStringExtra("article_id");
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
                            Log.e(String.valueOf(2), "okhttpData: " + title);
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

                            Log.e("TAG", "image: " + image);
//                            Log.e("TAG", "image3: " + image3);
//                            Log.e("TAG", "image4: " + image4);
//                            Log.e("TAG", "image5: " + image5);
                            //判断image是否有图片
                            if (image == "null") {
                            } else {
                                imagelist.add(image);
                            }

                            //判断image2是否有图片
                            if (image2 == "null") {
                            } else {
                                imagelist.add(image2);
                            }

                            //判断image3是否有图片
                            if (image3 == "null") {

                            } else {
                                imagelist.add(image3);
                            }

                            //判断image4是否有图片
                            if (image4 == "null") {
                            } else {
                                imagelist.add(image4);
                            }

                            //判断image5是否有图片
                            if (image5 == "null") {
                            } else {
                                imagelist.add(image5);
                            }

                            //判断image6是否有图片
                            if (image6 == "null") {
                            } else {
                                imagelist.add(image6);
                            }

                            //判断image7是否有图片
                            if (image7 == "null") {
                            } else {
                                imagelist.add(image7);
                            }

                            //判断image8是否有图片
                            if (image8 == "null") {
                            } else {
                                imagelist.add(image8);
                            }

                            //判断image9是否有图片
                            if (image9 == "null") {
                            } else {
                                imagelist.add(image9);
                            }


//                            imagelist.add(image);


                            Map<String, Object> map = new HashMap<>();
                            map.put("title", title);
                            map.put("content", content);
                            map.put("user", user);
                            map.put("user_id", user_id);
                            map.put("user_image", user_image);
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

        });
    }


    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {

                case 1:
                    tv_home_second_title.setText(title);
                    tv_home_second_content.setText(content);

                    banner.setImageLoader(new GlideImageLoader())
                            .setImages(imagelist)
                            .isAutoPlay(false)
                            .start();

                    tv_home_second_user.setText(user);
                    Glide.with(HomeSecondActivity.this)
                            .load(user_image)
                            .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0, 0)))
                            .into(iv_home_second_image);

                case 2:

                    commentRecycleView.setLayoutManager(new LinearLayoutManager(HomeSecondActivity.this));
                    Log.e(String.valueOf(456456), "onBindViewHolder: " + user);
                    HomeSecondAdapter adapter = new HomeSecondAdapter(commentlist, HomeSecondActivity.this);
                    commentRecycleView.setAdapter(adapter);

                case 3:
                    Log.e("likelist.size()", "likelist.size(): " + likelist.size());
                    tv_home_second_like.setText(String.valueOf(likelist.size()));

                case 4:
                    Bundle bundle;
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    try {
                        if (result.equals("success")) {
                            result = "点赞成功";
                            Toast.makeText(HomeSecondActivity.this, result, Toast.LENGTH_SHORT).show();
                        } else {
                            result = "点赞失败";
                            Toast.makeText(HomeSecondActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                case 6:
                    tv_home_second_collect.setText(String.valueOf(collectlist.size()));
                    
                case 5:
                    Bundle bundle1;
                    bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    try {
                        if (result1.equals("success")) {
                            result1 = "收藏成功";
                            Toast.makeText(HomeSecondActivity.this, result1, Toast.LENGTH_SHORT).show();
                        } else {
                            result1 = "收藏失败";
                            Toast.makeText(HomeSecondActivity.this, result1, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                case 7:
                    tv_home_second_comment.setText(String.valueOf(commentnumberlist.size()));

            }
            return false;
        }
    });

    private void bindView() {
        bt_homesecond_back = findViewById(R.id.bt_home_second_back);
        tv_home_second_user = findViewById(R.id.tv_home_second_user);
        tv_home_second_title = findViewById(R.id.tv_home_second_title);
        tv_home_second_content = findViewById(R.id.tv_home_second_content);
        tv_home_second_like = findViewById(R.id.tv_home_second_like);
        tv_home_second_collect = findViewById(R.id.tv_home_second_collect);
        tv_home_second_comment = findViewById(R.id.tv_home_second_comment);
        tv_home_second_comment_send = findViewById(R.id.tv_home_second_comment_send);
        iv_home_second_image = findViewById(R.id.iv_home_second_image);
        iv_home_second_like = findViewById(R.id.iv_home_second_like);
        iv_home_second_collect = findViewById(R.id.iv_home_second_collect);
        iv_home_second_comment = findViewById(R.id.iv_home_second_comment);

        commentRecycleView = findViewById(R.id.rv_home_second_comment);
        banner = findViewById(R.id.banner);
        banner.setIndicatorGravity(BannerConfig.CENTER);


        bt_homesecond_back.setOnClickListener(this);
        tv_home_second_title.setOnClickListener(this);
        tv_home_second_content.setOnClickListener(this);
        tv_home_second_like.setOnClickListener(this);
        tv_home_second_collect.setOnClickListener(this);
        tv_home_second_comment.setOnClickListener(this);
        tv_home_second_user.setOnClickListener(this);
        iv_home_second_image.setOnClickListener(this);
        iv_home_second_like.setOnClickListener(this);
        iv_home_second_collect.setOnClickListener(this);
        iv_home_second_comment.setOnClickListener(this);
        tv_home_second_comment_send.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_home_second_back:
                finish();
                break;
            case R.id.iv_home_second_image:
                Intent intent = new Intent(HomeSecondActivity.this, UserActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                break;

            case R.id.tv_home_second_comment_send:
                Intent intent1 = new Intent(HomeSecondActivity.this, CommentAcyivity.class);
                intent1.putExtra("article_id_second", getIntent().getStringExtra("article_id").trim());
                startActivity(intent1);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                break;

            case R.id.iv_home_second_like:
                okhttplike();
                int sum = Integer.parseInt(tv_home_second_like.getText().toString());
                tv_home_second_like.setText(String.valueOf(++sum));
                break;

            case R.id.iv_home_second_collect:
                okhttpcollect();
                int summ = Integer.parseInt(tv_home_second_collect.getText().toString());
                tv_home_second_collect.setText(String.valueOf(++summ));
                break;
        }
    }


    private void okhttplike() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpAll.AddLike(
                        getIntent().getStringExtra("article_id").trim(),
                        MyAPPlication.getGlobalvariable().trim()
                );
                Bundle bundle1 = new Bundle();
                bundle1.putString("result", result);
                Log.e("4", "addlike: " + bundle1);
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
                String result = HttpAll.AddCollect(
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
class HomeSecondAdapter extends RecyclerView.Adapter<HomeSecondAdapter.HomeSecondHolder> {

    public List<Map<String, Object>> commentlist;
    public Context con;
    public LayoutInflater inflater;

    public HomeSecondAdapter(List<Map<String, Object>> commentlist, Context con) {
        this.con = con;
        this.commentlist = commentlist;
        inflater = LayoutInflater.from(con);
    }

    @NonNull
    @Override
    public HomeSecondHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_second_comment, parent, false);
        final HomeSecondHolder holder = new HomeSecondHolder(view);


        holder.iv_adapter_home_second_comment_image.setOnClickListener(new View.OnClickListener() {
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
    public void onBindViewHolder(@NonNull HomeSecondAdapter.HomeSecondHolder holder, final int position) {
        holder.tv__adapter_home_second_comment_user.setText(commentlist.get(position).get("user").toString());
        holder.tv__adapter_home_second_comment_comment.setText(commentlist.get(position).get("comment").toString());
        Glide.with(con).load(commentlist.get(position).get("user_image").toString())
                .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0, 0)))
                .into(holder.iv_adapter_home_second_comment_image);

    }

    @Override
    public int getItemCount() {
        return commentlist.size();
    }

    class HomeSecondHolder extends RecyclerView.ViewHolder {
        View itemView;
        public TextView tv__adapter_home_second_comment_user;
        public TextView tv__adapter_home_second_comment_comment;
        public ImageView iv_adapter_home_second_comment_image;

        public HomeSecondHolder(View view) {
            super(view);
            itemView = view;
            tv__adapter_home_second_comment_user = view.findViewById(R.id.tv__adapter_home_second_comment_user);
            tv__adapter_home_second_comment_comment = view.findViewById(R.id.tv__adapter_home_second_comment_comment);
            iv_adapter_home_second_comment_image = view.findViewById(R.id.iv_adapter_home_second_comment_image);


        }
    }


}


class GlideImageLoader extends ImageLoader {
    public void displayImage(Context context, Object path, ImageView imageView) {


        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);

    }
}