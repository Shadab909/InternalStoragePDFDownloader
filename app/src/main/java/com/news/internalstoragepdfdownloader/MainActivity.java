package com.news.internalstoragepdfdownloader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    final String fileUrl = "https://www.clickdimensions.com/links/TestPDFfile.pdf";
    final String fileName = "TestPDFfile.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadPdf(fileName,fileUrl);
    }
  @SuppressLint("StaticFieldLeak")
    private void downloadPdf(String fileName , String fileUrl){
        new AsyncTask<Void,Integer,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids) {
                return downloadPdf();
            }
            @Nullable
            private Boolean downloadPdf(){
                try{
                    File file = getFileStreamPath(fileName);
                    if (file.exists()){
                        return true;
                    }
                    try{
                        FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        URL u = new URL(fileUrl);
                        URLConnection conn = u.openConnection();
                        int contentLength = conn.getContentLength();
                        InputStream input = new BufferedInputStream(u.openStream());
                        byte data[] = new byte[contentLength];
                        long total = 0;
                        int count;
                        while((count = input.read(data)) != -1){
                            total += count;
                            publishProgress((int)((total*100)/contentLength));
                            fileOutputStream.write(data,0,count);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        input.close();
                        return true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    Toast.makeText(MainActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
                    openPDF(fileName);
                }else{
                    Toast.makeText(MainActivity.this, "unable to download", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void openPDF(String fileName){
        try{
            File file = getFileStreamPath(fileName);
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}