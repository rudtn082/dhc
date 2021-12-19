package com.kxxxgs.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kxxxgs.dao.HomeDao;

@Service
public class HomeService implements HomeDao {
	@Autowired
	private HomeDao dao;
	
	@Override
	public List<HashMap<String, Object>> popularStockAjax(HashMap<String, Object> paramMap) throws Exception {
		return dao.popularStockAjax(paramMap); 
	}
	
	@Override
	public List<HashMap<String, Object>> searchStockAjax(HashMap<String, Object> paramMap) throws Exception {
		return dao.searchStockAjax(paramMap); 
	}

	@Override
	public void deleteStocksInfo() throws Exception {
		dao.deleteStocksInfo(); 
	}

	@Override
	public void insertStocksInfo(HashMap<String, Object> paramMap) throws Exception {
		dao.insertStocksInfo(paramMap); 
	}

	@Override
	public void insertSearchStocks(HashMap<String, Object> paramMap) throws Exception {
		dao.insertSearchStocks(paramMap); 
	}
	
	/*
	 * @Resource private HomeDao dao;
	 * 
	 * @Override public int testSel() throws Exception {
	 * System.out.println("serviceΩ√¿€"); return dao.testSel(); }
	 */
}
