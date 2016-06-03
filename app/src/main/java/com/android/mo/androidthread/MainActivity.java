package com.android.mo.androidthread;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.android.mo.config.Constants;
import com.android.mo.service.MessengerService;
import com.android.mo.view.DemoView;
import com.nineoldandroids.animation.ObjectAnimator;

import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();


    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FORM_SERVICE:
                    Log.e(TAG, "receive reply form service :" + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());


    private Messenger mService;

    private boolean isServiceConnection = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message message = Message.obtain(null, Constants.MSG_FORM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello word");
            message.setData(bundle);
            message.replyTo = mGetReplyMessenger;
            try {
                mService.send(message);
                isServiceConnection = true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private DemoView mDemoview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //ViewPager
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        MeExecutor.doNetIOGet(1, "http://feng-home.iteye.com/blog/657089", new MeCallback() {
//            @Override
//            public void onSuccess(int tag, String entity) {
//                Log.e("TAG","tag1");
//                mTextView.setText(1 + "");
//            }

//        Intent intent = new Intent(this, MessengerService.class);
//        bindService(intent, mConnection, BIND_AUTO_CREATE);


        mDemoview = (DemoView) findViewById(R.id.demoview);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        ObjectAnimator.ofFloat(mDemoview, "translationX", 0, 100).setDuration(1000).start();
//        ObjectAnimator.ofFloat(mDemoview, "translationY", 0, 100).setDuration(1000).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceConnection) {
            unbindService(mConnection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                //totalSize += Downloader.downloadFile(urls[i]);
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
        }
    }
}
