package com.defu.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ITxtFileApi extends IFileApi {
	/**
	 * 读取一个文本文件
	 * @param inputStream 输入流
	 * @return 字符串列表，一行一个元素
	 * @throws IOException
	 */
	public List<String> read(InputStream inputStream, String encode) throws IOException, UnsupportedEncodingException;
	public List<String> read(File file, String encode) throws IOException, UnsupportedEncodingException;
	public List<String> read(String filePath, String encode) throws IOException, UnsupportedEncodingException;
}
