package com.apprevelations.synchronize;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends Activity implements OnItemClickListener{
	
	String ROOT = "/";
	String CURRENT_DIRECTORY = "";
	String PARENT_DIRECTORY = "";
	Boolean EXIT_ON_BACK_PRESSED = false; 
	
	ArrayList<String> files = new ArrayList<String>();
	ListView lv;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_browse);
		
		lv = (ListView) findViewById(R.id.listView1);
		tv = (TextView) findViewById(R.id.textView1);
		
		String path = Environment.getExternalStorageDirectory().toString() + File.separator;
		CURRENT_DIRECTORY = path;
		tv.setText(CURRENT_DIRECTORY);
		
		File f = new File(path);
		
		if(f.getParent() != null){
			
			PARENT_DIRECTORY = f.getParent();
			
		}else{
			
			PARENT_DIRECTORY = null;
			
		}
		
		String file[] = f.list();
		files.add("..");
		
		if(file == null){
			
			Toast.makeText(getApplicationContext(), "Not Mounted", Toast.LENGTH_SHORT).show();
			
		}else{
			
			CURRENT_DIRECTORY = PARENT_DIRECTORY;
			
			
		}
		
		for (int i=0; i < file.length; i++){
		    Log.d("Files", "FileName:" + file[i]);
		    files.add(file[i]);
		}
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, 
                android.R.layout.simple_list_item_1,
                files );

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(this);
		
	}
	

	private void changeToParentDirectory(File parentDirectoryFile) {
		// TODO Auto-generated method stub
		
		if(parentDirectoryFile.exists()){
			
			if(parentDirectoryFile.isDirectory()){
				
				files.clear();
		        
				String file[] = parentDirectoryFile.list();
				
				files.add("..");
				
				for (int i=0; i < file.length; i++){
				    Log.d("Files", "FileName:" + file[i]);
				    files.add(file[i]);
				}
				
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
		                this, 
		                android.R.layout.simple_list_item_1,
		                files );
				
				lv.setAdapter(arrayAdapter);
				tv.setText(parentDirectoryFile.getPath());
				
				CURRENT_DIRECTORY = parentDirectoryFile.getPath();
				
				if(parentDirectoryFile.getParent() != null){
					
					PARENT_DIRECTORY = parentDirectoryFile.getParent();
					
				}else{
					
					PARENT_DIRECTORY = null;
					
				}
				
			}
			
		}
		
	}
	
	private void changeToChildDirectory(int pos) {
		// TODO Auto-generated method stub
		
		File dirToBeChecked = new File(CURRENT_DIRECTORY + File.separator + lv.getItemAtPosition(pos) + File.separator);
		
		if(dirToBeChecked.exists()){
				
			files.clear();
				        
			String file[] = dirToBeChecked.list();
				
			files.add("..");
			
			if(file == null){
				Toast.makeText(getApplicationContext(), "Not mounted", Toast.LENGTH_SHORT).show();
				PARENT_DIRECTORY = CURRENT_DIRECTORY;
				CURRENT_DIRECTORY = dirToBeChecked.getPath();
				return;
			}
				
			for (int i=0; i < file.length; i++){
			    Log.d("Files", "FileName:" + file[i]);
			    files.add(file[i]);
			}
				
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                this, 
	                android.R.layout.simple_list_item_1,
	                files );
				
			lv.setAdapter(arrayAdapter);
			tv.setText(dirToBeChecked.getPath());
			PARENT_DIRECTORY = CURRENT_DIRECTORY;
			CURRENT_DIRECTORY = dirToBeChecked.getPath();
		}
		
	}
	
	private boolean checkIfClickedItemIsDirectory(int pos) {
		// TODO Auto-generated method stub
		
		File dirToBeChecked = new File(CURRENT_DIRECTORY + File.separator + lv.getItemAtPosition(pos) + File.separator);
		
		if(dirToBeChecked.exists()){
			if(dirToBeChecked.isDirectory()){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		if(parent == lv){
			Toast.makeText(getApplicationContext(), view.toString() + "\n" + String.valueOf(position) + "\n" + String.valueOf(id), Toast.LENGTH_SHORT).show();
			
			if(checkIfClickedItemIsDirectory(position)){
				
				changeToChildDirectory(position);
				
			}else{
				
			}
		}
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		File parentDirectory = new File(CURRENT_DIRECTORY);
		
		if((parentDirectory.getParentFile() != null) && (parentDirectory.getParentFile().exists())){
			changeToParentDirectory(parentDirectory.getParentFile());
			EXIT_ON_BACK_PRESSED = false;
		}else{
			Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
			EXIT_ON_BACK_PRESSED = true;
		}
		
		if(EXIT_ON_BACK_PRESSED){
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
