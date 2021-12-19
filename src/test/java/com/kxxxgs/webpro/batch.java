package com.kxxxgs.webpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.Test;

public class batch {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/stock"; // jdbc:mysql://127.0.0.1:3306/여러분이 만드신
																			// 스키마이름
	private static final String USER = "root"; // DB 사용자명
	private static final String PW = "1043kyung"; // DB 사용자 비밀번호

	@Test
	public void testConnection() throws Exception {

		String OUTPUT_FILE_PATH = "D:\\temp";
		String FILE_NM = "test.txt";
	    File folder = new File(OUTPUT_FILE_PATH);

	    if(!(folder.exists() && folder.isDirectory())) {
		    if (folder.mkdir()){
		      System.out.println("디렉토리 생성 성공");
		    }else{
		      System.out.println("디렉토리 생성 실패");

		    }
	    }
	    
		String FILE_URL = "https://kind.krx.co.kr/corpgeneral/corpList.do?method=download&pageIndex=1&currentPageSize=3000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		try(InputStream in = new URL(FILE_URL).openStream()){
			Path imagePath = Paths.get(OUTPUT_FILE_PATH+"/"+FILE_NM);
			Files.copy(in, imagePath);
		} catch(Exception e) {
			
		} finally {

			Class.forName(DRIVER);

			try (Connection con = DriverManager.getConnection(URL, USER, PW)) {
				System.out.println("성공");
				
				Statement stmt = con.createStatement();

				BufferedReader reader = new BufferedReader(new FileReader(OUTPUT_FILE_PATH+"/"+FILE_NM));

				String sql = "";
				String str = "";
				String STOCKS_ID = "";
				String STOCKS_NM = "";
				String STOCKS_TYPE = "0";
				int i = 0;
				while ((str = reader.readLine()) != null) {
					if(str.contains("<td")) {
						i++;
						// 종목명
						if(i == 1) {
							//System.out.print(str.split("<td>")[1].split("</td>")[0] + "\n");
							STOCKS_NM = str.split("<td>")[1].split("</td>")[0];
						}
						// 종목 코드
						else if (i == 2) {
							STOCKS_ID = str.split("<td style=\"mso-number-format:'@';text-align:center;\">")[1].split("</td>")[0];
							//System.out.print(str.split("<td style=\"mso-number-format:'@';text-align:center;\">")[1].split("</td>")[0] + "\n");
						}
						else if (i == 9) {
							i = 0;
							sql = "INSERT INTO STOCKS_INFO (STOCKS_ID, STOCKS_NM, STOCKS_TYPE, INPUT_DATETIME) VALUES ('" + STOCKS_ID + "','" + STOCKS_NM + "','" + STOCKS_TYPE + "', NOW())";
							int affectedCount = stmt.executeUpdate(sql);
							System.out.println(affectedCount);
						}
					}
				}
				
			} catch (Exception e) {
				System.out.println("에러발생");
				e.printStackTrace();
			}
			
		}
	}
}
