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
            <button onclick="playGame('가위')">✊ 가위</button>
            <button onclick="playGame('바위')">✋ 바위</button>
            <button onclick="playGame('보')">✌ 보</button>
        </div>

        <!-- 게임 결과 표시 -->
        <div class="result">
            <p id="user-choice">당신의 선택: </p>
            <p id="computer-choice">컴퓨터의 선택: </p>
            <p id="game-result">결과: </p>
        </div>
		
		<div class="btnWrap">
                <button id="start-btn">게임 시작</button>
        </div>
		
		
        <!-- 다음 라운드 진행 선택 -->
        <div class="continue" id="continue-section" style="display: none;">
            <p>다음 라운드를 진행하시겠습니까?</p>
            <button onclick="nextRound()">네 😊</button>
            <button onclick="stopGame()">스탑 😢</button>
        </div>

        <!-- 포인트 부족 시 경고 -->
        <div class="warning" id="warning-section" style="display: none;">
            <p>포인트가 부족하여 게임을 계속할 수 없습니다! 😞</p>
            <button onclick="resetGame()">게임 초기화 🔄</button>
        </div>

        <div class="emojis">
            <span class="emoji">🧡</span>
            <span class="emoji">💙</span>
            <span class="emoji">💚</span>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>


