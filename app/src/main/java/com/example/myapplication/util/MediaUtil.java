package com.example.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MediaUtil {
  private final static String TAG = "Mediautil";

  public static String formatDuration(int milliseconds) {
    int seconds = milliseconds / 1000;
    int hour = seconds / 3600;
    int minute = seconds / 60;
    int second = seconds % 60;
    String str;
    if (hour > 0) {
      str = String.format("%02d:%02d:%02d", hour, minute, second);
    } else {
      str = String.format("%02d:%02d", minute, second);
    }
    return str;
  }

  public static String getRecordFilePath(Context context, String dir_name, String extend_name) {
    String path = "";
    File recordDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + dir_name + "/");
    if (!recordDir.exists()) {
      recordDir.mkdirs();
    }
    try {
      File recordFile = File.createTempFile(DateUtil.getNowDateTime(), extend_name, recordDir);
      path = recordFile.getAbsolutePath();
      Log.d(TAG, "dir_name=" + dir_name + ", extend_name=" + extend_name + ", path=" + path);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return path;
  }

  public static Bitmap getOneFrame(Context ctx, Uri uri, int pos) {
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    retriever.setDataSource(ctx, uri);
    Bitmap bitmap = retriever.getFrameAtTime(pos * 1000);
    Log.d(TAG, "getWidth=" + bitmap.getWidth() + ", getHeight=" + bitmap.getHeight());
    return bitmap;
  }

  public static List<String> getFrameList(Context ctx, Uri uri, int beginPos, int count) {
    String videoPath = uri.toString();
    String videoName = videoPath.substring(videoPath.lastIndexOf("/") + 1);
    if (videoName.contains(".")) {
      videoName = videoName.substring(0, videoName.lastIndexOf("."));
    }
    Log.d(TAG, "videoName=" + videoName);
    List<String> pathList = new ArrayList<>();
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    retriever.setDataSource(ctx, uri);
    // 获得视频时长
    String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    Log.d(TAG, "duration=" + duration);
    int dura_int = Integer.parseInt(duration) / 1000;
    for (int i = 0; i < dura_int - beginPos / 1000 && i < count; ++i) {
      String path = String.format("%s/%s_%d.jpg",
          ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), videoName, i);
      if (beginPos != 0 || !new File(path).exists()) {
        Bitmap frame = retriever.getFrameAtTime(beginPos * 1000 + i * 1000 * 1000);
        int ratio = frame.getWidth() / 500 + 1;
        Bitmap small = BitmapUtil.getScaleBitmap(frame, 1.0 / ratio);
        BitmapUtil.saveImage(path, small);
      }
      pathList.add(path);
    }
    return pathList;
  }

  public static String copyVideoFromUri(Context ctx, Uri uri) {
    Log.d(TAG, "copyVideoFromUri uri=" + uri.toString());
    String videoPath = String.format("%s/%s.mp4",
        ctx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(),
        DateUtil.getNowDateTime());
    // 打开指定uri获得输入流对象，利用缓存输入和输出流复制文件
    try (InputStream is = ctx.getContentResolver().openInputStream(uri);
         BufferedInputStream bis = new BufferedInputStream(is);
         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(videoPath))) {
      byte[] bytes = new byte[bis.available()];
      bis.read(bytes);
      bos.write(bytes);
      Log.d(TAG, "文件复制完成，源文件大小="+bytes.length+"，新文件大小="+bytes.length);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return videoPath;
  }
}
