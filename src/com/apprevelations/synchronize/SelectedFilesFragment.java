package com.apprevelations.synchronize;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	
	private ArrayList<Intent> startedServicesIntentArrayList = new ArrayList<Intent>();
	
	String TAG = "SelectectedFilesFragment";
	
	StartServiceListener mCallback;
	
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
    public interface StartServiceListener {
        public void requestStartService(Intent serviceIntent);
        public void requestStopService(Intent serviceIntent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (StartServiceListener) activity;
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
        		if(selectedFilesArrayList.isEmpty()){
        			Toast.makeText(getActivity(), "No files selected", Toast.LENGTH_SHORT).show();
        		} else {
        			Toast.makeText(getActivity(), TAG, Toast.LENGTH_SHORT).show();
            		Intent i = new Intent(getActivity(), com.apprevelations.synchronize.ServerService.class);
            		i.putExtra("File Path", selectedFilesArrayList.get(0));
            		startedServicesIntentArrayList.add(i);
            		mCallback.requestStartService(i);
        		}
        		return true;
        		
        	case R.id.action_stop_sending : 
        		if(startedServicesIntentArrayList.isEmpty()){
        			Toast.makeText(getActivity(), "No service to stop", Toast.LENGTH_SHORT).show();
        		} else {
        			Toast.makeText(getActivity(), "Stopping service", Toast.LENGTH_SHORT).show();
        			mCallback.requestStopService(startedServicesIntentArrayList.get(0));
        		}
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
    public void onStop() {
        super.onStop();
    }
}
