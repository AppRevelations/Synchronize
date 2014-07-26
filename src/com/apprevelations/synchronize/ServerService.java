package com.apprevelations.synchronize;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServerService extends Service{
	
	private static final int ERROR_CODE_NO_FILE_PATH_SPECIFIED = 0;
	private static final int SUCCESS = 10;
	
	public static final int SERVERPORT = 8080;
	public static String SERVERIP = "10.0.2.15";
	private ServerSocket serverSocket;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
		
		String filePath = intent.getStringExtra("File Path");
		if(filePath == null){
			return ERROR_CODE_NO_FILE_PATH_SPECIFIED;
		}
		
		if(SERVERIP != null){
			SERVERIP = getLocalIpAddress();
			Toast.makeText(getApplicationContext(), SERVERIP, Toast.LENGTH_SHORT).show();
			
		} else {
			Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
			try {
				while (true) {
					// listen for incoming clients
					Socket client = serverSocket.accept();
					Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT);
					
					OutputStream out = client.getOutputStream();
					
					FileInputStream in = new FileInputStream(filePath);
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = in.read(buffer)) > 0) {
                      out.write(buffer, 0, count);
                      //pb.incrementProgressBy(8192);
                    }
                    Log.d("ClientActivity", "C: Sent.");
                    in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Error : " + e.toString(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
		return SUCCESS;
		//return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {
            // make sure you close the socket upon exiting
            serverSocket.close();
            Toast.makeText(getApplicationContext(), "Closing Socket", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

/*
	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}
*/

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

/*
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		super.onTaskRemoved(rootIntent);
	}
*/

	@Override
	protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
		// TODO Auto-generated method stub
		super.dump(fd, writer, args);
	}
	
	private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                    	//return inetAddress.getHostAddress().toString();
                    	String sAddr = inetAddress.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        
                        if (isIPv4){
                            return sAddr;
                        } else if (!isIPv4) {
                            int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                            return delim<0 ? sAddr : sAddr.substring(0, delim);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }

}
