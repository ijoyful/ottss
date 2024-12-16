package com.ottss.controller;

import java.io.IOException;

import com.ottss.dao.RouletteDAO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {
	@RequestMapping(value = "/mypage/bestrecord", method = RequestMethod.GET)
	public ModelAndView bestrecord(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 최고기록 확인
		ModelAndView mav = new ModelAndView("player/bestrecord");
		return mav;
	}

	@RequestMapping(value = "/mypage/mypoint", method = RequestMethod.GET)
	public ModelAndView mypoint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 포인트 확인
		/*포인트 확인 내역:
		 * 잔여 포인트 확인
		 * 포인트 사용 내역 확인(카테고리별로 확인할 수 있으면 좋고 아님 말고)
		 */
		ModelAndView mav = new ModelAndView("player/mypoint");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		RouletteDAO dao = new RouletteDAO();
		int left_pt = dao.leftPoint(info.getId()); // 남은 포인트

		return mav;
	}
}
