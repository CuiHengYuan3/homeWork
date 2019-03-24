package com.example.lenovo.mywork;

public  interface DownLoadListener {
    void  onProgress(int progress);
    void onFailed();
    void onSucccess();
    void onPaused();
     void onCancled();

}
