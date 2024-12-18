package com.ottss.controller.admin;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ottss.dao.AdminReportDAO;
import com.ottss.dao.ShowDAO;
import com.ottss.domain.ReportDTO;
import com.ottss.domain.STComDTO;
import com.ottss.domain.STDTO;
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
public class ShowManageController {
	
	@RequestMapping("/admin/show/list") //GET,POST 둘다 처리
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServerException, SQLException{
		//게시글 리스트 : 파라미터,[page,schType,kwd]
		
		ModelAndView mav = new ModelAndView("admin/show/list");
		 
		int size = 10;
		ShowDAO dao = new ShowDAO();
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
			
			// 한페이지에 표시할 데이터 개수
			String pageSize = req.getParameter("size");
			if (pageSize != null) {
				size = Integer.parseInt(pageSize);
			}
			
			//데이터 개수
			int dataCount;
			if(kwd.length() == 0) {
				dataCount = dao.dataCount();
			}else {
				dataCount = dao.dataCount(schType, kwd);
			}
			
			//전체 페이지 수
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			//게시물 가져오기
			int offset = (current_page -1) * size;
			if(offset < 0) offset = 0;
			
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
			String listUrl = cp + "/admin/show/list";
			//글리스트 주소
			String articleUrl = cp + "/admin/show/article?page=" + current_page;
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
	
	@RequestMapping(value = "/admin/show/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼

		ModelAndView mav = new ModelAndView("admin/show/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/admin/show/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글등록하기 : 넘어온 파라미터 - subject, content

		ShowDAO dao = new ShowDAO();

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

		return new ModelAndView("redirect:/admin/show/list");
	}
	
	
	@RequestMapping(value="/admin/show/article", method =RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글보기
		
		ShowDAO dao = new ShowDAO();
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
				return new ModelAndView("redirect:/admin/show/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent())); //???
			
			STDTO prevDto = dao.findByPrev(dto.getSt_num(), schType, kwd);
			STDTO nextDto = dao.findByNext(dto.getSt_num(), schType, kwd);
			
			//파일
			List<STDTO> listFile = dao.listSTFile(num);
			
			//게시물공감여부
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			boolean isUserLike = dao.isUserShowLike(num, info.getId());
			
			ModelAndView mav = new ModelAndView("admin/show/article");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("listFile", listFile);
			mav.addObject("isUserLike", isUserLike);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return new ModelAndView("redirect:/admin/show/list?" + query);
	
	}
	
	
	@RequestMapping(value="/admin/show/update", method =RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//수정폼
		ShowDAO dao = new ShowDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			long num = Long.parseLong(req.getParameter("st_num"));
			STDTO dto = dao.findById(num);
			
			if(dto == null) {
				return new ModelAndView("redirect:/admin/show/list?page=" + page);
			}
			
			if (!dto.getId().equals(info.getId())) {
				return new ModelAndView("redirect:/admin/show/list?page=" + page);
			}
			
			ModelAndView mav = new ModelAndView("admin/show/write");
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/show/list?page=" + page);
	}
	
	
	@RequestMapping(value = "/admin/show/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정 완료
		ShowDAO dao = new ShowDAO();

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

		return new ModelAndView("redirect:/admin/show/list?page=" + page);
	}
	
	@RequestMapping(value = "/admin/show/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//삭제
		ShowDAO dao = new ShowDAO();
		
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
		
		return new ModelAndView("redirect:/admin/show/list?" + query);
	}	
	
	
	// 댓글 저장 - AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/admin/show/insertReply", method=RequestMethod.POST)
	public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String state = "false";
		ShowDAO dao = new ShowDAO();
		HttpSession session = req.getSession();
		
		try {
			
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			STComDTO dto = new STComDTO();
			
			dto.setSt_num(Long.parseLong(req.getParameter("st_num")));
			dto.setContent(req.getParameter("content"));
			
			dto.setId(info.getId());
			
			dao.insertReply(dto);
			
			state = "true";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);
		
		return model;
		
	}
	
	// 댓글 리스트 - AJAX : Test
	@RequestMapping(value = "/admin/show/listReply", method = RequestMethod.GET)
	public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		//넘어오는 파라미터 : num, pageNo
		
		ShowDAO dao = new ShowDAO();
		MyUtil util = new MyUtilBootstrap();
		
		//HttpSession session = req.getSession(); 댓글 좋아요 구현하려면 필요함!! id
		
		try {
			//SessionInfo info = (SessionInfo)session.getAttribute("member"); 댓글 좋아요 구현하려면 필요함!!
			long num = Long.parseLong(req.getParameter("st_num"));
			String pageNo = req.getParameter("pageNo");
			int current_page = 1;
			if(pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}
			int size = 5;
			int total_page = 0;
			int replyCount = 0;
			
			replyCount = dao.dataCountReply(num);
			total_page = util.pageCount(replyCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page -1)* size;
			if(offset < 0) offset = 0;			
			
			List<STComDTO> list = dao.listReply(num, offset, size);
			
			for(STComDTO dto : list) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			
			String paging = util.pagingMethod(current_page, total_page, "listPage");
			
			ModelAndView mav = new ModelAndView("admin/show/listReply");
			mav.addObject("listReply", list);
			mav.addObject("pageNo",current_page);
			mav.addObject("replyCount", replyCount);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			
			return mav;			
		} catch (Exception e) {

			resp.sendError(406);
			throw e;
		}
		
		
		
	}
	
	//댓글 삭제 - AJAX : JSON
	@ResponseBody
	@RequestMapping(value = "/admin/show/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
		//넘어온 파라미터 : stc_num
		
		Map<String, Object> model = new HashMap<String, Object>();
		ShowDAO dao = new ShowDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String state = "false";
		
		try {
			long stc_num = Long.parseLong(req.getParameter("stc_num"));
			dao.deleteReply(stc_num, info.getId(), info.getPowerCode());
			state = "true";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);	
		
		return model;
	
	}
	
	//게시글 공감 저장
	@ResponseBody
	@RequestMapping(value = "/admin/show/insertShowLike", method = RequestMethod.POST)
	public Map<String, Object> insertShowLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//넘어온 파라미터 : st_num, 공감/취소여부
		Map<String, Object> model = new HashMap<String, Object>();
		
		ShowDAO dao = new ShowDAO();
		
		HttpSession session = req.getSession();
		
		String state = "false";
		int likeCount = 0;
		
		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			long st_num = Long.parseLong(req.getParameter("st_num"));
			String userLiked = req.getParameter("userLiked");
			String id = info.getId();
			
			if(userLiked.equals("true")) {
				dao.deleteShowLike(st_num, id);
			} else {
				dao.insertShowLike(st_num, id);
			}
			likeCount = dao.countShowLike(st_num);
			state = "true";
		} catch (SQLException e) {
			state = "liked";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);
		model.put("likeCount", likeCount);
		
