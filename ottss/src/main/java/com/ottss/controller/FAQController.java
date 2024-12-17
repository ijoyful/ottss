package com.ottss.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ottss.dao.AdminReportDAO;
import com.ottss.dao.FAQDAO;
import com.ottss.domain.FAQDTO;
import com.ottss.domain.ReportDTO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.annotation.ResponseBody;
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
public class FAQController {
	// FAQ 게시글 리스트
	@RequestMapping(value = "/qna/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 넘어온 파라미터: [page, schType, kwd]
		ModelAndView mav = new ModelAndView("qna/list");
		FAQDAO dao = new FAQDAO();
		MyUtil util = new MyUtilBootstrap();

		try {
			String page = req.getParameter("page");
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}

			int size = 30;
			int dataCount, total_page;
			if (kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);

			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0) offset = 0;

			List<FAQDTO> list;
			if (kwd.length() != 0) {
				list = dao.listFAQ(offset, size, schType, kwd);
			} else {
				list = dao.listFAQ(offset, size);
			}

			List<FAQDTO> listfaq;
			listfaq = dao.listFAQ();

			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			listUrl = cp + "/qna/list?" + query;
			articleUrl = cp + "/qna/article?page=" + current_page + "&" + query;
			String paging = util.paging(current_page, total_page, listUrl);

			mav.addObject("list", list);
			mav.addObject("listfaq", listfaq);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 글 쓰기 폼
	@RequestMapping(value = "/qna/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("qna/write");
		mav.addObject("mode", "write");

		return mav;
	}

	// 글 쓰기 제출
	@RequestMapping(value = "/qna/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FAQDAO dao = new FAQDAO();
		String size = req.getParameter("size");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");

		FileManager fileManager = new FileManager();

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "qna";

		try {
			FAQDTO dto = new FAQDTO();
			dto.setQ_title(req.getParameter("title"));
			dto.setQ_content(req.getParameter("content"));
			dto.setUser_id(info.getId());

			List<MyMultipartFile> listFile = fileManager.doFileUpload(req.getParts(), pathname);
			dto.setListFile(listFile);

			long num = dao.insertQuestion(dto);
			return new ModelAndView("redirect:/qna/article?page=1&size" + size + "&num=" + num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/qna/list?size=" + size);
	}

	// 글 보기
	@RequestMapping(value = "/qna/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		MyUtil util = new MyUtilBootstrap();
		FAQDAO dao = new FAQDAO();

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			// 조회수
			dao.updateHitCount(num);
			// 게시물 가져오기
			FAQDTO dto = dao.findByNum(num);
			if (dto == null) {
				return new ModelAndView("redirect:/qna/list?" + query);
			}
			dto.setQ_content(util.htmlSymbols(dto.getQ_content()));
			if (dto.getA_content() != null) {
				dto.setA_content(util.htmlSymbols(dto.getA_content()));
			}

			FAQDTO prevDTO = dao.findByPrev(dto.getFaq_num(), schType, kwd);
			FAQDTO nextDTO = dao.findByNext(dto.getFaq_num(), schType, kwd);

			List<FAQDTO> listFile = dao.listFAQFile(num);

			ModelAndView mav = new ModelAndView("qna/article");
			mav.addObject("dto", dto);
			mav.addObject("prevDTO", prevDTO);
			mav.addObject("nextDTO", nextDTO);
			mav.addObject("listFile", listFile);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/qna/list?" + query);
	}

}
