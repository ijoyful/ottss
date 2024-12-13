<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
	#footer {
		position: absolute;
		left: 0;
		right: 0;
		bottom: 0;
	}
@charset "UTF-8";

* { padding: 0; margin: 0; }
*, *::after, *::before { box-sizing: border-box; }

body {
	font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
	font-size: 13px;
	color: #222;
}

.container {
	width: 600px;
	margin: 30px auto;
}

.container > .container-header {
	padding-bottom: 10px;
}
.container > .container-header > h1 {
	text-align: center;
}

.slider-container, .slider-container * { box-sizing: border-box; padding: 0; margin: 0; }
.slider-container { width: 100%; height: 200px; display: flex; }

.quiz-timer { display: flex; justify-content: flex-end; padding:10px 60px 10px 60px; }
.quiz-timer .timer { color: tomato; }

.slider-left, .slider-right { width: 60px; height: inherit; }
.slider-move { display: flex; width:inherit; height: inherit; align-items: center; justify-content: center; }
.slider-move button {
	width: 40px; height: 40px;
	background: #fff; border: 1px solid #ced4da; border-radius: 40px;
	opacity: 0.7;
}
.slider-move button:hover, .slider-move button:active {
	font-weight: 600; color: #0d58ba; cursor: pointer;
}
.slider-content { 
	width: 500px; height: inherit; 
	border: 1px solid #ced4da; border-radius: 3px;
	overflow: hidden;
}
.slider-content > .slider-wrap { 
	width: 100%; height: inherit;
	display: flex;
	transition: transform 0.5s;  /* 엘리먼트를 transform(변형) 시키는데 걸리는 시간(초) 0.5초 */
}
.slider-content > .slider-wrap > .item {
	width: 100%; height: inherit; list-style: none;
	padding:10px;
	flex: 0 0 100%; /* <flex-grow> <flex-shrink> <flex-basis> */
		/* 증가너비비율 감소너비비율 기본너비를설정 */
}

.scoring-result { padding: 5px 200px; }

.item p { line-height: 110%; font-weight: bold; }
.item ol { padding: 15px; margin-left: 15px; }
.item li { align-items: center; padding: 3px; }
.item label, .item input { vertical-align: baseline; }

.container-footer { padding: 15px; text-align: center; }
.btn {
	color: #333333;
	border: 1px solid #999999;
	background-color: #ffffff;
	padding: 5px 10px;
	border-radius: 4px;
	font-weight: 500;
	cursor:pointer;
	font-size: 14px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.btn:active, .btn:focus, .btn:hover {
	background-color: #f8f9fa;
	color:#333333;
}
.btn[disabled], fieldset[disabled] .btn {
	pointer-events: none;
	cursor: not-allowed;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
	opacity: .65;
}

.btn-primary {
    background: #106eea;
	border:1px solid #00a6eb;
    color:#ffffff;
}
.btn-primary:hover, .btn-primary:active, .btn-primary:focus {
    background: #3284f1;
    border:1px solid #00a6eb;
    color:#ffffff;
}

</style>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<div class="container">
	<div class="container-header">
		<h1> Q U I Z </h1>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/quiz_data.js"></script>
</body>
</html>