package com.example.smb.encryption;

import javax.crypto.SecretKey;

public class EncryptionKey {
  private final SecretKey mKey;
  private final byte[] mIv;

  EncryptionKey(SecretKey key, byte[] iv) {
    mKey = key;
    mIv = iv;
  }

  SecretKey getKey() {
    return mKey;
  }

  byte[] getIv() {
    return mIv;
  }
}
