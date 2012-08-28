package it.polimi.dei.deepse.shopreview.actions;

import it.polimi.dei.deepse.shopreview.GetProductName;
import it.polimi.dei.deepse.shopreview.GetProductPrice;
import it.polimi.dei.deepse.shopreview.domain.GetProduct;
import it.polimi.dei.deepse.shopreview.domain.GetProductNameYQL;

import java.io.IOException;
import java.net.URISyntaxException;

import org.dsol.annotation.Action;
import org.dsol.annotation.ReturnValue;
import org.json.JSONException;
import org.selfmotion.Callback;
import org.selfmotion.Result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ProductActions {

	private Context context;
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	/*
	 * ======================
	 * 
	 * GET PRODUCT NAME ACTIONS
	 * 
	 * ======================
	 * 	
	 */
	@Action(name = "getProductName", priority = 1)
	@ReturnValue("name")
	public Result getProductName(String barcode) throws URISyntaxException,
			IllegalStateException, IOException, JSONException {
		GetProduct getProductName = new GetProduct(barcode);
		String productName = getProductName.getProductName();
		return new Result(productName != null, productName);
	}

	@Action(name = "getProductName", priority = 2)
	@ReturnValue("name")
	public Result getProductNameYQL(String barcode) throws URISyntaxException,
			IllegalStateException, IOException, JSONException {
		GetProductNameYQL getProductName = new GetProductNameYQL(barcode);
		String productName = getProductName.getProductName();
		return new Result(productName != null, productName);
	}

	@Action(name = "getProductName", priority = 3)
	public Class<? extends Activity> getProductNameManually() {
		return GetProductName.class;
	}

	@Callback(concreteAction = "getProductNameManually")
	@ReturnValue("name")
	public Result getProductNameManuallyCallback(Intent data, int responseCode) {
		Result result = null;
		switch(responseCode){
			case Activity.RESULT_OK:
				Bundle extras = data.getExtras();
				result = new Result(true, extras.getString("inputData"));
				break;
			case Activity.RESULT_CANCELED:
				Log.i("ShopReview", "User did not enter the product name.");
				result = Result.FAIL;
				break;
		}
		return result;
	}
	
	/*
	 * ====================================
	 */
	
	/*
	 * GET PRODUCT PRICE
	 */
	
	@Action(name="inputPrice")
	public Intent inputPrice(String productName){
		Intent intent = new Intent(context, GetProductPrice.class);
		intent.putExtra("productName", productName);
		return intent;
	}

	@Callback(concreteAction="inputPrice")
	@ReturnValue("productPrice")
	public Result inputPriceCallback(Intent data, int responseCode){
		Result result = null;
		switch(responseCode){
			case Activity.RESULT_OK:
				Bundle extras = data.getExtras();
				result = new Result(true, extras.getString("inputData"));
				break;
			case Activity.RESULT_CANCELED:
				Log.i("ShopReview", "User did not enter the product price.");
				result = Result.FAIL;
				break;
		}
		return result;
	}
}
