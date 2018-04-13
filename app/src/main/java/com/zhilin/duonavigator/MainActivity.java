package com.zhilin.duonavigator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.shoujiduoduo.duonavigator.DuoNavigator;
import com.shoujiduoduo.duonavigator.NavigatorItem;

public class MainActivity extends AppCompatActivity implements DuoNavigator.OnTabSelectedListener{

    private DuoNavigator duoNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        duoNavigator = findViewById(R.id.navigator);
        initNavigator();

    }

    private void initNavigator() {
        duoNavigator.addNavigatorItem(new NavigatorItem(R.mipmap.page_1,R.mipmap.page_1_2x))
                .addNavigatorItem(new NavigatorItem(R.mipmap.page_2,R.mipmap.page_2_2x))
                .addNavigatorItem(new NavigatorItem(R.mipmap.page_3,R.mipmap.page_3_2x))
                .addNavigatorItem(new NavigatorItem(R.mipmap.page_4,R.mipmap.page_4_2x))
                .setAnimDuration(100)
                .animEnable(true)
                .setActiveTabBgColor(Color.GRAY).initialise();
        duoNavigator.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        Toast.makeText(this,""+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
