package com.defu.util.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.defu.util.ISystemTaskApi;

public class WindowsSystemTaskApi extends BaseCmdApi implements ISystemTaskApi {
	@Override
	public List<Integer> taskPidsByName(String taskName) {
		// TODO Auto-generated method stub
		List<Integer> rst = new ArrayList<>();
		BufferedReader br = null;
		try{
			Process proc = systemCmd("tasklist /nh /FI \"IMAGENAME eq " + taskName + "\"");
			
			if(proc == null) return null;
			
			br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			String l;
			String[] ls;
			
			while((l = br.readLine()) != null) {
				ls = l.split("\\s+");
				if(ls[0] != null && ls[0].trim().equals(taskName)) {
					rst.add(Integer.parseInt(ls[1].trim()));
//					systemCmd("taskkill /PID /T" + l.split("\\\\s+")[1].trim());
				}
			}
		}
		catch(Exception ex){
			if(log.isDebugEnabled()) log.debug("读取cmd输出异常：" + ex.getMessage());
			return null;
		}
		finally{
			if(br != null) try {
				br.close();
			}
			catch(Exception ex) {}
		}
		
		return rst;
	
	}

	@Override
	public Process startExe(String path) {
		// TODO Auto-generated method stub
		return systemCmd(path);
	}

	@Override
	public Process stopSystemTaskByName(String taskName) {
		// TODO Auto-generated method stub
		return systemCmd("taskkill /F /IM \"" + taskName + "\"");
	}

	@Override
	public Process stopSystemTaskGroupByParentName(String parentTaskName) {
		// TODO Auto-generated method stub
		return systemCmd("taskkill /F /IM \"" + parentTaskName + "\" /T");
	}

	@Override
	public String taskNameByPid(Integer pid) {
		// TODO Auto-generated method stub
		try{
			Process proc = systemCmd("tasklist /nh /FI \"PID eq " + pid + "\"");
			
			if(proc == null) return null;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			String l;
			String[] ls;
			
			while((l = br.readLine()) != null) {
				ls = l.split("\\s+");
				if(ls.length > 1 && ls[1] != null && Integer.parseInt(ls[1].trim()) == pid) {
					return ls[0].trim();
				}
			}
		}
		catch(Exception ex){
			if(log.isDebugEnabled()) log.debug("读取cmd输出异常：" + ex.getMessage());
			return null;
		}
		
		return null;
	}
}
