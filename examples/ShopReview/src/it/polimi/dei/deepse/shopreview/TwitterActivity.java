package it.polimi.dei.deepse.shopreview;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TwitterActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new HelloWebViewClient());
		
		setContentView(webview);
		
		webview.loadUrl("https://twitter.com/intent/tweet?text=test");

	}
	
	private class HelloWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	setResult(RESULT_OK);	    	
	        finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