		return model;
	}

	@RequestMapping(value = "/admin/show/report", method = RequestMethod.GET)
	public ModelAndView reportForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("show/report");
		mav.addObject("num", req.getParameter("num"));
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/admin/show/report", method = RequestMethod.POST)
	public Map<String, Object> reportSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		AdminReportDAO dao = new AdminReportDAO();
		String state = "false";

		try {
			ReportDTO dto = new ReportDTO();
			dto.setTarget_num(Long.parseLong(req.getParameter("num")));
			dto.setId(req.getParameter("id"));
			dto.setReport_reason(req.getParameter("reason"));
			dto.setTarget_table("SHOW_TIP_BOARD");

			dao.insertReport(dto);

			state = "true";
		} catch (SQLException e) {
			if (e.getErrorCode() == 1) {
				String msg = "이미 신고한 게시물입니다.";
				model.put("msg", msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			state = "false";
		}
		
		model.put("state", state);
		
		return model;
	}
	
	@RequestMapping(value = "/admin/show/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// checkBox 리스트 삭제
		
		ShowDAO dao = new ShowDAO();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			kwd = URLDecoder.decode(kwd, "utf-8");
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			String [] nn = req.getParameterValues("nums");
			long []nums = new long[nn.length];
			
			for(int i=0; i<nn.length; i++) {
				nums[i] = Long.parseLong(nn[i]);
			}
			
			// 게시글 삭제
			dao.deleteShow(nums);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/show/list?" + query);
	}
	
}
