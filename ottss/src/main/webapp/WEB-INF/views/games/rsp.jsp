<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>가위바위보 게임</title>
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; margin-top: 20px; }
        h1 { color: darkblue; margin-bottom: 20px; }
        button { margin: 10px; padding: 10px 20px; font-size: 16px; cursor: pointer; }
        #result { margin-top: 20px; font-size: 20px; color: green; }
        #rsp-area { margin: 20px 0; font-size: 50px; display: flex; justify-content: center; align-items: center; }
        #rsp-area div { margin: 0 50px; min-width: 50px; }
        .prediction-btn { margin: 10px; padding: 10px 30px; font-size: 18px; }
        .highlight { border: 3px solid red; padding: 10px; border-radius: 10px; }
        #game-controls { margin: 20px 0; }
        #next-round-controls { margin: 20px 0; display: none; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
    
    <main id="main">
    <div class="mainInner">
    <h1>가위바위보 게임</h1>
    <div>
        <!-- 게임 정보 -->
        <p>게임 참가비: <strong>10포인트</strong></p>
        <p>총 라운드: <strong>10 라운드</strong></p>
        <p>현재 라운드: <span id="current-round">1</span></p>
    </div>

    <!-- 가위바위보 영역 -->
    <div id="rsp-area">
        <div id="player1">👊</div> <!-- 예측하는 쪽 -->
        <div>🆚</div>
        <div id="player2">✌️</div>
    </div>

    <!-- 승/무/패 예측 버튼 -->
	<div>
	    <h2>왼쪽 결과를 예측하세요</h2>
	    <button class="prediction-btn" onclick="makePrediction('승', this)">승</button>
	    <button class="prediction-btn" onclick="makePrediction('무', this)">무</button>
	    <button class="prediction-btn" onclick="makePrediction('패', this)">패</button>
	</div>

    <!-- 게임 시작 버튼 -->
    <div id="game-controls">
        <button onclick="startGame()">게임 시작</button>
    </div>

    <!-- 다음 라운드/게임 종료 버튼 -->
    <div id="next-round-controls">
        <button onclick="nextRound()">다음 라운드</button>
        <button onclick="endGame()">게임 종료</button>
    </div>
	<div>
        <p>현재 획득 포인트: <span id="user-point">0</span></p>
    </div>
    <!-- 결과 출력 -->
    <div id="result"></div>
    </div>
    </main>

    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

    <script type="text/javascript">
	    let currentRound = 1;  // 현재 라운드
	    let userPoint = 10;    // 사용자 포인트
	    let userPrediction = '';  // 사용자의 예측 저장
	
	    const totalRounds = 10;
	    const rsp = ["👊", "✌️", "✋"];
	
	    // 사용자의 예측을 저장하는 함수
	    function makePrediction(prediction, btn) {
	        userPrediction = prediction;
	        // 모든 예측 버튼에서 'highlight' 클래스 제거
	        $('.prediction-btn').removeClass('highlight');
	        // 클릭한 버튼에만 'highlight' 클래스 추가
	        $(btn).addClass('highlight');
	    }
		
	    function ajaxFun(url, method, formData, dataType, fn, file=false){
	    	const settings = {
	    			type: method,
	    			data: formData,
	    			dataType: dataType,
	    			success: function(data){
	    				fn(data);
	    			},
	    			beforeSend: function(jqXHR) {
	    				jqXHR.setRequestHeader('AJAX', true);
	    			},
	    			complete: function(){
	    				
	    			},
	    			error: function(jqXHR){
	    				if(jqXHR.status === 403){
	    					login();
	    					return false;
	    				} else if (jqXHR.status === 406){
	    					alert('요청 처리가 실패했습니다.');
	    					return false;
	    				}
	    				console.log(jqXHR.responseText);
	    			}
	    	};
	    	
	    	if(file) {
	    		settings.processData = false; // 파일 전송시 필수. 서버로 보낼 데이터를 쿼리문자열로 변환 여부
	    		settings.contentType = false; // 파일 전송시 필수. 기본은 application/x-www-urlencoded
	    	}

	    	$.ajax(url, settings);
	    }
	    
	    // 게임 시작 함수
	    function startGame() {
	        // 예측이 없는 경우 경고 메시지 표시
	        if (!userPrediction) {
	            alert("먼저 승/무/패를 예측해주세요.");
	            return;
	        }
	
	        // 서버로 게임 시작 요청 (Ajax)
	        const url = '${pageContext.request.contextPath}/games/rsp/start'; // 요청 URL
	        const formData = {}; // 전송할 데이터 (필요시 추가)
	
	        const fn = function (data) {
	            if (data.state === "true") {
	                // 서버에서 게임 시작 성공 응답이 오면
	                $('#result').text("게임 진행 중..."); // 상태 표시
	                animateRSP(); // 애니메이션 시작
	
	                // 2초 후 결과 처리
	                setTimeout(() => {
	                    stopAnimation(); // 애니메이션 멈춤
	                    processGameResult(); // 게임 결과 처리
	                }, 2000);

	            } else {
	                // 게임 시작 실패 시
	                alert(data.message || '게임 시작에 실패했습니다. 다시 시도해주세요.');
	            }
	        };
	
	        // Ajax 호출
	        ajaxFun(url, 'post', formData, 'json', fn);
	    }
	
	    
	    // 게임 종료 함수
	    function endGame() {
	  
	        const formData = {
	            currentRound: currentRound,  // 현재 라운드
	            userPoint: userPoint         // 사용자 포인트
	        };
	        
	        // 서버로 게임 종료 요청 (Ajax)
	        const url = '${pageContext.request.contextPath}/games/rsp/end'; // 요청 URL

	        const fn = function (data) {
	            if (data.state === "true") {
	                // 서버에서 게임 종료 성공 응답이 오면
	                $('#result').text("게임 종료!"); // 상태 표시
	                alert(data.message || "게임이 종료되었습니다."); // 종료 메시지
	            } else {
	                // 게임 종료 실패 시
	                alert(data.message || '게임 종료에 실패했습니다. 다시 시도해주세요.');
	            }
	        };

	        // Ajax 호출
	        ajaxFun(url, 'POST', formData, 'json', fn);
	    }

	    
	    // 가위바위보 애니메이션
	    let animationInterval;
	    function animateRSP() {
	        animationInterval = setInterval(() => {
	            $("#player1").text(rsp[Math.floor(Math.random() * 3)]);
	            $("#player2").text(rsp[Math.floor(Math.random() * 3)]);
	        }, 100); // 0.1초마다 변경
	    }
	
	    // 애니메이션 정지
	    function stopAnimation() {
	        clearInterval(animationInterval);
	    }
	
	    // 결과 처리
	    function processGameResult() {
	        const finalPlayer1 = $("#player1").text();
	        const finalPlayer2 = $("#player2").text();
	
	        const winner = determineWinner(finalPlayer1, finalPlayer2); // 승부 판정
	        let resultText;
	
	        if (userPrediction === winner) {
	            userPoint *= 2; // 승리 시 포인트 2배
	            resultText = `🎉 예측 성공! 포인트가 ${userPoint}로 증가했습니다.`;
	            showNextRoundButtons(); // 예측 성공 시 다음 라운드 버튼 표시
	        } else {
	            userPoint = 0; // 실패 시 포인트 0
	            resultText = `😭 예측 실패! 포인트가 0이 되었습니다.`;
	            endGame(); // 예측 실패 시 바로 게임 종료
	        }
	
	        $('#result').html(resultText);
	        $('#user-point').text(userPoint);
	    }
	
	    // 승부 판정 함수
	    function determineWinner(p1, p2) {
	        if (p1 === p2) return '무'; // 비김
	        if (
	            (p1 === "👊" && p2 === "✌️") ||
	            (p1 === "✌️" && p2 === "✋") ||
	            (p1 === "✋" && p2 === "👊")
	        ) return '승'; // 왼쪽 승리
	        return '패'; // 오른쪽 승리
	    }
	
	    // 다음 라운드 버튼 표시
	    function showNextRoundButtons() {
	        $('#game-controls').hide();  // 게임 시작 버튼 숨기기
	        $('#next-round-controls').show();  // 다음 라운드 버튼 표시
	    }
	
	    // 다음 라운드 함수
	    function nextRound() {
	        if (currentRound < totalRounds) {
	            currentRound++;
	            $('#current-round').text(currentRound);
	            resetGame(); // 게임 상태 리셋
	        } else {
	            endGame(); // 라운드가 다 끝났으면 게임 종료
	        }
	    }
		
	    // 게임 상태 리셋 함수
	    function resetGame() {
	        userPrediction = ''; // 예측 초기화
	        $('.prediction-btn').removeClass('highlight');
	        $('#result').text(''); // 결과 초기화
	        $('#next-round-controls').hide(); // 다음 라운드 버튼 숨기기
	        $('#game-controls').show(); // 게임 시작 버튼 다시 표시
	    }
	      
	    
    </script>
</body>
</html>
