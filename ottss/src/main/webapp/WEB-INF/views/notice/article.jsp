<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
		<div class="mainInner">
		    <div class="container">
				<div class="body-container">	
					<div class="body-title">
						<h3><i class="bi bi-clipboard"></i> 공지사항</h3>
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
										<c:if test="${not empty prevDto}">
											<a href="${pageContext.request.contextPath}/notice/article?${query}&n_num=${prevDto.n_num}">${prevDto.title}</a>
										</c:if>
										<c:if test="${empty prevDto}">
											<span>마지막 글입니다.</span>
										</c:if>
									</td>
								</tr>
								<tr>
									<td colspan = "2"> 이전글 :
										<c:if test="${not empty NextDto}">
											<a href="${pageContext.request.contextPath}/notice/article?${query}&n_num=${NextDto.n_num}">${NextDto.title}</a>
										</c:if>
										<c:if test="${empty NextDto}">
											<span>처음 글입니다.</span>
										</c:if>
									</td>
								</tr>
							</tbody>
						</table>
		                <table class="table table-borderless">
							<tr>
								<td width="50%">&nbsp;</td>
								<td class="text-end">
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/notice/list?${query}';">리스트</button>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</main>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>