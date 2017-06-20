package com.android.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.test.banner.Banner;

public class MainActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        banner = (Banner) findViewById(R.id.banner);
        //设置数据
        banner.setData(ImagePath.getListMap());
        //没有数据的情况
//        banner.setData(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开始轮播
        banner.startRun();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止轮播
        banner.stopRun();
    }
}
