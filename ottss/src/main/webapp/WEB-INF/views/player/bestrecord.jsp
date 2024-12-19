<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
	#main {margin: unset;}
	
	.listInner {
		display: flex;
    	flex-direction: row;
    	justify-content: space-between;
    	flex-wrap: wrap;
	}
	
	.recodeList {
		width: 49%;
		padding: 15px;
	    border: 1px solid #000;
	    border-radius: 10px;
	    text-align: center;
	}
	.recodeList:nth-child(-n+2) {margin-bottom: 2%;}
	
	.listBigTitle {margin-bottom: 20px; font-size: 20px;}
	.listBigTitle p {font-weight: bold;}
	
	.recodeList table {width: 100%;}
	.recodeList table tr {height: 30px; line-height: 30px;}
	
</style>
</head>

<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<main id="main">
		<jsp:include page="/WEB-INF/views/player/layout/left.jsp"/>
		<div class="mainInner">
			<div class="body-container">	
				<div class="body-title">
					<h3><i class="bi bi-pencil-square"></i> ${sessionScope.member.nickName}님의 최고 기록 </h3>
				</div>
				<div class="body-main">
					<div class="listWrap">
						<!-- 기록 시작 -->
						<div class="listInner">
							<c:forEach var="record" items="${bestRecord}" varStatus="status">
								<div class="recodeList">
									<div class="listBigTitle">
										<c:choose>
											<c:when test="${status.index == 0}"><p>[ 두더지 ]</p></c:when>
											<c:when test="${status.index == 1}"><p>[ 가위바위보 ]</p></c:when>
											<c:when test="${status.index == 2}"><p>[ 룰렛 ]</p></c:when>
											<c:otherwise><p>[ 퀴즈 ]</p></c:otherwise>
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
						</div>
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