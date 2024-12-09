package com.ottss.filter;

import java.io.IOException;

import com.ottss.domain.SessionInfo;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//페이지이름 아직 안정해져서 isExcludeUrl 아직 안건듬

//@WebFilter("/*")
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// request 필터 : 선 작업

		// 로그인 체크
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		HttpSession session = req.getSession();

		String uri = req.getRequestURI();
		String cp = req.getContextPath();

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null && isExcludeUrl(req) == false) {
			// 로그인이 필요한 메뉴를 로그인 하지 않은 유저가 접속한 경우
			if (isAjaxRequest(req)) {
				// AJAX 접속인 경우
				resp.sendError(403);
			} else {
				// AJAX 접속이 아닌 경우

				// uri 에서 cp 제거
				if (uri.indexOf(req.getContextPath()) == 0) {
					uri = uri.substring(req.getContextPath().length());
				}
				uri = "redirect:" + uri;

				String queryString = req.getQueryString();
				if (queryString != null) {
					uri += "?" + queryString;
				}

				// 로그인 전 주소를 세션에 저장
				session.setAttribute("preLoginURI", uri);

				resp.sendRedirect(cp + "/member/login");
			}

			return;

		} else if (info != null && uri.indexOf("admin") != -1) {
			// 관리자 메뉴를 userLevel이 51 미만인 유저가 접속한 경우
			if (info.getPowerCode() < 51) {
				resp.sendRedirect(cp + "/member/noAuthorized");
				return;
			}
		}

		// 다음 필터 또는 필터의 마지막이면 end-pointer(서블릿, jsp)등 실행
		chain.doFilter(request, response);

		// response 필터 : 후 작업

	}


	// 요청이 ajax 인지를 확인하는 메소드
	private boolean isAjaxRequest(HttpServletRequest req) {
		String h = req.getHeader("AJAX");

		return h != null && h.equals("true");
	}
	
	private boolean isExcludeUrl(HttpServletRequest req) {
		// TODO Auto-generated method stub
		return false;
	}
}
