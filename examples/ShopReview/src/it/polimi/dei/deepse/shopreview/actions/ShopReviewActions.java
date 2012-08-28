package it.polimi.dei.deepse.shopreview.actions;

import it.polimi.dei.deepse.shopreview.domain.GetProduct;
import it.polimi.dei.deepse.shopreview.domain.OnlinePrice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.dsol.annotation.Action;
import org.dsol.annotation.ReturnValue;
import org.json.JSONException;

import android.util.Log;


public class ShopReviewActions{
	
	@Action(name="localSearch")
	public void localSearch(){
		Log.i("self-motion","Local search executed!");
	}
	
	@Action(name="webSearch")
	@ReturnValue("onlinePrices")
	public List<OnlinePrice> webSearch(String barcode) throws URISyntaxException, IllegalStateException, IOException, JSONException{
		GetProduct getProduct = new GetProduct(barcode);
		List<OnlinePrice> onlinePrices = getProduct.getOnlinePrices();
		return onlinePrices;
	}	
}
