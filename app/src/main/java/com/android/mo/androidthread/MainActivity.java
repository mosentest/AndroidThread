package com.android.mo.androidthread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mTextView = (TextView) findViewById(R.id.textView);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MeExecutor.doNetIOGet(1, "http://feng-home.iteye.com/blog/657089", new MeCallback() {
            @Override
            public void onSuccess(int tag, String entity) {
                Log.e("TAG","tag1");
                mTextView.setText(1 + "");
            }

            @Override
            public void onFailure(int tag, String msg, int code) {

            }
        });
        MeExecutor.doNetIOGet(2, "http://feng-home.iteye.com/blog/657089", new MeCallback() {
            @Override
            public void onSuccess(int tag, String entity) {
                Log.e("TAG","tag2");
                mTextView.setText(1 + "2");
            }

            @Override
            public void onFailure(int tag, String msg, int code) {

            }
        });
        MeExecutor.doNetIOGet(3, "http://feng-home.iteye.com/blog/657089", new MeCallback() {
            @Override
            public void onSuccess(int tag, String entity) {
                Log.e("TAG","tag3");
                mTextView.setText(1 + "3");
            }

            @Override
            public void onFailure(int tag, String msg, int code) {

            }
        });
        MeExecutor.doNetIOGet(4, "http://feng-home.iteye.com/blog/657089", new MeCallback() {
            @Override
            public void onSuccess(int tag, String entity) {
                Log.e("TAG","tag4");
                mTextView.setText(1 + "4");
            }

            @Override
            public void onFailure(int tag, String msg, int code) {

            }
        });
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
