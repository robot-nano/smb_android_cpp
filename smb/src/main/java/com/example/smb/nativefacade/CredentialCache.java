package com.example.smb.nativefacade;

public interface CredentialCache {
  void putCredential(String uri, String workgroup, String username, String password);
  void removeCredential(String uri);
}
