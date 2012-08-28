package org.selfmotion;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dsol.annotation.Action;
import org.dsol.annotation.ReturnValue;
import org.dsol.planner.api.AbstractAction;
import org.dsol.planner.api.Goal;
import org.dsol.planner.api.Plan;
import org.dsol.planner.api.PlanResult;
import org.dsol.planner.api.Planner;
import org.dsol.planner.api.State;
import org.dsol.planner.impl.DefaultPlanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class SelfMotion {

	private static DefaultPlanner planner;
	private static Map<String,List<ConcreteAction>> concreteActions = new HashMap<String,List<ConcreteAction>>();
	private static Map<String,ActionCallback> callbackMethods = new HashMap<String,ActionCallback>();

	private static Set<Integer> managedRequests = new HashSet<Integer>();
	private static Map<String,Object> data = new HashMap<String, Object>();
	
	private static Plan currentPlan = null;
	private static int currentLevel;
	private static Context currentContext = null;
	private static List<AbstractAction> currentActions;
	private static OnFinishListener onFinishListener;

	private static boolean isRunning;
	public static String lastActivity;

	private static AbstractAction currentAbstractAction;
	private static int indexOfLastConcreteActionTried = 0;
	private static ConcreteAction lastConcreteActionTried;

	private static State initialState = null;
	private static State currentState = null;
	
	public static void setPlanner(DefaultPlanner planner) {
		SelfMotion.planner = planner;
	}
	
	public static void setContext(Context context){
		currentContext = context;
	}
	
	public static Planner getPlanner() {
		return planner;
	}
	
	private static void reset(){
		planner.reset();
		currentPlan = null;
		currentLevel = 0;
		data.clear();
		managedRequests.clear();
		data.clear();
	}
	
	public static void execute(	String initialStateAsString,
								List<Goal> goal, 
								OnFinishListener onFinishListenerLocal) {
		reset();
		
		planner.setInitialStateAndGoal(new ByteArrayInputStream(initialStateAsString.getBytes()), goal);
		initialState = planner.getInitialState();
		currentState = initialState.clone();
		onFinishListener = onFinishListenerLocal;

		planAndGo();
	}
	
	private static void planAndGo(){
		currentAbstractAction = null;
		planner.setInitialState(currentState);
		List<PlanResult> plans = planner.plan();
		PlanResult planResult = plans.get(0);
		if (planResult.planFound()){
			currentPlan = planResult.getPlan();
			if(currentPlan.size() > 0){
				init();	
			}
		} else {			
			Log.i("self-motion", "Plan not found for goal " + planner.getCurrentGoal());
			if(planner.tryNextGoal()){
				planAndGo();
			}
			else{
				onFinishListener.onError(null);
			}
		}
	}
	
	public static void init(){
		currentLevel = 0;
		isRunning = true;
		currentActions = currentPlan.getLevel(currentLevel);
		continueExecution();
	}
	
	
	public static void continueExecution(){
		if(currentAbstractAction != null){
			currentState.apply(currentAbstractAction.getPostConditions());
		}
		if(currentActions.isEmpty()){
			currentLevel++;
			if(currentLevel == currentPlan.size()){
				if(onFinishListener != null){
					onFinishListener.onSuccess(null, planner.getCurrentGoal(), data);
				}
				isRunning = false;
			}
			else{
				currentActions = currentPlan.getLevel(currentLevel);
				continueExecution();
			}
		}
		else{
			indexOfLastConcreteActionTried = 0;
			currentAbstractAction = currentActions.remove(0);
			executeConcreteAction();
		}
	}

	private static void executeConcreteAction(){
		List<ConcreteAction> availableConcreteActions = getAvailableConcreteActions(currentAbstractAction);
		for(int i = indexOfLastConcreteActionTried; i < availableConcreteActions.size(); i++){
			try {
				lastConcreteActionTried = availableConcreteActions.get(i);
				Class[] paramTypes = lastConcreteActionTried.getParamTypes();
				lastConcreteActionTried.setContext(currentContext);
				Object returnValue = null;
				if(paramTypes.length > 0){
					Object params[] = new Object[paramTypes.length];
					int j = 0;
					for(String paramKey:currentAbstractAction.getParamList()){
						params[j] = SelfMotion.get(paramKey);
						j++;
					}
					returnValue = lastConcreteActionTried.execute(params);
				}
				else{
					returnValue = lastConcreteActionTried.execute();
				}
				//Exception are expensive
				if(returnValue instanceof Result){
					Result result = (Result)returnValue;
					if(!result.wasSuccessful()){
						continue;//try next concrete action
					}
					returnValue = result.getReturnValue();
				}
				
				if(returnValue instanceof Class){
					returnValue = new Intent(currentContext,(Class)returnValue);
				}
				lastActivity ="NO_ACTIVITY";
				if(returnValue instanceof Intent){//Asynchronous call
					lastActivity = currentAbstractAction.getName();
					Integer requestCode = generateRequestCode(lastActivity);
					managedRequests.add(requestCode);
					Intent intent = (Intent)returnValue;
					if(!isIntentAvailable(intent)){
						continue;
					}					
					String intentAction = intent.getAction();
					if(intentAction == null || !intentAction.equals(Intent.ACTION_VIEW)){
						((Activity)currentContext).startActivityForResult(intent, requestCode);
					}
					else{
						((Activity)currentContext).startActivity(intent);
					}
				}
				else{//Synchronous call
					addReturnValueToExecutionData(lastConcreteActionTried.getMethod(), returnValue);
					continueExecution();					
				}			
				return;
			} catch (Exception e) {
				e.printStackTrace();
				//Try next one
			}
			indexOfLastConcreteActionTried++;
		}
		fail();
	}
	
	public static boolean isIntentAvailable(Intent intent) {
		final PackageManager packageManager = currentContext
				.getPackageManager();
		List resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfo!= null && resolveInfo.size() > 0) {
			return true;
		}
		return false;
	}
	
	private static void addReturnValueToExecutionData(Method method, Object returnValue){
		if(returnValue == null){
			return;
		}
		String returnValueKey[] = getReturnValueKey(method);
		if (returnValueKey != null) {
			if (returnValue.getClass().isArray()
					&& (Array.getLength(returnValue) == returnValueKey.length)) {

				Object[] returnValues = (Object[]) returnValue;
				for (int i = 0; i < returnValues.length; i++) {
					data.put(returnValueKey[i], returnValues[i]);
				}
			} else {
				data.put(returnValueKey[0], returnValue);
			}

		}
	}
	
	private static String[] getReturnValueKey(Method method) {
		String returnValueKey[] = null;
		if (method.isAnnotationPresent(ReturnValue.class)) {
			// The key used is based on the value of the annotation @ReturnValue
			ReturnValue returnValueAnnotation = method
					.getAnnotation(ReturnValue.class);
			returnValueKey = returnValueAnnotation.value();
		}
		return returnValueKey;
	}
	
	public static void fail(){
		planner.removeOperation(currentAbstractAction);
		planAndGo();
	}

	private static List<ConcreteAction> getAvailableConcreteActions(AbstractAction action) {
		String key = action.getName();
		if(!concreteActions.containsKey(key)){
			return new ArrayList<ConcreteAction>();
		}
		return concreteActions.get(key);
	}

	public static void callback(int requestCode, int responseCode, Intent intent) {
		
		String callbackId = generateCallbackId(lastConcreteActionTried.getName());
		ActionCallback actionCallback = callbackMethods.get(callbackId);
		
		boolean moveTonextStep = true;
		if(actionCallback != null){
			managedRequests.remove(requestCode);
			try {
				Object returnValue = actionCallback.execute(intent, responseCode);
				boolean successfulExecution = true;
				if(returnValue instanceof Result){
					Result result = (Result)returnValue;
					successfulExecution = result.wasSuccessful();
					returnValue = result.getReturnValue();						
				}
				if(successfulExecution){
					addReturnValueToExecutionData(actionCallback.getMethod(), returnValue);
				}
				moveTonextStep = successfulExecution;
			} catch (Exception e) {
				e.printStackTrace();
				moveTonextStep = false;
			}
		}
		else{
			Log.i("self-motion", "No callback found for response_code:"+responseCode+" Abstract action:"+currentAbstractAction.toString()+" Concrete Action"+lastConcreteActionTried.getName());
		}
		if(!moveTonextStep){
			indexOfLastConcreteActionTried++;
			executeConcreteAction();
		}
		else{
			continueExecution();
		}
	}
	
	public static void resume() {
		continueExecution();
	}
	
	public static void initConcreteActions(List<Class> classesWithConcreteActions) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for(Class clazz:classesWithConcreteActions){
			Object classObject = clazz.getConstructor().newInstance();
			for(Method method : clazz.getDeclaredMethods()){
				if(method.isAnnotationPresent(Action.class)){
					Action action = method.getAnnotation(Action.class);
					String name = action.name();
					List<ConcreteAction> actions = concreteActions.get(name);
					if(actions == null){
						actions = new ArrayList<ConcreteAction>();
						concreteActions.put(name, actions);
					}
					actions.add(new ConcreteAction(method, classObject,action.priority()));
				}
				else if(method.isAnnotationPresent(Callback.class)){
					Callback callback = method.getAnnotation(Callback.class);
					String concreteAction = callback.concreteAction();
					String id = generateCallbackId(concreteAction);
					callbackMethods.put(id, new ActionCallback(method, classObject));
				}
			}
		}
		
		sortConcreteActionsByPriority();
		
	}

	private static void sortConcreteActionsByPriority(){
		Set<String> keys = SelfMotion.concreteActions.keySet();
		ConcreteActionComparator concreteActionComparator = new ConcreteActionComparator();
		for(String key:keys){
			List<ConcreteAction> concreteActions = SelfMotion.concreteActions.get(key);
			Collections.sort(concreteActions,concreteActionComparator);
		}
	
	}
	
	private static String generateCallbackId(String concreteAction){
		return concreteAction;
	}
	
	public static void put(String key, Object value){
		data.put(key, value);
	}
	
	public static Object get(String key){
		return data.get(key);
	}
	
	public static boolean contains(String key){
		return data.containsKey(key);
	}

	public static boolean isManagedRequest(int requestCode) {
		return managedRequests.contains(requestCode);
	}

	public static boolean isRunning() {
		return isRunning;
	}
	
	
	
	private static int generateRequestCode(String actionName){
		int requestCode = actionName.hashCode();
		/*
		 * From the javadocs: Launch an activity for which you would like a
		 * result when it finished. When this activity exits, your
		 * onActivityResult() method will be called with the given requestCode.
		 * Using a negative requestCode is the same as calling
		 * startActivity(Intent) (the activity is not launched as a
		 * sub-activity).
		 * 
		 * AND WE DONT WANT THIS!
		 */
		if(requestCode < 0){
			requestCode = Math.abs(requestCode);
		}
		
		return requestCode;
	}

	static class ConcreteActionComparator implements Comparator<ConcreteAction>{

		@Override
		public int compare(ConcreteAction lhs, ConcreteAction rhs) {
			if(lhs.getPriority() >= rhs.getPriority()){
				return 1;
			}
			return -1;
		}
		
	}
	
}
