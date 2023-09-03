package com.example.smb.browsing;

import com.example.smb.base.DirectoryEntry;
import com.example.smb.nativefacade.SmbClient;
import com.example.smb.nativefacade.SmbDir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MasterBrowsingProvider implements NetworkBrowsingProvider {
  private static final String MASTER_BROWSING_DIR= "smb://";

  private final SmbClient mClient;

  MasterBrowsingProvider(SmbClient client) {
    mClient = client;
  }

  @Override
  public List<String> getServers() throws BrowsingException {
    List<String> serversList = new ArrayList<>();

    try {
      SmbDir rootDir = mClient.openDir(MASTER_BROWSING_DIR);

      List<DirectoryEntry> workgroups = getDirectoryChildren(rootDir);
      for (DirectoryEntry workgroup : workgroups) {
        if (workgroup.getType() == DirectoryEntry.WORKGROUP) {
          List<DirectoryEntry> servers = getDirectoryChildren(
              mClient.openDir(MASTER_BROWSING_DIR + workgroup.getName()));

          for (DirectoryEntry server : servers) {
            if (server.getType() == DirectoryEntry.SERVER) {
              serversList.add(server.getName());
            }
          }
        }
      }
    } catch (IOException e) {
      throw new BrowsingException(e.getMessage());
    }

    return serversList;
  }

  private static List<DirectoryEntry> getDirectoryChildren(SmbDir dir) throws IOException {
    List<DirectoryEntry> children = new ArrayList<>();

    DirectoryEntry currentEntry;
    while ((currentEntry = dir.readDir()) != null) {
      children.add(currentEntry);
    }

    return children;
  }
}
