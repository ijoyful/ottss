package com.ottss.controller;

import java.io.IOException;

import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FAQController {
	// FAQ 게시글 리스트
	@RequestMapping(value = "/faq/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 넘어온 파라미터: [page, schType, kwd]
		ModelAndView mav = new ModelAndView("faq/list");
		

		return mav;
	}
}
