package com.defu.util.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.defu.util.IPdfApi;

public class PdfApi implements IPdfApi {

	private BaseFont chineseBaseFont;

	public BaseFont getChineseBaseFont() throws Exception, IOException {
		return chineseBaseFont;
	}
	
	public void setChineseBaseFont(BaseFont font) {
		chineseBaseFont = font;
	}

	public void export(File tpl, File tag, Map<String, Object> datas) throws Exception {
		export(tpl, tag, datas, -1);
	}

	public void export(File tpl, File tag, Map<String, Object> datas, int maxRowNum) throws Exception {
		if (tag.exists()) {
			tag.delete();
		} else {
			tag.getParentFile().mkdirs();
		}
		tag.createNewFile();
		export(new FileInputStream(tpl), new FileOutputStream(tag), datas, maxRowNum);
	}

	public void export(FileInputStream tpl, FileOutputStream tag, Map<String, Object> datas, int maxRows)
			throws Exception {
		if (maxRows <= 0) {
			maxRows = Integer.MAX_VALUE;
		}
		XWPFDocument doc = new XWPFDocument(tpl);
		Document pdfdoc = new Document(PageSize.A4);
		FileOutputStream pdfFile = tag;
		PdfWriter.getInstance(pdfdoc, pdfFile);
		pdfdoc.open();
		List<IBodyElement> els = doc.getBodyElements();
		for (IBodyElement el : els) {
			if (el instanceof XWPFTable) {
				readTable((XWPFTable) el, datas, pdfdoc, maxRows);
			} else if (el instanceof XWPFParagraph) {
				readText((XWPFParagraph) el, datas, pdfdoc);
			} else {
			}
		}
		pdfdoc.close();
	}

	private void readText(XWPFParagraph el, Map<String, Object> datas, Document pdfdoc)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		int fontSize = 12;
		for (XWPFRun run : el.getRuns()) {
			String runTxt = run.getText(0);
			int runfontSize = run.getFontSize();
			if (runfontSize > 0 && runfontSize > fontSize) {
				fontSize = run.getFontSize();
			}
			sb.append(runTxt);
		}

		String text = regTest(sb.toString(), datas);

