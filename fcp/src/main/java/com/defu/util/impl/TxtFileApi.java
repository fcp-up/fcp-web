package com.defu.util.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.defu.util.ITxtFileApi;

public class TxtFileApi extends FileApi implements ITxtFileApi {
	@Override
	public List<String> read(InputStream inputStream, String encode) throws IOException, UnsupportedEncodingException {
		List<String> rst = new ArrayList<String>();
		String s;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, encode));
		
		while((s = br.readLine()) != null){
			if(s.length() > 0 && (int)s.charAt(0) == 65279) s = s.substring(1);
			rst.add(s);
		}
		
		return rst;
	}

	@Override
	public List<String> read(File file, String encode) throws IOException, UnsupportedEncodingException {
		return read(new FileInputStream(file), encode);
	}

	@Override
	public List<String> read(String filePath, String encode) throws IOException, UnsupportedEncodingException {
		return read(new File(filePath), encode);
	}

	@Override
	public void write(String filePath, String encode, String txt) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
		raf.write(txt.getBytes(encode));
		raf.close();
	}

}
