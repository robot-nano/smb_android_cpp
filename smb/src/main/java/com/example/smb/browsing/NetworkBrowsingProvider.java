package com.example.smb.browsing;

import java.util.List;

public interface NetworkBrowsingProvider {

  List<String> getServers() throws BrowsingException;
}
