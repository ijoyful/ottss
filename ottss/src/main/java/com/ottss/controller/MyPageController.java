package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ottss.dao.MyPageDAO;
import com.ottss.dao.RouletteDAO;
import com.ottss.domain.PlayRecordDTO;
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

	@ResponseBody
	@RequestMapping(value = "/mypage/attend", method = RequestMethod.GET)
	public Map<String, Object> listAgeSection(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();

		try {

			model.put("state", "true");
		} catch (Exception e) {
			model.put("state", "false");
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/mypage/bestrecord", method = RequestMethod.GET)
	public ModelAndView bestrecord(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 최고기록 확인
		ModelAndView mav = new ModelAndView("player/bestrecord");
		HttpSession session = req.getSession();
		MyPageDAO dao = new MyPageDAO();

		try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			String id = info.getId();
			List<PlayRecordDTO> bestRecord = dao.bestrecord(id);
			if (bestRecord.size() == 0) {
				resp.sendError(406); // best record 사이즈가 0
			}

			mav.addObject("bestRecord", bestRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			int dataCount = dao.pointdataCount(id, mode);

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
			List<PointRecordDTO> ptrecordlist = dao.pointrecord(id, offset, size, mode); // 로그인한 유저의 포인트 사용 내역

			model.put("left_pt", left_pt);

			model.put("list", ptrecordlist);
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
