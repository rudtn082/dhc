package com.kxxxgs.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.kxxxgs.service.HomeService;

@Controller
public class batch {
	private Logger logger = LoggerFactory.getLogger(batch.class);

	@Autowired
	private HomeService service;
	
	// 새벽 3시 30분 배치
	@Scheduled(cron="0 30 3 ? * *")
	//@Scheduled(cron="10 * * * * *")
	public void bacthStart() throws Exception {
		logger.debug("batch start............");

		String OUTPUT_FILE_PATH = "/home/temp";
		//로컬
		//OUTPUT_FILE_PATH = "D:/temp";
		String FILE_NM = "test.txt";
	    File folder = new File(OUTPUT_FILE_PATH);

	    if(!(folder.exists() && folder.isDirectory())) {
		    if (folder.mkdir()){
				logger.debug("디렉토리 생성 성공");
		    }else{
				logger.debug("디렉토리 생성 실패");
				new Exception("");
		    }
	    }
	    
		String FILE_URL = "https://kind.krx.co.kr/corpgeneral/corpList.do?method=download&pageIndex=1&currentPageSize=3000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		try(InputStream in = new URL(FILE_URL).openStream()){
			Path imagePath = Paths.get(OUTPUT_FILE_PATH+"/"+FILE_NM);
			Files.copy(in, imagePath);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				service.deleteStocksInfo();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				HashMap<String, Object> map = new HashMap<>();
				BufferedReader reader  =  new BufferedReader(new InputStreamReader(new FileInputStream(OUTPUT_FILE_PATH+"/"+FILE_NM),"euc-kr"));

				String str = "";
				int i = 0;
				while ((str = reader.readLine()) != null) {
					if(str.contains("<td")) {
						i++;
						// 종목명
						if(i == 1) {
							map.put("STOCKS_NM", str.split("<td>")[1].split("</td>")[0]);
						}
						// 종목 코드
						else if (i == 2) {
							map.put("STOCKS_ID", str.split("<td style=\"mso-number-format:'@';text-align:center;\">")[1].split("</td>")[0]);
						}
						else if (i == 9) {
							i = 0;
							map.put("STOCKS_TYPE", "0");
							service.insertStocksInfo(map);
						}
					}
				}
				reader.close();
				
				File file = new File(OUTPUT_FILE_PATH+"/"+FILE_NM);
				if(file.delete()){
					logger.debug("파일삭제 성공");
				}else{
					logger.debug("파일삭제 실패");
				}

				logger.debug("batch end...........");
			}
		}
	}
}
