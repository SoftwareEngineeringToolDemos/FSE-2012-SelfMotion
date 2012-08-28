package it.polimi.dei.deepse.shopreview.actions;

import it.polimi.dei.deepse.shopreview.EnableGpsActivity;
import it.polimi.dei.deepse.shopreview.GetApproximateUserPosition;
import it.polimi.dei.deepse.shopreview.GetPositionManually;
import it.polimi.dei.deepse.shopreview.GetUserPosition;
import it.polimi.dei.deepse.shopreview.domain.UserLocation;

import org.dsol.annotation.Action;
import org.dsol.annotation.ReturnValue;
import org.selfmotion.Callback;
import org.selfmotion.Result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class LocationActions {

	private Context context;
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	
	@Action(name = "getPositionWithGPS")
	public Class<? extends Activity> getPositionWithGPS() {
		return GetUserPosition.class;
	}

	@Callback(concreteAction = "getPositionWithGPS")
	@ReturnValue("gpsPosition")
	public Result getPositionWithGPSCallback(Intent data, int responseCode) {
		Result result = null;
		switch(responseCode){
			case GetUserPosition.USER_POSITION_RETRIEVED:
				Bundle extras = data.getExtras();
				Location location = (Location) extras.get("location");
				result = new Result(true, new UserLocation(location.getLatitude(),location.getLongitude()));
				break;
			case GetUserPosition.USER_POSITION_NOT_RETRIEVED:
				result = Result.FAIL;
				break;
		}
		return result;
	}
	
	@Action(name = "getApproximatePosition")
	public Class<? extends Activity> getApproximatePosition() {
		return GetApproximateUserPosition.class;
	}
	
	@Callback(concreteAction = "getApproximatePosition")
	@ReturnValue("userApproximatePosition")
	public Result getApproximatePositionCallback(Intent data, int responseCode) {
		Result result = null;
		switch(responseCode){
			case GetApproximateUserPosition.USER_POSITION_RETRIEVED:
				Bundle extras = data.getExtras();
				Location location = (Location) extras.get("location");
				result = new Result(true, new UserLocation(location.getLatitude(),location.getLongitude()));
				break;
			case GetApproximateUserPosition.USER_POSITION_NOT_RETRIEVED:
				result = new Result(true);
				break;
		}
		return result;
	}

	@Action(name = "getPositionManually")
	public Intent getPositionManually(UserLocation userLocation) {
				
		Intent intent = new Intent(context, GetPositionManually.class);
		intent.putExtra("approximatePosition",userLocation);
		
		return intent;
	}

	@Callback(concreteAction = "getPositionManually")
	@ReturnValue("userDefinedPosition")
	public Result getPositionManuallyCallback(Intent data, int responseCode) {		
		Result result = null;
		switch(responseCode){
			case Activity.RESULT_OK:
				Bundle extras = data.getExtras();
				result = new Result(true, (UserLocation) extras.get("location"));
				break;
			case Activity.RESULT_CANCELED:
				Log.i("ShopReview", "Position not retrieved!");
				result = Result.FAIL;
				break;
		}
		return result;
	}
	
	@Action(name = "enableGPS")
	public Class<? extends Activity> enableGPS() {
		return EnableGpsActivity.class;
	}

	@Callback(concreteAction = "enableGPS")
	public Result enableGPSCallback(Intent data, int responseCode) {
		Result result = null;
		switch(responseCode){
			case EnableGpsActivity.GPS_ENABLED:
				Log.i("ShopReview", "Gps enabled");
				result = new Result(true);
				break;
			case EnableGpsActivity.GPS_NOT_ENABLED:
				Log.i("ShopReview", "Gps was not enabled!");
				result = Result.FAIL;
				break;
		}
		return result;
	}
}
