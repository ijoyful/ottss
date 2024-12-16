package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.dao.QuizDAO;
import com.ottss.domain.QuizPlayDTO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.annotation.ResponseBody;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class QuizController {
	@RequestMapping("/games/quiz") // GET, POST 모두 처리
	public ModelAndView quiz(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new ModelAndView("/games/quiz");
	}
	
	@ResponseBody
	@RequestMapping(value = "games/quiz/start", method = RequestMethod.POST)
	public Map<String, Object> startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<>();
		HttpSession session = req.getSession();
		QuizDAO dao = new QuizDAO();
		String state = "false";
		
		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			String id = info.getId();
			int point = Integer.parseInt(req.getParameter("bet"));
			int left_point = dao.leftPoint(id);
			
			if(left_point < point) {
				model.put("state", "false");
				model.put("message", "돈도 없으면서 퀴즈는 푸시고 싶으세요?");
				return model;
			} else {
				QuizPlayDTO dto = new QuizPlayDTO();
				dto.setId(id);
				dto.setUsed_point(point);
				dao.startGame(dto);
				state = "true";
			}
			model.put("state", state);
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(406);
			throw e;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/games/quiz/end" ,method = RequestMethod.POST)
	public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> model = new HashMap<String, Object>();
		HttpSession session = req.getSession();
		QuizDAO dao = new QuizDAO();
		String state = "false";

		try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			QuizPlayDTO dto = new QuizPlayDTO();
			dto.setId(info.getId());
			dto.setWin_point(Integer.parseInt(req.getParameter("win_point")));
			dto.setResult(req.getParameter("result"));
			dto.setGame_num(Integer.parseInt(req.getParameter("game_num")));

			dao.endGame(dto);
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("state", state);
		return model;
	}
}
