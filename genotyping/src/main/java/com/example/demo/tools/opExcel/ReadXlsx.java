package com.example.demo.tools.opExcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;

public class ReadXlsx {
    public static void readXlsx(String tempFileName, ArrayList<String> args) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("src/main/webapp/"+tempFileName+"/primerOutPutPath/"+"user_"+tempFileName+".xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow firstRow = sheet.getRow(1);
        for(Cell cell : firstRow){
            cell.setCellType(CellType.STRING);
            String value = cell.getStringCellValue();
            args.add(value);
        }
        workbook.close();
    }
}
