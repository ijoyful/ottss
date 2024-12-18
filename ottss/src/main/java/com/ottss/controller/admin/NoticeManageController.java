package com.ottss.controller.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 게시글 리스트
		
		ModelAndView mav = new ModelAndView("admin/notice/list");

		int size = 10;
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtilBootstrap();

		try {

			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}

			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}
			
			// 한페이지에 표시할 데이터 개수
			String pageSize = req.getParameter("size");
			if (pageSize != null) {
				size = Integer.parseInt(pageSize);
			}

			int dataCount, total_page;
			
			if (kwd.length() != 0) {
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
			
			List<NoticeDTO> list;
			if(kwd.length() == 0) {
				list = dao.listNotice(offset, size);
			} else {
				list = dao.listNotice(offset, size, schType, kwd);
			}
			
			// 공지글
			List<NoticeDTO> listNotice = null;
			if(current_page == 1) {
				listNotice = dao.listNotice();
			}
			
			// 현재 시간과 게시글 시간 차이
			long gap;
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime today = LocalDateTime.now();
			for(NoticeDTO dto : list) {
				LocalDateTime dateTime = LocalDateTime.parse(dto.getReg_date(), fmt);
				gap = dateTime.until(today, ChronoUnit.DAYS);
				dto.setGap(gap);
				
				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			listUrl = cp + "/admin/notice/list?" + query;
			articleUrl = cp + "/admin/notice/article?page=" + current_page + "&" + query;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("listNotice", listNotice);
			mav.addObject("dataCount", dataCount);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			mav.addObject("paging", paging);
			mav.addObject("size", size);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/admin/notice/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 글 등록 폼
		
		// 한줄에 표시할 게시글 수
		String size = req.getParameter("size");

		ModelAndView mav = new ModelAndView("admin/notice/write");

		mav.addObject("size", size);
		mav.addObject("mode", "write");

		return mav;
	}

	@RequestMapping(value = "/admin/notice/write", method = RequestMethod.POST)
	public ModelAndView writeFormSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 글 저장
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		FileManager fileManager = new FileManager();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		NoticeDAO dao = new NoticeDAO();

		String size = req.getParameter("size");

		try {

			NoticeDTO dto = new NoticeDTO();

			dto.setId(info.getId());

			if (req.getParameter("notice_status") != null) {
				dto.setNotice_status(Integer.parseInt(req.getParameter("notice_status")));
			}
			dto.setTitle(req.getParameter("title"));
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
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 글 보기
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		NoticeDAO dao = new NoticeDAO();
	
		
		try {
			long n_num = Long.parseLong(req.getParameter("n_num"));
			
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
			
			// 조회수
			dao.updateHitCount(n_num);
			
			// 게시물 가져오기
			NoticeDTO dto = dao.findById(n_num);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?" + query);
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			// 이전글 다음글
			NoticeDTO prevDto = dao.findByPrev(dto.getN_num(), schType, kwd);
			NoticeDTO NextDto = dao.findByNext(dto.getN_num(), schType, kwd);
			
			List<NoticeDTO> listFile = dao.listNoticeFile(n_num);
			
			ModelAndView mav = new ModelAndView("admin/notice/article");
			
			mav.addObject("dto", dto);
			mav.addObject("prevDto", prevDto);
			mav.addObject("NextDto", NextDto);
			mav.addObject("listFile", listFile);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}

	@RequestMapping(value = "/admin/notice/download", method = RequestMethod.GET)
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 파일 다운로드
		
		NoticeDAO dao = new NoticeDAO();
		
		HttpSession session = req.getSession();
		
		FileManager fileManager = new FileManager();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		boolean b = false;
		
		try {
			
			long fileNum = Long.parseLong(req.getParameter("file_num"));
			
			NoticeDTO dto = dao.findByFileId(fileNum);
			
			if(dto != null) {
				b = fileManager.doFiledownload(dto.getS_fileName(), dto.getC_fileName(), pathname, resp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( ! b ) {
			resp.setContentType("text/html; charset=utf-8;");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일 다운로드가 실패했습니다.'); history.back(); </script>");
		}
		
	}

	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 수정 폼
		
		NoticeDAO dao = new NoticeDAO();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		
		try {
			
			long n_num = Long.parseLong(req.getParameter("n_num"));
			
			NoticeDTO dto = dao.findById(n_num);
			if(dto == null) {
				return new ModelAndView("admin/notice/list?page=" + page + "&size=" + size);
			}
			
			// 파일
			List<NoticeDTO> listFile = dao.listNoticeFile(n_num);
			
			ModelAndView mav = new ModelAndView("admin/notice/write");
			
			mav.addObject("dto", dto);
			mav.addObject("listFile", listFile);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			mav.addObject("mode", "update");
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ModelAndView("admin/notice/list?page=" + page + "&size=" + size);
	}

	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 수정 완료
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		FileManager fileManager = new FileManager();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		if(info.getPowerCode() < 51) {
			return new ModelAndView("redirect:/");
		}
		
		NoticeDAO dao = new NoticeDAO();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		
		try {
			
			NoticeDTO dto = new NoticeDTO();
			
			dto.setN_num(Long.parseLong(req.getParameter("n_num")));
			if(req.getParameter("notice_status") != null) {
				dto.setNotice_status(Integer.parseInt(req.getParameter("notice_status")));
			}
			
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			
			List<MyMultipartFile> listFile = fileManager.doFileUpload(req.getParts(), pathname);
			dto.setListFile(listFile);
			
			dao.updateNotice(dto);
			
			return new ModelAndView("redirect:/admin/notice/article?page=" + page + "&size=" + size + "&n_num=" + dto.getN_num());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("admin/notice/list?page=" + page + "&size=" + size);
	}

	@RequestMapping(value = "/admin/notice/deleteFile")
	public ModelAndView deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 파일만 수정 (수정 폼에서)
		
		HttpSession session = req.getSession();
		FileManager fileManager = new FileManager();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		NoticeDAO dao = new NoticeDAO();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			
			long n_num = Long.parseLong(req.getParameter("n_num"));
			long fileNum = Long.parseLong(req.getParameter("file_num"));
			
			NoticeDTO dto = dao.findByFileId(fileNum);
			
			if(dto != null) { // 파일이 존재하면
				// 파일 삭제
				fileManager.doFiledelete(pathname, dto.getS_fileName());
				
				// 테이블 정보 삭제
				dao.deleteNoticeFile("one", fileNum);
			}
			
			// 다시 수정 화면으로
			return new ModelAndView("redirect:/admin/notice/update?n_num=" + n_num + "&page=" + page + "&size=" + size);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
	}

	@RequestMapping(value = "/admin/notice/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 게시글 삭제
		
		HttpSession session = req.getSession();
		FileManager fileManager = new FileManager();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		NoticeDAO dao = new NoticeDAO();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			
			long n_num = Long.parseLong(req.getParameter("n_num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all"	;
				kwd = "";
			}
			
			kwd = URLDecoder.decode(kwd, "utf-8");
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			// 파일 삭제
			List<NoticeDTO> listFile = dao.listNoticeFile(n_num);
			for(NoticeDTO dto : listFile) {
				fileManager.doFiledelete(pathname, dto.getS_fileName());
			}
			
			
			// 파일 테이블 삭제
			dao.deleteNoticeFile("all", n_num);
			
			// 게시글 삭제
			dao.deleteNotice(n_num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}

	@RequestMapping(value = "/admin/notice/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// checkBox 리스트 삭제
		
		HttpSession session = req.getSession();
		FileManager fileManager = new FileManager();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		NoticeDAO dao = new NoticeDAO();
		
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
			
			// 파일 삭제 및 파일 테이블 삭제
			for(int i=0; i<nums.length; i++) {
				
				List<NoticeDTO> listFile = dao.listNoticeFile(nums[i]);
				
				for(NoticeDTO dto : listFile) {
					fileManager.doFiledelete(pathname, dto.getS_fileName());
				}
				
				dao.deleteNoticeFile("all", nums[i]);
			}
			
			// 게시글 삭제
			dao.deleteNotice(nums);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}

}
