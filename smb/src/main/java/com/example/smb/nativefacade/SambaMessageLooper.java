package com.example.smb.nativefacade;

import android.os.Looper;

import java.util.concurrent.CountDownLatch;

public class SambaMessageLooper {

  private final Thread mLooperThread = new Thread(new Runnable() {
    @Override
    public void run() {
      prepare();
    }
  });
  private final CountDownLatch mLatch = new CountDownLatch(1);

  private volatile Looper mLooper;
  private volatile NativeSambaFacade mClientImpl;
  private volatile NativeCredentialCache mCredentialCacheImpl;

  private SambaFacadeClient mServiceClient;
  private CredentialCacheClient mCredentialCacheClient;

  public SambaMessageLooper() {
    init();
  }

  public SmbFacade getClient()  {
    return mServiceClient;
  }

  public CredentialCache getCredentialCache() {
    return mCredentialCacheClient;
  }

  private void init() {
    try {
      mLooperThread.start();
      mLatch.await();

      mCredentialCacheClient = new CredentialCacheClient(mLooper, mCredentialCacheImpl);

      mServiceClient = new SambaFacadeClient(mLooper, mClientImpl);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void prepare() {
    Looper.prepare();
    mLooper = Looper.myLooper();

    mCredentialCacheImpl = new NativeCredentialCache();
    mClientImpl = new NativeSambaFacade(mCredentialCacheImpl);
    mLatch.countDown();

    Looper.loop();
  }
}
