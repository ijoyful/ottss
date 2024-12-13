<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/roulette.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">
</head>
<body>
<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
<main id="main">
	<div class="mainInner">
		<div class="title">
			<p>삼식이의 룰렛</p>
			<p>배팅할 포인트: <input type="text" name="bet"></p>
		</div>
		<div class="btnWrap">
			<button onclick="start();">룰렛 시작</button>
		</div>
		<div class="game-board">
			<canvas width="380" height="380"></canvas>
		</div>
	</div>
</main>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/roulette.js"></script>

</body>
</html>