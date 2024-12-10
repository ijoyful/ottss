<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/reset.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/admin/list.css" type="text/css">
</head>
<body>
	<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>
	
	<main id="main">
		<jsp:include page="/WEB-INF/views/admin/layout/left.jsp"/>
		<div class="wrapper">
			<div class="body-container">
			    <div class="body-main">
			    	<ul class="listTitle">
		                <li>ID</li>
		                <li>이름</li>
		                <li>닉네임</li>
		                <li>생년월일</li>
		                <li>전화번호</li>
		                <li>이메일</li>
		                <li>가입 날짜</li>
		                <li>보유 포인트</li>
		                <li>권한 번호</li>
		                <li>차단</li>
		            </ul>
		            <c:forEach var="list" items="${list}">
			            <ul class="listContent">
			                <li>${list.id}</li>
			                <li>${list.name}</li>
			                <li data-nickName="${list.nickName}">${list.nickName}</li>
			                <li>${list.birth}</li>
			                <li>${list.tel1}-${list.tel2}-${list.tel3}</li>
			                <li>${list.email1}@${list.email2}</li>
			                <li>${list.reg_date}</li>
			                <li>${list.point}</li>
			                <li>${list.powercode}</li>
			                <li>
			                	<c:choose>
			                		<c:when test="${list.block == 0}">
			                			<button class="btn block">차단</button>
			                		</c:when>
			                		<c:otherwise>
			                			<button class="btn unBlock">차단 해제</button>
			                		</c:otherwise>
			                	</c:choose>
		                	</li>
			            </ul>
		            </c:forEach>
				</div>
	            <div class="page-navigation">
					${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</div>
			</div>		
		</div>
	</main>
	
	<div class="background"></div>
	<div class="blockPopUp">
		<div class="blockPopUpInner">
			<p>님을 차단하시겠습니까?</p>
			<div class="btnWrap">
				<button>확인</button>
				<button>취소</button>			
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>
</body>
</html>