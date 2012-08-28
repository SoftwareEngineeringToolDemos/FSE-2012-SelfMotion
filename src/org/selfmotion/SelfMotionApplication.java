package org.selfmotion;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.dsol.planner.api.Planner;
import org.dsol.planner.impl.DefaultPlanner;

import android.app.Application;
import android.content.pm.PackageManager;
import android.location.LocationManager;

public abstract class SelfMotionApplication extends Application{

	private InputStream abstractActions = null;
	DefaultPlanner planner = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//OperatorFactory.multiThread = false;
		int i = this.getResources().getIdentifier("abstract_actions", "raw", this.getPackageName());
		planner = new DefaultPlanner();
		initPlanner(i);
		try {
			SelfMotion.initConcreteActions(getConcreteActions());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract List<Class> getConcreteActions(); 
	
	
	public void initPlanner(int abstract_actions_resource){
		readAbstractActions(abstract_actions_resource);
		planner.setIsMultiThread(false);
		try {
			planner.initialize(	abstractActions, 
								new ByteArrayInputStream(getInitialState().getBytes()),
								new ByteArrayInputStream(Planner.EMPTY_GOAL.getBytes()));
			SelfMotion.setPlanner(planner);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readAbstractActions(int abstractActionsResource){
		abstractActions = this.getResources().openRawResource(abstractActionsResource);
	}
	
	
	public String getInitialState(){
		StringBuffer initialState = new StringBuffer();
		
		//GPS
		if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)){
			initialState.append("hasGPS");
			initialState.append(",");	
			LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
				initialState.append("isGPSEnabled");
			}else{
				initialState.append("~isGPSEnabled");
			}
			
		}
		else{
			initialState.append("~hasGPS");
		}
		initialState.append(",");

		if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			initialState.append("hasCamera");
			initialState.append(",");
			if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)){
				initialState.append("hasCameraWithAutoFocus");
				initialState.append(",");
				initialState.append("~hasCameraWithFixedFocus");
			}
			else {
				initialState.append("~hasCameraWithAutoFocus");
				initialState.append(",");
				initialState.append("hasCameraWithFixedFocus");
			}
		}
		else{
			initialState.append("~hasCamera");
		}
		
		return "start("+initialState.toString()+")";
		
	}



}
