package com.example.smb.nativefacade;

import androidx.core.util.Pools.Pool;
import androidx.core.util.Pools.SynchronizedPool;

import java.io.IOException;

class MessageValues<T> implements AutoCloseable {

  private static final Pool<MessageValues<?>> POOL = new SynchronizedPool<>(20);

  private volatile T mObj;
  private volatile int mInt;
  private volatile long mLong;
  private volatile IOException mException;
  private volatile RuntimeException mRuntimeException;

  private MessageValues() {}

  void checkException() throws IOException {
    if (mException != null) {
      throw mException;
    }
    if (mRuntimeException != null) {
      throw mRuntimeException;
    }
  }

  T getObj() throws IOException {
    checkException();
    return mObj;
  }

  void setObj(T obj) {
    mObj = obj;
  }

  int getInt() throws IOException {
    checkException();
    return mInt;
  }

  long getLong() throws IOException {
    checkException();
    return mLong;
  }

  void setLong(long value) {
    mLong = value;
  }

  void setInt(int value) {
    mInt = value;
  }

  void setException(IOException exception) {
    mException = exception;
  }

  void setRuntimeException(RuntimeException exception) {
    mRuntimeException = exception;
  }

  @SuppressWarnings("unchecked")
  static <T> MessageValues<T> obtain() {
    MessageValues<?> response = POOL.acquire();
    if (response == null) {
      response = new MessageValues<>();
    }
    return (MessageValues<T>) response;
  }

  @Override
  public void close() {
    mObj = null;
    mInt = 0;
    mLong = 0L;
    mException = null;
    mRuntimeException = null;
    POOL.release(this);

  }
}
