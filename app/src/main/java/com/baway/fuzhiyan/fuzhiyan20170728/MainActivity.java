package com.baway.fuzhiyan.fuzhiyan20170728;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/*
* author:付智焱
* time:2017-7-28 14:38:35
*
* 类用途：加载主页面的布局。。显示数据
*
* */
public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private UserBean bean;
    private List<UserBean.DataBean> list = new ArrayList<>();
    private Handler handler = new Handler();
    private LinearLayoutManager linearLayoutManager;

    //最后一条可见条目
    private int findLastVisibleItemPosition;
    private String url = "http://www.yulin520.com/a2a/impressApi/news/mergeList?sign=C7548DE604BCB8A17592EFB9006F9265&pageSize=20&gender=2&ts=1871746850&page=1";
    private String page = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //给recyclerview增加分割线
        recyclerView.addItemDecoration(new MyItemDecoration());
        //判断是否有网络
        boolean networkAvailable = NetUtils.isNetworkAvailable(this);
        if (networkAvailable){
            initData();
            Toast.makeText(MainActivity.this, "您已连接网络", Toast.LENGTH_SHORT).show();
        }else {
            //没有网络时 跳转设置页面
            AlertDialog.Builder b = new AlertDialog.Builder(this).setTitle("没有可用的网络").setMessage("请开启GPRS或WIFI网路连接");
            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent mIntent = new Intent("android.settings.WIRELESS_SETTINGS");
                    startActivity(mIntent);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //errorImage.setVisibility(View.VISIBLE);
                }
            }).create();
            b.show();
        }
        initData();
        //滑动监听
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断滑动状态是否为停止
                if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == newState) {

                    //当前滚动 停止
                    int findLastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int findFirstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();


                    int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    //当最后一条可见条目这停止就加载更多

                    if (findLastVisibleItemPosition == list.size() - 1) {
                        initData();

                        myAdapter.notifyDataSetChanged();
                    }


                }

            }
        });

    }

    //okhttp异步加载数据
    private void initData() {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response != null && response.isSuccessful() && response.body() != null) {
                    String htmlStr = response.body().string();
                    bean = UserBean.objectFromData(htmlStr);

                }

                list = bean.data;
                //handler.post开启子线程
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(list + "");
                        myAdapter = new MyAdapter(MainActivity.this, list);
                        myAdapter.setOnItemClickListener(MainActivity.this);
                        recyclerView.setAdapter(myAdapter);

                    }
                });
            }
        });
//


    }


    @Override
    public void onItemClickListener(int position, View view) {

        Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_LONG).show();
    }

    //分割线类
    class MyItemDecoration extends RecyclerView.ItemDecoration {
        /**
         * @param outRect 边界
         * @param view    recyclerView ItemView
         * @param parent  recyclerView
         * @param state   recycler 内部数据管理
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //设定底部边距为1px
            outRect.set(0, 0, 0, 1);
        }
    }


}


//




