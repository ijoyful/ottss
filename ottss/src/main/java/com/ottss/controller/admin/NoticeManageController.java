package com.ottss.controller.admin;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64.Decoder;
import java.util.List;

import com.ottss.dao.NoticeDAO;
import com.ottss.domain.NoticeDTO;
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
public class NoticeManageController {
	
	@RequestMapping(value = "/admin/notice/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		ModelAndView mav = new ModelAndView("admin/notice/list");
		
		/*
		 * int size = 10; NoticeDAO dao = new NoticeDAO(); MyUtil util = new
		 * MyUtilBootstrap();
		 * 
		 * try {
		 * 
		 * String page = req.getParameter("page"); int current_page = 1; if(page !=
		 * null) { current_page = Integer.parseInt(page); }
		 * 
		 * String schType = req.getParameter("schType"); String kwd =
		 * req.getParameter("kwd"); if(schType == null) { schType = "all"; kwd = ""; }
		 * 
		 * if(req.getMethod().equalsIgnoreCase("GET")) { kwd = URLDecoder.decode(kwd,
		 * "utf-8"); }
		 * 
		 * String pageSize = req.getParameter("size"); if(pageSize != null) { size =
		 * Integer.parseInt(pageSize); }
		 * 
		 * int dataCount, total_page;
		 * 
		 * if(kwd.length() != 0) { }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/notice/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String size = req.getParameter("size");

		ModelAndView mav = new ModelAndView("admin/notice/write");
		
		mav.addObject("size", size);
		mav.addObject("mode", "write");
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/notice/write", method = RequestMethod.POST)
	public ModelAndView writeFormSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		FileManager fileManager = new FileManager();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		NoticeDAO dao = new NoticeDAO();
		
		String size = req.getParameter("size");
		
		try {
			
			NoticeDTO dto = new NoticeDTO();
			
			dto.setId(info.getId());
			
			if(req.getParameter("notice_status") != null) {
				dto.setNotice_status(Integer.parseInt(req.getParameter("notice_status")));
			}
			dto.setTitle(req.getParameter("content"));
			dto.setContent(req.getParameter("content"));
			
			List<MyMultipartFile> listFile = fileManager.doFileUpload(req.getParts(), pathname);
			dto.setListFile(listFile);
			
			dao.insertNotice(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/notice/list?size=" + size);
	}
	
	@RequestMapping(value = "/admin/notice/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		ModelAndView mav = new ModelAndView("admin/notice/article");
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/notice/download", method = RequestMethod.GET)
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
	}
	
	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		ModelAndView mav = new ModelAndView("admin/notice/write");
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		return new ModelAndView("redirect:/admin/notice/list");
	}
	
	@RequestMapping(value = "/admin/notice/deleteFile")
	public ModelAndView deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		return new ModelAndView("redirect:/admin/notice/list");
	}
	
	@RequestMapping(value = "/admin/notice/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		return new ModelAndView("redirect:/admin/notice/list");
	}
	
	@RequestMapping(value = "/admin/notice/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		return new ModelAndView("redirect:/admin/notice/list");
	}

}
