/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.polimi.dei.deepse.shopreview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class EnableGpsActivity extends Activity {
	
	public final static int GPS_ENABLED = 1;
	public final static int GPS_NOT_ENABLED = 2;

	protected void onCreate(Bundle savedInstanceState) {  
	     super.onCreate(savedInstanceState);  
	     //setContentView(R.layout.main);  
          createGpsDisabledAlert();  
	} 
	
	private void createGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Hey, your GPS is disabled! Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes, please!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showGpsOptions();
							}
						});
		builder.setNegativeButton("No, sorry.",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						setResult(GPS_NOT_ENABLED);
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private int REQUEST_CODE = 114;
	
	private void showGpsOptions(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);  
			if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				setResult(GPS_ENABLED);	
			}
			else{
				setResult(GPS_NOT_ENABLED);
			}
            finish();
        }
    }
}
