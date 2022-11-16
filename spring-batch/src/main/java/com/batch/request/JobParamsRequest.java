package com.batch.request;

public class JobParamsRequest {

	private String paramKey;
	
	private String paramValue;

	public String getParamKey() {
		return paramKey;
	}

	
	
	public JobParamsRequest(String paramKey, String paramValue) {
		super();
		this.paramKey = paramKey;
		this.paramValue = paramValue;
	}



	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	
}
