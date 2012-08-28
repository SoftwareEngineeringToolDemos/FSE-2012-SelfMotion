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

import it.polimi.dei.deepse.shopreview.domain.OnlinePrice;
import it.polimi.dei.deepse.shopreview.domain.UserLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dsol.planner.api.Fact;
import org.dsol.planner.api.Goal;
import org.selfmotion.OnFinishListener;
import org.selfmotion.SelfMotionActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.SelfMotionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class ShopReviewActivity extends SelfMotionActivity implements
		OnFinishListener {

	private SelfMotionButton findPrices;
	private CheckBox share;
	private TextView main_label;

	public ShopReviewActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.sr_activity);

		main_label = (TextView) findViewById(R.id.main_label);
		share = (CheckBox) findViewById(R.id.share);

		// Hook up button presses to the appropriate event handler.
		findPrices = (SelfMotionButton) findViewById(R.id.find_prices);
		findPrices.setOnClickListener(showPrices);
		findPrices.setOnFinishListener(this);

		((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);

	}

	/**
	 * A call-back for when the user presses the back button.
	 */
	OnClickListener mBackListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};
	/**
	 * A call-back for when the user presses the clear button.
	 */
	OnClickListener showPrices = new OnClickListener() {
		public void onClick(View v) {
			main_label
					.setText("Please wait while we try to get the product info...");
		}
	};

	Goal firstGoal = new Goal();
	Goal secondGoal = new Goal();

	@Override
	public List<Goal> getGoal(View view) {
		if (view == findPrices) {

			firstGoal.clear();
			firstGoal.add(new Fact("price(productPrice)"));
			firstGoal.add(new Fact("listOfOnlinePrices(onlinePrices)"));
			firstGoal.add(new Fact("position(gpsPosition)"));

			secondGoal.clear();
			secondGoal.add(new Fact("price(productPrice)"));
			secondGoal.add(new Fact("listOfOnlinePrices(onlinePrices)"));
			secondGoal.add(new Fact("position(userDefinedPosition)"));

			if (share.isChecked()) {
				firstGoal.add(new Fact("sharedPrice"));
				secondGoal.add(new Fact("sharedPrice"));
			}

			List<Goal> goal = new ArrayList<Goal>();
			goal.add(firstGoal);
			goal.add(secondGoal);
			return goal;
		}
		return null;
	}

	@Override
	public void onSuccess(View view, Goal goal, Map<String, Object> executionData) {
		CharSequence result = "";
		if(view == findPrices){
			UserLocation position = null;
			if (goal.equals(firstGoal)) {
				position = (UserLocation) executionData.get("gpsPosition");
			} else if (goal.equals(secondGoal)) {
				position = (UserLocation) executionData.get("userDefinedPosition");
			}

			result = Html.fromHtml(createSummary(position,
					(String) executionData.get("productBarcode"),
					(String) executionData.get("name"),
					(String) executionData.get("productPrice"),
					(List<OnlinePrice>) executionData.get("onlinePrices")));		
		}
		main_label.setText(result);
	}

	public String createSummary(UserLocation position, String productBarcode,
			String productName, String productPrice,
			List<OnlinePrice> listOfOnlinePrices) {

		StringBuffer text = new StringBuffer("<b>PRODUCT</b><br/><br/>");
		text.append("<b> - UPC: </b>").append(productBarcode).append("<br/>");
		text.append("<b> - Name: </b>").append(productName).append("<br/>");
		text.append("<b> - Price: </b>").append(productPrice).append("<br/>");

		if (listOfOnlinePrices != null) {
			text.append("<b> - Online stores: </b>").append(
					listOfOnlinePrices.size());
			OnlinePrice bestPrice = getBestPrice(listOfOnlinePrices);
			if (bestPrice != null) {
				text.append("<br/>");
				text.append("<b> - Best online price: </b>")
						.append(bestPrice.getStore()).append(" ")
						.append(bestPrice.getPrice()).append(" (")
						.append(bestPrice.getCurrency()).append(")");
			}
			text.append("<br/>");
			text.append("<b> - Your location: </b>").append(position);
		}

		return text.toString();
	}

	private OnlinePrice getBestPrice(List<OnlinePrice> listOfOnlinePrices) {
		if (listOfOnlinePrices.size() == 0) {
			return null;
		}

		OnlinePrice min = new OnlinePrice("", Double.MAX_VALUE, "");

		for (OnlinePrice price : listOfOnlinePrices) {
			if (price.getPrice() < min.getPrice()) {
				min = price;
			}
		}

		return min;
	}

	@Override
	public void onError(View view) {
		Toast.makeText(this, "Operation cancelled", 5).show();
		main_label.setText(getResources().getString(R.string.main_label));
	}

}
