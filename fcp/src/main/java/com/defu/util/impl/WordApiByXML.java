package com.defu.util.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.defu.util.IWordApi;

public class WordApiByXML implements IWordApi {

	@Override
	public void export(File tpl, File tag, Map<String, Object> datas) throws Exception {
		export(tpl, tag, datas, -1);
	}

	@Override
	public void export(File tpl, File tag, Map<String, Object> datas, int maxRows) throws Exception {
		FileUtils.copyFile(tpl, tag);
		ZipFile zp = new ZipFile(tag);
		File tempFiles = new File(tag.getPath() + ".temp");
		File wordFiles = new File(tempFiles.getPath() + "/word");
		zp.extractFile("word/document.xml", tempFiles.getPath());
		File tagFile = new File(wordFiles.getPath() + "/document.xml");
		SAXReader reader = new SAXReader();
		Document doc = reader.read(tagFile);
		export(doc, datas, maxRows);
		XMLWriter writer = new XMLWriter(new FileOutputStream(tagFile));
		writer.write(doc);
		writer.close();
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		zp.addFolder(wordFiles, parameters);
		FileUtils.deleteQuietly(tempFiles);
	}

	public void export(Document doc, Map<String, Object> datas, int maxRows)
			throws Exception {
		if (maxRows <= 0) {
			maxRows = Integer.MAX_VALUE;
		}
		Element root = doc.getRootElement();
		List<?> body = root.elements("body");
		Iterator<?> it = ((Element) body.get(0)).elementIterator();
		while (it.hasNext()) {
			Object node = it.next();
			if (!(node instanceof Element)) {
				continue;
			}
			Element nodeElement = (Element) node;
			String nodeElementName = nodeElement.getName();
			if ("p".equals(nodeElementName)) {
				replaceTextAtP(nodeElement, datas, "");
			} else if ("tbl".equals(nodeElementName)) {
				TableInfos tableInfo = replaceTextAtTbl(nodeElement, datas, null);
				if (tableInfo != null) {
					autoCreatTbl(nodeElement, datas, tableInfo, maxRows);
				}
				replaceTextAtTbl(nodeElement, datas, "");
			}
		}
	}

	private void autoCreatTbl(Element tblElement, Map<String, Object> datas, TableInfos tableInfo, int maxRows) {
		List<?> trs = tblElement.elements("tr");
		List<Object> autoTrs = new ArrayList<>();
		for (int i = tableInfo.autoRowStart, j = 0; j < tableInfo.autoRowCount; i++, j++) {
			Element tr = (Element) trs.get(i);
			autoTrs.add(tr);
			tblElement.remove(tr);
		}
		int autoRowIndex = 0;
		int tableDataIndex = tableInfo.dataStartIndex;
		while (tableDataIndex < tableInfo.tableDatas.size() && autoRowIndex < maxRows) {
			autoRowIndex++;
			Object currTableData = tableInfo.tableDatas.get(tableDataIndex);
			for (int i = 0; i < autoTrs.size(); i++) {
				Element autoRow = (Element) autoTrs.get(i);
				Element clonedAutoRow = (Element) autoRow.clone();
				clonedAutoRow.setParent(null);
				tblElement.add(clonedAutoRow);
				replaceTextAtTr(currTableData, clonedAutoRow, autoRowIndex, "");
			}
			tableDataIndex++;
		}
	}

	@SuppressWarnings("unchecked")
	static void replaceTextAtTr(Object currTableData, Element trElement, int autoRowIndex, String def) {
		Map<String, Object> datas = null;
		if (!(currTableData instanceof Map)) {
			return;
		}
		datas = (Map<String, Object>) currTableData;
		datas.put("listIndex", autoRowIndex);
		Iterator<?> tcs = trElement.elementIterator("tc");
		while (tcs.hasNext()) {
			Object tc = tcs.next();
			if (!(tc instanceof Element)) {
				continue;
			}
			Element tcElement = (Element) tc;
			Iterator<?> ps = tcElement.elementIterator("p");
			while (ps.hasNext()) {
				Object p = ps.next();
				if (!(p instanceof Element)) {
					continue;
				}
				Element pElement = (Element) p;
				replaceTextAtTrP(datas, pElement, def);
			}
		}
	}

