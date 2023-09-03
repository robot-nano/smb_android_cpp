package com.example.smb.base;

import android.os.AsyncTask;

public abstract class BiResultTask<Param, Progress, Result>
extends AsyncTask<Param, Progress, Result> {

  private volatile Exception mException;

  public abstract Result run(Param... params) throws Exception;

  @Override
  protected Result doInBackground(Param... params) {
    try {
      return run(params);
    } catch (Exception e) {
      mException = e;
      return null;
    }
  }

  public abstract void onSucceeded(Result result);
  public void onFailed(Exception exception) {}

  @Override
  protected void onPostExecute(Result result) {
    if (mException == null) {
      onSucceeded(result);
    } else {
      onFailed(mException);
    }
  }
}
