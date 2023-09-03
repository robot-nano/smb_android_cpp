package com.example.smb.base;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DirectoryEntry {

  @IntDef({WORKGROUP, SERVER, FILE_SHARE, PRINTER_SHARE, COMMS_SHARE, IPC_SHARE, DIR, FILE, LINK})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Type {}
  public static final int WORKGROUP = 1;
  public static final int SERVER = 2;
  public static final int FILE_SHARE = 3;
  public static final int PRINTER_SHARE = 4;
  public static final int COMMS_SHARE = 5;
  public static final int IPC_SHARE = 6;
  public static final int DIR = 7;
  public static final int FILE = 8;
  public static final int LINK = 9;

  private final int mType;
  private final String mComment;
  private String mName;

  public DirectoryEntry(@Type int type, String comment, String name) {
    mType = type;
    mComment = comment;
    mName = name;
  }

  public @Type int getType() {
    return mType;
  }

  public String getComment()   {
    return mComment;
  }

  public String getName() {
    return mName;
  }

  public void setName(String newName) {
    mName = newName;
  }
}
