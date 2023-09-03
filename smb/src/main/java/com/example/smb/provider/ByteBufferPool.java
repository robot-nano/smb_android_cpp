package com.example.smb.provider;

import androidx.core.util.Pools;

import java.nio.ByteBuffer;

public class ByteBufferPool {

  private static final int BUFFER_CAPACITY = 1024 * 1024;
  private final Pools.Pool<ByteBuffer> mBufferPool = new Pools.SynchronizedPool<>(16);

  public ByteBuffer obtainBuffer() {
    ByteBuffer buffer = mBufferPool.acquire();

    if (buffer == null) {
      buffer = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
    }

    return buffer;
  }

  public void recycleBuffer(ByteBuffer buffer) {
    buffer.clear();
    mBufferPool.release(buffer);
  }
}
