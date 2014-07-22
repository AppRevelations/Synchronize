package com.apprevelations.synchronize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ClientActivity extends Activity {

    private EditText serverIp;

    private Button connectPhones;

    private String serverIpAddress = "";

    private boolean connected = false;

    private Handler handler = new Handler();
    
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);

        serverIp = (EditText) findViewById(R.id.server_ip);
        connectPhones = (Button) findViewById(R.id.connect_phones);
        connectPhones.setOnClickListener(connectListener);
        
        pb = (ProgressBar) findViewById(R.id.progressBar12);
    }

    private OnClickListener connectListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected) {
                serverIpAddress = serverIp.getText().toString();
                if (!serverIpAddress.equals("")) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
            }
        }
    };

    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, ServerActivity.SERVERPORT);
                connected = true;
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                    .getOutputStream())), true);
                            // where you issue the commands
//                            out.println("Hey Server!");
                        
                        OutputStream out1 = socket.getOutputStream();
                        
                        pb.setMax(Integer.parseInt(String.valueOf(new File(Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "com.mxtech.videoplayer.ad-1.apk").length())));
                        
                        FileInputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "com.mxtech.videoplayer.ad-1.apk");
                        byte[] buffer = new byte[8192];
                        int count;
                        while ((count = in.read(buffer)) > 0) {
                          out1.write(buffer, 0, count);
                          pb.incrementProgressBy(8192);
                        }
                        Log.d("ClientActivity", "C: Sent.");
                        in.close();
                        //Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
                        break;
                        
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }
}