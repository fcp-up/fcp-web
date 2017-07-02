package com.defu.util;

import java.nio.file.NoSuchFileException;

public interface IGzApi {
	/**
	 * 压缩一个文件，该方法是一个异步方法
	 * @param fp 文件路径
	 * @param ogn 是否保留源文件
	 * @return 压缩文件路径
	 * @throws NoSuchFileException
	 */
	String zip(String fp, boolean ogn) throws NoSuchFileException;
	/**
	 * 解压一个文件
	 * @param fp 文件路径
	 * @param ogn 是否保留源文件
	 * @return 解压后文件路径
	 * @throws NoSuchFileException
	 */
	String unzip(String fp, boolean ogn) throws NoSuchFileException;
	/**
	 * 压缩一个文件
	 * @param fp 文件路径
	 * @return 压缩文件路径
	 * @throws NoSuchFileException
	 */
	String zip(String fp) throws NoSuchFileException;
	
	String unzip(String fp) throws NoSuchFileException;
	
	public abstract static class Callback{
		
		public void success(String fp){}
	}
}
