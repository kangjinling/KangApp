package com.kang.app;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import common.library.DataBinding.BaseActivity;
import common.library.LogPrint;

public class MainActivity extends BaseActivity{

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        LogPrint.println("-------------initData");
        startActivity(new Intent(this.getApplicationContext(),
                com.alimaplibrary.MainActivity.class));
    }

    @Override
    protected void initEventBus(Object command) {

    }
}
