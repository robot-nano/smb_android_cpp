package com.example.smb.cache;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.util.Pools;
import androidx.core.util.Pools.SynchronizedPool;

import com.example.smb.BuildConfig;
import com.example.smb.document.DocumentMetadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CacheResult implements AutoCloseable {

  @IntDef({CACHE_MISS, CACHE_HIT, CACHE_EXPIRED})
  @Retention(RetentionPolicy.SOURCE)
  @interface State {}
  public static final int CACHE_MISS = 0;
  public static final int CACHE_HIT = 1;
  public static final int CACHE_EXPIRED = 2;

  private static final Pools.Pool<CacheResult> POOL = new SynchronizedPool<>(10);

  private @State int mState;
  private @Nullable DocumentMetadata mItem;

  private CacheResult() {}

  private @State int getState() {
    return mState;
  }

  public DocumentMetadata getItem() {
    return mItem;
  }

  static CacheResult obtain(int state, @Nullable DocumentMetadata item) {
    CacheResult result = POOL.acquire();
    if (result == null) {
      result = new CacheResult();
    }
    result.mState = state;
    result.mItem = item;

    return result;
  }

  public void recycle() {
    mState = CACHE_MISS;
    mItem = null;
    boolean recycled = POOL.release(this);
    if (BuildConfig.DEBUG && !recycled) throw new IllegalStateException("One item is not enough!");
  }

  @Override
  public void close() throws Exception {
    recycle();
  }
}
