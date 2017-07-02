package com.defu.util;

import java.io.File;
import java.util.Map;

public interface IPdfApi {

	void export(File tpl, File tag, Map<String, Object> datas) throws Exception;

	void export(File file, File exportFile, Map<String, Object> pcDataMap, int maxRowNum) throws Exception;

}
