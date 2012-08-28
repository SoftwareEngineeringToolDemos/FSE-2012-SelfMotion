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
import android.os.Bundle;
import android.widget.EditText;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class GetProductName extends Activity {

	private EditText input = null;

	public GetProductName() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Product");
		alert.setMessage("Please, enter the product name:");

		// Set an EditText view to get user input
		input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", okClicked);

		alert.setNegativeButton("Cancel",cancelClicked);

		alert.show();

	}

	DialogInterface.OnClickListener okClicked = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			String value = input.getText().toString();
			Intent data = new Intent();
			data.putExtra("inputData", value);
			setResult(RESULT_OK, data);
			finish();
		}
	};

	DialogInterface.OnClickListener cancelClicked = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			setResult(RESULT_CANCELED);
			finish();

		}
	};


}
