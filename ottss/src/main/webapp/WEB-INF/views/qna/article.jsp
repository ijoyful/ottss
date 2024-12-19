<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

</head>
<body>

	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
		<div class="mainInner">
			<div class="body-container">	
				<div class="body-title">
					<h3><i class="bi bi-clipboard"></i> QnA </h3>
				</div>
				
				<div class="body-main">
					
					<table class="table board-article">
						<thead>
							<tr>
								<td colspan="2" align="left">
									${dto.q_title}
								</td>
							</tr>
						</thead>
	
						<tbody>
							<tr>
								<td width="50%">
									닉네임 : ${dto.q_nickname}
								</td>
								<td align="right">
									질문일자: ${dto.question_date} | 조회 ${dto.hitCount}
								</td>
							</tr>
							
							<tr>
								<td colspan="2" valign="top" height="200" style="border-bottom:none;">
									${dto.q_content}
								</td>
							</tr>
							
							<tr>
								<td colspan="2">
									<c:forEach var="vo" items="${listFile}" varStatus="status">
										<p class="border text-secondary mb-1 p-2">
											<i class="bi bi-folder2-open"></i>
											<a href="${pageContext.request.contextPath}/qna/download?fileNum=${vo.fileNum}">${vo.c_fileName}</a>
										</p>
									</c:forEach>
								</td>
							</tr>
						</tbody>
					</table>
					<table class="table board-article">
						<c:if test="${empty dto.a_nickname}">
						<thead>
							<tr>
								<td colspan="2" align="center" style="font-weight: 700">
									회원님의 궁금증 해결을 위해 삼식이가 열심히 달리고 있다냥🐈. 빠른 시일 내에 답변 하겠다냥!
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">
									<img alt="달리는 고양이" src="${pageContext.request.contextPath}/resources/images/running_cat.jpg" style="width: 50%;">
								</td>
							</tr>
						</thead>
						</c:if>
						<c:if test="${not empty dto.a_nickname}">
						<thead>
							<tr>
								<td colspan="2" align="left">
									[ ${dto.q_title} ] 에 대한 답변입니다.
								</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="50%">
									닉네임 : ${dto.a_nickname}
								</td>
								<td align="right">
									답변일자: ${dto.answer_date}
								</td>
							</tr>
							
							<tr>
								<td colspan="2" valign="top" height="200" style="border-bottom: none;">
									${dto.a_content}
								</td>
							</tr>
						</c:if>
							<tr>
								<td colspan="2">
									다음글 :
									<c:if test="${not empty prevDTO}">
										<a href="${pageContext.request.contextPath}/qna/article?${query}&num=${prevDTO.faq_num}">${prevDTO.q_title}</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									이전글 :
									<c:if test="${not empty nextDTO}">
										<a href="${pageContext.request.contextPath}/qna/article?${query}&num=${nextDTO.faq_num}">${nextDTO.q_title}</a>
									</c:if>
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
								<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/qna/list?${query}';">리스트</button>
							</td>
						</tr>
					</table>
					
				</div>
			</div>
		</div>
	</main>


	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>