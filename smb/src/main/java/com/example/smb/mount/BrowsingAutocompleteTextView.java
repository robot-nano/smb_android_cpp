package com.example.smb.mount;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class BrowsingAutocompleteTextView extends AppCompatAutoCompleteTextView {
  public BrowsingAutocompleteTextView(Context context) {
    super(context);
  }

  public BrowsingAutocompleteTextView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public BrowsingAutocompleteTextView(Context context, AttributeSet attributeSet, int v) {
    super(context, attributeSet, v);
  }

  @Override
  public boolean enoughToFilter() {
    return true;
  }

  private void filter() {
    int length = getText().length();
    performFiltering(getText(), length == 0 ? 0 : getText().charAt(length - 1));
  }
}
