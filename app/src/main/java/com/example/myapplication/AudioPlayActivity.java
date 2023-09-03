package com.example.myapplication;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.AudioRecyclerAdapter;
import com.example.myapplication.bean.AudioInfo;
import com.example.myapplication.databinding.ActivityPlayAudioBinding;
import com.example.myapplication.util.FileUtil;
import com.example.myapplication.widget.RecyclerExtras;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayActivity extends AppCompatActivity
    implements RecyclerExtras.OnItemClickListener {
  private final static String TAG = "AudioPlayActivity";
  private RecyclerView rv_audio;
  private final List<AudioInfo> mAudioList = new ArrayList<AudioInfo>(); // 音频列表
  private final Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // 音频库的Uri
  private final String[] mAudioColumn = new String[]{ // 媒体库的字段名称数组
      MediaStore.Audio.Media._ID, // 编号
      MediaStore.Audio.Media.TITLE, // 标题
      MediaStore.Audio.Media.DURATION, // 播放时长
      MediaStore.Audio.Media.SIZE, // 文件大小
      MediaStore.Audio.Media.DATA}; // 文件路径
  private AudioRecyclerAdapter mAdapter; // 音频列表的适配器
  private final MediaPlayer mMediaPlayer = new MediaPlayer(); // 媒体播放器
  private Timer mTimer = new Timer(); // 计时器
  private int mLastPosition = -1; // 上次播放的音频序号

  private ActivityPlayAudioBinding binding;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityPlayAudioBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    rv_audio = findViewById(R.id.rv_audio);
    loadAudioList();
    showAudioList();
  }

  private void loadAudioList() {
    mAudioList.clear();
    // 通过内容解析器查询音频库，并返回结果的游标。记录结果按照时间降序返回
    Cursor cursor = getContentResolver().query(mAudioUri, mAudioColumn,
        null, null, "date_modified desc");
    if (cursor != null) {
      Log.d(TAG, "cursor is not null");
      for (int i = 0; i < 10 && cursor.moveToNext(); ++i) {
        Log.d(TAG, "cursor i=" + i);
        AudioInfo audio = new AudioInfo();
        audio.setId(cursor.getLong(0));
        audio.setTitle(cursor.getString(1));
        audio.setDuration(cursor.getInt(2));
        audio.setSize(cursor.getLong(3));
        audio.setPath(cursor.getString(4));
        Log.d(TAG, audio.getTitle() + " " + audio.getDuration() + " " + audio.getSize() + " " + audio.getPath());
        if (!FileUtil.checkFileUri(this, audio.getPath())) {
          i--;
          continue;
        }
        mAudioList.add(audio);
      }
      cursor.close();
    } else {
      Log.d(TAG, "cursor is null");
    }
  }

  private void showAudioList() {
    LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    rv_audio.setLayoutManager(manager);
    mAdapter = new AudioRecyclerAdapter(this, mAudioList);
    mAdapter.setOnItemClickListener(this);
    rv_audio.setAdapter(mAdapter);
  }

  @Override
  public void onItemClick(View view, final int position) {
    if (mLastPosition != -1 && mLastPosition != position) {
      AudioInfo last_audio = mAudioList.get(mLastPosition);
      last_audio.setProgress(-1);
      mAudioList.set(mLastPosition, last_audio);
      mAdapter.notifyItemChanged(mLastPosition);
    }
    mLastPosition = position;
    final AudioInfo audio = mAudioList.get(position);
    Log.d(TAG, "onItemClick position=" + position + ",audio.getPath()=" + audio.getPath());
    mTimer.cancel();
    mMediaPlayer.reset();
    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    try {
      mMediaPlayer.setDataSource(audio.getPath());
      mMediaPlayer.prepare();
      mMediaPlayer.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    mTimer = new Timer();
    mTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        audio.setProgress(mMediaPlayer.getCurrentPosition());
        mAudioList.set(position, audio);
        mHandler.sendEmptyMessage(position);
        Log.d(TAG, "CurrentPosition=" + mMediaPlayer.getCurrentPosition() + ",position=" + position);
      }
    }, 0, 1000);
  }

  private final Handler mHandler = new Handler(Looper.myLooper()) {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      mAdapter.notifyItemChanged(msg.what);
    }
  };
}
