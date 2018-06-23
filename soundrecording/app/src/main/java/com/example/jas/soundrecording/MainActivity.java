package com.example.jas.soundrecording;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ListActivity
{
    Button btn1, btn2;

    private MediaRecorder mediarecorder; // 创建录音机

    File recordFile = null;
    File sdcardPath = null;// 录音文件目录

    String Tempfile = "Record_temp_";
    List<String> fileList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            sdcardPath = Environment.getExternalStorageDirectory();
            setTitle(sdcardPath.getName());
        } else {
            Toast.makeText(MainActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
        }
        getRecordList();

        btn1 = (Button) findViewById(R.id.StartRecording);
        btn2 = (Button) findViewById(R.id.StopRecording);

        ShakeListener shakeListener = new ShakeListener(this);//创建一个摇晃对象
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener(){//调用setOnShakeListener方法进行监听

            public void onShake() {
                Recording();
            }

        });

        btn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Recording();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {

                    if (recordFile != null) {
                        mediarecorder.stop();
                        fileList.add(recordFile.getName());

                        ArrayAdapter<String> list = new
                                ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.activity_list_item, R.id.list_item, fileList);

                        setListAdapter(list);
                        mediarecorder = null;
                        Toast.makeText(MainActivity.this, "停止", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.i("eeeee", e.getMessage());
                }
            }
        });
    }

    public void Recording()
    {try {

        recordFile = File.createTempFile(Tempfile, ".amr",
                sdcardPath);
        Log.i("aaaaa", "1");
        mediarecorder = new MediaRecorder();
        Log.i("aaaaa", "2");
        mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置麦克风
        Log.i("aaaaa", "3");
        mediarecorder
                .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);   //设置输出文件格式
        Log.i("aaaaa", "4");
        mediarecorder
                .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // 设置编码格式


        Log.i("aaaaa", "5");
        mediarecorder.setOutputFile(recordFile.getAbsolutePath()); // 使用绝对路径进行保存文件
        Log.i("aaaaa", "6");
        mediarecorder.prepare();
        mediarecorder.start();
        Toast.makeText(MainActivity.this, "开始", Toast.LENGTH_LONG).show();
    } catch (IOException e) {
        e.printStackTrace();
    }}

    void getRecordList() {
        // 首先检测是否存在SDCard
        try {
            File home = sdcardPath;
            fileList.clear();
            if (home.list(new Fileter()).length > 0) {
                for (File file : home.listFiles(new Fileter())) {
                    fileList.add(file.getName());
                }

                ArrayAdapter<String> list = new ArrayAdapter<String>(this,
                        android.R.layout.activity_list_item, R.id.list_item, fileList);

                setListAdapter(list);

            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    class Fileter implements FilenameFilter {

        public boolean accept(File dir, String filename) {
            return filename.equals(".amr");
        }

    }
}

