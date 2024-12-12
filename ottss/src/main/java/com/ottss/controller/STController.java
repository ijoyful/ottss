package com.ottss.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.List;

import com.ottss.dao.STDAO;
import com.ottss.domain.STDTO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.FileManager;
import com.ottss.util.MyMultipartFile;
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
			int size = 5;
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
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼

		ModelAndView mav = new ModelAndView("show/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/show/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글등록하기 : 넘어온 파라미터 - subject, content

		STDAO dao = new STDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		//파일 처리
		FileManager filemanager = new FileManager();
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "show";
		
		try {
			STDTO dto = new STDTO();

			// id는 세션에 저장된 정보
			dto.setId(info.getId());
			// 파라미터
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			
			List<MyMultipartFile> listFile = filemanager.doFileUpload(req.getParts(), pathname);
			dto.setListFile(listFile);
			
			dao.insertST(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/show/list");
	}
	
	
	@RequestMapping(value="/show/article", method =RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글보기
		
		STDAO dao = new STDAO();
		MyUtil util = new MyUtilBootstrap();
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			long num = Long.parseLong(req.getParameter("st_num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType ="all";
				kwd="";
			}
			kwd = URLDecoder.decode(kwd,"utf-8");
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd,"UTF-8");
			}
			
			dao.updateHitCount(num);
			
			STDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/show/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent())); //???
			
			STDTO prevDto = dao.findByPrev(dto.getSt_num(), schType, kwd);
			STDTO nextDto = dao.findByNext(dto.getSt_num(), schType, kwd);
			
			ModelAndView mav = new ModelAndView("show/article");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return new ModelAndView("redirect:/show/list?" + query);
	
	}
	
	
	@RequestMapping(value="/show/update", method =RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//수정폼
		STDAO dao = new STDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			long num = Long.parseLong(req.getParameter("st_num"));
			STDTO dto = dao.findById(num);
			
			if(dto == null) {
				return new ModelAndView("redirect:/show/list?page=" + page);
			}
			
			if (!dto.getId().equals(info.getId())) {
				return new ModelAndView("redirect:/bbs/list?page=" + page);
			}
			
			ModelAndView mav = new ModelAndView("show/write");
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/show/list?page=" + page);
	}
	
	
	@RequestMapping(value = "/show/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정 완료
		STDAO dao = new STDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String page = req.getParameter("page");
		try {
			STDTO dto = new STDTO();

			dto.setSt_num(Long.parseLong(req.getParameter("st_num")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dto.setId(info.getId());
			
			
			dao.updateST(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/show/list?page=" + page);
	}
	
	@RequestMapping(value = "/show/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//삭제
		STDAO dao = new STDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			long num = Long.parseLong(req.getParameter("st_num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");
			
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}
			
			dao.deleteST(num, info.getId(), info.getPowerCode());	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/show/list?" + query);
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
