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

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">
</head>
<body>
<main id="main">
	<div class="mainInner">
		<div class="title">
			<p>삼식이의 룰렛</p>
			<p>획득 포인트 <span id="score">0p</span></p>
		</div>
		<div class="game-board">
			<canvas width="380" height="380"></canvas>
		</div>
		<div class="btnWrap">
			<button onclick="rotate();">룰렛 시작</button>
		</div>
	</div>
</main>

<script type="text/javascript" src="roulette2.js"></script>
</body>
</html>