<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
	    <div class="mainInner">
	        <div class="writeInner">	<!-- article에서는 가운데 정렬 풀어주세요 -->
                <ul>
                    <li class="listTitle">제목</li>
                    <li class="listContent">${dto.title}</li>
                </ul>
                <ul>
                    <li class="listTitle">작성자</li>
                    <li class="listContent">${dto.nickname}</li>
                </ul>
                <ul>
                    <li class="listTitle">날짜</li>
                    <li class="listContent">${dto.reg_date}</li>
                </ul>
                <ul class="content">
                    <li class="listTitle">내용</li>
                    <li class="listContent"><textarea readonly>${dto.content}</textarea></li>
                </ul>
                <ul>
                    <li class="listTitle">첨부파일</li>
                    <li class="listContent">
                    	<c:forEach var="vo" items="${listFile}" varStatus="status">
                    		<a href="${pageContext.request.contextPath}/freeboard/download?fileNum=${vo.fileNum}">${vo.c_fileName}</a>
                    	</c:forEach>
                    </li>
                </ul>
		        <ul>
		        	<li class="listTitle">다음글</li>
                    <li class="listContent">
                    	<c:if test="${not empty nextDTO}">
                    		<a href="${pageContext.request.contextPath}/freeboard/article?${query}&fb_num=${nextDTO.fb_num}">${nextDTO.title}</a>
                    	</c:if>
                    </li>
		        </ul>
                <ul>
		        	<li class="listTitle">이전글</li>
                    <li class="listContent">
                    	<c:if test="${not empty prevDTO}">
                    		<a href="${pageContext.request.contextPath}/freeboard/article?${query}&num=${prevDTO.fb_num}">${prevDTO.title}</a>
                    	</c:if>
                    </li>
		        </ul>
	        </div>
	        
			<table class="table table-borderless">
				<tr>
					<td width="50%">&nbsp;</td>
					<td class="text-end">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/freeboard/list?${query}';">리스트</button>
					</td>
				</tr>
			</table>
	    </div> <!-- mainInner -->
	</main>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>