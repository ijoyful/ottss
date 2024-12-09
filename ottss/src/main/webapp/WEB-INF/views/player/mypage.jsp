<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/core2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/menu2.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/menu2.js"></script>
<script type="text/javascript">
// 메뉴 활성화
$(function(){
    var url = window.location.pathname;
    var urlRegExp = new RegExp(url.replace(/\/$/, '') + "$");
    
    try {
    	$('nav ul>li>a').each(function() {
    		if ( urlRegExp.test(this.href.replace(/\/$/, '')) ) {
    			$(this).addClass('active_menu');
    			return false;
    		}
    	});
    	if($('nav ul>li>a').hasClass("active_menu")) return false;

     	var parent = url.replace(/\/$/, '').substr(0, url.replace(/\/$/, '').lastIndexOf("/"));
     	if(! parent) parent = "/";
        var urlParentRegExp = new RegExp(parent);
    	$('nav ul>li>a').each(function() {
    		if($(this).attr("href")=="#") return true;
    		
    		var phref = this.href.replace(/\/$/, '').substr(0, this.href.replace(/\/$/, '').lastIndexOf("/"));
    		
    		if ( urlParentRegExp.test(phref) ) {
    			$(this).addClass('active_menu');
    			return false;
    		}
    	});
    	if($('nav ul>li>a').hasClass("active_menu")) return false;
    	
    	$('nav ul>.menu--item__has_sub_menu').each(function() {
    		if (urlRegExp.test(this.href.replace(/\/$/, '')) ) {
    			$(this).addClass('active_menu');
    			return false;
    		}
    	});
    	
    }catch(e) {
    }
});

$(function(){
	$('nav ul>.menu--item__has_sub_menu ul>li>a').each(function() {
		if($(this).hasClass('active_menu')) {
			$(this).closest(".menu--item__has_sub_menu").addClass('menu--subitens__opened');
			return false;
		}
	});
});
</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
	    <div class="mainInner">
	    	마이페이지
	    </div> <!-- mainInner -->
	
	</main>
	<nav class="vertical_nav" style="top: 120px;">
		<ul id="js-menu" class="menu">
			<li class="menu--item">
		        <a href="#" class="menu--link" title="출석확인">
					<i class="menu--icon bi bi-person-square"></i>
					<span class="menu--label">출석확인</span>
				</a>
			</li>
		
			<li class="menu--item menu--item__has_sub_menu">
				<label class="menu--link" title="최고기록확인">
					<i class="menu--icon bi bi-question-square"></i>
					<span class="menu--label">최고기록확인</span>
				</label>
		
				<ul class="sub_menu">
					<li class="sub_menu--item">
						<a href="#" class="sub_menu--link">가위바위보</a>
					</li>
					<li class="sub_menu--item">
						<a href="#" class="sub_menu--link">두더지</a>
					</li>
					<li class="sub_menu--item">
						<a href="#" class="sub_menu--link">룰렛</a>
					</li>
					<li class="sub_menu--item">
						<a href="#" class="sub_menu--link">퀴즈</a>
					</li>
				</ul>
			</li>
		
		
			<li class="menu--item">
		        <a href="#" class="menu--link" title="포인트확인">
					<i class="menu--icon bi bi-calendar"></i>
					<span class="menu--label">포인트확인</span>
				</a>
			</li>
			
			<li class="menu--item">
		        <a href="#" class="menu--link" title="사용내역">
					<i class="menu--icon bi bi-geo"></i>
					<span class="menu--label">사용내역</span>
				</a>
			</li>
			
			<li class="menu--item">
		        <a href="${pageContext.request.contextPath}/login/pwd?mode=update" class="menu--link" title="회원정보수정">
					<i class="menu--icon bi bi-geo"></i>
					<span class="menu--label">회원정보수정</span>
				</a>
			</li>
			
			<li class="menu--item">
		        <a href="${pageContext.request.contextPath}/login/pwd?mode=delete" class="menu--link" title="회원탈퇴">
					<i class="menu--icon bi bi-geo"></i>
					<span class="menu--label">"회원탈퇴"</span>
				</a>
			</li>
		
			
		</ul>
	</nav>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>