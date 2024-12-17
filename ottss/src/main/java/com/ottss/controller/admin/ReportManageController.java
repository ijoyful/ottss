package com.ottss.controller.admin;

import java.io.IOException;

import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ReportManageController {
	
	@RequestMapping(value = "/admin/")
	public ModelAndView 메소드명(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		ModelAndView mav = new ModelAndView("폴더명/파일명");
		
		return mav;
	}
	
}
