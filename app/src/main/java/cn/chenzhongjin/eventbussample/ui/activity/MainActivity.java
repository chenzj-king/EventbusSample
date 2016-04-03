/*
 *   Copyright (c) 2016  DreamLiner Studio
 *   Licensed under the Apache License, Version 2.0 (the "License”);
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package cn.chenzhongjin.eventbussample.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.chenzhongjin.eventbussample.R;
import cn.chenzhongjin.eventbussample.eventbus.TestMesData;
import cn.chenzhongjin.eventbussample.ui.activity.adapter.CustomFragmentPagerAdapter;
import cn.chenzhongjin.eventbussample.ui.base.BaseActivity;
import cn.chenzhongjin.eventbussample.ui.fragment.Fragment1;
import cn.chenzhongjin.eventbussample.ui.fragment.Fragment2;
import cn.chenzhongjin.eventbussample.widgets.CustomViewPager;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.custom_viewpager)
    CustomViewPager mCustomViewpager;
    @Bind(R.id.tab1)
    LinearLayout mTab1Button;
    @Bind(R.id.tab2)
    LinearLayout mTab2Button;

    private ArrayList<Fragment> mFragmentList;
    private int i = 0;

    @Override
    protected boolean isRegisterEvent() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mFragmentList = new ArrayList<>();

        mFragmentList.add(new Fragment1());
        mFragmentList.add(new Fragment2());

        mCustomViewpager.setAdapter(new CustomFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mCustomViewpager.setOffscreenPageLimit(3);
        clickOne();
    }

    @OnClick(R.id.send_event)
    void clickPostEvent() {
        EventBus.getDefault().post(new TestMesData("第" + (++i) + "次测试发送!!"));
    }

    @OnClick(R.id.tab1)
    void clickOne() {
        setDefaultColor();
        mTab1Button.setBackgroundResource(R.drawable.bg_fillet_blue);
        mCustomViewpager.setCurrentItem(0);
    }

    @OnClick(R.id.tab2)
    void clickTwo() {
        setDefaultColor();
        mTab2Button.setBackgroundResource(R.drawable.bg_fillet_blue);
        mCustomViewpager.setCurrentItem(1);
    }

    void setDefaultColor() {
        mTab1Button.setBackgroundResource(R.drawable.bg_fillet_white);
        mTab2Button.setBackgroundResource(R.drawable.bg_fillet_white);
    }

    @Subscribe
    public void handle(TestMesData testMesData) {
        mToolbar.setTitle("" + testMesData.getContent());
    }
}
