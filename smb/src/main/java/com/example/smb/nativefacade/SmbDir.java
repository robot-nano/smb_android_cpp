package com.example.smb.nativefacade;

import androidx.annotation.Nullable;

import com.example.smb.base.DirectoryEntry;

import java.io.Closeable;
import java.io.IOException;

public interface SmbDir extends Closeable {

  @Nullable
  DirectoryEntry readDir() throws IOException;

}
