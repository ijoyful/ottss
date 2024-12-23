package com.ottss.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.mvc.annotation.ResponseBody;
import com.ottss.dao.MemberDAO;
import com.ottss.domain.MemberDTO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@RequestMapping(value = "/login/login", method = RequestMethod.GET)
	public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//로그인 폼
		ModelAndView mav = new ModelAndView("login/login");
		return mav;
	}
	
	@RequestMapping(value = "/login/login", method = RequestMethod.POST)
	public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 처리
		
		// 세션 객체
		HttpSession session = req.getSession();
		
		MemberDAO dao = new MemberDAO();
		
		// 클라이언트가 보낸 아이디/패스워드
		String id = req.getParameter("id");
		String pwd = req.getParameter("pwd");
		
		MemberDTO dto = dao.loginMember(id, pwd);
		if(dto != null && dto.getBlock() == 0) {
			// 로그인 성공한 경우
			// 세션에 아이디, 이름, 권한등을 저장
			
			// 세션 유지시간을 60분으로 설정 : 톰켓 기본은 30분
			session.setMaxInactiveInterval(60 * 60);
			
			// 세션에 저장할 정보
			SessionInfo info = new SessionInfo();
			info.setId(dto.getId());
			info.setNickName(dto.getNickName());
			info.setPowerCode(dto.getPowercode());
	
			
			// 세션에 member 라는 이름으로 로그인 정보를 저장
			session.setAttribute("member", info);
			
			// 로그인 전 주소가 존재하는 경우
			String preLoginURI = (String)session.getAttribute("preLoginURI");
			session.removeAttribute("preLoginURI");
			if(preLoginURI != null) {
				return new ModelAndView(preLoginURI);
			}
			
			// 메인 화면으로 리다이렉트
			return new ModelAndView("redirect:/");
		}
		
		// 아이디 또는 패스워드가 일치하지 않은 경우
		// 다시 로그인 폼으로 포워딩
		ModelAndView mav = new ModelAndView("login/login");
		mav.addObject("message", "아이디 또는 패스워드가 일치하지 않습니다.");
		return mav;
	}	
	
	
	@RequestMapping(value = "/login/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그 아웃
		HttpSession session = req.getSession();
		
		// 세션에 저장된 정보 지우기
		session.removeAttribute("member");
		
		// 세션에 저장된 모든 내용을 지우고 세션을 초기화
		session.invalidate();

		// 메인화면으로 리다이렉트
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/login/noAuthorized", method = RequestMethod.GET)
	public ModelAndView noAuthorized(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 권한이 없는 경우
		ModelAndView mav = new ModelAndView("member/noAuthorized");
		return mav;
	}	
	
	@RequestMapping(value = "/login/member", method = RequestMethod.GET)
	public ModelAndView memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 폼
		ModelAndView mav = new ModelAndView("login/member");
		
		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");

		return mav;
	}
	
	@RequestMapping(value = "/login/member", method = RequestMethod.POST)
	public ModelAndView memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 처리
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		String message = "";
		try {
			MemberDTO dto = new MemberDTO();
			dto.setId(req.getParameter("id"));
			dto.setPwd(req.getParameter("pwd"));
			dto.setName(req.getParameter("name"));
			dto.setNickName(req.getParameter("nickName"));
			dto.setBirth(req.getParameter("birth"));
			dto.setRecommendId(req.getParameter("recommendId"));
			
			dto.setEmail1(req.getParameter("email1"));
			dto.setEmail2(req.getParameter("email2"));

			dto.setTel1(req.getParameter("tel1"));
			dto.setTel2(req.getParameter("tel2"));
			dto.setTel3(req.getParameter("tel3"));

		
			dao.insertMember(dto);
			
			
			String recoId = req.getParameter("recommendId");
			
			//추천인아이디 있는지 확인해야됨....
			if(dao.findById(recoId) != null) {
				dao.updateRecommend(dto);
			}
			
			
			session.setAttribute("mode", "insert");
			session.setAttribute("nickName", dto.getNickName());
			
			return new ModelAndView("redirect:/login/complete");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1) {
				message = "아이디 중복으로 회원 가입이 실패 했습니다.";
			} else if (e.getErrorCode() == 1400) {
				message = "필수 사항을 입력하지 않았습니다.";
			} else if (e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				message = "날짜 형식이 일치하지 않습니다.";
			} else {
				message = "회원 가입이 실패 했습니다.";
			// 기타 - 2291:참조키 위반, 12899:폭보다 문자열 입력 값이 큰경우
			}
		} catch (Exception e) {
			message = "회원 가입이 실패 했습니다.";
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("login/member");
		
		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");
		mav.addObject("message", message);
		
		return mav;
	}	
	
	@RequestMapping(value = "/login/pwd", method = RequestMethod.GET)
	public ModelAndView pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인 폼
		ModelAndView mav = new ModelAndView("login/pwd");
		
		String mode = req.getParameter("mode");
		mav.addObject("mode", mode);

		return mav;
	}	
	
	
	@RequestMapping(value = "/login/pwd", method = RequestMethod.POST)
	public ModelAndView pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			//DB에서 회원 정보 가져오기
			MemberDTO dto = dao.findById(info.getId());
			if (dto == null) {
				session.invalidate();
				return new ModelAndView("redirect:/");
			}
		
			
			String pwd = req.getParameter("pwd");
			String mode = req.getParameter("mode");
			if(! dto.getPwd().equals(pwd)) {
				ModelAndView mav = new ModelAndView("login/pwd");
			
				mav.addObject("mode", mode);
				mav.addObject("message", "패스워드가 일치하지 않습니다.");
				
				return mav;
			}
			
			
			if(mode.equals("delete")) {
				//회원탈퇴
				dao.deleteMember(info.getId());
				
				session.removeAttribute("member");
				session.invalidate();
				
				return new ModelAndView("redirect:/?message=success");
			}
			
			
			//회원정보수정 - 회원정보수정폼으로 이동
			ModelAndView mav = new ModelAndView("login/member");
			
			mav.addObject("title", "회원 정보 수정");
			mav.addObject("dto", dto);
			mav.addObject("mode", "update");
			
			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/");
	}
	
	
	@RequestMapping(value = "/login/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원정보 수정 완료
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		try {
			MemberDTO dto = new MemberDTO();
			
			dto.setId(req.getParameter("id"));
			dto.setPwd(req.getParameter("pwd"));
			dto.setBirth(req.getParameter("birth"));
			dto.setTel1(req.getParameter("tel1"));
			dto.setTel2(req.getParameter("tel2"));
			dto.setTel3(req.getParameter("tel3"));
			dto.setEmail1(req.getParameter("email1"));
			dto.setEmail2(req.getParameter("email2"));
			
			dao.updateMember(dto);
			
			session.setAttribute("mode", "update");
			session.setAttribute("name", dto.getName());
			return new ModelAndView("redirect:/login/complete");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/login/complete", method = RequestMethod.GET)
	public ModelAndView complete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String mode = (String)session.getAttribute("mode");
		String nickName = (String)session.getAttribute("nickName");
		
		session.removeAttribute("mode");
		session.removeAttribute("userName");
		
		if(mode == null) {
			return new ModelAndView("redirect:/");
		}
		
		String title;
		String message = "<b>" + nickName + "</b>님 ";
		if(mode.equals("insert")) {
			title = "회원 가입";
			message += "회원가입이 완료 되었습니다.<br>로그인 하시면 정보를 이용하실수 있습니다.";
		} else {
			title = "회원 정보 수정";
			message += "회원정보가 수정 되었습니다.<br>메인 화면으로 이동하시기 바랍니다.";
		}

		ModelAndView mav = new ModelAndView("login/complete");

		mav.addObject("title", title);
		mav.addObject("message", message);
		
		return mav;
	}
	
	@ResponseBody //AJAX : JSON 으로 반환
	@RequestMapping(value = "/login/userIdCheck", method=RequestMethod.POST)
	public Map<String, Object> userIdCheck (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//아이디 중복 검사
		MemberDAO dao = new MemberDAO();
		
		String id = req.getParameter("id");
		MemberDTO dto = dao.findById(id);
		String passed = "false";
		if(dto == null) {
			passed = "true";
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("passed", passed);
		
		return model;
	}
	
	
	@ResponseBody //AJAX : JSON 으로 반환
	@RequestMapping(value = "/login/userNickCheck", method=RequestMethod.POST)
	public Map<String, Object> userNickCheck (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//닉네임 중복 검사
		MemberDAO dao = new MemberDAO();
		
		String nickName = req.getParameter("nickName");
		MemberDTO dto = dao.findByNick(nickName);
		String passed = "false";
		if(dto == null) {
			passed = "true";
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("passed", passed);
		
		return model;
	}
	
	@RequestMapping(value = "/player/mypage", method = RequestMethod.GET)
	public ModelAndView mypage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 마이페이지
		ModelAndView mav = new ModelAndView("player/mypage");
		return mav;
	}
	
}
