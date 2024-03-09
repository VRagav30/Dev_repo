package com.cop.model.converter;

import java.math.BigDecimal;

import com.creditdatamw.zerocell.converter.Converter;

public class BigDecimalConverter implements Converter<BigDecimal> {
	@Override
	public BigDecimal convert(String value, String columnName, int row) {
		System.out.println("InsideConverter" + value);
		System.out.println("String value of bigdecimal is :"+value);
		return new BigDecimal(value);
		
	}
}