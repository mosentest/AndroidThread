package com.android.mo.melibrary.base;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.android.mo.melibrary.netstatus.NetChangeObserver;

/**
 * Created by Administrator on 2016/5/25.
 */
public class BaseFragmentActivity extends FragmentActivity {
    /**
     * 屏幕信息
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    /**
     * context
     */
    protected Context mContext = null;

    /**
     * network status
     */
    protected NetChangeObserver mNetChangeObserver = null;

}
