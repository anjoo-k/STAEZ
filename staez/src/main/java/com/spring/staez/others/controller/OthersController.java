package com.spring.staez.others.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OthersController {
	
	@GetMapping("main.me") // 메인페이지, "/"로 대체
	public String reserveMain() {
		return "others/reserveMain";
	}
	
	@GetMapping("searchResult.me") // 전체 검색 결과
	public String searchResultPage() {
		return "others/searchResultPage";
	}
	
	@GetMapping("searchResult.co") // 공연 더보기 검색 결과
	public String searchResultMoreConcert() {
		return "others/searchResultMoreConcert";
	}
	
	@GetMapping("calendar.co") // 공연 캘린더
	public String concertCalendar() {
		return "others/concertCalendar";
	}
	
	@GetMapping("calendar.me") // 나의 예매 캘린더
	public String reservationCalendar() {
		return "others/reservationCalendar";
	}
}
