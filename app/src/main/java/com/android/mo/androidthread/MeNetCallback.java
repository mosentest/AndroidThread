package com.android.mo.androidthread;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 *
 * Created by Administrator on 2016/5/24.
 */
public interface MeNetCallback {

    public void onSuccess(int tag, String entity);

    public void onFailure(int tag, String msg, int code);

}
