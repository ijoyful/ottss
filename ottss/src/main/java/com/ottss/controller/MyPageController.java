package com.ottss.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.dao.MyPageDAO;
import com.ottss.dao.RouletteDAO;
import com.ottss.domain.PointRecordDTO;
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
		RouletteDAO roudao = new RouletteDAO();
		// 포인트 사용 기록(게임만, 특정 날짜 범위만, 상점만, 소비만, 획득만)
		String mode = req.getParameter("mode");

		try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			int left_pt = roudao.leftPoint(info.getId()); // 남은 포인트
			MyPageDAO dao = new MyPageDAO();
			List<PointRecordDTO> ptrecordlist = dao.pointrecord(info.getId()); // 로그인한 유저의 포인트 사용 내역
			List<PointRecordDTO> xlist = new ArrayList<PointRecordDTO>();

			mav.addObject("left_pt", left_pt);
			if (mode.equals("all")) {
				mav.addObject("ptrecordlist", ptrecordlist);				
			} else if (mode.equals("game")) { // 보고자 하는 선택사항이 게임이면
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategory().equals("게임")) {
						xlist.add(dto);
					}
				}
				mav.addObject("xlist", xlist);
			} else if (mode.equals("shop")) { // 보고자 하는 선택사항이 상점쇼핑 목록이라면
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategory().equals("상점")) {
						xlist.add(dto);
					}
				}
				mav.addObject("xlist", xlist);
			} else if (mode.equals("used")) { // 보고자 하는 선택사항이 소비목록이라면
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategories() == 1) {
						xlist.add(dto);
					}
				}
				mav.addObject("xlist", xlist);
			} else if (mode.equals("win")) {
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategories() == 9) {
						xlist.add(dto);
					}
				}
				mav.addObject("xlist", xlist);
			} else if (mode.equals("date")) {
				xlist = dao.pointrecord(req.getParameter("date"), info.getId());
				mav.addObject("xlist", xlist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
}
