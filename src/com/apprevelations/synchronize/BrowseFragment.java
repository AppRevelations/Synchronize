package com.apprevelations.synchronize;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseFragment extends Fragment implements OnItemClickListener {
	
	String TAG = "BrowseFragment";
	
	OnFileSelectedListener mCallback;
	
	String ROOT = Environment.getRootDirectory().getPath();
	String CURRENT_DIRECTORY = "";
	String PARENT_DIRECTORY = "";
	
	ArrayList<String> files;
	ListView lv;
	TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.fragment_browse, container, false);
		setHasOptionsMenu(true);
		
		lv = (ListView) v.findViewById(R.id.listView1);
		tv = (TextView) v.findViewById(R.id.textView1);
		
		lv.setOnItemClickListener(this);
		
		SharedPreferences userPreferences = getActivity().getSharedPreferences("User Preferences", Activity.MODE_PRIVATE);
		
		String userPreferredHomeDirectory = userPreferences.getString("Preffered Directory", null);
		if(userPreferredHomeDirectory == null){
			String state = Environment.getExternalStorageState();
			if(Environment.MEDIA_MOUNTED.equals(state)){
				Toast.makeText(getActivity(), "Media mounted.", Toast.LENGTH_SHORT).show();
				userPreferredHomeDirectory = Environment.getExternalStorageDirectory().getPath();
			}else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
				Toast.makeText(getActivity(), "Media mounted as read only.", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_CHECKING.equals(state)){
				Toast.makeText(getActivity(), "Media currently checking...", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_BAD_REMOVAL.equals(state)){
				Toast.makeText(getActivity(), "Bad Removal : Media was removed before it was unmounted.", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_NOFS.equals(state)){
				Toast.makeText(getActivity(), "Media is blank or is using an unsupported filesystem.", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_REMOVED.equals(state)){
				Toast.makeText(getActivity(), "Media is not present.", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_SHARED.equals(state)){
				Toast.makeText(getActivity(), "Media not mounted and shared via USB mass storage.", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_UNKNOWN.equals(state)){
				Toast.makeText(getActivity(), "Unknown storage state, such as when a path isn't backed by known storage media.", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_UNMOUNTABLE.equals(state)){
				Toast.makeText(getActivity(), "Media cannot be mounted. Typically this happens if the file system on the media is corrupted.", Toast.LENGTH_SHORT).show();
			}else if(Environment.MEDIA_UNMOUNTED.equals(state)){
				Toast.makeText(getActivity(), "Media is not mounted.", Toast.LENGTH_SHORT).show();
			}
		}
		
		setDirectory(userPreferredHomeDirectory);
		
		return v;
		
	}

	private void setDirectory(String directoryToBeSet) {
		// TODO Auto-generated method stub
		
		if(directoryToBeSet != null){
			
			File dir = new File(directoryToBeSet);
			
			if(dir.exists()){
				
				if(dir.isDirectory()){
					
					if(dir.canRead()){
						
						ArrayList<String> files = new ArrayList<String>();
						String file[] = dir.list();
						if(dir.getParentFile() != null){
							if(dir.exists()){
								if(dir.isDirectory()){
									if(dir.canRead()){
										files.add("..");
									}
								}
							}
						}
						
						for (int i=0; i < file.length; i++){
						    Log.d("Files", "FileName:" + file[i]);
						    files.add(file[i]);
						}
						
						lv.setAdapter(new ArrayAdapter<String>(
				                getActivity(), 
				                android.R.layout.simple_list_item_1,
				                files));
						
						CURRENT_DIRECTORY = dir.getAbsolutePath();
						tv.setText(CURRENT_DIRECTORY);
						
//						Toast.makeText(getActivity(), "Directory can be read.", Toast.LENGTH_SHORT).show();
						
					}else{
					
						Toast.makeText(getActivity(), "Directory cannot be read.", Toast.LENGTH_SHORT).show();
						
					}
					
				}else{
				
					Toast.makeText(getActivity(), "Not a directory but a file.", Toast.LENGTH_SHORT).show();
					
					if(dir.canRead()){
						
//						Toast.makeText(getActivity(), "File can be read.", Toast.LENGTH_SHORT).show();
						mCallback.onFileSelected(dir.getAbsolutePath());
						
					}else{
						
						Toast.makeText(getActivity(), "File cannot be read.", Toast.LENGTH_SHORT).show();
						
					}
					
				}
				
			}else{
				
				Toast.makeText(getActivity(), "Directory or file does not exist.", Toast.LENGTH_SHORT).show();
				
			}
			
		}else{
			
			Toast.makeText(getActivity(), "Directory to be set is null.", Toast.LENGTH_SHORT).show();
			
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		if(parent == lv){
			
//			Toast.makeText(getActivity(), "Item in list of files and folders was clicked.", Toast.LENGTH_SHORT).show();
			String selectedDirectory = null;
			if(lv.getItemAtPosition(position).equals("..")){
				File tempCurrentFile = new File(CURRENT_DIRECTORY);
				if((tempCurrentFile.getParent() != null) && (tempCurrentFile.getParentFile().exists())){								//duplication??
					selectedDirectory = tempCurrentFile.getParent();
				}else{
					
				}
			}else{
				selectedDirectory = CURRENT_DIRECTORY + File.separator + lv.getItemAtPosition(position);
			}
			setDirectory(selectedDirectory);
			
		}else{
			
			Toast.makeText(getActivity(), "Item in list of files and folders was not clicked.", Toast.LENGTH_SHORT).show();
			
		}
		
	}

/*	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		File currentDirectory = new File(CURRENT_DIRECTORY);
		
		if((currentDirectory.getParent() != null) && (currentDirectory.getParentFile().exists())){										//duplication??
			setDirectory(currentDirectory.getParent());
		}else{
			Toast.makeText(getActivity(), "Press back again to exit", Toast.LENGTH_SHORT).show();
//			if(CURRENT_DIRECTORY == ){
				super.onBackPressed();
//			}
		}
		
	}
*/
	
	// Container Activity must implement this interface
    public interface OnFileSelectedListener {
        public void onFileSelected(String filePath);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFileSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		//super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.browse_frag_menu, menu);
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
        		
        }
        return super.onOptionsItemSelected(item);
    }

}
