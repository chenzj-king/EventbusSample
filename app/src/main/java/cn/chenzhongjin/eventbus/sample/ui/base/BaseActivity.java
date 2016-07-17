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

package cn.chenzhongjin.eventbus.sample.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * @文件名: BaseActivity
 * @功能描述:
 * @Create by chenzj on 2016/1/30 16:58
 * @email: chenzj@sq580.com
 * @修改记录:
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    protected boolean isCanBack = false;

    protected boolean isRegisterEvent() {
        return true;
    }

    protected void getBundleExtras(Bundle extras) {
        isCanBack = extras.getBoolean("isCanBack");
    }

    protected abstract int getLayoutId();

    protected void initSpecialView() {
    }

    protected abstract void initViews();

    protected void handleMes(Message msg) {
    }

    protected MyHandler mHandler;

    @Override
    protected void onStart() {
        super.onStart();
        if (isRegisterEvent())
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // base setup
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        mHandler = new MyHandler(this);

        initSpecialView();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove and eventbus
        if (isRegisterEvent())
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    public void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    public void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    public void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    public void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    public void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * Eventbus相关
     *
     * @param event
     */
    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    protected static class MyHandler extends Handler {

        private final WeakReference<BaseActivity> mActivity;

        public MyHandler(BaseActivity activity) {
            mActivity = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mActivity.get();
            if (activity != null) {
                // do someThing
                activity.handleMes(msg);
            }
        }
    }

    protected Toast mToast;

    public Toast getToast() {
        return mToast;
    }

    /**
     * show toast
     *
     * @param msg
     */
    public void showToast(final String msg) {
        // 防止遮盖虚拟按键
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!TextUtils.isEmpty(msg)) {
                    mToast = Toast.makeText(BaseActivity.this, "" + msg, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
    }

    public void showToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mToast = Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: hideSoftInputView
     * @Description: 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}  

