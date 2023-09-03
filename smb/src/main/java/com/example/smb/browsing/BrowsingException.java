package com.example.smb.browsing;

public class BrowsingException extends Exception {
  public BrowsingException(String message) {
    super("Browsing failed: " + message);
  }

  public BrowsingException(String message, Throwable cause) {
    super("Browsing failed: " + message, cause);
  }
}
