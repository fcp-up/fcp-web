package com.defu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

public interface IExcelApi {
	/**
	 * 读取一个Excel文件
	 * 
	 * @param f
	 *			文件
	 * @return 一个Excel表格数据列表
	 * @throws IOException
	 *			 文件读取IO异常
	 * @throws FileNotFoundException
	 *			 文件不存在
	 */
	public List<SheetData> read(InputStream f) throws IOException,
			FileNotFoundException;

	/**
	 * 读取一个Excel文件指定下标的数据表格
	 * 
	 * @param f
	 *			文件
	 * @param index
	 *			指定下标
	 * @return 一个数组列表
	 * @throws IOException
	 *			 文件读取IO异常
	 * @throws FileNotFoundException
	 *			 文件不存在
	 */
	public List<Object[]> read(InputStream f, int index) throws IOException,
			FileNotFoundException;

	/**
	 * 将一个二维数组导入到一个excel文件并生存一个新文件，原excel文件不变
	 * 
	 * @param f
	 *			原文件
	 * @param nfp
	 *			新文件路径
	 * @param fn
	 *			新文件名
	 * @param xlsData
	 *			表数据
	 */
	public void export(File f, String nfp, String fn, SheetData xlsData);

	/**
	 * 
	 * 将一个二维数组导入到一个excel文件，并写入输出流，不生成新文件，原excel文件不变
	 * 
	 * @param f
	 *			原文件
	 * @param os
	 *			输出流
	 * @param xlsData
	 *			表数据
	 */
	public void export(File f, OutputStream os, SheetData xlsData);

	/**
	 * 
	 * 将一个ExcelData导入到一个excel文件，并写入输出流，不生成新文件，原excel文件不变
	 * 
	 * @param f
	 *			原文件
	 * @param os
	 *			输出流
	 * @param xlsData
	 *			excel数据
	 */
	public void export(File f, OutputStream os, ExcelData xlsData);

	/**
	 * 填充一个excel poi对象
	 * @param book excel poi对象
	 * @param sheetIndex 填充表下标
	 * @param data 填充数据
	 */
	public void push(Workbook book, int sheetIndex, List<Object[]> data);
	
	public Workbook readToPoi(InputStream f) throws IOException, FileNotFoundException;
	
	/**
	 * 填充一个单元格
	 * @param book excel文档poi对象
	 * @param sheetIndex 单元格所属表下标
	 * @param rowIndex 单元格所属行下标
	 * @param cellIndex 单元格所属列下标
	 * @param v 单元格值
	 */
	public void fill(Workbook book, int sheetIndex, int rowIndex, int cellIndex, String v);
	
	/**
	 * 将一个excel poi对象写入输出流
	 * @param wb excel poi对象
	 * @param os 输出流
	 * @throws Exception
	 */
	public void writeExcel(Workbook wb, OutputStream os) throws Exception;
	
	/**
	 * 将一个excel poi对象写入到指定文件
	 * @param wb excel poi对象
	 * @param file 指定文件
	 * @throws Exception
	 */
	public void writeExcel(Workbook wb, File file) throws Exception;
	
	/**
	 * 将一个excel poi对象写入到指定文件
	 * @param wb excel poi对象
	 * @param filePath 指定文件路径
	 * @throws Exception
	 */
	public void writeExcel(Workbook wb, String filePath) throws Exception;
	
	/**
	 * 简单Excel表格数据，将一个Excel表格当作一个二维数组处理
	 * 
	 * @author likeajin
	 *
	 */
	public static class SheetData {
		/**
		 * 表名
		 */
		private String name;
		/**
		 * 表下标
		 */
		private int index;
		/**
		 * 表数据
		 */
		private List<Object[]> data;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public List<Object[]> getData() {
			return data;
		}

		public void setData(List<Object[]> data) {
			this.data = data;
		}

	}

	
	public static class ExcelData {
		private List<SheetData> sheetDatas;

		public List<SheetData> getSheetDatas() {
			return sheetDatas;
		}

		public void setSheetDatas(List<SheetData> sheetDatas) {
			this.sheetDatas = sheetDatas;
		}
		
	}
	
	
	/**
	 * 列，下标为列在表属性columns中对应的下标
	 * @author Administrator
	 *
	 */
	public static class Column {
		/**
		 * 列头
		 */
		private String header;
		/**
		 * 列宽
		 */
		private int width;

		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

	}

}
