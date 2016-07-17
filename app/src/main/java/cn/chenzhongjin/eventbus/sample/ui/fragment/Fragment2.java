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

package cn.chenzhongjin.eventbus.sample.ui.fragment;

import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.chenzhongjin.eventbus.sample.R;
import cn.chenzhongjin.eventbus.sample.eventbus.TestMesData;
import cn.chenzhongjin.eventbus.sample.ui.base.BaseFragment;

/**
 * @文件名: Fragment1
 * @功能描述:
 * @Create by chenzj on 2016/1/30 16:57
 * @email: chenzj@sq580.com
 * @修改记录:
 */
public class Fragment2 extends BaseFragment {

    @BindView(R.id.fragment_test_text)
    TextView fragmentTestText;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initViews(View view) {
        fragmentTestText.setText("Fra2");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleTestMess(TestMesData testMesData) {
        fragmentTestText.setText(String.format("fra2:%s", testMesData.getContent()));
    }
}

