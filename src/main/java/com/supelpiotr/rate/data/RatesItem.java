package com.supelpiotr.rate.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RatesItem{

	@JsonProperty("no")
	private String no;

	@JsonProperty("mid")
	private double mid;

	@JsonProperty("effectiveDate")
	private String effectiveDate;

	public String getNo(){
		return no;
	}

	public double getMid(){
		return mid;
	}

	public String getEffectiveDate(){
		return effectiveDate;
	}
}