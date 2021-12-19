package com.kxxxgs.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface HomeDao {
    public List<HashMap<String, Object>> popularStockAjax(HashMap<String, Object> paramMap) throws Exception;
	
    public List<HashMap<String, Object>> searchStockAjax(HashMap<String, Object> paramMap) throws Exception;

    public void deleteStocksInfo() throws Exception;

    public void insertStocksInfo(HashMap<String, Object> paramMap) throws Exception;
    
    public void insertSearchStocks(HashMap<String, Object> paramMap) throws Exception;
}
