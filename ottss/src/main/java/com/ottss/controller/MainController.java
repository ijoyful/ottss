package com.ottss.controller;

import java.io.IOException;
import java.util.List;

import com.ottss.dao.FAQDAO;
import com.ottss.dao.FreeDAO;
import com.ottss.dao.NoticeDAO;
import com.ottss.domain.FAQDTO;
import com.ottss.domain.FreeDTO;
import com.ottss.domain.NoticeDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("main/main");
		
		NoticeDAO noticeDAO = new NoticeDAO();
		List<NoticeDTO> listNotice = noticeDAO.listNotice(0, 3);
		FreeDAO freeDAO = new FreeDAO();
		List<FreeDTO> listFree = freeDAO.freeList(0, 3);
		FAQDAO qnaDAO = new FAQDAO();
		List<FAQDTO> listQnA = qnaDAO.listFAQ(0, 3);

		mav.addObject("listNotice", listNotice);
		mav.addObject("listFree", listFree);
		mav.addObject("listQnA", listQnA);
		
		return mav;
	}
}
