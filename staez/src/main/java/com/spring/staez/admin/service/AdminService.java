package com.spring.staez.admin.service;

import java.util.ArrayList;

import com.spring.staez.admin.model.vo.Category;
import com.spring.staez.admin.model.vo.ImpossibleSeat;
import com.spring.staez.community.model.vo.Board;
import com.spring.staez.concert.model.vo.Theater;

public interface AdminService {

	int faqIncert(Board b, int categoryNo);

	ArrayList<Category> selectFaqCategory(int refCategoryNo);

	int incertTheater(Theater t);

	int toggleSeat(ImpossibleSeat seat, String status);

	ArrayList<Theater> selectTheaterList(String keyword);

}
