package com.defu.util.impl;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class FullDocxByXml {
	public final Document style;
	public final Document document;

	public FullDocxByXml(File rootDirectory) throws Exception {
		SAXReader reader = new SAXReader();
		style = reader.read(new File(rootDirectory.getPath() + "/word/styles.xml"));
		document = reader.read(new File(rootDirectory.getPath() + "/word/document.xml"));
	}

	public int getFontSize(Element pElement) {
		Element rPr = pElement.element("rPr");
		Element szCs = rPr.element("szCs");
		if (szCs == null) {
			Element docDefaults = style.getRootElement().element("docDefaults");
			szCs = docDefaults.element("rPrDefault").element("rPr").element("szCs");
		}
		return Integer.valueOf(szCs.attributeValue("val"));
	}

	public String getParagraphAlignment(Element pElement) {
		Element pPr = pElement.element("pPr");
		Element jc = pPr.element("jc");
		if (jc == null) {
			return "left";
		}
		return jc.attributeValue("val");
	}

	public int getTableColCnt(Element tblElement) {
		Element tblGrid = tblElement.element("tblGrid");
		return tblGrid.nodeCount();
	}

	public Element getTcFromTcP(Element pElement) {
		Iterator<?> runs = pElement.elementIterator("r");
		StringBuilder runText = new StringBuilder();
		Element firstRun = null;
		while (runs.hasNext()) {
			Object run = runs.next();
			if (!(run instanceof Element)) {
				continue;
			}
			Element runElement = (Element) run;
			Element runTextElement = runElement.element("t");
			runText.append(runTextElement.getText());
			if (firstRun == null) {
				firstRun = runElement;
			} else {
				pElement.remove((Node) run);
			}
		}
		if (firstRun != null && runText.length() > 0) {
			firstRun.element("t").setText(runText.toString());
		}
		return firstRun;
	}
}
