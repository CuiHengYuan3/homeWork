package com.example.lenovo.mywork;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownLoadTask extends AsyncTask<String, Integer, Integer> {
    private static final String TAG = "DownLoadTask";
    private DownLoadListener downLoadListener;
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int PAUSED = 2;
    public static final int CANCELED = 3;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownLoadTask(DownLoadListener downLoadListener) {
        this.downLoadListener = downLoadListener;


    }


    @Override
    protected Integer doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: ");
        InputStream is = null;
        RandomAccessFile accessFile = null;
        File file = null;
        try {
            long downLoadedLength = 0;
            String downLoadUrl = strings[0];
            String fileName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downLoadedLength = file.length();
            }
            long contentLength = getContentLength(downLoadUrl);
            if (contentLength == 0) {
                return FAILED;
            } else if (contentLength == downLoadedLength) {
                return SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().addHeader("RANGE", "bytes=" + downLoadedLength + "-").url(downLoadUrl).build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                accessFile = new RandomAccessFile(file, "rw");
                accessFile.seek(downLoadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return CANCELED;
                    } else if (isPaused) {
                        return PAUSED;

                    } else {
                        total += len;
                        accessFile.write(b, 0, len);
                        int progress = (int) ((total + downLoadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            downLoadListener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case SUCCESS:
                downLoadListener.onSucccess();
                break;
            case FAILED:
                downLoadListener.onFailed();
                break;

            case PAUSED:
                downLoadListener.onPaused();
                break;
            case CANCELED:
                downLoadListener.onCancled();
                break;
            default:
                break;
        }
    }
public  void  pasuseDownLoad(){
        isCanceled=true;
}
    public  void  cancelDownLoad(){
isCanceled=true;
    }




    private Long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0L;
    }


}
