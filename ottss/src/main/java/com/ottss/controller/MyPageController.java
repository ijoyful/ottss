package com.ottss.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ottss.dao.MyPageDAO;
import com.ottss.dao.RouletteDAO;
import com.ottss.domain.PointRecordDTO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.annotation.ResponseBody;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

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

	@RequestMapping(value = "/mypage/mypoint")
	public ModelAndView mypoint(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("player/mypoint");
		HttpSession session = req.getSession();
		RouletteDAO dao = new RouletteDAO();

		try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			int left_pt = dao.leftPoint(info.getId());

			mav.addObject("left_pt", left_pt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	

	@ResponseBody
	@RequestMapping(value = "/mypage/pointhistory", method = RequestMethod.GET)
	public Map<String, Object> pointhistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 포인트 확인
		/*포인트 확인 내역:
		 * 잔여 포인트 확인
		 * 포인트 사용 내역 확인(카테고리별로 확인할 수 있으면 좋고 아님 말고)
		 */
		Map<String, Object> model = new HashMap<String, Object>();
		HttpSession session = req.getSession();
		RouletteDAO roudao = new RouletteDAO();
		MyPageDAO dao = new MyPageDAO();
		MyUtil util = new MyUtilBootstrap();
		// 포인트 사용 기록(게임만, 특정 날짜 범위만, 상점만, 소비만, 획득만)

		try {
			String mode = req.getParameter("mode");
			if (mode == null) {
				mode = "all";
			}
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			String id = info.getId();
			// 넘어온 페이지 번호
			String page = req.getParameter("pageNo");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			int dataCount = dao.dataCount(id, mode);

			// 전체 페이지 수
			int size = 20;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 데이터 가져오기
			int offset = (current_page - 1) * size;
			if (offset < 0) offset = 0;

			int left_pt = roudao.leftPoint(id); // 남은 포인트
			List<PointRecordDTO> ptrecordlist = dao.pointrecord(id, offset, size); // 로그인한 유저의 포인트 사용 내역
			List<PointRecordDTO> xlist = new ArrayList<PointRecordDTO>();

			model.put("left_pt", left_pt);
			if (mode.equals("all")) {
				for (PointRecordDTO dto : ptrecordlist) {
					xlist.add(dto);
				}
			} else if (mode.equals("game")) { // 보고자 하는 선택사항이 게임이면
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategory().equals("게임")) {
						xlist.add(dto);
					}
				}
			} else if (mode.equals("shop")) { // 보고자 하는 선택사항이 상점쇼핑 목록이라면
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategory().equals("상점")) {
						xlist.add(dto);
					}
				}
			} else if (mode.equals("used")) { // 보고자 하는 선택사항이 소비목록이라면
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategories() == 1) {
						xlist.add(dto);
					}
				}
			} else if (mode.equals("win")) {
				for (PointRecordDTO dto : ptrecordlist) {
					if (dto.getCategories() == 9) {
						xlist.add(dto);
					}
				}
			}
			model.put("list", xlist);
			model.put("pageNo", current_page);
			model.put("total_page", total_page);
			model.put("dataCount", dataCount);
			model.put("state", "true");
		} catch (Exception e) {
			model.put("state", "false");
			e.printStackTrace();
			throw e;
		}

		return model;
	}
}
