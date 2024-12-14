package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.dao.RouletteDAO;
import com.ottss.domain.RouletteDTO;
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
public class RouletteController {
	@ResponseBody
	@RequestMapping(value = "/games/roulette")
	public ModelAndView rouletteStart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new ModelAndView("games/roulette");
	}

	@ResponseBody
	@RequestMapping(value = "/games/roulette/start")
	public Map<String, Object> startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		HttpSession session = req.getSession();
		RouletteDAO dao = new RouletteDAO();
		String state = "false";

		try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			String id = info.getId();
			int point = Integer.parseInt(req.getParameter("bet")); // 건 포인트
			int left_point = dao.leftPoint(id);

			if (left_point < point) { // 만약 포인트가 부족하다면
				model.put("state", state);
				model.put("message", "룰렛에 배팅할 포인트가 부족합니다.");
				return model;
			} else {
				RouletteDTO dto = new RouletteDTO();
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
	@RequestMapping(value = "/games/roulette/end")
	public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) {
		// 넘어온 파라미터: 사용 포인트, 얻은 포인트, 게임 결과(ex. 룰렛: 몇배), 플레이한 게임 번호
		Map<String, Object> model = new HashMap<String, Object>();
		HttpSession session = req.getSession();
		RouletteDAO dao = new RouletteDAO();
		String state = "false";

		try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			RouletteDTO dto = new RouletteDTO();
			dto.setId(info.getId());
			dto.setWin_point(Integer.parseInt(req.getParameter("win_point")));
			dto.setUsed_point(Integer.parseInt(req.getParameter("bet")));
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
