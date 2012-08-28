package it.polimi.dei.deepse.shopreview.domain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.selfmotion.HttpService;

public class GetProductNameYQL {

	private String url;

	public GetProductNameYQL(String upc) throws URISyntaxException {
		this.url = getUrl(upc);
	}

	protected String getUrl(String upc) throws URISyntaxException {

		URI uri = new URI(
				"http",
				"query.yahooapis.com",
				"//v1/public/yql",
				"q=select * from html where url='http://searchupc.com/default.aspx?q="
						+ upc
						+ "' and xpath='//body/center/form/table[@id=\"usersuggestion\"]/tr[2]/td[2]/table/tbody/tr[1]/td/span/text()'&format=json",
				null);
		return uri.toASCIIString();
	}

	public String getProductName() throws IllegalStateException, IOException,
			JSONException {

		JSONObject json = new JSONObject(new HttpService(url).invoke());
		JSONObject query = json.getJSONObject("query");
		
		if(query.has("results")){
			int count = query.getInt("count");
			if(count > 0){
				String result = query.getString("results");
				if(result != null){
					return result;
				}					
			}
		}		
		
		return null;

	}

}
