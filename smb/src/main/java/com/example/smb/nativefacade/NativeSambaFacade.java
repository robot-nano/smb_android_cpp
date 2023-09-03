package com.example.smb.nativefacade;

import android.system.ErrnoException;
import android.system.StructStat;

import com.example.smb.BuildConfig;

import java.io.IOException;

public class NativeSambaFacade implements SmbClient {

  private final long mCredentialCacheHandler;
  private long mNativeHandler;

  static {
    System.loadLibrary("samba_client");
  }

  NativeSambaFacade(NativeCredentialCache cache) {
    mCredentialCacheHandler = cache.getNativeHandler();
  }

  private boolean isInitialized() {
    return mNativeHandler != 0;
  }

  @Override
  public void reset() {
    if (isInitialized()) {
      nativeDestroy(mNativeHandler);
    }
    mNativeHandler = nativeInit(BuildConfig.DEBUG, mCredentialCacheHandler);
  }

  @Override
  public SmbDir openDir(String uri) throws IOException {
    try {
      checkNativeHandler();
      return new SambaDir(mNativeHandler, openDir(mNativeHandler, uri));
    } catch (ErrnoException e) {
      throw new IOException("Failed to read directory " + uri, e);
    }
  }

  @Override
  public StructStat stat(String uri) throws IOException {
    try {
      checkNativeHandler();
      return stat(mNativeHandler, uri);
    } catch (ErrnoException e) {
      throw new IOException("Failed to get stat of " + uri, e);
    }
  }

  @Override
  public void createFile(String uri) throws IOException {
    try {
      checkNativeHandler();
      createFile(mNativeHandler, uri);
    } catch (ErrnoException e) {
      throw new IOException("Failed to create file at "   + uri, e);
    }
  }

  @Override
  public void mkdir(String uri) throws IOException {
    try {
      checkNativeHandler();
      mkdir(mNativeHandler, uri);
    } catch (ErrnoException e) {
      throw new IOException("Failed to make directory at " + uri, e);
    }
  }

  @Override
  public void rename(String uri, String newUri) throws IOException {
    try {
      checkNativeHandler();
      rename(mNativeHandler, uri, newUri);
    } catch (ErrnoException e) {
      throw new IOException("Failed to rename " + uri + " to " + newUri, e);
    }
  }

  @Override
  public void unlink(String uri) throws IOException {
    try {
      checkNativeHandler();
      unlink(mNativeHandler, uri);
    } catch (ErrnoException e) {
      throw new IOException("Failed to unlink " + uri, e);
    }
  }

  @Override
  public void rmdir(String uri) throws IOException {
    try {
      checkNativeHandler();
      rmdir(mNativeHandler, uri);
    }  catch (ErrnoException e) {
      throw new IOException("Failed to rmdir " + uri, e);
    }
  }

  @Override
  public SmbFile openFile(String uri, String mode) throws IOException {
    try {
      checkNativeHandler();
      return new SambaFile(mNativeHandler, openFile(mNativeHandler, uri, mode));
    } catch (ErrnoException e) {
      throw new IOException("Failed to open " + uri, e);
    }
  }

  private void checkNativeHandler() {
    if (!isInitialized()) {
      throw new IllegalStateException("Samba client is not initialized.");
    }
  }

  private native long nativeInit(boolean debug, long cacheHandler);

  private native void nativeDestroy(long handler);

  private native int openDir(long handler, String uri) throws ErrnoException;

  private native StructStat stat(long handler, String uri) throws ErrnoException;

  private native void createFile(long handler, String uri) throws ErrnoException;

  private native void mkdir(long handler, String uri) throws ErrnoException;

  private native void rmdir(long handler, String uri) throws ErrnoException;

  private native void rename(long handler, String uri, String newUri) throws ErrnoException;

  private native void unlink(long handler, String uri) throws ErrnoException;

  private native int openFile(long handler, String uri, String mode) throws ErrnoException;
}
