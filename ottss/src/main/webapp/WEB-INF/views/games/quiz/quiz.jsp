<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="games/quiz/quiz.css" type="text/css">
<script type="text/javascript" src="games/quiz/quiz_data.js"></script>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
	#footer {
		position: absolute;
		left: 0;
		right: 0;
		bottom: 0;
	}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<h1>퀴즈 짜야지.. 쒸바..</h1>
	<div class="container">
	<div class="container-header">
		<h2>문제 풀이</h2>
	</div>
	
	<div class="container-body">
		<div class="quiz-timer">
			<div>남은 시간 : </div>
			<div class="timer"></div>
		</div>
		
		<div class="slider-container">
			<div class="slider-left">
				<div class="slider-move">
					<button type="button" class="btn-move-left"> ◀ </button>
				</div>
			</div>
			<div class="slider-content">
				<div class="slider-wrap"></div>
			</div>
			<div class="slider-right">
				<div class="slider-move">
					<button type="button" class="btn-move-right"> ▶ </button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container-footer">
		<button type="button" class="btn btn-primary btnSubmit"> 제출하기 </button>
	</div>
	
</div>
</body>
</html>