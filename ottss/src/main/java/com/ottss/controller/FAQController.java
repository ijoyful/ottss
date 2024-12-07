package com.ottss.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.ottss.dao.FAQDAO;
import com.ottss.domain.FAQDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FAQController {
	// FAQ 게시글 리스트
	@RequestMapping(value = "/faq/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 넘어온 파라미터: [page, schType, kwd]
		ModelAndView mav = new ModelAndView("faq/list");
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
			listUrl = cp + "/faq/list?" + query;
			articleUrl = cp + "/faq/article?page" + current_page + "&" + query;
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
}
