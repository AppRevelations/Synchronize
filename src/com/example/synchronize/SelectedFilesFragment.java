package com.example.synchronize;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SelectedFilesFragment extends Fragment{
	
	OnHeadlineSelectedListener mCallback;
	
//	ListView selectedFilesListView;
	LinearLayout topHierarchialLinearLayout;
	ArrayList<String> selectedFilesArrayList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_selected_files, container, false);
		
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
		updateSelectedFilesList(filePath);
		
	}

	private void updateSelectedFilesList(String filePath) {
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
		//progressBar.setLayoutDirection()
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

}
