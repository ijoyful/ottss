<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/main.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/mainList.css" type="text/css">

</head>
<body>

	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
		
	<main id="main">
        <div class="mainInner">
            <div class="title_bar">
                <p>GAME'S</p>
                <div class="slide_arrow">
                    <span class="arrow prev"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-chevron-left"><polyline points="15 18 9 12 15 6"></polyline></svg></span>
                    <span class="arrow next"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-chevron-right"><polyline points="9 18 15 12 9 6"></polyline></svg></span>
                </div>
            </div>
            <div class="gameList">
                <div class="latest_wrap">
                    <div class="swiper-container latest_slide">
                        <ul class="swiper-wrapper">
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/rsp.gif')"><a href="${pageContext.request.contextPath}/games/rsp"><span>가위바위보</span></a></li>
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/mole.gif')"><a href="${pageContext.request.contextPath}/games/mole"><span>레전드 두더지</span></a></li>
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/roulette.gif')"><a href="${pageContext.request.contextPath}/games/roulette"><span>돌림판</span></a></li>
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/quiz.gif')"><a href="${pageContext.request.contextPath}/games/quiz"><span>퀴즈</span></a></li>
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/rsp.gif')"><a href="${pageContext.request.contextPath}/games/rsp"><span>가위바위보</span></a></li>
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/mole.gif')"><a href="${pageContext.request.contextPath}/games/mole"><span>레전드 두더지</span></a></li>
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/roulette.gif')"><a href="${pageContext.request.contextPath}/games/roulette"><span>돌림판</span></a></li>
                            <li class="swiper-slide" style="background-image: url('${pageContext.request.contextPath}/resources/images/gameImg/quiz.gif')"><a href="${pageContext.request.contextPath}/games/quiz"><span>퀴즈</span></a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="listWrap">
            	<!-- 공지사항 시작 -->
                <div class="listInner">
                	<div class="listBigTitle">
                		<span>[ 공지사항 ]</span>
                		<a href="${pageContext.request.contextPath}/notice/list">더보기</a>
               		</div>
                	<ul class="listTitle">
	                    <li>제목</li>
	                </ul>
	                <c:forEach var="listNotice" items="${listNotice}">
		                <ul class="listContent">
		                    <li><a href="${pageContext.request.contextPath}/notice/article?page=1&size=10&n_num=${listNotice.n_num}">${listNotice.title}</a></li>
		                </ul>
	                </c:forEach>
	            </div>
	            <!-- 공지사항 끝 -->
	            
	            <!-- QnA -->
	            <div class="listInner">
	            	<div class="listBigTitle">
                		<span>[ QnA ]</span>
                		<a href="${pageContext.request.contextPath}/">더보기</a>
               		</div>
                	<ul class="listTitle">
	                    <li>제목</li>
	                </ul>
	                <c:forEach var="listQnA" items="${listQnA}">
		                <ul class="listContent">
		                    <li><a href="${pageContext.request.contextPath}/qna/article?page=1&size=10&num=${listQnA.faq_num}">${listQnA.q_title}</a></li>
		                </ul>
	                </c:forEach>
	            </div>
	            <!-- QnA 끝 -->
	            
	            <!-- 자유게시판 -->
                <div class="listInner">
                	<div class="listBigTitle">
                		<span>[자유게시판]</span>
                		<a href="${pageContext.request.contextPath}/freeboard/list">더보기</a>
               		</div>
                	<ul class="listTitle">
	                    <li>제목</li>
	                </ul>
	                <c:forEach var="listFree" items="${listFree}">
		                <ul class="listContent">
		                    <li><a href="${pageContext.request.contextPath}/freeboard/article?page=1&size=10&num=${listFree.fb_num}">${listFree.title}</a></li>
		                </ul>
	                </c:forEach>
	            </div>
	            <!-- 자유게시판 끝 -->
            </div> <!-- tableListWrap -->
        </div> <!-- mainInner -->
    </main>
    
    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
	<script type="text/javascript">
	
	    var product_slide = new Swiper(".latest_slide", {
	        loop: true, // 루프 기능
	        speed: 1000,
	        slidesPerView: 1.5, 
	        spaceBetween : 12,
	        centeredSlides: true,
	        navigation: {
	            prevEl: '.slide_arrow .prev',
	            nextEl: '.slide_arrow .next',
	        },
	        autoplay: {
	            delay: 3000, // 3초마다 자동 재생
	            disableOnInteraction: false,
	        },
	        breakpoints:{
	            1025:{
	                slidesPerView: 4, 
	                spaceBetween : 30,
	                centeredSlides: false,
	            },
	            769:{
	                slidesPerView: 3, 
	                spaceBetween : 20,
	                centeredSlides: true,
	            },
	            481:{
	                slidesPerView: 3, 
	                spaceBetween : 15,
	                centeredSlides: true,
	            },
	            381:{
	                slidesPerView: 2, 
	                spaceBetween : 15,
	                centeredSlides: false,
	            },
	        },
	    });
	    
	    <c:if test="${param.message == 'success'}">
        	alert('회원 탈퇴가 성공적으로 이루어졌습니다.');
   		</c:if>
	
	</script>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>