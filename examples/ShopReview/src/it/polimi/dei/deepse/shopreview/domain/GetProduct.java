package it.polimi.dei.deepse.shopreview.domain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.selfmotion.HttpService;

public class GetProduct {

	private String url;

	public GetProduct(String upc) throws URISyntaxException {
		this.url = getUrl(upc);
	}

	protected String getUrl(String upc) throws URISyntaxException {
	//	http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=CC50DBF1-283F-462D-ABC8-C811F205B030&upc=upc_code 

		URI uri = new URI(
				"http",
				"www.searchupc.com",
				"/handlers/upcsearch.ashx",
				"request_type=3&access_token=CC50DBF1-283F-462D-ABC8-C811F205B030&upc=" + upc,
				null);
		
		return uri.toASCIIString();
	}

	public String getProductName() throws IllegalStateException, IOException,
			JSONException {

		JSONObject json = invoke();
		if(json.has("0")){
			JSONObject firstResult = json.getJSONObject("0");
			return firstResult.getString("productname");
		}
		return null;
	}

	public List<OnlinePrice> getOnlinePrices() throws JSONException, IllegalStateException, IOException {
		JSONObject jsonObject = invoke();
		JSONObject result = null;
		ArrayList<OnlinePrice> prices = new ArrayList<OnlinePrice>();
		for(int i = 0; i < jsonObject.length();i++ ){
			if(jsonObject.has(""+i)){
				result = jsonObject.getJSONObject(""+i);
				prices.add(new OnlinePrice(	result.getString("storename"), 
											Double.valueOf(result.getString("price")),
											result.getString("currency")));
			}
		}
		return prices;
	}
	
	private JSONObject invoke() throws IllegalStateException, JSONException, IOException{
		return new JSONObject(new HttpService(url).invoke());
	}

}
