package com.example.smb;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;
import android.net.ConnectivityManager.NetworkCallback;

import androidx.annotation.NonNull;

import com.example.smb.SambaConfiguration.OnConfigurationChangedListener;
import com.example.smb.browsing.NetworkBrowser;
import com.example.smb.cache.DocumentCache;
import com.example.smb.nativefacade.CredentialCache;
import com.example.smb.nativefacade.SambaMessageLooper;
import com.example.smb.nativefacade.SmbFacade;

import java.io.File;

public class SambaProviderApplication extends Application {

  private static final String TAG = "SambaProviderApplication";

  private final DocumentCache mCache = new DocumentCache();
  private final TaskManager mTaskManager = new TaskManager();

  private SmbFacade mSambaClient;
  private ShareManager mShareManager;
  private NetworkBrowser mNetworkBrowser;

  @Override
  public void onCreate() {
    super.onCreate();

    init(this);
  }

  private void initialize(Context context) {
    if (mSambaClient != null) {
      // Already initialized.
      return;
    }

    initializeSambaConf(context);

    final SambaMessageLooper looper = new SambaMessageLooper();
    CredentialCache credentialCache = looper.getCredentialCache();
    mSambaClient = looper.getClient();

    mShareManager = new ShareManager(context, credentialCache);

    mNetworkBrowser = new NetworkBrowser(mSambaClient, mTaskManager);

    registerNetworkCallback(context);
  }

  private void initializeSambaConf(Context context) {
    final File home = context.getDir("home", MODE_PRIVATE);
    final File share = context.getExternalFilesDir(null);
    final SambaConfiguration sambaConf = new SambaConfiguration(home, share);

    final OnConfigurationChangedListener listener = new OnConfigurationChangedListener() {
      @Override
      public void onConfigurationChanged() {
        if (mSambaClient != null) {
          mSambaClient.reset();
        }
      }
    };

    // Sync from external folder. The reason why we don't use external folder directory as HOME is
    // because there are cases where external storage is not ready, and we don't have an extenral
    // folder at all.
    if (sambaConf.syncFromExternal(listener)) {
      if (BuildConfig.DEBUG) Log.d(TAG, "Syncing smb.conf from external folder. No need to try"
          + "flushing default config.");
      return;
    }

    sambaConf.flushDefault(listener);
  }

  private void registerNetworkCallback(Context context) {
    final ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    manager.registerNetworkCallback(
        new NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build(),
        new NetworkCallback() {
          @Override
          public void onAvailable(@NonNull Network network) {
            mSambaClient.reset();
          }
        });
  }

  public static void init(Context context) {
    getApplication(context).initialize(context);
  }

  public static ShareManager getServerManager(Context context) {
    return getApplication(context).mShareManager;
  }

  public static SmbFacade getSambaClient(Context context) {
    return getApplication(context).mSambaClient;
  }

  public static DocumentCache getDocumentCache(Context context) {
    return getApplication(context).mCache;
  }

  public static TaskManager getTaskManager(Context context) {
    return getApplication(context).mTaskManager;
  }

  public static NetworkBrowser getNetworkBrowser(Context context) {
    return getApplication(context).mNetworkBrowser;
  }

  private static SambaProviderApplication getApplication(Context context) {
    return ((SambaProviderApplication) context.getApplicationContext());
  }
}
