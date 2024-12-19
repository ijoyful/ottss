<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
	.mainInner {text-align: center;}
	
	.container {width: 600px; margin: 30px auto;}

	.container > .container-header {padding-bottom: 10px; text-align: center;}
	.container > .container-header > h1 {font-size: 2.5em; text-align: center; margin-bottom: 20px; color: #FF7F00; font-weight: bold;}
	
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
	
	.returnBtnWarp {text-align: center; margin-top: 50px;}
	
	.btn, .returnBtn {
		font-size: 20px;
    	padding: 10px 20px;
		color: #333333;
		border: 1px solid #999999;
		background-color: #ffffff;
		border-radius: 4px;
		font-weight: 500;
		cursor:pointer;
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
	<main id="main">
		<div class="mainInner">
			<div class="container">
				<div class="container-header">
					<h1> 삼식이의 퀴즈 </h1>
					<button type="button" onclick="quizstart()" class="btn btn-start btn-lg">S T A R T</button>
				</div>
				<div class="container-body" style="display: none;">
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
					<div class="container-footer" >
						<button type="button" class="btn btn-primary btnSubmit"> 제출하기 </button>
					</div>
				</div>
			</div>
		</div>
	</main>
	
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

	<script type="text/javascript">
	function quizstart() {
		  // 퀴즈 영역 표시
		  document.querySelector(".container-body").style.display = "block";
		  document.querySelector(".btn-start").style.display = "none";
	
		  // START 버튼 비활성화
		  document.querySelector("button[onclick='quizstart()']").disabled = true;
		};	
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/quiz_data.js"></script>
</body>
</html>