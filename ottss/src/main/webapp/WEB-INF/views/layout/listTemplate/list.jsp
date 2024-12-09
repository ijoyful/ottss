<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/list.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<style type="text/css">
	#footer {
		position: absolute;
		left: 0;
		right: 0;
		bottom: 0;
	}
</style>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
	    <div class="mainInner">
	        <div class="listInner">
	            <ul class="listTitle">
	                <li>No.</li>
	                <li>제목</li>
	                <li>작성자</li>
	                <li>작성일</li>
	                <li>조회수</li>
	            </ul>
	            <ul class="listContent">
	                <li>1</li>
	                <li>가위바위보 게임 확률 어떻게 되는거죠??</li>
	                <li>비공개</li>
	                <li>2024-12-02</li>
	                <li>20</li>
	            </ul>
	        </div>
	    </div> <!-- mainInner -->
	</main>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>