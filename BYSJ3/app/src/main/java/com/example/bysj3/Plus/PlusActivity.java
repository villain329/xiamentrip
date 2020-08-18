package com.example.bysj3.Plus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bysj3.MyAPPlication;
import com.example.bysj3.Plus.UpLoad.Const;
import com.example.bysj3.R;
import com.example.bysj3.Util.HttpData;
import com.example.bysj3.Util.HttpUtilSearch;
import com.example.bysj3.Util.ServerUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PlusActivity extends AppCompatActivity implements View.OnClickListener {
    public String user;
    public String user_id;
    public String password;
    public String user_image;
    public String image;
    private TextView tv_plus_send_article;
    private Button bt_plus_back;
    private EditText et_plus_title;
    private EditText et_plus_content;

    private int maxSelectNum = 9;
    private RecyclerView mRecyclerView;
    private GridImageAdapter mAdapter;

    private int ResultCode = 2;
    private final static int ADD_DATA = 2;

    public static final int CHOSE_PHOTO = 3;

    public List<Map<String, Object>> imagelist;
    String result = "";
    private String url2 = "http://39.97.251.81:8080/test5/GetIdServlet";

//    private String url = "http://39.97.251.81:8080/picture";

    private String url = "http://39.97.251.81:8080/test5/PhotoServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // 被回收
        } else {
            clearCache();
        }
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_plus);

        okhttp();
        mRecyclerView = findViewById(R.id.rv_plus);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(this, 8), false));
        mAdapter = new GridImageAdapter(getContext(), onAddPicClickListener);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapter.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }


        mAdapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapter.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {

                    default:
                        // 预览图片 可自定长按保存路径
//                        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
//                        animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
//                        animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
                        PictureSelector.create(PlusActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                //.bindCustomPlayVideoCallback(callback)// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });


        bindView();
    }

    private void okhttp() {
        String anydata = MyAPPlication.getGlobalvariable();
        HttpUtilSearch.sendOkHttpRequest(url2, anydata, new Callback() {
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
                            user_id = object.getString("user_id");
                            password = object.getString("password");
                            user = object.getString("user");
                            user_image = object.getString("user_image");

                        }
                        Message msg = new Message();
                        msg.what = 5;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
    private void clearCache() {

        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(getContext());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

//图片显示
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

            PictureSelector.create(PlusActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .loadImageEngine(GlideEngine.createGlideEngine())
                    .isCamera(false)
                    .selectionMedia(mAdapter.getData())// 是否传入已选图片
                    .compress(true)
                    .compressQuality(80)
//                    .forResult(PictureConfig.CHOOSE_REQUEST);
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            for (LocalMedia media : result) {
                            }
                            mAdapter.setList(result);
//                            Log.e(String.valueOf(1), "压缩:" + media.getCompressPath());
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancel() {
                            Log.e(String.valueOf(1), "PictureSelector Cancel");
                        }
                    });
        }
    };


    private void bindView() {
        bt_plus_back = findViewById(R.id.bt_plus_back);
        et_plus_title = findViewById(R.id.et_plus_title);
        et_plus_content = findViewById(R.id.et_plus_content);
        tv_plus_send_article = findViewById(R.id.tv_plus_send_article);


        bt_plus_back.setOnClickListener(this);
        et_plus_title.setOnClickListener(this);
        et_plus_content.setOnClickListener(this);
        tv_plus_send_article.setOnClickListener(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    mAdapter.setList(selectList);
                    mAdapter.notifyDataSetChanged();

                    break;
            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE:
                // 存储权限
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        PictureFileUtils.deleteCacheDirFile(getContext(), PictureMimeType.ofImage());
                    } else {
                        Toast.makeText(PlusActivity.this,
                                getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapter.getData());
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras;
            switch (action) {
                case BroadcastAction.ACTION_DELETE_PREVIEW_POSITION:
                    // 外部预览删除按钮回调
                    extras = intent.getExtras();
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.s(getContext(), "delete image index:" + position);
                    if (position < mAdapter.getData().size()) {
                        mAdapter.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                    break;
            }
        }
    };


    public Context getContext() {
        return this;
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ADD_DATA: {
                    Bundle bundle;
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    Toast.makeText(PlusActivity.this, result, Toast.LENGTH_SHORT).show();
                    try {
                        if (result.equals("success")) {
                            Intent intent = new Intent();
                            intent.putExtra("title", et_plus_title.getText().toString());
                            intent.putExtra("content", et_plus_content.getText().toString());
                            setResult(ResultCode, intent);
                            finish();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击 返回 事件
            case R.id.bt_plus_back:
                finish();
                break;

            //点击 发布 事件
            case R.id.tv_plus_send_article:
                String title = et_plus_title.getText().toString().trim();
                String content = et_plus_content.getText().toString().trim();
               okHttpAdd();
                if (title.length() == 0 || content.length() == 0) {
                    Toast.makeText(PlusActivity.this, "请输入内容 ", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdapter.getData().size() == 0) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString().trim(),
                                        et_plus_content.getText().toString().trim(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 1) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 2) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 3) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(2).getCompressPath()).trim(),
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 4) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(2).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(3).getCompressPath()).trim(),
                                        "null",
                                        "null",
                                        "null",
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 5) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(2).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(3).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(4).getCompressPath()).trim(),
                                        "null",
                                        "null",
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 6) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(2).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(3).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(4).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(5).getCompressPath()).trim(),
                                        "null",
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 7) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(2).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(3).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(4).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(5).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(6).getCompressPath()).trim(),
                                        "null",
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 8) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(2).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(3).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(4).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(5).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(6).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(7).getCompressPath()).trim(),
                                        "null"
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            if (mAdapter.getData().size() == 9) {
                                String result = HttpData.AddData(
                                        et_plus_title.getText().toString(),
                                        et_plus_content.getText().toString(),
                                        user.trim(),
                                        user_id.trim(),
                                        user_image.trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(0).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(1).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(2).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(3).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(4).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(5).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(6).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(7).getCompressPath()).trim(),
                                        Const.DOWNLOAD_URL+ServerUtils.formUpload(Const.UPLOAD_URL,mAdapter.getData().get(8).getCompressPath()).trim()
                                );
                                Bundle bundle = new Bundle();
                                bundle.putString("result", result);
                                Message msg = new Message();
                                msg.what = ADD_DATA;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }

                        }
                    }).start();
                }
                break;
        }
    }

    private void okHttpAdd() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    image = mAdapter.getData().get(i).getCompressPath();
                    result = ServerUtils.formUpload(Const.UPLOAD_URL, image);
                    Log.e(String.valueOf(1), "tu:" + image);
                    Log.e("jj", "result:" + result);
//                    Log.e("jj", "Const.UPLOAD_URL:" + Const.UPLOAD_URL);
                    Log.e("jj", "path:" + image);
                }
            }
        }).start();

    }


}

