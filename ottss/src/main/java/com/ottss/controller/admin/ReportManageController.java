package com.ottss.controller.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.ottss.dao.AdminReportDAO;
import com.ottss.domain.ReportDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ReportManageController {
	@RequestMapping("/admin")
	public ModelAndView 메소드명(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		ModelAndView mav = new ModelAndView("admin/report/reportList");
		
		AdminReportDAO dao = new AdminReportDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			// 검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			// 디코딩
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}
			
			// 데이터 개수
			int dataCount;
			if(kwd.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(schType, kwd);
			}
			
			// 전체 페이지
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<ReportDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listReport(offset, size);
			} else {
				list = dao.listReport(offset, size, schType, kwd);
			}
			
			for (ReportDTO dto : list) {
				
				if(dto.getTarget_table().equals("SHOW_TIP_BOARD")) {
					dto.setTarget_table("자랑/분석 게시판");
				} else if(dto.getTarget_table().equals("free_board")) {
					dto.setTarget_table("자유 게시판");
				}
				
			}
			
			// 쿼리
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd" + URLEncoder.encode(kwd, "utf-8");
			}
			
			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/admin/report/reportList";
			
			if(query.length() != 0) {
				listUrl += "?" + query;
			}
			
			// 페이징
			String paging = util.paging(current_page, total_page, listUrl);
			
			mav.addObject("reportList", list);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
}
