package com.example.smb.mount;

import android.net.ConnectivityManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smb.ShareManager;
import com.example.smb.TaskManager;
import com.example.smb.cache.DocumentCache;
import com.example.smb.nativefacade.SmbClient;

public class MountServerActivity extends AppCompatActivity {

  private static final String TAG = "MountServerActivity";

  private static final String ACCOUNT_BROWSE = "android.provider.action.BROWSE";
  private static final String SHARE_PATH_KEY = "sharePath";
  private static final String NEEDS_PASSWORD_KEY = "needsPassword";
  private static final String DOMAIN_KEY = "domain";
  private static final String USERNAME_KEY = "username";
  private static final String PASSWORD_KEY = "password";
  private static final String AUTH_LAUNCH_KEY = "authLaunch";

  private final OnClickListener mPasswordStateChangeListener = new OnClickListener() {

    @Override
    public void onClick(View v) {
      final boolean isChecked = mNeedPasswordCheckbox.isChecked();
      setNeedsPasswordState(isChecked);
    }
  };

  private final OnClickListener mMountListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      tryMount();
    }
  };

  private final OnKeyListener mMountKeyListener = new OnKeyListener() {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
      if (keyEvent.getAction() == KeyEvent.ACTION_UP
          && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
        tryMount();
        return true;
      }
      return false;
    }
  };

  private DocumentCache mCache;
  private TaskManager mTaskManager;
  private ShareManager mShareManager;
  private SmbClient mClient;
  private BrowsingAutocompleteTextView mBrowsingAdapter;

  private CheckBox mNeedPasswordCheckbox;
  private View mPasswordHideGroup;

  private BrowsingAutocompleteTextView mSharedPathEditText;
  private EditText mDomainEditText;
  private EditText mUsernameEditText;
  private EditText mPasswordEditText;

  private ConnectivityManager mConnectivityManager;

  private void clearCredentials() {

  }

  private void setNeedsPasswordState(boolean needsPassword) {
    mNeedPasswordCheckbox.setChecked(needsPassword);

    mPasswordHideGroup.setVisibility(needsPassword ? View.VISIBLE : View.GONE);
    if (!needsPassword) {
      clearCredentials();
    }
  }

  private void tryMount() {

  }
}
