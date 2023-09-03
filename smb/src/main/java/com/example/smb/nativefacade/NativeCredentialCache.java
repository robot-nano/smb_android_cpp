package com.example.smb.nativefacade;

class NativeCredentialCache implements CredentialCache {

  static {
    System.loadLibrary("samba_client");
  }

  private long mNativeHandler;

  NativeCredentialCache() {
    mNativeHandler = nativeInit();
  }

  long getNativeHandler() {
    return mNativeHandler;
  }

  @Override
  public void putCredential(String uri, String workgroup, String username, String password) {
    putCredential(mNativeHandler, uri, workgroup, username, password);
  }

  @Override
  public void removeCredential(String uri) {
    removeCredential(mNativeHandler, uri);
  }

  private native long nativeInit();

  private native void putCredential(
      long handler, String uri, String workgroup, String username, String password);

  private native void removeCredential(long handler, String uri);
}
