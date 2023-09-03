package com.example.myapplication;

import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.myapplication.databinding.ActivitySelfAudioBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SelfAudioActivity extends AppCompatActivity {
  private ActivitySelfAudioBinding binding;
  private ImageButton forwardBtn, backwardBtn, playBtn;
  private ImageView audioImg;
  private TextView songName, albumName, startTime, songTime;
  private static int sTime = 0, eTime = 0;
  private final int mTime = 60000, mSeconds = 1000;
  private SeekBar songProgs;
  private static final boolean playing = false;

  private final MediaPlayer mPlayer = new MediaPlayer();
  private final ArrayList<String> audioList = new ArrayList<>();
  private final MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
  private final Handler hdler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivitySelfAudioBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    Glide
        .with(this)
        .load(R.drawable.img)
        .transform(new CenterCrop(), new RoundedCorners(30))
        .into(binding.audioImg);

    binding.audioImg.setImageResource(R.drawable.img);

    ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
      Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(0, insets.top, 0, insets.bottom);
      return WindowInsetsCompat.CONSUMED;
    });

//    File dir = new File("/storage/self/primary/Alarms/audio");
    File dir = new File("/storage/emulated/0/Alarms/audio");
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; ++i) {
      audioList.add(files[i].getPath());
    }

    /////
    audioImg = binding.audioImg;
    backwardBtn = binding.btnPre;
    forwardBtn = binding.btnNext;
    playBtn = binding.btnPlay;
    startTime = binding.startTime;
    songTime = binding.endTime;
    songName = binding.title;
    albumName = binding.subtitle;
    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    binding.audioRoot.setBackgroundResource(R.color.black);
    songProgs = binding.songBar;
    songProgs.setClickable(false);

    /////

    playBtn.setOnClickListener(v -> {
      try {
        if (!playing) {
          String firstSong = audioList.get(0);
          mPlayer.setDataSource(firstSong);
          mPlayer.prepare();
          sTime = mPlayer.getCurrentPosition();
          eTime = mPlayer.getDuration();
          metadataRetriever.setDataSource(firstSong);
          byte[] data = metadataRetriever.getEmbeddedPicture();
          songName.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
          albumName.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
          audioImg.setImageBitmap(
              BitmapFactory.decodeByteArray(data, 0, data.length));
          hdler.postDelayed(UpdateSongTime, 1000);
          mPlayer.start();

          startTime.setText("00:00");
          eTime = mPlayer.getDuration();
          songTime.setText(String.format("%02d:%02d", eTime / 60000,
              (eTime / 1000) % 60));
          songProgs.setMax(eTime);
        } else {

        }
      } catch (IOException e) {
        Log.w("====", "" + e);
      }

    });
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private final Runnable UpdateSongTime = new Runnable() {
    @Override
    public void run() {
      sTime = mPlayer.getCurrentPosition();
      startTime.setText(String.format("%02d:%02d", sTime / mTime,
          (sTime / 1000) % 60));
      songProgs.setProgress(sTime);
      hdler.postDelayed(this, 100);
    }
  };
}