		Font fontChinese = new Font(getChineseBaseFont(), fontSize, Font.NORMAL);
		Paragraph par = new Paragraph(text, fontChinese);
		int alignment = el.getAlignment().getValue();
		if (alignment == ParagraphAlignment.LEFT.getValue()) {
			par.setAlignment(Element.ALIGN_LEFT);
		} else if (alignment == ParagraphAlignment.RIGHT.getValue()) {
			par.setAlignment(Element.ALIGN_RIGHT);
		} else if (alignment == ParagraphAlignment.CENTER.getValue()) {
			par.setAlignment(Element.ALIGN_CENTER);
		}
		pdfdoc.add(par);
	}

	private void readTable(XWPFTable table, Map<String, Object> datas, Document pdfdoc, int maxRows)
			throws Exception {
		XWPFTableCell fristCell = table.getRow(0).getCell(0);
		String fristCellText = WordApi.getSimpCellText(fristCell);
		String infoStr = WordApi.regTableLebel(fristCellText);
		int dataStartIndex = 0;
		int autoRowStart = 0;
		int autoRowCount = 0;
		boolean autoTable = false;
		String tableInfo = null;
		if (infoStr != null) {
			String[] infos = infoStr.split(",");
			if (infos.length == 4) {
				tableInfo = infos[0];
				try {
					dataStartIndex = Integer.valueOf(infos[1]);
				} catch (Exception e) {
				}
				try {
					autoRowStart = Integer.valueOf(infos[2]);
				} catch (Exception e) {
				}
				try {
					autoRowCount = Integer.valueOf(infos[3]);
				} catch (Exception e) {
				}
				Object tableDataObj = WordApi.getValue(tableInfo, datas);
				if (!(tableDataObj instanceof List<?>)) {
					return;
				}
			}
			autoTable = true;
		}
		//获取已有表格信息
		int colCnt = 0;
		int rowCnt = 0;
		int[] colWidths = null;
		for (XWPFTableRow row : table.getRows()) {
			List<XWPFTableCell> cells = row.getTableCells();
			int col = cells.size();
			if (col > colCnt) {
				colCnt = col;
				colWidths = new int[cells.size()];
				for (int i = 0; i < cells.size(); i++) {
					colWidths[i] = (int) cells.get(i).getCTTc().getTcPr().getTcW().getW().longValue();
				}
			}
			rowCnt++;
		}
		ArrayList<CellMod[]> cellMods = new ArrayList<>();
		copyHasTableCell(table, cellMods, colCnt);
		if (autoTable) {
			Object tableDataObj = WordApi.getValue(tableInfo, datas);
			if (tableDataObj instanceof List<?>) {
				String fristCellNewText = WordApi.replace(fristCellText, "#{" + infoStr + "}", "");
				cellMods.get(0)[0].text = fristCellNewText;
				List<?> tableDataList = (List<?>) tableDataObj;
				addAutoTableText(cellMods, colCnt, tableDataList, dataStartIndex, autoRowStart, autoRowCount, maxRows);
			}
		}
		rowCnt = cellMods.size();
		PdfPTable pdfTable = new PdfPTable(colCnt);
		pdfTable.setTotalWidth(pdfdoc.getPageSize().getWidth() * 0.9f);
		pdfTable.setLockedWidth(true);
		if (colWidths != null) {
			pdfTable.setWidths(colWidths);
		}
		pdfTable.setSpacingBefore(12);
		Font fontChinese = new Font(getChineseBaseFont(), 12, Font.NORMAL);

		for (int row = 0; row < rowCnt; row++) {
			for (int col = 0; col < colCnt; col++) {
				CellMod cellmod = cellMods.get(row)[col];
				if (cellmod.hidden) {
					continue;
				}
				String cellText = cellmod.text;
				cellText = regTest(cellText, datas);
				PdfPCell newCell = new PdfPCell(new Phrase(cellText, fontChinese));
				if (cellmod.colspan > -1) {
					newCell.setColspan(cellmod.colspan);
				}
				if (cellmod.rowspan > -1) {
					newCell.setRowspan(cellmod.rowspan);
				}
				pdfTable.addCell(newCell);
			}
		}
		pdfdoc.add(pdfTable);
	}

	@SuppressWarnings("unchecked")
	private void addAutoTableText(ArrayList<CellMod[]> cellMods, int colCnt, List<?> tableDataList,
			int dataStartIndex, int autoRowStart, int autoRowCount, int maxRows) {
		int tableDataListIndex = dataStartIndex;
		int tableRowIndex = autoRowStart;
		boolean end = false;
		while (!end) {
			if (tableDataListIndex < tableDataList.size() - 1 && tableDataListIndex < maxRows) {
				ArrayList<CellMod[]> rows = new ArrayList<CellMod[]>();
				for (int i = 0, x = tableRowIndex; i < autoRowCount; i++, x++) {
					CellMod[] row = cellMods.get(x);
					rows.add(row);
				}
				for (CellMod[] row : rows) {
					CellMod[] newRow = new CellMod[colCnt];
					for (int i = 0; i < newRow.length; i++) {
						CellMod newCellMod = new CellMod();
						newCellMod.copy(row[i]);
						newRow[i] = newCellMod;
					}
					cellMods.add(newRow);
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
				CellMod[] row = cellMods.get(tableRowIndex);
				for (CellMod cell : row) {
					while (true) {
						String cellStr = cell.text;
						String key = WordApi.regTableLebel(cellStr);
						if (key == null) {
							break;
						}
						String value;
						if (cellStr.startsWith("$")) {
							value = String.valueOf(WordApi.getValueNullReturnKey(key, rowData));
						} else {
							Object valueObj = WordApi.getValue(key, rowData);
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
						String cellNewStr = WordApi.replace(cellStr, "#{" + key + '}', value);
						cell.text = cellNewStr;
					}
				}
			}
			tableDataListIndex++;
		}
	}

	private void copyHasTableCell(XWPFTable table, ArrayList<CellMod[]> cellMods, int colCnt) {
		int rowi = 0;
		String colSpanStrTag = "<w:gridSpan w:val=\"";
		String rowSpanStrTag = "<w:vMerge w:val=\"restart\"/>";
		String rowSpanCtnStrTag = "<w:vMerge/>";
		for (XWPFTableRow row : table.getRows()) {
			int coli = 0;
			for (XWPFTableCell cell : row.getTableCells()) {
				CellMod cellmod = new CellMod();
				CTTc cttc = cell.getCTTc();
				int addOtherCellCount = 0;
				cellmod.text = WordApi.getSimpCellText(cell);
				if (cttc != null) {
					CTTcPr tcpr = cttc.getTcPr();
					String tcprStr = tcpr.toString();
					int colSpanStartIndex = tcprStr.indexOf(colSpanStrTag);
					if (colSpanStartIndex >= 0) {
						int colSpanEndIndex = tcprStr.indexOf("\"", colSpanStartIndex + colSpanStrTag.length());
						String colSpanStr = tcprStr.substring(colSpanStartIndex + colSpanStrTag.length(),
								colSpanEndIndex);
						cellmod.colspan = Integer.valueOf(colSpanStr);
						addOtherCellCount = cellmod.colspan - 1;
					}
					int rowSpanStartIndex = tcprStr.indexOf(rowSpanStrTag);
					if (rowSpanStartIndex >= 0) {
						cellmod.rowspan = 1;
					}
					int rowSpanCtnStartIndex = tcprStr.indexOf(rowSpanCtnStrTag);
					if (rowSpanCtnStartIndex >= 0) {
						cellmod.hidden = true;
						for (int i = rowi - 1; i >= 0; i--) {
							CellMod checkCellMod = cellMods.get(i)[coli];
							if (checkCellMod.rowspan >= 0) {
								checkCellMod.rowspan += 1;
							}
						}
					}
				}
				CellMod[] rowCellmods;
				if (rowi >= cellMods.size()) {
					rowCellmods = null;
				} else {
					rowCellmods = cellMods.get(rowi);
				}
				if (rowCellmods == null) {
					rowCellmods = new CellMod[colCnt];
					cellMods.add(rowCellmods);
				}
				rowCellmods[coli] = cellmod;
				while (addOtherCellCount > 0) {
					rowCellmods[++coli] = new CellMod(true);
					addOtherCellCount--;
				}
				coli++;
			}
			rowi++;
		}
	}

	private String regTest(String text, Map<String, Object> datas) {
		StringBuilder sb = new StringBuilder();
		int isLabel = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '$' && isLabel == 0) {
				isLabel = 1;
			} else if (c == '{' && isLabel == 1) {
				sb.delete(0, sb.length());
				isLabel = 2;
				continue;
			} else if (c == '}' && isLabel == 2) {
				isLabel = 0;
				String fullKey = sb.toString();
				Object result = WordApi.getValue(fullKey, datas);
				String value;
				if (result == null) {
					value = "";
				} else {
					value = String.valueOf(result);
				}
				String label = "${" + fullKey + "}";
				String newText = WordApi.replace(text, label, value);
				int ip = value .length() - label.length();//newText.length() - text.length();
				i += ip;
				text = newText;
			}
			sb.append(c);
		}
		return text;
	}

	class CellMod {
		CellMod() {
			this(false);
		}

		public void copy(CellMod cellMod) {
			hidden = cellMod.hidden;
			rowspan = cellMod.rowspan;
			colspan = cellMod.colspan;
			text = cellMod.text;
		}

		CellMod(boolean hidden) {
			this.hidden = hidden;
		}

		boolean hidden;
		int rowspan = -1;
		int colspan = -1;
		String text = null;
	}

}
