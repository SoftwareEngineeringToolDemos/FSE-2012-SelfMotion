package it.polimi.dei.deepse.shopreview;

import it.polimi.dei.deepse.shopreview.actions.BarcodeActions;
import it.polimi.dei.deepse.shopreview.actions.LocationActions;
import it.polimi.dei.deepse.shopreview.actions.ProductActions;
import it.polimi.dei.deepse.shopreview.actions.SharePriceActions;
import it.polimi.dei.deepse.shopreview.actions.ShopReviewActions;

import java.util.ArrayList;
import java.util.List;

import org.selfmotion.SelfMotionApplication;

public class ShopReviewApplication extends SelfMotionApplication{

	@Override
	public List<Class> getConcreteActions() {
		List<Class> classes = new ArrayList<Class>();
		classes.add(ShopReviewActions.class);
		classes.add(LocationActions.class);
		classes.add(BarcodeActions.class);
		classes.add(ProductActions.class);
		classes.add(SharePriceActions.class);
		return classes;
	}
}
