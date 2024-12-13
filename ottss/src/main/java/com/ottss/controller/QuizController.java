package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.ResponseBody;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class QuizController {
	@RequestMapping("/games/quiz/quiz") // GET, POST 모두 처리
	public ModelAndView quiz(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new ModelAndView("/games/quiz/quiz");
	}
	
	public Map<String, Object> startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<>();
		HttpSession session = req.getSession();
		
		try {
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		if(info == null) {
			model.put("state", "false");
			return model;
		}
		
		String userId = info.getId();
	
		} catch (Exception e) {

		}
		
		return model;
	}
}
