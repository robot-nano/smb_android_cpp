package com.example.smb;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskManager {

  private static final String TAG = "TaskManager";

  private final Map<Uri, AsyncTask> mTasks = new HashMap<>();

  private final Executor mExecutor = Executors.newCachedThreadPool();

  public  <T> void runTask(Uri uri, AsyncTask<T, ?, ?> task, T... args) {
    synchronized (mTasks) {
      if (!mTasks.containsKey(uri) || mTasks.get(uri).getStatus() == AsyncTask.Status.FINISHED) {
        mTasks.put(uri, task);
        task.executeOnExecutor(mExecutor, args);
      } else {
        Log.i(TAG,
            "Ignore this task for " + uri + " to avoid running multiple updates at the same time.");
      }
    }
  }

  public void runIoTask(AsyncTask<Void, Void, Void> task) {
    task.executeOnExecutor(mExecutor);
  }
}
