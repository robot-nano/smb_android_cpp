package com.example.smb.encryption;

public class EncryptionException extends Exception {
  public EncryptionException(String message) {
    super("Encryption failed: " + message);
  }

  public EncryptionException(String message, Throwable cause) {
    super("Encryption failed: " + message, cause);
  }
}
