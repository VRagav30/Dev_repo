package com.cop.model.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.creditdatamw.zerocell.converter.Converter;

public class DateConverter implements Converter<Date> {

	@Override
	public Date convert(String value, String columnName, int row) {
		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(value);
			System.out.println("InsideConverter" + date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
}