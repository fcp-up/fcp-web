package com.defu.util.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.defu.util.IExcelApi;

public class ExcelApi implements IExcelApi {

	@Override
	public Workbook readToPoi(InputStream f) throws IOException, FileNotFoundException {
		// TODO Auto-generated method stub
		return new HSSFWorkbook(f);
	}

	@Override
	public void fill(Workbook book, int sheetIndex, int rowIndex, int cellIndex, String v) {
		// TODO Auto-generated method stub
		if(book == null) return;
		Sheet t = book.getSheetAt(sheetIndex);
		if(t == null) {
			int i = book.getNumberOfSheets();
			while(i < sheetIndex) {
				t = book.createSheet();
				i++;
			}
		}
		Row r = t.getRow(rowIndex);
		if(r == null) r = t.createRow(rowIndex);
		Cell c = r.getCell(cellIndex);
		if(c == null) c = r.createCell(cellIndex);
		c.setCellValue(v);
	}
	
	@Override
	public List<SheetData> read(InputStream f) throws IOException, FileNotFoundException {
		// TODO Auto-generated method stub
		Workbook wb = null;
		Sheet es;
		
		int sl;
		wb = new HSSFWorkbook(f);
		
		sl = wb.getNumberOfSheets();
		
		List<SheetData> rs = new ArrayList<SheetData>();
		SheetData d;
		
		for(int i = 0; i < sl; i++) {
			es = wb.getSheetAt(i);
			d = new SheetData();
			d.setIndex(i);
			d.setName(es.getSheetName());
			d.setData(read(es));
			rs.add(d);
		}
		
		return rs;
	}
	
