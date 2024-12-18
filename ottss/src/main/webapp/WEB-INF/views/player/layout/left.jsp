<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/core2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/menu2.css" type="text/css">
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
<nav class="vertical_nav" style="top: 120px;">
	<ul id="js-menu" class="menu">
		<li class="menu--item">
	        <a href="${pageContext.request.contextPath}/player/mypage" class="menu--link" title="출석확인">
				<i class="menu--icon bi bi-person-square"></i>
				<span class="menu--label">출석확인</span>
			</a>
		</li>
	
		<li class="menu--item">
	        <a href="${pageContext.request.contextPath}/mypage/bestrecord" class="menu--link" title="최고기록확인">
				<i class="menu--icon bi bi-question-square"></i>
				<span class="menu--label">최고기록확인</span>
			</a>
		</li>
	
	
		<li class="menu--item">
	        <a href="${pageContext.request.contextPath}/mypage/mypoint" class="menu--link" title="포인트확인">
				<i class="menu--icon bi bi-calendar"></i>
				<span class="menu--label">포인트 이용내역</span>
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
				<span class="menu--label">회원탈퇴</span>
			</a>
		</li>
	
		
	</ul>
</nav>
