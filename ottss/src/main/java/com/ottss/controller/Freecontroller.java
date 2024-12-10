package com.ottss.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.ottss.dao.FreeDAO;
import com.ottss.domain.FreeDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class Freecontroller {
	@RequestMapping("/freeboard/list") // GET, POST 모두 처리
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트 : - [page],[schType, kwd]
		ModelAndView mav = new ModelAndView("freeboard/list");

		FreeDAO dao = new FreeDAO();
		MyUtil util = new MyUtilBootstrap();

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			// 검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			// GET 방식이면 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}

			// 데이터 개수
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount(); // 검색이 아닌경우
			} else {
				dataCount = dao.dataCount(schType, kwd); // 검색인 경우
			}

			// 전체 페이지 수
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<FreeDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.freeList(offset, size);
			} else {
				list = dao.freeList(offset, size, schType, kwd);
			}
			// 쿼리
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" +
						URLEncoder.encode(kwd, "utf-8");
			}			

			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/freeboard/list";
					// 글 리스트 주소
			String articleUrl = cp + "/freeboard/article?page=" + current_page;
						// 글보기 주소
			if(query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			// 페이징
			String paging = util.paging(current_page, total_page, listUrl);
			
			
			// 포워딩할 JSP에 전달할 속성
			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			
		} catch (Exception e) {

		}
		return mav;
	}
}
