package com.cop.utilities.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	@SuppressWarnings("resource")
	public static <T> void writeToExcelInMultiSheets(final String fileName, final String sheetName,
			final List<T> data) {
		File file = null;
		OutputStream fos = null;
		XSSFWorkbook workbook = null;
		try {
			file = new File(fileName);
			Sheet sheet = null;
			if (file.exists()) {
				workbook = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
			} else {
				workbook = new XSSFWorkbook();
			}
			sheet = workbook.createSheet(sheetName);
			List<String> fieldNames = getFieldNamesForClass(data.get(0).getClass());
			int rowCount = 0;
			int columnCount = 0;
			Row row = sheet.createRow(rowCount++);
			for (String fieldName : fieldNames) {
				if (fieldName != "serialVersionUID") {
					Cell cell = row.createCell(columnCount++);
					cell.setCellValue(fieldName);
				}
			}
			Class<? extends Object> classz = data.get(0).getClass();
			for (T t : data) {
				row = sheet.createRow(rowCount++);
				columnCount = 0;

				for (String fieldName : fieldNames) {
					if (fieldName != "serialVersionUID") {
						Cell cell = row.createCell(columnCount);
						Method method = null;
						try {
							method = classz.getMethod("get" + capitalize(fieldName));
						} catch (NoSuchMethodException nme) {
							method = classz.getMethod("get" + fieldName);
						}
						Object value = method.invoke(t, (Object[]) null);
						if (value != null) {
							if (value instanceof String) {
								cell.setCellValue((String) value);
							} else if (value instanceof Long) {
								cell.setCellValue((Long) value);
							} else if (value instanceof Integer) {
								cell.setCellValue((Integer) value);
							} else if (value instanceof Double) {
								cell.setCellValue((Double) value);
							} else if (value instanceof BigDecimal) {
								cell.setCellValue(((BigDecimal) value).doubleValue());
							} else if (value instanceof Date) {
								CreationHelper createHelper = workbook.getCreationHelper();
								CellStyle cellStyle = workbook.createCellStyle();
								if (fieldName == "date")
									cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
								else
									cellStyle.setDataFormat(
											createHelper.createDataFormat().getFormat("dd/MM/yyyy hh:mm"));
								cell.setCellValue((Date) (value));
								cell.setCellStyle(cellStyle);
							}
						}
						columnCount++;
					}
				}
			}
			fos = new FileOutputStream(file);
			workbook.write(fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
			}
		}
	}

	// retrieve field names from a POJO class
	private static List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
		List<String> fieldNames = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	// capitalize the first letter of the field name for retriving value of the
	// field later
	private static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
