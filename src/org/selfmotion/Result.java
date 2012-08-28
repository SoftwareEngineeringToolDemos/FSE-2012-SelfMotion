package org.selfmotion;

public class Result {
	
	public static Result FAIL = new Result(false);
	
	private boolean successful;
	private Object returnValue;
	
	public Result(boolean successful) {
		super();
		this.successful = successful;
	}
	
	public Result(boolean successful, Object returnValue) {
		super();
		this.successful = successful;
		this.returnValue = returnValue;
	}
	
	public boolean wasSuccessful() {
		return successful;
	}
	
	public Object getReturnValue() {
		return returnValue;
	}
	

}
