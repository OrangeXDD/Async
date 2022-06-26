package com.atrem.async;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    TextView text;
    Button btn, btn2, bntStop;
    DownloadTask downloadTask, mDownloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        btn = findViewById(R.id.btn);
        btn2 = findViewById(R.id.btn2);
        bntStop = findViewById(R.id.btnStop);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTask = new DownloadTask();
                mDownloadTask = new DownloadTask();
                downloadTask.execute("1.jpg","2.jpg","3.jpg","4.jpg");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = -1;

                try {
                    result = mDownloadTask.get(1, TimeUnit.SECONDS);
                    Toast.makeText(getApplicationContext(), "Полученный результат: " + result, Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Полученный результат: " + result, Toast.LENGTH_SHORT).show();
                }
            }
        });
        bntStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTask.cancel(true);
            }
        });
    }

    public class DownloadTask extends AsyncTask<String, Integer, Integer>  {
        int i;
        int result = -1;
        @Override
        protected Integer doInBackground(String... strings) {
            try{
                for(int i = 1; i<=100; i++) {
                    publishProgress(i);
                    getFloor(i);
                    if(isCancelled()) return null;
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
                return null;
            }
            i = 2012;
            return i;
        }
        private void getFloor(int floor) throws InterruptedException{
            SystemClock.sleep(100);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            text.setText("Идет загрузка");
            btn.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Integer unused) {
            super.onPostExecute(unused);
            text.setText("Результат: " + Integer.toString(i));
            btn.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            text.setText("Время: " + values[0]/10+" сек");
        }
        @Override
        protected void onCancelled(){
            text.setText("Отмена загрузки");
            btn.setEnabled(true);
        }
    }
}