package com.defu.util.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

import com.defu.util.IWordApi;

public class WordApi implements IWordApi {

	/* (non-Javadoc)
	 * @see com.defu.util.impl.IWordApi#export(java.io.File, java.io.File, java.util.Map)
	 */
	@Override
	public void export(File tpl, File tag, Map<String, Object> datas) throws Exception {
		export(tpl, tag, datas, -1);
	}

	/* (non-Javadoc)
	 * @see com.defu.util.impl.IWordApi#export(java.io.File, java.io.File, java.util.Map)
	 */
	@Override
	public void export(File tpl, File tag, Map<String, Object> datas, int maxRows) throws Exception {
		if (tag.exists()) {
			tag.delete();
		} else {
			tag.getParentFile().mkdirs();
		}
		tag.createNewFile();
		export(new FileInputStream(tpl), new FileOutputStream(tag), datas, maxRows);
	}

	public void export(InputStream tpl, OutputStream tag, Map<String, Object> datas, int maxRows)
			throws Exception {
		XWPFDocument doc = new XWPFDocument(tpl);
		// 段落替换文本
		List<XWPFParagraph> paragraphs = doc.getParagraphs();
		for (XWPFParagraph paragraph : paragraphs) {
			String str = paragraph.getText();
			regLabel(str, datas, paragraph);
		}
		List<XWPFTable> tables = doc.getTables();
		// 构建自增表格
		for (XWPFTable table : tables) {
			creatAutoRows(table, datas, maxRows);
		}
		// 表格替换文本
		for (XWPFTable table : tables) {
			List<XWPFTableRow> rows = table.getRows();
			for (XWPFTableRow row : rows) {
				List<XWPFTableCell> cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					List<XWPFParagraph> cellParagraphs = cell.getParagraphs();
					for (XWPFParagraph paragraph : cellParagraphs) {
						String str = getSimpParagraphText(paragraph);
						regLabel(str, datas, paragraph);
					}
				}
			}
		}
		doc.write(tag);
		tag.close();
	}

	private String getSimpParagraphText(XWPFParagraph paragraph) {
		StringBuilder sb = new StringBuilder();
		for (XWPFRun run : paragraph.getRuns()) {
			String runTxt = run.getText(0);
			sb.append(runTxt);
		}
		return sb.toString();
	}

	static String getSimpCellText(XWPFTableCell cell) {
		StringBuilder sb = new StringBuilder();
		for (XWPFParagraph par : cell.getParagraphs()) {
			for (XWPFRun run : par.getRuns()) {
				String runTxt = run.getText(0);
				sb.append(runTxt);
			}
		}
		return sb.toString();
	}

	private void setSimpCellText(XWPFTableCell cell, String txt) {
		boolean ok = true;
		List<XWPFParagraph> paragraphs = cell.getParagraphs();
		if (paragraphs.isEmpty()) {
			cell.addParagraph();
		}
		for (XWPFParagraph par : paragraphs) {
			List<XWPFRun> runs = par.getRuns();
			if (runs.isEmpty()) {
				par.createRun();
			}
			for (XWPFRun run : runs) {
				if (ok) {
					run.setText(txt, 0);
					ok = false;
				} else {
					run.setText("", 0);
				}
			}
		}
	}

	private void copyStyle(XWPFTableCell tag, XWPFTableCell src) {
		XWPFParagraph srcp = null;
		if (!src.getParagraphs().isEmpty()) {
			srcp = src.getParagraphs().get(0);
		}
		XWPFRun srcpc = null;
		if (!srcp.getRuns().isEmpty()) {
			srcpc = srcp.getRuns().get(0);
		}
		if (tag.getParagraphs().isEmpty()) {
			tag.addParagraph();
		}
		for (XWPFParagraph par : tag.getParagraphs()) {
			if (srcp != null) {
				par.setVerticalAlignment(srcp.getVerticalAlignment());
				par.setAlignment(srcp.getAlignment());
				if (par.getRuns().isEmpty()) {
					par.createRun();
				}
				for (XWPFRun run : par.getRuns()) {
					if (srcpc != null) {
						run.setColor(srcpc.getColor());
						String fontFamily = srcpc.getFontFamily();
						if (fontFamily != null) {
							run.setFontFamily(fontFamily);
						}
						int fontSize = srcpc.getFontSize();
						if (fontSize > 0) {
							run.setFontSize(srcpc.getFontSize());
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void creatAutoRows(XWPFTable table, Map<String, Object> datas, int maxRows) {
		if (maxRows < 0)
			maxRows = Integer.MAX_VALUE;
		XWPFTableCell fristCell = table.getRow(0).getCell(0);
		String fristCellText = getSimpCellText(fristCell);
		String infoStr = regTableLebel(fristCellText);
		if (infoStr == null) {
			return;
		}
		String[] infos = infoStr.split(",");
		if (infos.length != 4) {
			return;
		}
		String tableInfo = infos[0];
		int dataStartIndex = 0;
		try {
			dataStartIndex = Integer.valueOf(infos[1]);
		} catch (Exception e) {
		}
		int autoRowStart = 0;
		try {
			autoRowStart = Integer.valueOf(infos[2]);
		} catch (Exception e) {
		}
		int autoRowCount = 0;
		try {
			autoRowCount = Integer.valueOf(infos[3]);
		} catch (Exception e) {
		}
		Object tableDataObj = getValue(tableInfo, datas);
		if (!(tableDataObj instanceof List<?>)) {
			return;
		}
		String fristCellNewText = replace(fristCellText, "#{" + infoStr + "}", "");
		setSimpCellText(fristCell, fristCellNewText);
		List<?> tableDataList = (List<?>) tableDataObj;
		int tableDataListIndex = dataStartIndex;
		int tableRowIndex = autoRowStart;
		boolean end = false;
		while (!end) {
			if (tableDataListIndex < tableDataList.size() - 1 && tableDataListIndex + 1 < maxRows) {
				ArrayList<XWPFTableRow> rows = new ArrayList<XWPFTableRow>();
				for (int i = 0, x = tableRowIndex; i < autoRowCount; i++, x++) {
					XWPFTableRow row = table.getRow(x);
					rows.add(row);
				}
				for (XWPFTableRow row : rows) {
					XWPFTableRow newRow = table.insertNewTableRow(table.getRows().size());
					for (int i = 0; i < row.getCtRow().sizeOfTcArray(); i++) {
						newRow.createCell();
					}
					newRow.getCtRow().setTrPr(row.getCtRow().getTrPr());
					List<XWPFTableCell> cells = row.getTableCells();
					List<XWPFTableCell> newCells = newRow.getTableCells();
					for (int j = 0; j < cells.size(); j++) {
						XWPFTableCell cell = cells.get(j);
						XWPFTableCell newCell = newCells.get(j);
						CTTcPr aaa = cell.getCTTc().getTcPr();
						newCell.getCTTc().setTcPr(aaa);
						String celltxt = getSimpCellText(cell);
						setSimpCellText(newCell, celltxt);
						copyStyle(newCell, cell);
					}
				}
			} else {
				end = true;
			}
			for (int i = 0; i < autoRowCount; i++, tableRowIndex++) {
				Map<String, Object> rowData;
				if (tableDataListIndex >= tableDataList.size()) {
					rowData = new HashMap<>();
				} else {
					rowData = (Map<String, Object>) tableDataList.get(tableDataListIndex);
				}
				XWPFTableRow row = table.getRow(tableRowIndex);
				for (XWPFTableCell cell : row.getTableCells()) {
					while (true) {
						String cellStr = getSimpCellText(cell);
						String key = regTableLebel(cellStr);
						if (key == null) {
							break;
						}
						String value;
						if (cellStr.startsWith("$")) {
							value = String.valueOf(getValueNullReturnKey(key, rowData));
						} else {
							Object valueObj = getValue(key, rowData);
							if (valueObj == null) {
								if ("listIndex".equals(key)) {
									value = String.valueOf(tableDataListIndex + 1);
								} else {
									value = "";
								}
							} else {
								value = String.valueOf(valueObj);
							}
						}
						String cellNewStr = replace(cellStr, "#{" + key + '}', value);
						setSimpCellText(cell, cellNewStr);
					}
				}
			}
			tableDataListIndex++;
		}
	}

	static String regTableLebel(String str) {
		if(str == null){
			return null;
		}
		int isLabel = 0;
		boolean ignoreNext = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\\') {
				ignoreNext = true;
				continue;
			}
			if (ignoreNext == true) {
				sb.append(c);
				continue;
			}
			if (c == '#' && isLabel == 0) {
				int next = i + 1;
				if (next < str.length() && str.charAt(next) == '{')
					isLabel = 1;
			} else if (c == '{') {
				if (isLabel == 1) {
					sb.delete(0, sb.length());
					isLabel = 2;
					continue;
				}
			} else if (c == '}' && isLabel == 2) {
				isLabel = 0;
				return sb.toString();
			}
			sb.append(c);
		}
		return null;
	}

	private void regLabel(String str, Map<String, Object> datas, XWPFParagraph paragraph) {
		int isLabel = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '$' && isLabel == 0) {
				isLabel = 1;
			} else if (c == '{' && isLabel == 1) {
				sb.delete(0, sb.length());
				isLabel = 2;
				continue;
			} else if (c == '}' && isLabel == 2) {
				isLabel = 0;
				String fullKey = sb.toString();
				Object result = getValue(fullKey, datas);
				String value;
				if (result == null) {
					value = "";
				} else {
					value = String.valueOf(result);
				}
				String label = "${" + fullKey + "}";
				replace(label, value, paragraph);
			}
			sb.append(c);
		}
	}

	static Object getValueNullReturnKey(String fullKey, Map<String, Object> datas) {
		if (fullKey == null) {
			return null;
		}
		fullKey = getValueBracket(fullKey, datas);
		String[] keys = split(fullKey, '.');
		Object val = datas;
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].trim();
			if (val instanceof Map<?, ?>) {
				Object objKet = key;
				Map<?, ?> map = ((Map<?, ?>) val);
				for (Entry<?, ?> ent : map.entrySet()) {
					if (ent.getKey() instanceof Integer) {
						try {
							objKet = Integer.valueOf(key);
						} catch (Exception e) {
						}
					}
					break;
				}
				val = map.get(objKet);
				if (val == null) {
					return fullKey;
				}
				continue;
			}
			if (val instanceof List<?>) {
				try {
					int index = Integer.parseInt(key);
					val = ((List<?>) val).get(index);
					continue;
				} catch (Exception e) {
				}
			}
			return fullKey;
		}
		return val;
	}

	static Object getValue(String fullKey, Map<String, Object> datas) {
		if (fullKey == null) {
			return null;
		}
		fullKey = getValueBracket(fullKey, datas);
		String[] keys = split(fullKey, '.');
		Object val = datas;
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].trim();
			if (val instanceof Map<?, ?>) {
				Object objKet = key;
				Map<?, ?> map = ((Map<?, ?>) val);
				for (Entry<?, ?> ent : map.entrySet()) {
					if (ent.getKey() instanceof Integer) {
						try {
							objKet = Integer.valueOf(key);
						} catch (Exception e) {
						}
					}
					break;
				}
				val = map.get(objKet);
				continue;
			}
			if (val instanceof List<?>) {
				try {
					int index = Integer.parseInt(key);
					val = ((List<?>) val).get(index);
					continue;
				} catch (Exception e) {

				}
			}
			val = null;
		}
		return val;
	}

	private static String[] split(String str, char spc) {
		int x = 0;
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '{') {
				x++;
			} else if (c == '}') {
				x--;
			} else if (c == '.' && x == 0) {
				list.add(sb.toString());
				sb.delete(0, sb.length());
				continue;
			}
			sb.append(c);
		}
		list.add(sb.toString());
		String[] r = new String[list.size()];
		return list.toArray(r);
	}

	private static String getValueBracket(String str, Map<String, Object> datas) {
		int isLabel = 0;
		String newStr = str;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '(') {
				sb.delete(0, sb.length());
				isLabel = 1;
				continue;
			} else if (c == ')' && isLabel == 1) {
				isLabel = 0;
				String braStr = sb.toString();
				String barValue = String.valueOf(getValue(braStr, datas));
				newStr = replace(newStr, '(' + braStr + ')', barValue);
			}
			sb.append(c);
		}
		return newStr;
	}

	static String replace(String str, String s1, String s2) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		int nextIndex = 0;
		while (true) {
			nextIndex = str.indexOf(s1, index);
			if (nextIndex == -1) {
				sb.append(str, index, str.length());
				break;
			}
			sb.append(str, index, nextIndex);
			sb.append(s2);
			index = nextIndex + s1.length();
		}
		return sb.toString();
	}

	private void replace(String label, String value, XWPFParagraph paragraph) {
		StringBuilder sb = new StringBuilder();
		List<XWPFRun> runsList = paragraph.getRuns();
		for (XWPFRun run : runsList) {
			sb.append(run.getText(0));
		}
		String oldValue = sb.toString();
		sb.delete(0, sb.length());
		if (oldValue == null || oldValue.isEmpty() || !oldValue.contains(label)) {
			return;
		}
		String newValue = replace(oldValue, label, value);
		int runSize = runsList.size();
		for (int i = 0; i < runSize; i++) {
			if (i == 0) {
				runsList.get(0).setText(newValue, 0);
			} else {
				runsList.get(i).setText("", 0);
			}
		}
		sb.delete(0, sb.length());
	}

}
