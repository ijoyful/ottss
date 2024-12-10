<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
	
<main>
	<div class="container">
		<div class="body-container">	
			<div class="body-title">
				<h3><i class="bi bi-clipboard"></i> 자게유시판 </h3>
			</div>
			
			<div class="body-main">
				
				<table class="table board-article">
					<thead>
						<tr>
							<td colspan="2" align="left">
								${dto.title}
							<hr>
							</td>
						</tr>
					</thead>
					
					<tbody>
						<tr>
							<td width="50%" style="margin-top: 0px;">
								닉네임 : ${dto.nickname}<hr>
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
								<c:forEach var="dto" items="${listFile}" varStatus="status">
									<p class="border text-secondary mb-1 p-2">
										<i class="bi bi-folder2-open"></i>
										<a href="${pageContext.request.contextPath}/faq/download?fileNum=${vo.fileNum}">${vo.c_fileName}</a>
									</p>
								</c:forEach>
							</td>
						</tr>
					</tbody>
				</table>
				
				
				<table class="table table-borderless">
					<tr>
						<td width="50%">
							&nbsp;
						</td>
						<td class="text-end">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/freeboard/list?${query}';">리스트</button>
						</td>
					</tr>
				</table>
				
			</div>
		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>