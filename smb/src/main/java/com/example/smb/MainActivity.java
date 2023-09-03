package com.example.smb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.security.jce.JceSecurityProvider;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

//  private String SERVER_ADDRESS = "www.server.asia";
  private String SERVER_ADDRESS;

  SMBClient client = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    InetAddress address = getInetAddressByName("www.wserver.asia");
    Log.d("===", "");


  }

  public static InetAddress getInetAddressByName(String name)
  {
    AsyncTask<String, Void, InetAddress> task = new AsyncTask<String, Void, InetAddress>()
    {

      @Override
      protected InetAddress doInBackground(String... params)
      {
        try
        {
          InetAddress address = InetAddress.getByName(params[0]);
          SmbConfig cfg = SmbConfig.builder().
              withMultiProtocolNegotiate(true).
              build();
          SMBClient client = new SMBClient();
//          Connection connection = client.connect(address.getHostAddress());
          Connection connection = client.connect("www.wserver.asia");

          AuthenticationContext ac = new AuthenticationContext("w2204", "qwe".toCharArray(), null);
          Session session = connection.authenticate(ac);

          // Create the RPC transport layer & the service


          try (DiskShare share = (DiskShare) session.connectShare("sambashare")) {
            Log.d("====", "");

//            for (FileIdBothDirectoryInformation f : share.list("FOLDER", "*.TXT")) {
//              System.out.println("File : " + f.getFileName());
//            }
          } catch (Exception e) {
            Log.d("====", e.toString());
          }

          return address;
        }
        catch (UnknownHostException e)
        {
          return null;
        } catch (IOException e) {
          return null;
        }
      }
    };
    try
    {
      return task.execute(name).get();
    }
    catch (InterruptedException e)
    {
      return null;
    }
    catch (ExecutionException e)
    {
      return null;
    }

  }
}