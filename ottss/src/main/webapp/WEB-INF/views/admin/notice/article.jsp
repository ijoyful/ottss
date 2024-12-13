<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">
</head>
<body>

	<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>
	
	<main id="main">
		<jsp:include page="/WEB-INF/views/admin/layout/left.jsp"/>
		<div class="wrapper">
			<div class="body-container">
				<div class="body-title">
					<h3><i class="bi bi-clipboard"></i> 공지사항 </h3>
				</div>
			    <div class="body-main">
		    	    <div class="mainInner">
				        <div class="writeInner">
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
			                    <li class="listContent"><textarea>${dto.content}</textarea></li>
			                </ul>
			                <ul>
			                    <li class="listTitle">첨부파일</li>
			                    <li class="listContent"></li>
			                </ul>
				        </div>
				        <ul>
				        	<li>이전글</li>
				        </ul>
				        <ul>
				        	<li>다음글</li>
				        </ul>
				        
				        <%-- <table class="table table-borderless">
							<tr>
								<td width="50%">
									<c:choose>
										<c:when test="${sessionScope.member.userId == dto.userId}">
											<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/update?num=${dto.num}&page=${page}&size=${size}';">수정</button>
										</c:when>
										<c:otherwise>
											<button type="button" class="btn btn-light" disabled>수정</button>
										</c:otherwise>
									</c:choose>
							    	
					    			<button type="button" class="btn btn-light" onclick="deleteBoard();">삭제</button>
								</td>
								<td class="text-end">
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list?${query}';">리스트</button>
								</td>
							</tr>
						</table> --%>
				    </div> <!-- mainInner -->
				</div>
			</div>		
		</div>
	</main>
	
	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>
</body>
</html>