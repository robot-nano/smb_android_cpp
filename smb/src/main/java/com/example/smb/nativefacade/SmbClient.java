package com.example.smb.nativefacade;

import android.system.StructStat;

import com.hierynomus.smbj.utils.SmbFiles;

import java.io.IOException;

public interface SmbClient {

  void reset();

  SmbDir openDir(String uri) throws IOException;

  StructStat stat(String uri) throws IOException;

  void createFile(String uri) throws IOException;

  void mkdir(String uri) throws IOException;

  void rename(String uri, String newUri) throws IOException;

  void unlink(String uri) throws IOException;

  void rmdir(String uri) throws IOException;

  SmbFile openFile(String uri, String mode) throws IOException;
}
