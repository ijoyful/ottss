<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/ranking.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp" />

	<main id="main">
		<div class="mainInner">
			<div class="rankingInner">
				<div class="game1">
					<div class="gameTitle">${title[0]}</div>
					<ul class="gameSubTitle">
						<li>랭킹</li>
						<li>닉네임</li>
						<li>포인트</li>
					</ul>
					<c:forEach var="vo" items="${list1}" varStatus="status">
					<ul>
						<li>${status.count}</li>
						<li>${vo.nickname}</li>
						<li>${vo.win_point}</li>
					</ul>
					</c:forEach>
				</div>
				<div class="game2">
					<div class="gameTitle">${title[1]}</div>
					<ul class="gameSubTitle">
						<li>랭킹</li>
						<li>닉네임</li>
						<li>최종 라운드</li>
					</ul>
					<c:forEach var="vo" items="${list2}" varStatus="status">
					<ul>
						<li>${status.count}</li>
						<li>${vo.nickname}</li>
						<li>${vo.result}</li>
					</ul>
					</c:forEach>
				</div>
				<div class="game3"> 
					<div class="gameTitle">${title[2]}</div>
					<ul class="gameSubTitle">
						<li>랭킹</li>
						<li>닉네임</li>
						<li>총포인트</li>
					</ul>
					<c:forEach var="vo" items="${list3}" varStatus="status">
					<ul>
						<li>${status.count}</li>
						<li>${vo.nickname}</li>
						<li>${vo.sum_win_point}</li>
					</ul>
					</c:forEach>
				</div>
				<div class="game4">
					<div class="gameTitle">${title[3]}</div>
					<ul class="gameSubTitle">
						<li>랭킹</li>
						<li>닉네임</li>
						<li>포인트</li>
					</ul>
					<c:forEach var="vo" items="${list4}" varStatus="status">
					<ul>
						<li>${status.count}</li>
						<li>${vo.nickname}</li>
						<li>${vo.result}</li>
					</ul>
					</c:forEach>
				</div>
				
				
			</div>
		</div>
		<!-- mainInner -->
	</main>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>