<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>가위바위보 게임</title>
    <link rel="icon" href="data:;base64,iVBORw0KGgo=">
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
    <style>
        body {
            font-family: "맑은 고딕", Arial, sans-serif;
            text-align: center;
            margin: 0;
            padding: 0;
            background-color: #f7f7f7;
        }

        h1 {
            color: #023b6d;
            font-size: 32px;
            margin-top: 30px;
        }

        .game-container {
            margin-top: 40px;
        }

        .choices {
            margin: 30px 0;
        }

        button {
            padding: 15px 30px;
            font-size: 18px;
            margin: 10px;
            border: 2px solid #023b6d;
            border-radius: 5px;
            background-color: #e0f0fe;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #cce4f6;
        }

        .result {
            margin-top: 20px;
            font-size: 20px;
            color: #333;
            font-weight: bold;
        }

        .round-info, .points-info {
            font-size: 20px;
            margin: 10px 0;
            font-weight: 500;
        }

        .continue {
            margin-top: 30px;
        }

        .warning {
            color: red;
            margin-top: 20px;
            font-size: 18px;
        }

        .emojis {
            font-size: 30px;
            margin-top: 15px;
        }

        .emoji {
            margin: 0 10px;
        }

    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="mainInner game-container">
        <h1>가위바위보 게임 ✨</h1>
        <p>게임 참가비: 10p</p>
        <p>현재 포인트: <span id="points">10</span>p</p>

        <!-- 현재 라운드 표시 -->
        <div class="round-info" id="round-info">현재 라운드: 1</div>

        <!-- 플레이어가 선택할 수 있는 버튼들 -->
        <div class="choices" id="choices">
            <button data-choice="가위">✊ 가위</button>
            <button data-choice="바위">✋ 바위</button>
            <button data-choice="보">✌ 보</button>
        </div>

        <!-- 게임 결과 표시 -->
        <div class="result">
            <p id="user-choice">당신의 선택: </p>
            <p id="computer-choice">컴퓨터의 선택: </p>
            <p id="game-result">결과: </p>
        </div>

        <!-- 게임 시작 버튼 -->
        <div class="btnWrap">
            <button id="start-btn">게임 시작</button>
        </div>

        <!-- 다음 라운드 진행 선택 -->
        <div class="continue" id="continue-section" style="display: none;">
            <p>다음 라운드를 진행하시겠습니까?</p>
            <button id="next-round">네 😊</button>
            <button id="stop-game">스탑 😢</button>
        </div>

        <!-- 포인트 부족 시 경고 -->
        <div class="warning" id="warning-section" style="display: none;">
            <p>포인트가 부족하여 게임을 계속할 수 없습니다! 😞</p>
            <button id="reset-game">게임 초기화 🔄</button>
        </div>

        <div class="emojis">
            <span class="emoji">🧡</span>
            <span class="emoji">💙</span>
            <span class="emoji">💚</span>
        </div>
    </div>

<script type="text/javascript">

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

$(function () {
    // "게임 시작" 버튼 클릭 이벤트 처리
    $('#start-btn').click(function () {
        // URL 및 데이터 설정
        const url = '${pageContext.request.contextPath}/games/rsp/start';
        const formData = {};       // 전송할 데이터 (필요시 추가 가능)

        // 응답 처리 함수 정의
        const fn = function (data) {
            if (data.success) {
                // 성공 시 UI 업데이트
                $('#points').text(data.points); // 현재 포인트 업데이트
                $('#round-info').text(`현재 라운드: ${data.round}`); // 라운드 정보 업데이트
                $('#choices').show(); // 선택 버튼 활성화
                $('#continue-section').hide(); // 다음 라운드 섹션 숨기기
                $('#warning-section').hide();  // 경고 메시지 숨기기

                alert('게임이 시작되었습니다! 즐거운 게임 되세요! 😊');
            } else {
                // 실패 시 알림
                alert(data.message || '게임 시작에 실패했습니다. 다시 시도해주세요.');
            }
        };

        // Ajax 호출
        ajaxFun(url, 'post', formData, 'json', fn);
    });
});



</script>




    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>


