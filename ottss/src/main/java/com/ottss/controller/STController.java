package com.ottss.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.List;

import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.domain.SessionInfo;
import com.ottss.dao.STDAO;
import com.ottss.domain.STDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class STController {
	
	@RequestMapping("/show/list") //GET,POST 둘다 처리
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServerException, SQLException{
		//게시글 리스트 : 파라미터,[page,schType,kwd]
		
		ModelAndView mav = new ModelAndView("show/list");
		
		STDAO dao = new STDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			//검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			//GET 방식이면 디코딩
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd,"utf-8");
			}
			
			//데이터 개수
			int dataCount;
			if(kwd.length() == 0) {
				dataCount = dao.dataCount();
			}else {
				dataCount = dao.dataCount(schType, kwd);
			}
			
			//전체 페이지 수
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			//게시물 가져오기
			int offset = (current_page -1) * size;
			if(offset < 0)
				offset = 0;
			
			List<STDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listBoard(offset, size);
			} else {
				list = dao.listBoard(offset, size, schType, kwd);
			}
			
			//쿼리
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			//페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/show/list";
			//글리스트 주소
			String articleUrl = cp + "/show/article?page=" + current_page;
			//글보기 주소
			if(query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" +query;
			}
			
			//페이징
			String paging = util.paging(current_page, total_page, listUrl);
			
			//포워딩할 JSP에 전달할 속성
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
			e.printStackTrace();
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/show/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 글쓰기 폼

		ModelAndView mav = new ModelAndView("show/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/show/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 글등록하기 : 넘어온 파라미터 - subject, content

		STDAO dao = new STDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			STDTO dto = new STDTO();

			// id는 세션에 저장된 정보
			dto.setId(info.getId());
			
			// 파라미터
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dao.insertST(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/show/list");
	}
	
	
	@RequestMapping(value="/show/article", method =RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new ModelAndView("show/article");
	
	}
	
	
}
