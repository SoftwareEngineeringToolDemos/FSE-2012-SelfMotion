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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GetApproximateUserPosition extends Activity {

	public final static int USER_POSITION_RETRIEVED = 1;
	public final static int USER_POSITION_NOT_RETRIEVED = 2;

	private LocationManager locationManager = null;
	private LocationListener locationListener = null;
	private ProgressDialog dialog;
	private Timer timer;
	private TimerTask timerTask;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		dialog = ProgressDialog.show(GetApproximateUserPosition.this, "", "Trying to get your approximated position. Please wait...", true);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				if (timerTask.cancel()) {
					locationManager.removeUpdates(locationListener);
					dialog.dismiss();
//					Toast.makeText(GetApproximateUserPosition.this,
//							"Approximated user location retrieved!", 2).show();
					Intent result = new Intent();
					result.putExtra("location", location);
					setResult(USER_POSITION_RETRIEVED, result);
					finish();
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
				0, locationListener);

		dialog.show();
		timer = new Timer();

		timerTask = new TimerTask() {
			@Override
			public void run() {
				locationNotRetrieved();
			}
		};

		timer.schedule(timerTask, 60000);
	}

	private void locationNotRetrieved() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
				locationManager.removeUpdates(locationListener);
				Toast.makeText(GetApproximateUserPosition.this,
						"User location not retrieved!", 2).show();
				setResult(USER_POSITION_NOT_RETRIEVED);
				finish();
			}
		});
	}
}
