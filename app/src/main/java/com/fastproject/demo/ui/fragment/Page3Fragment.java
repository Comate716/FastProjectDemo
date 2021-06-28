package com.fastproject.demo.ui.fragment;

import com.fastproject.demo.R;
import com.fastproject.demo.app.TitleBarFragment;
import com.fastproject.demo.ui.activity.HomeActivity;

public class Page3Fragment extends TitleBarFragment<HomeActivity> {


    public static Page3Fragment newInstance() {
        return new Page3Fragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page3;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
