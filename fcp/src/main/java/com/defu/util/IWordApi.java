package com.defu.util;

import java.io.File;
import java.util.Map;

public interface IWordApi {

	public abstract void export(File tpl, File tag, Map<String, Object> datas) throws Exception;

	public abstract void export(File tpl, File tag, Map<String, Object> datas, int maxRows) throws Exception;

}