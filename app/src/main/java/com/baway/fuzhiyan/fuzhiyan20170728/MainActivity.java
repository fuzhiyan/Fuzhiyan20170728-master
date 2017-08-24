package com.baway.fuzhiyan.fuzhiyan20170728;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

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
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private UserBean bean;
    private List<UserBean.DataBean> list = new ArrayList<>();
    private Handler handler = new Handler();
    private LinearLayoutManager linearLayoutManager;

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
        initData();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == newState) {

                    //当前滚动 停止
                    int findLastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int findFirstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                    int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    //加载更多

                    initData();
                    myAdapter.notifyDataSetChanged();
                }

                }
            });

        }

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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(list + "");
                        myAdapter = new MyAdapter(MainActivity.this, list);
                        recyclerView.setAdapter(myAdapter);
                    }
                });
            }
        });
//


    }


}


//




