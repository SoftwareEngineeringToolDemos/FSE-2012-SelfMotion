package android.view;

import java.util.List;
import java.util.Map;

import org.dsol.planner.api.Goal;
import org.selfmotion.OnFinishListener;
import org.selfmotion.SelfMotion;
import org.selfmotion.SelfMotionActivity;
import org.selfmotion.SelfMotionApplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelfMotionButton extends Button implements OnFinishListener, OnClickListener{

	public SelfMotionButton(Context context) {
		super(context);
	}

	public SelfMotionButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SelfMotionButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

		public List<Goal> getGoal() {
		SelfMotionActivity selfMotionActivity = (SelfMotionActivity)getContext();
		return selfMotionActivity.getGoal(this);
	}
	
	public String getInitialState(){
		SelfMotionActivity selfMotionActivity = (SelfMotionActivity)getContext();
		return ((SelfMotionApplication)selfMotionActivity.getApplication()).getInitialState();
	}

//	@Override
//	public boolean performClick() {
//		boolean result = super.performClick();
//		if(result){
//	
//			return true;
//		}
//		return false;
//	}
	
	private OnFinishListener onFinishListener;
	private OnClickListener onClickListener;
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.onClickListener = l;
		super.setOnClickListener(this);
	}
	
	public void setOnFinishListener(OnFinishListener l) {
		this.onFinishListener = l;
	}
	
	@Override
	public void onSuccess(View view, Goal goal, Map<String, Object> executionData) {
		if(onFinishListener != null){
			onFinishListener.onSuccess(this, goal, executionData);					
		}
	}

	@Override
	public void onError(View view) {
		if(onFinishListener != null){
			onFinishListener.onError(this);					
		}
	}

	@Override
	public void onClick(View v) {
		if(onClickListener != null){
			onClickListener.onClick(v);
		}
		List<Goal> goal = getGoal();
		if (goal != null) {
			SelfMotion.setContext(getContext());
			SelfMotion.execute(getInitialState(), goal, SelfMotionButton.this);
		}
	}

}
