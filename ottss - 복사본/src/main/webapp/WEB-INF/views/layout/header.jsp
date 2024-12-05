<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<header id="header">
    <div class="headerInner">
        <div class="info">
            <div class="logo"><p>삼식이네 놀이터</p></div>
            <div class="btnWrap">
                <div class="login"></div>
                <div class="mypage">
                    <a href="${pageContext.request.contextPath}/login/login">로그인</a> | 
                    <a href="${pageContext.request.contextPath}/login/register">회원가입</a>
                    <a href="${pageContext.request.contextPath}/player/mypage">마이페이지</a>
                </div>
            </div>
        </div>
		<nav id="nav">
			<div><a href="${pageContext.request.contextPath}/">홈</a></div>
			<div><a href="${pageContext.request.contextPath}/introduce/sogae">놀이터 소개</a></div>
			<div class="drop-down">
				<a href="${pageContext.request.contextPath}/game/gameList">게임</a>
				<ul class="dropdownMenu">
					<li><a href="${pageContext.request.contextPath}/">가위바위보</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">두더지</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">룰렛</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">퀴즈</a></li>
				</ul>
			</div>
	        <div class="drop-down">
	        	<a href="${pageContext.request.contextPath}/board/AllList">커뮤니티</a>
	        	<ul class="dropdownMenu">
	        		<li><a href="${pageContext.request.contextPath}/">자유게시판</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">분석게시판</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">자랑게시판</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">FAQ</a></li>
	        	</ul>
	        </div>
	        <div class="drop-down">
	        	<a href="${pageContext.request.contextPath}/shop/shop">상점</a>
	        	<ul class="dropdownMenu">
	        		<li><a href="${pageContext.request.contextPath}/">상점</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">인벤토리</a></li>
	        	</ul>
	        </div>
	        <div><a href="${pageContext.request.contextPath}/ranking/ranking">랭킹</a></div>
        </nav>
    </div>
</header>
<script type="text/javascript">
	$(function() {
		$('.drop-down').on("mouseover", function() {
			$(this).find('.dropdownMenu').stop().animate().fadeIn(500);
		});
		
		$('.drop-down').on("mouseleave", function() {
			$(this).find('.dropdownMenu').stop().animate().fadeOut(300);
		});
	});
</script>