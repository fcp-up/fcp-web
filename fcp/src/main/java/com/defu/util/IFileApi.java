package com.defu.util;

import java.io.File;

public interface IFileApi {
	/**
	 * 文件操作		获取文件扩展名
	 * 参数：	fileName 文件名
	 * 返回值：	fileExtName 文件扩展名
	 */
	 String getFileExtensionName(String fileName);
	 
	 /**
	 * 文件操作		获取文件名（不包含文件扩展名）
	 * 参数： fileFullName 文件名全称
	 * 返回值： fileName 文件名
	 */
	 String getFileNameNoExt(String fileFullName);

	 /**
	  * 删除文件
	  * @param files 待删除文件
	  */
	 void deleteFile(File... files);

	 /**
	  * 删除文件
	  * @param files 待删除文件路径
	  */
	 void deleteFile(String... filePaths);
}
