package com.defu.fcp.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.defu.fcp.base.AbstractDao;
import com.defu.fcp.base.AbstractService;
import com.defu.fcp.phone.dao.IDaoPhone;
import com.defu.fcp.phone.service.ISvcPhone;

@Service
public class SvcPhone extends AbstractService implements ISvcPhone {
	@Autowired IDaoPhone dao;

	public AbstractDao getDao() {
		return dao;
	}
	
}
