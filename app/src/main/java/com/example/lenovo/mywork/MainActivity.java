package com.example.lenovo.mywork;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
     String downloadUrl;
    Button download;
    Button cancel;
    Button pause;
    private DownLoadTask downLoadTask;
private  DownLoadListener listener=new DownLoadListener() {
    @Override
    public void onProgress(int progress) {
        Toast.makeText(MainActivity.this,"下载了"+progress,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailed() {
downLoadTask=null;
        Toast.makeText(MainActivity.this,"下载失败",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSucccess() {
downLoadTask=null;
        Toast.makeText(MainActivity.this,"下载成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaused() {
downLoadTask=null;
        Toast.makeText(MainActivity.this,"下载暂停",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancled() {
downLoadTask=null;
        Toast.makeText(MainActivity.this,"下载取消",Toast.LENGTH_LONG).show();
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
download=findViewById(R.id.download);
        pause=findViewById(R.id.pause);
        cancel=findViewById(R.id.pause);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.download:
        Log.d(TAG, "onClick: ");
        String url="https://qd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.7.1.704_android_r110206_GuanWang_537057973_release_10000484.apk\n";
if (downLoadTask==null){
    downloadUrl=url;
downLoadTask=new DownLoadTask(listener);
downLoadTask.execute(downloadUrl);
    Toast.makeText(MainActivity.this,"下载开始",Toast.LENGTH_LONG).show();
}
break;
    case R.id.pause:
     if (downLoadTask!=null) {
         downLoadTask.pasuseDownLoad();
     }
}
    }
}
