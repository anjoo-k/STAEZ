package com.spring.staez.concert.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.spring.staez.admin.model.vo.Category;
import com.spring.staez.community.model.vo.Board;
import com.spring.staez.concert.model.vo.Concert;
import com.spring.staez.concert.model.vo.ConcertLike;
import com.spring.staez.concert.model.vo.ConcertReview;
import com.spring.staez.concert.service.ConcertService;

@Controller
public class ConcertController {
	
	@Autowired
	private ConcertService concertService;
	
	// 공연 누르면 보여주는 공연 메인 페이지
//	@GetMapping("main.co")
//	public String concertMain() {
//		
//		return "concert/concertMain";
//	}
	
	// category를 가져와라 콘서트 '네비'에 뿌려주기
	@ResponseBody
	@RequestMapping(value = "connavi.co", produces="application/json; charset=UTF-8")
	public String selectCateCon() {
		ArrayList<Category> conList =  concertService.selectCateCon();
		return new Gson().toJson(conList);
	}
	
	// 공연을 categoryNo로 가져와서 공연세부페이지로, 메인페이지에 categoryNo를 내려주면 클릭하면 mian.co?에 들어감
	@RequestMapping(value = "main.co")
	public String concertMain(String categoryNo, Model model) {
		Category cat = concertService.selectCate(Integer.parseInt(categoryNo));
		model.addAttribute("cat", cat);
		return "concert/concertMain";
	}
	
	// 공연리스트 categoryNo로 다가져와(ajax로)
	@ResponseBody
	@RequestMapping(value = "maincon.co", produces="application/json; charset=UTF-8")
	public String mainDrawAjax(@RequestParam(value = "categoryNo")int categoryNo) {
		
		ArrayList<Concert> list =  concertService.selectconList(categoryNo);
		
		return new Gson().toJson(list);
	}
		
		
	// 공연을 concertNo로 가져와서 공연세부페이지로
	@RequestMapping(value = "detail.co", produces="application/json; charset=UTF-8")
	public String selectCon(@RequestParam(value = "concertNo") String concertNo, Model model) {
		Concert con = concertService.selectCon(Integer.parseInt(concertNo));
		ArrayList<ConcertReview> conComDlist =  concertService.selectComDetail(Integer.parseInt(concertNo));
		ArrayList<Board> conRevDlist =  concertService.selectRevDetail(Integer.parseInt(concertNo));
		
		model.addAttribute("rev", conRevDlist);
		model.addAttribute("com", conComDlist);
		model.addAttribute("con", con);
		
		return "concert/concertDetailMain";
	}
	
	
	
	
	// 좋아요 몇개인지
	@ResponseBody
	@RequestMapping(value = "likecount.co", produces="application/json; charset=UTF-8")
	public String likeCount(String userNo, String concertNo) {
		
		Map map = new HashMap();
		map.put("userNo", Integer.parseInt(userNo));
		map.put("concertNo", Integer.parseInt(concertNo));
		
		int conLikeCount = concertService.selectConLikeCount(Integer.parseInt(concertNo));
		System.out.println("conLikeCount:" +  conLikeCount);
		
		int userConLikeCount = concertService.selectUserConLike(map);
		System.out.println("userConLikeCount:" +  userConLikeCount);
		
		Map<String, Object> conMap = new HashMap<>();
		conMap.put("conLikeCount", conLikeCount); // 콘서트가 가진 좋아요 총 갯수 status Y
		conMap.put("userConLikeCount", userConLikeCount); // a유저가 1이라는 콘서트에 좋아요한 갯수 status Y
		
		System.out.println("map" + conMap);

		return new Gson().toJson(conMap);
	}
	
	
	// 좋아요 insert, update
	@ResponseBody
	@RequestMapping(value = "likeupdate.co")
	public String likeUpdate(ConcertLike like) {
		// like 있는지 없는지 체크후 없으면 insert

		int userNo = like.getUserNo();
		int concertNo = like.getConcertNo();
		Map map = new HashMap();
		map.put("userNo", userNo);
		map.put("concertNo", concertNo);

		System.out.println(like);
		System.out.println("userNo: "+ userNo + "concertNo: " + concertNo);
		
		int result =  concertService.selectUserConLikeAll(map); // a유저가 1이라는 콘서트에 좋아요한 적이 있냐 status 노상관
		System.out.println("selectUserConLikeAll:" + result);
		
		
		if(result > 0) {
			result = concertService.updateConLike(like); // 좋아요 한 적 있으면 update
		} else {
			result = concertService.insertConLike(like); // 좋아요 한 적 없으면 insert
		}
		
		System.out.println("updateConLike:" + result);
		System.out.println("insertConLike:" + result);
		
		int conLikeCount = concertService.selectConLikeCount(concertNo);
		System.out.println("conLikeCount:" + conLikeCount);
		
		if(result > 0) {
			return String.valueOf(conLikeCount);
		} else {
			return "좋아요 실패";
		}


	}
	

	
	// 공연을 concertNo로 가져와서 공연세부페이지로
	@ResponseBody
	@RequestMapping(value = "conDetail.co", produces="application/json; charset=UTF-8")
	public String conDetail(@RequestParam(value = "concertNo") String concertNo) {
		ArrayList<Concert> conDlist =  concertService.selectConDetail(Integer.parseInt(concertNo));
		return new Gson().toJson(conDlist);
	}
	
	// 공연을 concertNo로 가져와서 공연 판매정보 페이지로
	@ResponseBody
	@RequestMapping(value = "conSellDetail.co", produces="application/json; charset=UTF-8")
	public String conSellDetail(@RequestParam(value = "concertNo") String concertNo) {
		ArrayList<Concert> conDlist =  concertService.selectConDetail(Integer.parseInt(concertNo));
		return new Gson().toJson(conDlist);
	}
	
	// 공연을 concertNo로 가져와서 한줄평 페이지로
	@ResponseBody
	@RequestMapping(value = "commentDetail.co", produces="application/json; charset=UTF-8")
	public String commentDetail(@RequestParam(value = "concertNo") String concertNo) {
		ArrayList<ConcertReview> conComDlist =  concertService.selectComDetail(Integer.parseInt(concertNo));
		return new Gson().toJson(conComDlist);
	}
	
	// 공연을 concertNo로 가져와서 리뷰 페이지로
	@ResponseBody
	@RequestMapping(value = "reviewDetail.co", produces="application/json; charset=UTF-8")
	public String reviewDetail(@RequestParam(value = "concertNo") String concertNo) {
		ArrayList<Board> conRevDlist =  concertService.selectRevDetail(Integer.parseInt(concertNo));
		return new Gson().toJson(conRevDlist);
	}
	

}
