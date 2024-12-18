<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>

<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<main>
<jsp:include page="/WEB-INF/views/player/layout/left.jsp"/>
	<div class="container">
		<div class="body-container">	
			<div class="body-title">
				<h3><i class="bi bi-pencil-square"></i> ${sessionScope.member.nickName}님의 최고 기록 </h3>
			</div>
			<div class="body-main">
				<div class="listWrap">
				<!-- 기록 시작 -->
						<c:forEach var="record" items="${bestRecord}" varStatus="status">
					<div class="listInner">
						<div class="listBigTitle">
							<c:choose>
								<c:when test="${status.index == 0}"><span>[ 두더지 ]</span></c:when>
								<c:when test="${status.index == 1}"><span>[ 가위바위보 ]</span></c:when>
								<c:when test="${status.index == 2}"><span>[ 룰렛 ]</span></c:when>
								<c:otherwise><span>[ 퀴즈 ]</span></c:otherwise>
							</c:choose>
							
						</div>
							<c:if test="${empty record}">
							<span>기록이 존재하지 않습니다.</span>
							</c:if>
							<c:if test="${not empty record}">
							<table>
								<thead>
									<tr>
										<td>날짜</td>
										<td>
										<c:choose>
											<c:when test="${record.game_num == 1}">최고 점수</c:when>
											<c:when test="${record.game_num == 2}">최고 라운드</c:when>
											<c:otherwise>최고 기록</c:otherwise>
										</c:choose>
										</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>${record.play_date}</td>
										<td>${record.result}</td>
									</tr>
								</tbody>
							</table>
							</c:if>
					</div>
						</c:forEach>
					<!-- 기록 끝 -->
				</div> <!-- tableListWrap -->
			</div>
		</div>
	</div>
	

</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>