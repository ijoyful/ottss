package com.ottss.controller;

import java.io.IOException;

import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class QuizController {
	@RequestMapping("/games/quiz/quiz") // GET, POST 모두 처리
	public ModelAndView quiz(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	ModelAndView mav = new ModelAndView("/games/quiz/quiz");
	
	return mav;
		
	}
}
