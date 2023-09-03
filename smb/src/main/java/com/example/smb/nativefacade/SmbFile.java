package com.example.smb.nativefacade;

import android.system.StructStat;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface SmbFile extends Closeable {

  int read(ByteBuffer buffer, int maxLen) throws IOException;
  int write(ByteBuffer buffer, int length) throws IOException;
  long seek(long offset) throws IOException;
  StructStat fstat() throws IOException;
}
