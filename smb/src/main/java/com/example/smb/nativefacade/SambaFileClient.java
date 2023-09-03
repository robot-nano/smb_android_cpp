package com.example.smb.nativefacade;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.os.Message;
import android.system.StructStat;

import androidx.annotation.IntDef;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

public class SambaFileClient extends BaseClient implements SmbFile {

  @IntDef({READ, WRITE, CLOSE})
  @Retention(RetentionPolicy.SOURCE)
  @interface Operation {}
  private static final int READ = 1;
  private static final int WRITE = 2;
  private static final int CLOSE = 3;
  private static final int SEEK = 4;
  private static final int FSTAT = 5;

  SambaFileClient(Looper looper, SmbFile smbFileImpl) {
    mHandler = new SambaFileHandler(looper, smbFileImpl);
  }

  @Override
  public int read(ByteBuffer buffer, int maxLen) throws IOException {
    try (final MessageValues<ByteBuffer> messageValues = MessageValues.obtain()) {
      messageValues.setInt(maxLen);
      messageValues.setObj(buffer);
      final Message msg = mHandler.obtainMessage(READ, messageValues);
      enqueue(msg);
      return messageValues.getInt();
    }
  }

  @Override
  public int write(ByteBuffer buffer, int length) throws IOException {
    try (final MessageValues<ByteBuffer> messageValues = MessageValues.obtain()) {
      messageValues.setObj(buffer);
      final Message msg = mHandler.obtainMessage(WRITE, messageValues);
      msg.arg1 = length;
      enqueue(msg);
      return messageValues.getInt();
    }
  }

  @Override
  public long seek(long offset) throws IOException {
    try (final MessageValues<?> messageValues = MessageValues.obtain()) {
      final Message msg = mHandler.obtainMessage(SEEK, messageValues);
      messageValues.setLong(offset);

      enqueue(msg);
      return messageValues.getLong();
    }
  }

  @Override
  public StructStat fstat() throws IOException {
    try (final MessageValues<StructStat> messageValues = MessageValues.obtain()) {
      final Message msg = mHandler.obtainMessage(FSTAT, messageValues);
      enqueue(msg);
      return messageValues.getObj();
    }
  }

  @Override
  public void close() throws IOException {
    try (final MessageValues<?> messageValues = MessageValues.obtain()) {
      final Message msg = mHandler.obtainMessage(CLOSE, messageValues);
      enqueue(msg);
      messageValues.checkException();
    }
  }

  private static class SambaFileHandler extends BaseHandler {

    private SmbFile mSmbFileImpl;

    private SambaFileHandler(Looper looper, SmbFile smbFileImpl) {
      super(looper);

      mSmbFileImpl = smbFileImpl;
    }

    @Override
    @SuppressWarnings("unchecked")
    void processMessage(Message msg) {
      final MessageValues messageValues = (MessageValues) msg.obj;
      try {
        switch (msg.what) {
          case READ: {
            final int maxLen = messageValues.getInt();
            final ByteBuffer readBuffer = (ByteBuffer) messageValues.getObj();
            messageValues.setInt(mSmbFileImpl.read(readBuffer, maxLen));
            break;
          }
          case WRITE: {
            final ByteBuffer writeBuffer = (ByteBuffer)messageValues.getObj();
            final int length = msg.arg1;
            messageValues.setInt(mSmbFileImpl.write(writeBuffer, length));
            break;
          }
          case CLOSE:
            mSmbFileImpl.close();
            break;
          case SEEK:
            long offset = mSmbFileImpl.seek(messageValues.getLong());
            messageValues.setLong(offset);
            break;
          case FSTAT:
            messageValues.setObj(mSmbFileImpl.fstat());
            break;
          default:
            throw new UnsupportedOperationException("Unknown operation " + msg.what);
        }
      } catch (IOException e) {
        messageValues.setException(e);
      } catch (RuntimeException e) {
        messageValues.setRuntimeException(e);
      }
    }
  }
}
