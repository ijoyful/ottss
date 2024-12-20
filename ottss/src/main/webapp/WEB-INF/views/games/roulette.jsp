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
	.body-container {max-width: 800px;}
	
	.mainInner {
    	display: flex;
    	flex-direction: column;
    	align-items: center;
    	text-align: center;
	}
	
	.title {margin-bottom: 20px;}
	
	.title p:nth-child(1) {
		font-size: 25px;
		margin-bottom: 20px;
		font-weight: bold;
		text-align: center;
	}
	
	.title p:nth-child(2) {font-size: 18px;}
	
	.btnWrap1 {margin-bottom: 50px;}
	
	.resultColor {position: absolute; top: 50%; transform: translateY(-50%); left: 25%;}
	
	.resultColor > div {display: flex; align-items: center;}
	.resultColor div:not(:last-child) {margin-bottom: 5px;}
	.resultColor div .color {width: 30px; height: 30px; border-radius: 3px; margin-right: 10px;}
	
	.resultColor .zero .color {background-color: #a3a5b0;}
	.resultColor .two .color {background-color: #767889;}
	.resultColor .three .color {background-color: #d1d2d7;}
	.resultColor .five .color {background-color: #484b62;}
	.resultColor .ten .color {background-color: #E5E1DA;}
	.resultColor .hundred .color {background-color: #F72C5B;}
	
</style>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/roulette.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<main id="main">
		<div class="mainInner">
			<div class="title">
				<p>삼식이의 룰렛</p>
				<p>배팅할 포인트: <input type="text" name="bet"></p>
			</div>
			<div class="btnWrap1">
				<button onclick="start();">룰렛 시작</button>
			</div>
			<div class="game-board">
				<canvas width="380" height="380"></canvas>
			</div>
		</div>
		<input type="hidden" name="state" value="${state}">
	</main>
	<div class="resultColor">
		<div class="zero">
			<div class="color"></div>
			<p>0배</p>
		</div>
		<div class="two">
			<div class="color"></div>
			<p>2배</p>
		</div>
		<div class="three">
			<div class="color"></div>
			<p>3배</p>
		</div>
		<div class="five">
			<div class="color"></div>
			<p>5배</p>
		</div>
		<div class="ten">
			<div class="color"></div>
			<p>10배</p>
		</div>
		<div class="hundred">
			<div class="color"></div>
			<p>100배</p>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/roulette.js"></script>

</body>
</html>