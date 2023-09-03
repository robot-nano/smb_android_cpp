package com.example.smb.nativefacade;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

abstract class BaseClient {

  BaseHandler mHandler;

  void enqueue(Message msg) {
    try {
      synchronized (msg.obj) {
        mHandler.sendMessage(msg);
        msg.obj.wait();
      }
    } catch (InterruptedException e) {
      // It should never happen.
      throw new RuntimeException("Unexpected interruption.", e);
    }
  }

  abstract static class BaseHandler extends Handler {

    BaseHandler(Looper looper) {
      super(looper);
    }

    abstract void processMessage(Message msg);

    @Override
    public void handleMessage(Message msg) {
      synchronized (msg.obj) {
        processMessage(msg);
        msg.obj.notify();
      }
    }
  }
}
