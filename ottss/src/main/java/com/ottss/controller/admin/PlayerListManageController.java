package com.ottss.controller.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ottss.dao.AdminMemberDAO;
import com.ottss.domain.MemberDTO;
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

@Controller
public class PlayerListManageController {
	@RequestMapping(value = "/admin/player/list")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/player/playerList");

		AdminMemberDAO dao = new AdminMemberDAO();
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
				dataCount = dao.userCount(false);
			} else {
				dataCount = dao.userCount(schType, kwd, false);
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
			
			List<MemberDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listMember(offset, size, false);
			} else {
				list = dao.listMember(offset, size, schType, kwd, false);
			}
			
			// 쿼리
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/admin/player/playerList";
			
			if(query.length() != 0) {
				listUrl += "?" + query;
			}
			
			// 페이징
			String paging = util.paging(current_page, total_page, listUrl);
			
			mav.addObject("list", list);
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
	
	@ResponseBody
	@RequestMapping(value = "/admin/player/toggleBlock", method = RequestMethod.POST)
	public Map<String, Object> toggleBlockUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Map<String, Object> model = new HashMap<String, Object>();
		AdminMemberDAO dao = new AdminMemberDAO();
		
		try {
			
			String id = req.getParameter("id");
			int block = Integer.parseInt(req.getParameter("block"));
			
			int toggleBlock = block == 0 ? 1 : 0;
			
			dao.toggleUserBlind(id, toggleBlock);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
		
	}
}
