package com.defu.util.impl;

import java.io.File;

import com.defu.util.IFileApi;

public class FileApi implements IFileApi {
	@Override
	public String getFileExtensionName(String fileName){
		String fileExtName = fileName;
		try{
			if((fileName != null) && (fileName.length() > 0)){
				int dot = fileName.lastIndexOf('.');
				if((dot > -1) && (dot < (fileName.length() - 1))){
					fileExtName = fileName.substring(dot + 1);
				}
			}
			return fileExtName;
		}
		catch(Exception ex){
			return null;
		}
	}

	@Override
	public String getFileNameNoExt(String fileFullName) {
		String fileName = fileFullName;
		try{
			if((fileFullName != null)&&(fileFullName.length() > 0)){
				int dot = fileFullName.lastIndexOf('.');
				if((dot > -1) && (dot< (fileFullName.length() - 1))){
					fileName = fileFullName.substring(0, dot);
				}
			}
			return fileName;
		}
		catch(Exception ex){
			return null;
		}
	}

	@Override
	public void deleteFile(File... files) {
		for (File file : files) {
			if (file.exists()) {
				if (file.isDirectory()) {
					deleteFile(file.listFiles());
				}
				file.delete();
			}
		}
	}

	@Override
	public void deleteFile(String... filePaths) {
		for(String fp: filePaths) {
			deleteFile(new File(fp));
		}
	}
	
	
}
