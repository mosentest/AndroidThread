package com.android.mo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.android.mo.config.Constants;

public class MessengerService extends Service {

    private static final String TAG = MessengerService.class.getSimpleName();

    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }


    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FORM_CLIENT:
                    Log.e(TAG, "receive msg form client :" + msg.getData().getString("msg"));
                    Messenger messenger = msg.replyTo;
                    if (messenger != null) {
                        Message replyMessage = Message.obtain(null, Constants.MSG_FORM_SERVICE);
                        Bundle bundle = new Bundle();
                        bundle.putString("reply", "sai");
                        replyMessage.setData(bundle);
                        try {
                            messenger.send(replyMessage);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());
}
