package org.selfmotion;

import java.util.List;

import org.dsol.planner.api.Goal;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public abstract class SelfMotionActivity extends Activity {
	
	//Used to manage the case when one activity that does not return a result is called
	private boolean waitingForActivity=false;
	@Override
	protected void onResume() {
		super.onResume();
		if(waitingForActivity && SelfMotion.isRunning()){
			waitingForActivity=false;
			SelfMotion.resume();			
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(SelfMotion.isRunning()){
			waitingForActivity=true; 
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		waitingForActivity=false; 
		if(SelfMotion.isManagedRequest(requestCode)){
			SelfMotion.callback(requestCode, resultCode, data);			
		}
	}
	
	public abstract List<Goal> getGoal(View view);
	
	
}
