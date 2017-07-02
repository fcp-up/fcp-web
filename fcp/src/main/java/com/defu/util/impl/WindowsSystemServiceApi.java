package com.defu.util.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.defu.constant.SystemServiceState;
import com.defu.util.ISystemServiceApi;

public class WindowsSystemServiceApi extends BaseCmdApi implements ISystemServiceApi {

	@Override
	public SystemServiceState serviceState(String serviceName) {
		// TODO Auto-generated method stub	
		BufferedReader br = null;
		try{
			Process proc = systemCmd("sc query " + serviceName);
			
			if(proc == null) return null;
			
			br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			String reg = "^\\s*state\\s*:\\s*(\\d+)(\\s+.*$|$)", l;
			
			Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			Matcher m;
			
			boolean cmded = false;
			
			while((l = br.readLine()) != null) {
				cmded = true;
				m = p.matcher(l);
				if(m.matches()) {
					switch(Integer.parseInt(m.group(1))) {
					case 1: return SystemServiceState.STOPPED;
					case 2: return SystemServiceState.START_PENDING;
					case 3: return SystemServiceState.STOP_PENDING;
					case 4: return SystemServiceState.RUNNING;
					case 5: return SystemServiceState.CONTINUE_PENDING;
					case 6: return SystemServiceState.PAUSE_PENDING;
					case 7: return SystemServiceState.PAUSED;
					}
				}
			}
			
			if(cmded) return SystemServiceState.NOTEXISTS;
		}
		catch(Exception ex){
			return null;
		}
		finally{
			if(br != null) try {
				br.close();
			}
			catch(Exception ex) {}
		}
		
		return null;
	}

}
