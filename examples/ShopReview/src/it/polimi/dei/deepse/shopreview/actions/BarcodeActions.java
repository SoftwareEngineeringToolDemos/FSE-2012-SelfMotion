package it.polimi.dei.deepse.shopreview.actions;

import it.polimi.dei.deepse.shopreview.GetUpc;
import it.polimi.dei.deepse.shopreview.ScannerActivity;

import org.dsol.annotation.Action;
import org.dsol.annotation.ReturnValue;
import org.selfmotion.Callback;
import org.selfmotion.Result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.biggu.barcodescanner.client.android.Intents;

public class BarcodeActions {

	private Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	@Action(name = "barcodeReader", priority=1)
	public Intent barcodeReader() {		
		Intent intent = new Intent(context, ScannerActivity.class);
		intent.putExtra(Intents.Preferences.ENABLE_BEEP, true);
		intent.putExtra(Intents.Preferences.ENABLE_VIBRATE, true);
		return intent;
	}

	@Callback(concreteAction = "barcodeReader")
	@ReturnValue("productBarcode")
	public Result barcodeReaderCallback(Intent data, int responseCode) {
		Result result = null;
		switch(responseCode){
			case Activity.RESULT_OK:
				Bundle extras = data.getExtras();
				result = new Result(true, extras.getString("SCAN_RESULT"));
				break;
			case Activity.RESULT_CANCELED:
				result = Result.FAIL;
				break;
		}
		return result;
	}

	@Action(name = "barcodeReader", priority=2)
	public Class<? extends Activity> getUpcManually() {
		return GetUpc.class;
	}
	
	@Callback(concreteAction = "getUpcManually")
	@ReturnValue("productBarcode")
	public Result getUpcManuallyCallback(Intent data, int responseCode) {
		Result result = null;
		switch(responseCode){
			case Activity.RESULT_OK:
				Bundle extras = data.getExtras();
				result = new Result(true, extras.getString("inputData"));
				break;
			case Activity.RESULT_CANCELED:
				Log.i("ShopReview", "User did not enter the product upc");
				result = Result.FAIL;
				break;
		}
		return result;
	}
}
