package com.kxxxgs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kxxxgs.dao.HomeDao;

@Service
public class HomeService implements HomeDao {
	@Autowired
	private HomeDao dao;

	@Override
	public Object testSel() throws Exception {
		// TODO Auto-generated method stub
		Object aa = dao.testSel(); 
		return aa;
	}

	/*
	 * @Resource private HomeDao dao;
	 * 
	 * @Override public int testSel() throws Exception {
	 * System.out.println("serviceΩ√¿€"); return dao.testSel(); }
	 */
}
