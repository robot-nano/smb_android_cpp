package com.example.smb.base;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface OnTaskFinishedCallback<T> {
  @IntDef({SUCCEEDED, FAILED, CANCELLED})
  @Retention(RetentionPolicy.SOURCE)
  @interface Status {
  }

  int SUCCEEDED = 0;
  int FAILED = 1;
  int CANCELLED = 2;

  void onTaskFinished(@Status int status, @Nullable T item, @Nullable Exception exception);
}
