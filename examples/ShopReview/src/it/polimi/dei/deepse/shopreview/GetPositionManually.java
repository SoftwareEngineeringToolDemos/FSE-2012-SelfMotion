package it.polimi.dei.deepse.shopreview;

import it.polimi.dei.deepse.shopreview.domain.UserLocation;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class GetPositionManually extends MapActivity {

	private GestureDetector gd;
	private MapView mapView;
	private Drawable marker;
	private GeoPoint selectedLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		mapView = (MapView) findViewById(R.id.mapview);

		mapView.setClickable(true);
		mapView.setLongClickable(true);

		if(getIntent().getExtras().containsKey("approximatePosition")){
			Object location = getIntent().getExtras().get("approximatePosition");
			if (location != null) {
				mapView.getController().setCenter(
						((UserLocation) location).toGeoPoint());
				mapView.getController().setZoom(16);
			}	
		}
		
		mapView.setBuiltInZoomControls(true);

		marker = getResources().getDrawable(R.drawable.marker);
		int markerWidth = marker.getIntrinsicWidth();
		int markerHeight = marker.getIntrinsicHeight();
		marker.setBounds(0, markerHeight, markerWidth, 0);

		gd = new GestureDetector(this, new MyListener());
		this.gd.setIsLongpressEnabled(true);
		
		Toast.makeText(this, "Please, select your location manually.", 3).show();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean result = super.dispatchTouchEvent(ev);
		gd.onTouchEvent(ev);
		return result;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void askConfirmation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Confirm you current location?")
				.setCancelable(false)
				.setPositiveButton("Yes!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent();
								UserLocation userLocation = new UserLocation(
										selectedLocation);
								intent.putExtra("location", userLocation);
								setResult(RESULT_OK, intent);
								finish();
							}
						});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		
		//		WindowManager.LayoutParams WMLP = dialog.getWindow().getAttributes();
//
//		
//		WMLP.gravity = Gravity.TOP;
//		//WMLP.x = 100; // x position
//		//WMLP.y = 0; // y position
//
//		dialog.getWindow().setAttributes(WMLP);

		dialog.show();
	}

	class MyListener extends GestureDetector.SimpleOnGestureListener {
		// @Override
		// public void onLongPress(MotionEvent ev) {
		// Projection p = mapView.getProjection();
		// PositionOverlay positionOverlay = new PositionOverlay(marker);
		// mapView.getOverlays().add(positionOverlay);
		// positionOverlay.addItem(p.fromPixels((int) ev.getX(), (int)
		// ev.getY()), "You are here.", "");
		// }

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			mapView.getOverlays().clear();
			Projection p = mapView.getProjection();
			PositionOverlay positionOverlay = new PositionOverlay(marker);
			mapView.getOverlays().add(positionOverlay);
			selectedLocation = p.fromPixels((int) e.getX(), (int) e.getY());
			mapView.getController().setCenter(selectedLocation);			
			positionOverlay.addItem(selectedLocation, "", "");
			
			askConfirmation();
			
			return true;
		}
	}

	class PositionOverlay extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();

		public PositionOverlay(Drawable marker) {
			super(boundCenterBottom(marker));
			populate();
		}

		public void addItem(GeoPoint p, String title, String snippet) {
			OverlayItem newItem = new OverlayItem(p, title, snippet);
			overlayItemList.add(newItem);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return overlayItemList.get(i);
		}

		@Override
		public int size() {
			return overlayItemList.size();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
		}
	}
}
