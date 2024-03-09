package com.cop.utilities.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelReader {

	public List<Map<String, Object>> readExcelRecords(InputStream excelFile, List<String> lst) throws IOException {
		Workbook workbook = new XSSFWorkbook(excelFile);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		DataFormatter dataFormatter = new DataFormatter();
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		try {
			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				Sheet sheet = workbook.getSheetAt(sheetIndex);
				String sheetName = sheet.getSheetName();
				System.out.println("sheetName: " + sheetName);

				Iterator<Row> iterator = sheet.iterator();
				if (iterator.hasNext())
					iterator.next();
				while (iterator.hasNext()) {
					Row currentRow = iterator.next();
					// int lastColumn = currentRow.getLastCellNum();
					Map<String, Object> map = null;
					Boolean isEmptyRow = true;
					map = new HashMap<String, Object>();
					for (int columnIndex = 0; columnIndex < lst.size(); columnIndex++) {
						Cell currentCell = currentRow.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						Object value = null;
						
						
						if (currentCell != null) {
							if (currentCell.getCellTypeEnum() == CellType.STRING) {
								value = dataFormatter.formatCellValue(currentCell);
								if (value != null) {
									value = ((String) value).trim().toUpperCase();
								}
							} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
								value = dataFormatter.formatCellValue(currentCell);
							}else if (currentCell.getCellTypeEnum() == CellType.FORMULA) {
								CellValue cellValue = evaluator.evaluate(currentCell);
								if(cellValue != null) {
									value = cellValue.getNumberValue();
								}
							}
						}
						if (value != null)
							isEmptyRow = false;
						System.out.print(value + " ");
						map.put(lst.get(columnIndex), value);
					}
					System.out.println(" ");
					if (isEmptyRow)
						break;
					result.add(map);

				}

			}

		} catch (

		Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			workbook.close();
		}
		return result;
	}

	public List<String> readExcelHeader(InputStream excelFile) throws IOException {
		Workbook workbook = new XSSFWorkbook(excelFile);
		List<String> lst = new ArrayList<String>();
		try {
			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				Sheet sheet = workbook.getSheetAt(sheetIndex);
				String sheetName = sheet.getSheetName();
				System.out.println("sheetName: " + sheetName);
				Row headerRow = sheet.getRow(0);

				int lastColumn = headerRow.getLastCellNum();

				for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
					Cell currentCell = headerRow.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
					Object value = null;
					if (currentCell != null) {
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							value = currentCell.getStringCellValue();
							if (value != null) {
								value = ((String) value).trim();
							}
						} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							value = currentCell.getNumericCellValue();
						}
					}
					if (value == null)
						break;
					lst.add(value.toString().trim().toUpperCase());
				}

				System.out.println(" ");

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			workbook.close();
		}
		return lst;
	}

}
