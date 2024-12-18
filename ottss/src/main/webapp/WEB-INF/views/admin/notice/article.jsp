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
<script type="text/javascript">
	
	function deleteBoard() {
		if(confirm('게시글을 삭제하시겠습니까?')) {
			let query = 'n_num=${dto.n_num}&${query}';
			let url = '${pageContext.request.contextPath}/admin/notice/delete?' + query;
			location.href = url;
		}
	}
	
</script>
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
				        <table class="table board-article">
							<thead>
								<tr>
									<td align="left">${dto.title}</td>
								</tr>
							</thead>
							
							<tbody>
								<tr>
									<td width="50%" style="margin-top: 0px;">
										작성자 : ${dto.nickname}<hr>
									</td>
									<td align="right">
										작성일 : ${dto.reg_date} | 조회 ${dto.hitCount}<hr>
									</td>
								</tr>
								
								<tr>
									<td colspan="2" valign="top" height="200" style="border-bottom:none;">
										${dto.content}
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<c:forEach var="vo" items="${listFile}" varStatus="status">
											<p class="border text-secondary mb-1 p-2">
												<i class="bi bi-folder2-open"></i>
												<a href="${pageContext.request.contextPath}/freeboard/download?fileNum=${vo.fileNum}">${vo.fileNum}</a>
											</p>
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td colspan = "2"> 다음글 :
										<c:if test="${not empty NextDto}">
											<a href="${pageContext.request.contextPath}/notice/article?${query}&n_num=${NextDto.n_num}">${NextDto.title}</a>
										</c:if>
									</td>
								</tr>
								<tr>
									<td colspan = "2"> 이전글 :
										<c:if test="${not empty prevDto}">
											<a href="${pageContext.request.contextPath}/notice/article?${query}&n_num=${prevDto.n_num}">${prevDto.title}</a>
										</c:if>
									</td>
								</tr>
							</tbody>
						</table>
				        
				        <table class="table table-borderless">
							<tr>
								<td width="50%">
									<c:choose>
										<c:when test="${sessionScope.member.id == dto.id}">
											<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/update?n_num=${dto.n_num}&page=${page}&size=${size}';">수정</button>
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
						</table>
				</div>
			</div>		
		</div>
	</main>
	
	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>
</body>
</html>