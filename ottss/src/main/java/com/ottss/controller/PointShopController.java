package com.ottss.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.ottss.dao.PointShopDAO;
import com.ottss.domain.PointShopDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PointShopController {
	
	@RequestMapping(value = "/shop/shop", method = RequestMethod.GET)
	public ModelAndView itemList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 상점 아이템 리스트
		// 넘어온 파라미터 : [페이지 번호, size]
		ModelAndView mav = new ModelAndView("shop/shop");
		
		PointShopDAO dao = new PointShopDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd,"utf-8");
			}
			
			// 한페이지 상품 수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 10 : Integer.parseInt(pageSize);
			
			int dataCount, total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<PointShopDTO> list;
			if(kwd.length() != 0) {
				list = dao.itemList(offset, size, schType, kwd);
			} else {
				list = dao.itemList(offset, size);
			}
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;
			
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			listUrl = cp + "/shop/shop?" + query;
			articleUrl = cp + "/shop/shop?page=" + current_page + "&" + query;

			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("itemList", list);
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
