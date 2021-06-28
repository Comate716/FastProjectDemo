package com.fastproject.demo.ui.fragment;

import com.fastproject.demo.R;
import com.fastproject.demo.app.TitleBarFragment;
import com.fastproject.demo.ui.activity.HomeActivity;

public class Page4Fragment extends TitleBarFragment<HomeActivity> {

    public static Page4Fragment newInstance() {
        return new Page4Fragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page4;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
