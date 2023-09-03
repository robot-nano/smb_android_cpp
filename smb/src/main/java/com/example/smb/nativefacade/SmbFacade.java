package com.example.smb.nativefacade;

import android.annotation.TargetApi;
import android.os.ParcelFileDescriptor;
import android.os.storage.StorageManager;

import androidx.annotation.Nullable;

import com.example.smb.base.OnTaskFinishedCallback;
import com.example.smb.provider.ByteBufferPool;

import java.io.IOException;

public interface SmbFacade extends SmbClient {
  @TargetApi(26)
  ParcelFileDescriptor openProxyFile(
      String uri,
      String mode,
      StorageManager storageManager,
      ByteBufferPool bufferPool,
      @Nullable OnTaskFinishedCallback<String> callback) throws IOException;
}
