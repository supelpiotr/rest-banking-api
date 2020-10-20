package com.supelpiotr.rate.data;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RateDTO{

	@JsonProperty("code")
	private String code;

	@JsonProperty("rates")
	private List<RatesItem> rates;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("table")
	private String table;

	public String getCode(){
		return code;
	}

	public List<RatesItem> getRates(){
		return rates;
	}

	public String getCurrency(){
		return currency;
	}

	public String getTable(){
		return table;
	}
}