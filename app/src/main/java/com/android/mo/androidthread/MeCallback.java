package com.android.mo.androidthread;

/**
 *
 * Created by Administrator on 2016/5/24.
 */
public interface MeCallback{
    public void onSuccess(int tag, String entity);

    public void onFailure(int tag, String msg, int code);

}
