package com.cop.model.converter;

import com.creditdatamw.zerocell.converter.Converter;

public class StringConverter implements Converter<String> {
	@Override
	public String convert(String value, String columnName, int row) {
		System.out.println("Inside StringConverter " + value);
		return value.trim().toUpperCase();
	}
}