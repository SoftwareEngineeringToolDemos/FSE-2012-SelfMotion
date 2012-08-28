package org.selfmotion;

import java.util.Map;

import org.dsol.planner.api.Goal;

import android.view.View;

public interface OnFinishListener {
	
	public void onSuccess(View view, Goal goal, Map<String, Object> executionData);
	public void onError(View view);

}
