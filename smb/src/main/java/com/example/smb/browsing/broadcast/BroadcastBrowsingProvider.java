package com.example.smb.browsing.broadcast;

import android.util.Log;

import com.example.smb.BuildConfig;
import com.example.smb.browsing.BrowsingException;
import com.example.smb.browsing.NetworkBrowsingProvider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BroadcastBrowsingProvider implements NetworkBrowsingProvider {
  private static final String TAG = "BroadcastBrowsing";
  private static final int LISTENING_TIMEOUT = 3000; // 3 seconds
  private static final int NBT_PORT = 137;

  private final ExecutorService mExecutor = Executors.newCachedThreadPool();

  private int mTransId = 0;

  @Override
  public List<String> getServers() throws BrowsingException {
    try {
      List<Future<List<String>>> serverFutures = new ArrayList<>();

      List<String> broadcastAddress = BroadcastUtils.getBroadcastAddress();
      for (String address : broadcastAddress) {
        mTransId++;
        serverFutures.add(mExecutor.submit(new GetServersForInterfaceTask(address, mTransId)));
      }

      Set<String> servers = new HashSet<>();
      for (Future<List<String>> future : serverFutures) {
        servers.addAll(future.get());
      }

      return new ArrayList<>(servers);
    } catch (IOException | ExecutionException | InterruptedException e) {
      Log.e(TAG, "Failed to get servers via broadcast", e);
      throw new BrowsingException("Failed to get servers via broadcast", e);
    }
  }

  private class GetServersForInterfaceTask implements Callable<List<String>> {
    private final String mBroadcastAddress;
    private final int mTransId;

    GetServersForInterfaceTask(String broadcastAddress, int transId) {
      mBroadcastAddress = broadcastAddress;
      mTransId = transId;
    }

    @Override
    public List<String> call() throws Exception {
      try (DatagramSocket socket = new DatagramSocket()) {
        InetAddress address = InetAddress.getByName(mBroadcastAddress);

        sendNameQueryBroadcast(socket, address);

        return listenForServers(socket);
      }
    }

    private void sendNameQueryBroadcast(
        DatagramSocket socket,
        InetAddress address) throws IOException {
      byte[] data = BroadcastUtils.createPacket(mTransId);
      int dataLength = data.length;

      DatagramPacket packet = new DatagramPacket(data, 0, dataLength, address, NBT_PORT);
      socket.send(packet);

      if (BuildConfig.DEBUG) Log.d(TAG, "Broadcast package sent");
    }

    private List<String> listenForServers(DatagramSocket socket)   throws IOException {
      List<String> servers = new ArrayList<>();

      socket.setSoTimeout(LISTENING_TIMEOUT);

      while (true) {
        try {
          byte[] buf = new byte[1024];
          DatagramPacket packet = new DatagramPacket(buf, buf.length);

          socket.receive(packet);

          try {
            servers.addAll(BroadcastUtils.extractServers(packet.getData(), mTransId));
          } catch (BrowsingException e) {
            Log.e(TAG, "Failed to parse incoming packet: ", e);
          }
        } catch (SocketTimeoutException e) {
          break;
        }
      }

      return servers;
    }
  }
}
