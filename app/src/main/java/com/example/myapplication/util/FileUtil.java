package com.example.myapplication.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
  private final static String TAG = "FileUtil";

  public static void saveText(String path, String txt) {
    try (FileOutputStream fos = new FileOutputStream(path)) {
      fos.write(txt.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String openText(String path) {
    String readStr = "";
    try (FileInputStream fis = new FileInputStream(path)) {
      byte[] b = new byte[fis.available()];
      fis.read(b);
      readStr = new String(b);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return readStr;
  }

  public static void saveImage(String path, Bitmap bitmap) {
    try (FileOutputStream fos = new FileOutputStream(path)) {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Bitmap openImage(String path) {
    Bitmap bitmap = null;
    try (FileInputStream fis = new FileInputStream(path)) {
      bitmap = BitmapFactory.decodeStream(fis);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bitmap;
  }

  public static boolean checkFileUri(Context ctx, String path) {
    boolean result = true;
    File file = new File(path);
    if (!file.exists() || !file.isFile() || file.length() <= 0) {
      result = false;
    }
    try {
      Uri uri = Uri.parse(path);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        uri = FileProvider.getUriForFile(ctx,
            ctx.getPackageName(), new File(path));
        Log.w("===", "");
      }
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }
    return result;
  }

  public static void saveFileFromUri(Context ctx, Uri src, String dest) {
    try (InputStream is = ctx.getContentResolver().openInputStream(src);
             OutputStream os = new FileOutputStream(dest);) {
      int byteCount = 0;
      byte[] bytes = new byte[8096];
      while ((byteCount = is.read(bytes)) != -1) {
        os.write(bytes, 0, byteCount);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getPathFromContentUri(Context context, Uri uri) {
    String path = uri.toString();
    if (path.startsWith("content://")) {
      String[] proj = new String[]{
          MediaStore.Video.Media._ID,
          MediaStore.Video.Media.TITLE,
          MediaStore.Video.Media.SIZE,
          MediaStore.Video.Media.MIME_TYPE,
          MediaStore.Video.Media.DATA
      };
      try (Cursor cursor = context.getContentResolver().query(uri,
          proj, null, null, null)) {
        cursor.moveToFirst();
        if (cursor.getString(3) != null) {
          path = cursor.getString(3);
        }
        Log.d(TAG, cursor.getLong(0) + " " + cursor.getString(1)
            + " " + cursor.getLong(2) + " " + cursor.getString(3)
            + " " + cursor.getString(4));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return path;
  }
}