	static void replaceTextAtTrP(Map<String, Object> datas, Element pElement, String def) {
		Iterator<?> runs = pElement.elementIterator("r");
		StringBuilder runOldText = new StringBuilder();
		Element firstRun = null;
		while (runs.hasNext()) {
			Object run = runs.next();
			if (!(run instanceof Element)) {
				continue;
			}
			Element runElement = (Element) run;
			Element runTextElement = runElement.element("t");
			runOldText.append(runTextElement.getText());
			if (firstRun == null) {
				firstRun = runElement;
			} else {
				pElement.remove((Node) run);
			}
		}
		if (firstRun != null) {
			String newRunText = regTrText(runOldText.toString(), datas, "");
			firstRun.element("t").setText(newRunText);
		}
	}

	static TableInfos getTableInfo(String token, Map<String, Object> datas) {
		TableInfos tis = new TableInfos();
		if (token != null) {
			String[] infos = token.split(",");
			if (infos.length == 4) {
				tis.tableInfo = infos[0];
				try {
					tis.dataStartIndex = Integer.valueOf(infos[1]);
				} catch (Exception e) {
				}
				try {
					tis.autoRowStart = Integer.valueOf(infos[2]);
				} catch (Exception e) {
				}
				try {
					tis.autoRowCount = Integer.valueOf(infos[3]);
				} catch (Exception e) {
				}
				Object tableDataObj = WordApi.getValue(tis.tableInfo, datas);
				if (!(tableDataObj instanceof List<?>)) {
					tis.tableDatas = new ArrayList<>();
				} else {
					tis.tableDatas = (List<?>) tableDataObj;
				}
			}
			return tis;
		}
		return null;
	}

	static class TableInfos {
		String tableInfo;
		int dataStartIndex;
		int autoRowStart;
		int autoRowCount;
		List<?> tableDatas;
	}

	static TableInfos replaceTextAtTbl(Element tblElement, Map<String, Object> datas, String def) {
		TableInfos tableInfos = null;
		Iterator<?> trs = tblElement.elementIterator("tr");
		while (trs.hasNext()) {
			Object tr = trs.next();
			if (!(tr instanceof Element)) {
				continue;
			}
			Element trElement = (Element) tr;
			Iterator<?> tcs = trElement.elementIterator("tc");
			while (tcs.hasNext()) {
				Object tc = tcs.next();
				if (!(tc instanceof Element)) {
					continue;
				}
				Element tcElement = (Element) tc;
				Iterator<?> ps = tcElement.elementIterator("p");
				while (ps.hasNext()) {
					Object p = ps.next();
					if (!(p instanceof Element)) {
						continue;
					}
					Element pElement = (Element) p;
					String result = replaceTextAtP(pElement, datas, def);
					if (tableInfos == null) {
						String token = getTableToken(result);
						tableInfos = getTableInfo(token, datas);
						if (tableInfos != null) {
							replaceTextAtTrP(datas, pElement, "");
						}
					}
				}
			}
		}
		return tableInfos;
	}

	static String replaceTextAtP(Element pElement, Map<String, Object> datas, String def) {
		Iterator<?> runs = pElement.elementIterator("r");
		StringBuilder runOldText = new StringBuilder();
		ArrayList<Node> removeNode = new ArrayList<>();
		Element firstRun = null;
		while (runs.hasNext()) {
			Object run = runs.next();
			if (!(run instanceof Element)) {
				continue;
			}
			Element runElement = (Element) run;
			Element runTextElement = runElement.element("t");
			runOldText.append(runTextElement.getText());
			if (firstRun == null) {
				firstRun = runElement;
			} else {
				removeNode.add(runElement);
			}
		}
		for (Node node : removeNode) {
			pElement.remove(node);
		}
		if (firstRun != null) {
			String newRunText = regText(runOldText.toString(), datas, def);
			firstRun.element("t").setText(newRunText);
			return newRunText;
		}
		return "";
	}

