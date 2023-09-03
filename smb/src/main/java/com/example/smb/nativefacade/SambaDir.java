package com.example.smb.nativefacade;

import android.system.ErrnoException;

import androidx.annotation.Nullable;

import com.example.smb.base.DirectoryEntry;

import java.io.IOException;

class SambaDir implements SmbDir {

  private final long mNativeHandler;
  private int mNativeDh;

  SambaDir(long nativeHandler, int nativeFd) {
    mNativeHandler = nativeHandler;
    mNativeDh = nativeFd;
  }

  @Override
  public DirectoryEntry readDir() throws IOException {
    try {
      return readDir(mNativeHandler, mNativeDh);
    } catch (ErrnoException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void close() throws IOException {
    try {
      int dh = mNativeDh;
      mNativeDh = -1;
      close(mNativeHandler, dh);
    } catch (ErrnoException e) {
      throw new IOException(e);
    }
  }

  private native @Nullable DirectoryEntry readDir(long handler, int fd) throws ErrnoException;
  private native void close(long handler, int fd) throws ErrnoException;
}
