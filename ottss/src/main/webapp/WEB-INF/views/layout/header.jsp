<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<header id="header">
    <div class="headerInner">
        <div class="info">
            <div class="logo">
            	<div class="imgArea"><img src="${pageContext.request.contextPath}/resources/images/ottssImg/ottssLogo.png" onclick="home();"></div>
            	<p onclick="home();">삼식이네 놀이터</p>
           	</div>
            <div class="btnWrap" style="display: flex;">
                <div class="login">
	                <c:if test="${empty sessionScope.member}">
						<a href="${pageContext.request.contextPath}/login/login">로그인</a>&nbsp;|&nbsp;
                    	<a href="${pageContext.request.contextPath}/login/member">회원가입</a>	
					</c:if>            
  					<c:if test="${not empty sessionScope.member}">
						<div class="p-2">
							<a href="${pageContext.request.contextPath}/player/mypage">마이페이지</a>&nbsp;|&nbsp;
							<a href="${pageContext.request.contextPath}/login/logout" title="로그아웃">로그아웃</a>
						</div>					
					</c:if>
                </div>
                <div class="mypage">
					<c:if test="${sessionScope.member.powerCode == 99}">
						<div class="py-2">
							|&nbsp;<a href="${pageContext.request.contextPath}/admin" title="관리자">관리자페이지</a>
						</div>					
					</c:if>
                </div>
            </div>
        </div>
		<nav id="nav">
			<div><a href="${pageContext.request.contextPath}/">홈</a></div>
			<div><a href="${pageContext.request.contextPath}/introduce/sogae">놀이터 소개</a></div>
			<div class="drop-down">
				<span class="m-title">게임</span>
				<ul class="dropdownMenu">
					<li><a href="${pageContext.request.contextPath}/games/rsp">가위바위보</a></li>
	        		<li><a href="${pageContext.request.contextPath}/games/mole">두더지</a></li>
	        		<li><a href="${pageContext.request.contextPath}/games/roulette">룰렛</a></li>
	        		<li><a href="${pageContext.request.contextPath}/games/quiz">퀴즈</a></li>
				</ul>
			</div>
	        <div class="drop-down">
	        	<span class="m-title">커뮤니티</span>
	        	<ul class="dropdownMenu">
	        		<li><a href="${pageContext.request.contextPath}/notice/list">공지사항</a></li>
	        		<li><a href="${pageContext.request.contextPath}/freeboard/list">자유게시판</a></li>
	        		<li><a href="${pageContext.request.contextPath}/">분석게시판</a></li>
	        		<li><a href="${pageContext.request.contextPath}/show/list">자랑게시판</a></li>
	        		<li><a href="${pageContext.request.contextPath}/qna/list">QnA</a></li>
	        	</ul>
	        </div>
	        <div class="drop-down">
	        	<a href="${pageContext.request.contextPath}/shop/shop">상점</a>
	        	<ul class="dropdownMenu">
	        		<li><a href="${pageContext.request.contextPath}/shop/shop">상점</a></li>
	        		<li><a href="${pageContext.request.contextPath}/shop/inventory">인벤토리</a></li>
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

function home() {
	location.href = '${pageContext.request.contextPath}/main';
}
</script>