	/**
	 * 将参数 text 作为 <b>段落文本标记 -> ${}</b> ,以获取参数 datas 中对应的数据,并返回结果.<p>
	 * 
	 * 如果无法从参数 datas 中根据标记 text 得到一个非 null 的值,则:<br>
	 * 1.如果参数 def 为 null ,则返回 text .<br>
	 * 2.如果参数 def 非 null ,则返回 def.
	 * 
	 * @param text 作为标记的 String
	 * @param datas 作为数据来源的数据集合
	 * @param def 找不到对应的非 null 时用于替代结果的默认值
	 * @return 得到的结果
	 */
	static String regText(String text, Map<String, Object> datas, String def) {
		return regText0(text, datas, def, '$');
	}

	/**
	 * 将参数 text 作为 <b>表格文本标记 -> ${}</b> ,以获取参数 datas 中对应的数据,并返回结果.<p>
	 * 
	 * 如果无法从参数 datas 中根据标记 text 得到一个非 null 的值,则:<br>
	 * 1.如果参数 def 为 null ,则返回 text .<br>
	 * 2.如果参数 def 非 null ,则返回 def.
	 * 
	 * @param text 作为标记的 String
	 * @param datas 作为数据来源的数据集合
	 * @param def 找不到对应的非 null 时用于替代结果的默认值
	 * @return 得到的结果
	 */
	static String regTrText(String text, Map<String, Object> datas, String def) {
		return regText0(text, datas, def, '#');
	}

	/**
	 * 将参数 text 作为(段落/表格)标记 ,以获取参数 datas 中对应的数据,并返回结果.<p>
	 * 
	 * 标记的关键符通过参数 key 定义.匹配的标记将如下形式:<br>
	 * key{}<p>
	 * 
	 * 如果多层嵌套,则操作的为最外层匹配标记的部分.<p>
	 * 
	 * 如果无法从参数 datas 中根据标记 text 得到一个非 null 的值,则:<br>
	 * 1.如果参数 def 为 null ,则返回 text .<br>
	 * 2.如果参数 def 非 null ,则返回 def.
	 * 
	 * @param text 作为标记的 String
	 * @param datas 作为数据来源的数据集合
	 * @param def 找不到对应的非 null 时用于替代结果的默认值
	 * @return 得到的结果
	 */
	static String regText0(String text, Map<String, Object> datas, String def, char key) {
		StringBuilder sb = new StringBuilder();
		int isLabel = 0;
		int sk = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == key && isLabel == 0) {
				isLabel = 1;
			} else if (c == '{') {
				if (isLabel == 1) {
					sk++;
					sb.delete(0, sb.length());
					isLabel = 2;
					continue;
				}
			} else if (c == '}') {
				if (isLabel == 2) {
					sk--;
					if (sk == 0) {
						isLabel = 0;
						String fullKey = sb.toString();
						Object result = getValue(fullKey, datas);
						String value;
						String label = key + "{" + fullKey + "}";
						if (result == null) {
							if (def == null) {
								continue;
							} else {
								value = def;
							}
						} else {
							value = String.valueOf(result);
							StringBuilder sb1 = new StringBuilder(value.length());
							for (int j = 0; j < value.length(); j++) {
								char c1 = value.charAt(j);
								if (c1 < 32 || c1 == 127) {
									continue;
								}
								sb1.append(c1);
							}
							value = sb1.toString();

						}
						String newText = replace(text, label, value);
						int ip = value .length() - label.length();//newText.length() - text.length();
						i += ip;
						text = newText;
					}
				}
			}
			sb.append(c);
		}
		return text;
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

	static String getTableToken(String text) {
		StringBuilder sb = new StringBuilder();
		int isLabel = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '#' && isLabel == 0) {
				isLabel = 1;
			} else if (c == '{' && isLabel == 1) {
				sb.delete(0, sb.length());
				isLabel = 2;
				continue;
			} else if (c == '}' && isLabel == 2) {
				return sb.toString();
			}
			sb.append(c);
		}
		return null;
	}

}
