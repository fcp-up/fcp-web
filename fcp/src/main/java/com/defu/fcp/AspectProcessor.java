package com.defu.fcp;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import com.defu.fcp.atom.dao.*;

@Service
@Aspect
public class AspectProcessor {

	@Pointcut("execution(public * com.defu.fcp.atom.dao..*.add*(..))")
	public void addAspect() {
	}

	@Pointcut("execution(public * com.defu.fcp.atom.dao..*.delete*(..))")
	public void delAspect() {
		
	}

	@Pointcut("execution(public * com.defu.fcp.atom.dao..*.update*(..))")
	public void updateAspect() {
		
	}
	
	@Pointcut("addAspect() || delAspect() || updateAspect()")
	public void alterAspect() {
		
	}
	
	@After("alterAspect()")
	public void afterAlterArchive(JoinPoint jp) {
		Object obj = jp.getTarget();
		int archid = Const.ArchID.undefined;
		if(obj instanceof IDaoTerminal) {
			archid = Const.ArchID.terminal;
		}
		else if(obj instanceof IDaoDevice) {
			archid = Const.ArchID.device;
		}
		
		if(archid != Const.ArchID.undefined) {
			HttpSocketServer.notifyArchiveAlter(archid);
		}
	}
	
}
