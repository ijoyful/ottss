package com.ottss.controller.admin;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ottss.dao.AdminReportDAO;
import com.ottss.dao.FreeDAO;
import com.ottss.domain.FreeComDTO;
import com.ottss.domain.FreeDTO;
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
public class FreeManagecontroller {
	@RequestMapping("/admin/freeboard/list") // GET, POST 모두 처리
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트 : - [page],[schType, kwd]
		ModelAndView mav = new ModelAndView("admin/freeboard/list");
		
		int size = 10;
		FreeDAO dao = new FreeDAO();
		MyUtil util = new MyUtilBootstrap();

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			// 검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			// GET 방식이면 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}
			
			// 한페이지에 표시할 데이터 개수
			String pageSize = req.getParameter("size");
			if (pageSize != null) {
				size = Integer.parseInt(pageSize);
			}

			// 데이터 개수
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount(); // 검색이 아닌경우
			} else {
				dataCount = dao.dataCount(schType, kwd); // 검색인 경우
			}

			// 전체 페이지 수
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if (offset < 0) offset = 0;

			List<FreeDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.freeList(offset, size);
			} else {
				list = dao.freeList(offset, size, schType, kwd);
			}
			// 쿼리
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/admin/freeboard/list";
					// 글 리스트 주소
			String articleUrl = cp + "/admin/freeboard/article?page=" + current_page;
						// 글보기 주소
			if(query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			// 페이징
			String paging = util.paging(current_page, total_page, listUrl);
			
			
			// 포워딩할 JSP에 전달할 속성
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

		}
		return mav;
	}
	// 글 쓰기 폼
		@RequestMapping(value = "/admin/freeboard/write", method = RequestMethod.GET)
		public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			ModelAndView mav = new ModelAndView("admin/freeboard/write");
			mav.addObject("mode", "write");
			return mav;
		}

		// 글 쓰기 제출
		@RequestMapping(value = "/admin/freeboard/write", method = RequestMethod.POST)
		public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			FreeDAO dao = new FreeDAO();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");

			FileManager fileManager = new FileManager();

			String root = session.getServletContext().getRealPath("/");
			String pathname = root + "uploads" + File.separator + "freeboard";

			try {
				FreeDTO dto = new FreeDTO();
				
				dto.setId(info.getId());
				
				dto.setTitle(req.getParameter("title"));
				dto.setContent(req.getParameter("content"));

				List<MyMultipartFile> listFile = fileManager.doFileUpload(req.getParts(), pathname);
				dto.setListFile(listFile);
				
				dao.insertBoard(dto);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return new ModelAndView("redirect:/admin/freeboard/list?");
		}
	
	// 글 보기
	@RequestMapping(value = "/admin/freeboard/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		FreeDAO dao = new FreeDAO();

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
			FreeDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/freeboard/list?" + query);
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			if (dto.getContent() != null) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			FreeDTO prevDTO = dao.findByPrev(dto.getFb_num(), schType, kwd);
			FreeDTO nextDTO = dao.findByNext(dto.getFb_num(), schType, kwd);

			List<FreeDTO> listFile = dao.listFreeFile(num);

			ModelAndView mav = new ModelAndView("admin/freeboard/article");
			
			mav.addObject("dto", dto);
			mav.addObject("prevDTO", prevDTO);
			mav.addObject("nextDTO", nextDTO);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("listFile", listFile);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/freeboard/list?" + query);
	}
	
	@RequestMapping(value="/admin/freeboard/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//수정폼
		FreeDAO dao = new FreeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			long num = Long.parseLong(req.getParameter("fb_num"));
			FreeDTO dto = dao.findById(num);
			
			if(dto == null) {
				return new ModelAndView("redirect:/admin/freeboard/list?page=" + page);
			}
			
			if (!dto.getId().equals(info.getId())) {
				return new ModelAndView("redirect:/admin/freeboard/list?page=" + page);
			}
			
			ModelAndView mav = new ModelAndView("admin/freeboard/write");
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/admin/freeboard/list?page=" + page);
		
	}
	
	
	@RequestMapping(value = "/admin/freeboard/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정 완료
		FreeDAO dao = new FreeDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String page = req.getParameter("page");
		try {
			FreeDTO dto = new FreeDTO();

			dto.setFb_num(Long.parseLong(req.getParameter("fb_num")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dto.setId(info.getId());
			
			
			dao.updateFree(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/freeboard/list?page=" + page);
	}
	
	@RequestMapping(value = "/admin/freeboard/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//삭제
		FreeDAO dao = new FreeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			long num = Long.parseLong(req.getParameter("fb_num"));
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
			
			dao.deleteFree(num, info.getId(), info.getPowerCode());	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/freeboard/list?" + query);
	}	
	
	
	// 댓글 저장 - AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/admin/freeboard/insertComment", method=RequestMethod.POST)
	public Map<String, Object> insertComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String state = "false";
		FreeDAO dao = new FreeDAO();
		HttpSession session = req.getSession();
		
		try {
			
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			FreeComDTO dto = new FreeComDTO();
			
			dto.setFb_num(Long.parseLong(req.getParameter("fb_num")));
			dto.setContent(req.getParameter("content"));
			
			dto.setId(info.getId());
			
			dao.insertComment(dto);
			
			state = "true";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);
		
		return model;
		
	}
	
	// 댓글 리스트 - AJAX : Test
	@RequestMapping(value = "/admin/freeboard/listComment", method = RequestMethod.GET)
	public ModelAndView listComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		//넘어오는 파라미터 : num, pageNo
		
		FreeDAO dao = new FreeDAO();
		MyUtil util = new MyUtilBootstrap();
		
		//HttpSession session = req.getSession(); 댓글 좋아요 구현하려면 필요함!! id
		
		try {
			//SessionInfo info = (SessionInfo)session.getAttribute("member"); 댓글 좋아요 구현하려면 필요함!!
			long num = Long.parseLong(req.getParameter("fb_num"));
			String pageNo = req.getParameter("pageNo");
			int current_page = 1;
			if(pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}
			int size = 5;
			int total_page = 0;
			int replyCount = 0;
			
			replyCount = dao.dataCountComment(num);
			total_page = util.pageCount(replyCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1)* size;
			if(offset < 0) offset = 0;			
			
			List<FreeComDTO> list = dao.listComment(num, offset, size);
			for(FreeComDTO dto : list) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			
			String paging = util.pagingMethod(current_page, total_page, "listPage");
			
			ModelAndView mav = new ModelAndView("admin/freeboard/listComment");
			mav.addObject("listComment", list);
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
	@RequestMapping(value = "/admin/freeboard/deleteComment", method = RequestMethod.POST)
	public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
		//넘어온 파라미터 : fbc_num
		
		Map<String, Object> model = new HashMap<String, Object>();
		FreeDAO dao = new FreeDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String state = "false";
		
		try {
			long fbc_num = Long.parseLong(req.getParameter("fbc_num"));
			dao.deleteComment(fbc_num, info.getId(), info.getPowerCode());
			state = "true";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);	
		
		return model;
	
	}
	
	//게시글 공감 저장
	@ResponseBody
	@RequestMapping(value = "/admin/freeboard/insertFreeLike", method = RequestMethod.POST)
	public Map<String, Object> insertFreeLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//넘어온 파라미터 : fb_num, 공감/취소여부
		Map<String, Object> model = new HashMap<String, Object>();
		
		FreeDAO dao = new FreeDAO();
		
		HttpSession session = req.getSession();
		
		String state = "false";
		int likeCount = 0;
		
		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			long fb_num = Long.parseLong(req.getParameter("fb_num"));
			String userLiked = req.getParameter("userLiked");
			String id = info.getId();
			
			if(userLiked.equals("true")) {
				dao.deleteFreeLike(fb_num, id);
			} else {
				dao.insertFreeLike(fb_num, id);
			}
			likeCount = dao.countFreeLike(fb_num);
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

	@RequestMapping(value = "/admin/freeboard/report", method = RequestMethod.GET)
	public ModelAndView reportForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/freeboard/report");
		mav.addObject("num", req.getParameter("num"));
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/admin/freeboard/report", method = RequestMethod.POST)
	public Map<String, Object> reportSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		AdminReportDAO dao = new AdminReportDAO();
		String state = "false";

		try {
			ReportDTO dto = new ReportDTO();
			dto.setTarget_num(Long.parseLong(req.getParameter("num")));
			dto.setId(req.getParameter("id"));
			dto.setReport_reason(req.getParameter("reason"));
			dto.setTarget_table("free_board");

			dao.insertReport(dto);

			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);
		
		return model;
	}
	
	@RequestMapping(value = "/admin/freeboard/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// checkBox 리스트 삭제
		
		FreeDAO dao = new FreeDAO();
		
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
			dao.deleteFree(nums);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/freeboard/list?" + query);
	}
}
