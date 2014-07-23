package com.apprevelations.synchronize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedFilesFragment extends Fragment{
	
	String TAG = "SelectectedFilesFragment";
	
	private Boolean isConnected = false;
	
	public static final int SERVERPORT = 8080;
	public static String SERVERIP = "10.0.2.15";
	private ServerSocket serverSocket;
	
	OnHeadlineSelectedListener mCallback;
	
//	ListView selectedFilesListView;
	LinearLayout topHierarchialLinearLayout;
	ArrayList<String> selectedFilesArrayList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_selected_files, container, false);
		setHasOptionsMenu(true);
		
//		selectedFilesListView = (ListView) view.findViewById(R.id.selected_files_list_view);
		topHierarchialLinearLayout = (LinearLayout) view.findViewById(R.id.top_hierarchial_linear_layout);
		
		return view;
	}

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

	protected void addFileInList(String filePath) {
		// TODO Auto-generated method stub
		
		selectedFilesArrayList.add(filePath);
		addFilesToSelectedFilesList(filePath);
		
	}

	private void addFilesToSelectedFilesList(String filePath) {
		// TODO Auto-generated method stub
		
/*		selectedFilesListView.setAdapter(new ArrayAdapter<String>(
                getActivity(), 
                android.R.layout.simple_list_item_1,
                selectedFilesArrayList));
*/
		LinearLayout fileDetailsView = new LinearLayout(getActivity());
		fileDetailsView.setBackgroundColor(Color.WHITE);
		fileDetailsView.setOrientation(LinearLayout.VERTICAL);
		fileDetailsView.setPadding(15, 15, 15, 15);
		fileDetailsView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		
		LinearLayout fileName = new LinearLayout(getActivity());
		fileName.setBackgroundColor(Color.rgb(247, 247, 247));		//Hex #f7f7f7
		fileName.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		TextView fileNameTextView = new TextView(getActivity());
		fileNameTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		fileNameTextView.setText(filePath);
		fileName.addView(fileNameTextView, 0);
		
		LinearLayout progress = new LinearLayout(getActivity());
		ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		progressBar.setProgress(0);
		progress.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		progress.addView(progressBar, 0);
		progress.setBackgroundColor(Color.WHITE);
		
		fileDetailsView.addView(fileName, 0);
		fileDetailsView.addView(progress, 1);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 3, 0, 0);		
		topHierarchialLinearLayout.addView(fileDetailsView, lp);
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		//super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.selected_files_frag_menu, menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
        	case R.id.action_settings :
        		Toast.makeText(getActivity(), TAG, Toast.LENGTH_SHORT).show();
        		return true;
        		
        	case R.id.action_send_all : 
        		Toast.makeText(getActivity(), TAG, Toast.LENGTH_SHORT).show();
        		new SendingThread().execute(selectedFilesArrayList.get(0));
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public class SendingThread extends AsyncTask<String, String, Boolean>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(SERVERIP != null){
				SERVERIP = getLocalIpAddress();
				Toast.makeText(getActivity(), SERVERIP, Toast.LENGTH_SHORT).show();
				try {
					while (true) {
						// listen for incoming clients
						Socket client = serverSocket.accept();
						Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT);
						
						OutputStream out = client.getOutputStream();
						
						FileInputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "com.mxtech.videoplayer.ad-1.apk");
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
					Toast.makeText(getActivity(), "Error : " + e.toString(), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				cancel(true);
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled(Boolean result) {
			// TODO Auto-generated method stub
			//super.onCancelled(result);
			Toast.makeText(getActivity(), "Not Connected", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return isConnected;
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
                        if (true) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }

	@Override
    public void onStop() {
        super.onStop();
        try {
             // make sure you close the socket upon exiting
             serverSocket.close();
             Toast.makeText(getActivity(), "Closing Socket", Toast.LENGTH_SHORT).show();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
}