	private List<Object[]> read(Sheet es) {
		CellRangeAddress[] cras = new CellRangeAddress[es.getNumMergedRegions()];
		
		int j, k, rl, cl, ri, ci, cn = 0;
		Row er;
		Cell ec;
		List<Object[]> rs = new ArrayList<Object[]>();
		Object[] r;
		
		for(j = 0, rl = cras.length; j < rl; j++) {
			cras[j] = es.getMergedRegion(j);
		}
		
		rl = es.getLastRowNum();
		if(rl == 0 && es.getRow(0) == null) return rs;
		
		for(j = 0, rl = rl + 1; j < rl; j++) {
			er = es.getRow(j);
			
			if(er == null) {
				rs.add(null);
				continue;
			}
			
			cl = er.getLastCellNum();
			
			if(cl < 0) {
				rs.add(null);
				continue;
			}
			cn = Math.max(cn, cl);
			
			r = new Object[cl];
			
			outer: for(k = er.getFirstCellNum(); k < cl; k++) {
				for(CellRangeAddress cra:cras) {
					ri = cra.getFirstRow();
					ci = cra.getFirstColumn();
					if(j >= ri && j < cra.getLastRow() + 1 && k >= ci && k < cra.getLastColumn() + 1 && (j != ri || k != ci)) {
						if(j == ri) r[k] = r[ci];
						else r[k] = rs.get(ri)[ci];
						continue outer;
					}
				}
				ec = er.getCell(k);
				if(ec == null) r[k] = null;
				else {
					switch(ec.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						r[k] = "";
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						r[k] = Boolean.toString(ec.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_ERROR:
						r[k] = "CELL_ERROR";
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if(DateUtil.isCellDateFormatted(ec)) {
//							Date date = ec.getDateCellValue();
//							format = ec.getCellStyle().getDataFormatString();
//							format = format.replaceAll("/", "-");
//							format = format.replaceAll("[\\\\;@]", "");
							r[k] = ec.getDateCellValue();
							break;
						}
//						else {
//							ec.setCellType(Cell.CELL_TYPE_STRING);
//							r[k] = ec.getStringCellValue();
////							r[k] = ec.getNumericCellValue() + "";
//						}
					case Cell.CELL_TYPE_FORMULA:
					case Cell.CELL_TYPE_STRING:
						try {
							ec.setCellType(Cell.CELL_TYPE_STRING);
							r[k] = ec.getStringCellValue();
						}
						catch(Exception ex) {
							
						}
						break;
					}
				}
			}
			rs.add(r);			
		}
		
		for(j = 0, rl = rs.size(); j < rl; j++) {
			r = rs.get(j);
			if(r == null) {
				rs.set(j, new Object[cn]);
				continue;
			}
			cl = r.length;
			if(cl < cn) {
				Object[] newr = new Object[cn];
				for(k = 0; k < cl; k++) newr[k] = r[k];
				rs.set(j, newr);
			}
		}
		
		return rs;
	}

	@Override
	public List<Object[]> read(InputStream f, int index) throws IOException, FileNotFoundException {
		// TODO Auto-generated method stub
		return read(new HSSFWorkbook(f).getSheetAt(index));
	}

	@Override
	public void export(File f, String nfp, String fn, SheetData xlsData) {
		// TODO Auto-generated method stub		
		try{
			FileUtils.copyFile(f, new File(nfp, fn));
			export(f, new FileOutputStream(new File(nfp + "\\" + fn)), xlsData);
		}
		catch(Exception ex){
		}
	}

	@Override
	public void export(File f, OutputStream os, SheetData xlsData) {
		// TODO Auto-generated method stub		
		try{
			Workbook wb = exportExcel(f, xlsData, null);
			if(wb != null) writeExcel(wb, os);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	
	}

	private Workbook exportExcel(File f, SheetData xlsData, Workbook wb) throws Exception {
		if(xlsData == null || xlsData.getIndex() < 0) return null;
		
		
		if(wb == null) wb = new HSSFWorkbook(new FileInputStream(f));

		int sheetIndex = xlsData.getIndex();
		
		if(sheetIndex == wb.getNumberOfSheets()) {
			if(xlsData.getName() != null) wb.createSheet(xlsData.getName());
		}
		else {
			if(xlsData.getName() != null) wb.setSheetName(sheetIndex, xlsData.getName());
		}
		
		push(wb, sheetIndex, xlsData.getData());
		
		return wb;
	
	}
	
	public void push(Workbook book, int sheetIndex, List<Object[]> data) {
		Sheet t = null;
		
		if(sheetIndex == book.getNumberOfSheets()) {
			t = book.createSheet();
		}
		else {
			t = book.getSheetAt(sheetIndex);
		}

		Row r;
		Cell c;
		CellStyle cs;
		Font df = book.createFont();
		
		Object[] re;

		cs = book.createCellStyle();
		cs.setFont(df);
		cs.setBorderBottom((short)1);
		cs.setBorderLeft((short)1);
		cs.setBorderRight((short)1);
		cs.setBorderTop((short)1);

		int i, il, j, jl, rc;
		
		rc = t.getLastRowNum() + 1;
		
		il = data.size();
		if(il > 65536) {
			il = 65536;
		}
		
		for(i = 0; i < il; i++){
			r = t.createRow(rc++);
			r.setHeightInPoints(16);
			re = data.get(i);
			for(j = 0, jl = re.length; j < jl; j++){
				c = r.createCell(j);
				
				if(re[j] != null) {
//					c.setCellStyle(cs);
					c.setCellValue(re[j].toString());
				}
			}
		}
	}
	
	public void writeExcel(Workbook wb, OutputStream os) throws Exception {
		wb.write(os);
		os.flush();
		os.close();
	}

	public void writeExcel(Workbook wb, File f) throws Exception {
		writeExcel(wb, new FileOutputStream(f));
	}

	public void writeExcel(Workbook wb, String fp) throws Exception {
		writeExcel(wb, new FileOutputStream(new File(fp)));
	}
	
	@Override
	public void export(File f, OutputStream os, ExcelData xlsData) {
		// TODO Auto-generated method stub
		Workbook wb = null;
		for(SheetData sd: xlsData.getSheetDatas()) {
			try{
				wb = exportExcel(f, sd, wb);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(wb != null) try{
			writeExcel(wb, os);
		}
		catch(Exception ex){
			
		}
	}

}
