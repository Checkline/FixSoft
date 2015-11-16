package fixsoft;

import java.io.File;
import java.io.IOException;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;



public class ExcelFile {

	protected WritableWorkbook workbook;
	protected WritableSheet sheet;
	protected String fileName;
	protected FixSoft fixsoft;
	
	public ExcelFile(FixSoft fixsoft, String fileName){
		this.fileName = fileName;
		this.fixsoft = fixsoft;
		try {
			workbook = Workbook.createWorkbook(new File(this.fileName + ".xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createSheet(String sheetname){
		sheet = workbook.createSheet(sheetname, workbook.getSheets().length);		
	}
	
	public void addCell(int row, int col, int data){
		Number number = new Number(col, row, data);
		try {
			sheet.addCell(number);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}  		
	}
	
	public void addCell(int row, int col, String data){
		Label label = new Label(col, row, data);
		try {
			sheet.addCell(label);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} 		
	}
	
	public void addCell(int row, int col, double data){
		Number number = new Number(col, row, data);
		try {
			sheet.addCell(number);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}		
	}
	
	public void writeExcel(){
		try {
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}	
	}
	
}
