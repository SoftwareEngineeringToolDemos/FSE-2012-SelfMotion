package it.polimi.dei.deepse.shopreview.actions;

import it.polimi.dei.deepse.shopreview.TwitterActivity;

import org.dsol.annotation.Action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SharePriceActions {

	private Context context;

	public void setContext(Context context) {
		this.context = context;
	}
	
	@Action(name = "sharePrice", priority = 2)
	public Intent sharePriceUsingUberSocial(String productName,
			String productPrice) {
		Intent intent = new Intent("com.twidroid.SendTweet");

		intent.putExtra("com.twidroid.extra.MESSAGE",
				getTweet(productName, productPrice));
		intent.setType("application/twitter");
		return intent;
	}

	@Action(name = "sharePrice", priority = 1)
	public Intent sharePriceUsingTwicca(String productName, String productPrice) {
		Uri web_intent_uri = getWebIntentURI(productName, productPrice);

		Intent intent = new Intent(Intent.ACTION_VIEW, web_intent_uri);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setPackage("jp.r246.twicca");

		return intent;
	}

	@Action(name = "sharePrice", priority = 3)
	public Intent shareWithBrowser(String productName, String productPrice) {
//		Uri web_intent_uri = getWebIntentURI(productName, productPrice);
//
		Intent intent = new Intent(context,TwitterActivity.class);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		return intent;
	}
	
	
	//=========== Auxiliar Methods =====================//
	
	private Uri getWebIntentURI(String productName, String productPrice){
		Uri.Builder builder = Uri.parse("https://twitter.com/intent/tweet")
				.buildUpon();
		builder.appendQueryParameter("text",
				getTweet(productName, productPrice)); // WebIntent parameter.
		return builder.build(); 
	}

	private String getTweet(String productName, String productPrice) {
		return "The price for product " + productName + " is " + productPrice;
	}

}
