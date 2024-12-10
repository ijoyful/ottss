<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
input {
	font-size: 16px; 
	font-weight: 600;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
	    <div class="mainInner">
	        <div class="writeInner">
	            <form action="">
	                <ul>
	                    <li class="listTitle">제목</li>
	                    <li class="listContent"><input type="text"></li>
	                </ul>
	                <ul>
	                    <li class="listTitle">작성자</li>
	                    <li class="listContent"><input type="text" value="${sessionScope.member.nickName}" readonly ></li>
	                </ul>
	                
					<!-- 
	                <ul>
	                    <li class="listTitle">날짜</li>
	                    <li class="listContent"><input type="date" value="" readonly></li>
	                </ul>
					 -->
	                <ul class="content">
	                    <li class="listTitle">내용</li>
	                    <li class="listContent"><textarea></textarea></li>
	                </ul>
	                <ul>
	                    <li class="listTitle">첨부파일</li>
	                    <li class="listContent"><input type="file"></li>
	                </ul>
	                <ul>
	                	<li class ="submitBtn">
	                		<button type="button" class = "btn btn-sm" onclick="location.href='${pageContext.request.contextPath}/freeboard/list'"> 등록</button>
	                	</li>
	                </ul>
	            </form>
	        </div>
	    </div> <!-- mainInner -->
	</main>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>