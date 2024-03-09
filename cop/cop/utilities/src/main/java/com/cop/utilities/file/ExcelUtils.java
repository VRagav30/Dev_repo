package com.cop.utilities.file;

import java.io.File;
import java.util.List;

import com.creditdatamw.zerocell.Reader;

public class ExcelUtils {

	public static <S> List<S> readFile(S s, File file) {
		return Reader.of(s.getClass()).from(file).skipHeaderRow(true).skipEmptyRows(true).list();
	}

}
