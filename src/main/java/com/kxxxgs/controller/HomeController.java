package com.kxxxgs.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kxxxgs.service.HomeService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/main")
public class HomeController {
	@Autowired
	private HomeService service;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/logoAjax")
	public void logoAjax(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
		try {
			reponse.sendRedirect("../../resources/assets/logo.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/main.do")
	public Model main(Locale locale, Model model) throws Exception {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		logger.debug("[main] 접속시간 : " + formattedDate);

		// 음악검색
		try {
			// 1 page
			// model.addAttribute("test", service.testSel());

		} catch (Exception e) {
			logger.error(e.toString());
		}

		return model;
	}

	@RequestMapping(value = "/scrapAjax")
	@ResponseBody
	public ModelAndView scrapAjax(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();
		List<Entry<String, Integer>> wordCloudList = null;
		List<Entry<String, Integer>> sympathyList = null;
		List<Entry<String, Integer>> nonSympathyList = null;
		List<Entry<String, Integer>> inquireList = null;
		List<Entry<String, Integer>> writerList = null;

		if(paramMap.get("code") != null) {
			try {
				Document doc;
				Elements contents;
				Map<String, Integer> wordCloudMap = new HashMap(); // 타이틀 워드 클라우드
				Map<String, Integer> sympathyMap = new HashMap(); // 공감순별 타이틀
				Map<String, Integer> nonSympathyMap = new HashMap(); // 비공감순별 타이틀
				Map<String, Integer> inquireMap = new HashMap(); // 조회순별 타이틀
				Map<String, Integer> writerMap = new HashMap(); // 작성자순
				// 평균 조회수
				// 평균 공감수
				// 평균 비공감수
				
				// 최근 글 500개 분석 (20*25)
				for (int i = 1; i < 25; i++) {

					doc = Jsoup.connect("https://finance.naver.com/item/board.naver?code="+paramMap.get("code")+"&page=" + i).get();
					
					contents = doc.getElementsByClass("inner_sub").select("table").get(0).select("tbody").select("tr");
					
					// tr 반복
					for (Element tr : contents) {
						if(tr.select("td").size() < 6) continue;
						
						
						String date = tr.select("td").get(0).text(); // 날짜
						String title = tr.select("td").get(1).select("a").attr("title"); // 제목
						String writer = tr.select("td").get(2).text(); // 글쓴이
						String inquire = tr.select("td").get(3).text(); // 조회
						String sympathy = tr.select("td").get(4).text(); // 공감
						String nonSympathy = tr.select("td").get(5).text(); // 비공감
						String link = tr.select("td").get(1).select("a").attr("href"); // 링크

						// 조회순별 타이틀
						inquireMap.put(title+"||"+link, Integer.valueOf(inquire));
						
						// 공감순별 타이틀
						sympathyMap.put(title+"||"+link, Integer.valueOf(sympathy));
						
						// 비공감순별 타이틀
						nonSympathyMap.put(title+"||"+link, Integer.valueOf(nonSympathy));

						// 작성자순
						if (writerMap.containsKey(writer)) {
							writerMap.put(writer, writerMap.get(writer) + 1);
							// 키가 없을 시 단어(key)와 value를 1로 초기화한다.
						} else {
							writerMap.put(writer, 1);
						}

						// 타이틀 워드 클라우드
						String[] splitTest = title.split(" ");

						for (int z = 0; z < splitTest.length; z++) {
							if ("".equals(splitTest[z]))
								continue;
							if (splitTest[z].contains("삭제된") || splitTest[z].contains("게시물"))
								continue;
							if (wordCloudMap.containsKey(splitTest[z])) {
								wordCloudMap.put(splitTest[z].replaceAll("!\"#[$]%&\\(\\)\\{\\}@`[*]:[+];-.<>,\\^~|'\\[\\]", ""), wordCloudMap.get(splitTest[z]) + 1);
								// 키가 없을 시 단어(key)와 value를 1로 초기화한다.
							} else {
								wordCloudMap.put(splitTest[z].replaceAll("!\"#[$]%&\\(\\)\\{\\}@`[*]:[+];-.<>,\\^~|'\\[\\]", ""), 1);
							}
						}
					}
				}
				
				wordCloudList = new ArrayList<Entry<String, Integer>>(wordCloudMap.entrySet());
				sympathyList = new ArrayList<Entry<String, Integer>>(sympathyMap.entrySet());
				nonSympathyList = new ArrayList<Entry<String, Integer>>(nonSympathyMap.entrySet());
				inquireList = new ArrayList<Entry<String, Integer>>(inquireMap.entrySet());
				writerList = new ArrayList<Entry<String, Integer>>(writerMap.entrySet());

				// 오름차순으로 정렬
				Collections.sort(wordCloudList, new Comparator<Entry<String, Integer>>() {
					// compare로 값을 비교
					public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
						// 오름 차순 정렬
						return obj1.getValue().compareTo(obj2.getValue());
					}
				});

				// 내림차순으로 정렬
				Collections.sort(sympathyList, new Comparator<Entry<String, Integer>>() {
					// compare로 값을 비교
					public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
						// 내림 차순 정렬
						return obj2.getValue().compareTo(obj1.getValue());
					}
				});

				// 내림차순으로 정렬
				Collections.sort(nonSympathyList, new Comparator<Entry<String, Integer>>() {
					// compare로 값을 비교
					public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
						// 내림 차순 정렬
						return obj2.getValue().compareTo(obj1.getValue());
					}
				});

				// 내림차순으로 정렬
				Collections.sort(inquireList, new Comparator<Entry<String, Integer>>() {
					// compare로 값을 비교
					public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
						// 내림 차순 정렬
						return obj2.getValue().compareTo(obj1.getValue());
					}
				});

				// 내림차순으로 정렬
				Collections.sort(writerList, new Comparator<Entry<String, Integer>>() {
					// compare로 값을 비교
					public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2) {
						// 내림 차순 정렬
						return obj2.getValue().compareTo(obj1.getValue());
					}
				});
				
				// 워드 클라우드 최대 단어는 50개
				if(wordCloudList.size() > 50) {
					for(int i = wordCloudList.size()-50; i > 0; i--) {
						wordCloudList.remove(i);
					}	
				}
				
				mav.addObject("wordCloudList", wordCloudList);
				mav.addObject("writerList", writerList);
				mav.addObject("inquireList", inquireList);
				mav.addObject("nonSympathyList", nonSympathyList);
				mav.addObject("sympathyList", sympathyList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//mav.addObject("result", null);
		}
		mav.setViewName("jsonView");
		
		return mav;
	}
	
	@RequestMapping(value = "/popularStockAjax")
	@ResponseBody
	public ModelAndView popularStockAjax(HttpServletRequest request, @RequestParam HashMap<String, Object> paramMap, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		mav.addObject("result", service.popularStockAjax(paramMap));
		mav.setViewName("jsonView");
		
		return mav;
	}

	@RequestMapping(value = "/searchStockAjax")
	@ResponseBody
	public ModelAndView searchStockAjax(HttpServletRequest request, @RequestParam HashMap<String, Object> paramMap, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		mav.addObject("result", service.searchStockAjax(paramMap));
		mav.setViewName("jsonView");
		
		return mav;
	}

	@RequestMapping(value = "/clickStockAjax")
	@ResponseBody
	public ModelAndView clickStockAjax(HttpServletRequest request, @RequestParam HashMap<String, Object> paramMap, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		service.insertSearchStocks(paramMap);
		mav.setViewName("jsonView");
		
		return mav;
	}
}