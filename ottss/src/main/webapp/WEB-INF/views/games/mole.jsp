<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>두더지 게임</title>
    <link rel="icon" href="data:;base64,iVBORw0KGgo=">
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
    <style type="text/css">
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        h1 {
            margin: 20px 0;
        }

        .game-board {
            display: grid;
            grid-template-columns: repeat(3, 120px);
            grid-gap: 10px;
            justify-content: center;
            margin: 20px auto;
        }

        .hole {
            width: 120px;
            height: 120px;
            background: url('${pageContext.request.contextPath}/resources/images/moletest/ddang.png') no-repeat bottom center;
            background-size: contain;
            position: relative;
            overflow: hidden;
            cursor: pointer;
        }

        .mole {
            width: 100px;
            height: 100px;
            background-size: cover;
            position: absolute;
            top: 100%; /* 두더지가 기본적으로 보이지 않도록 위치 설정 */
            left: 50%;
            transform: translateX(-50%);
            z-index: -1; /* 기본적으로 땅 뒤에 배치 */
            pointer-events: none; /* 기본적으로 클릭 불가 */
            transition: top 0.3s ease; /* 두더지가 올라올 때 애니메이션 */
        }

        .mole.up {
            top: 20%; /* 두더지가 올라오는 위치 */    
            pointer-events: all; /* 두더지가 올라왔을 때 클릭 가능 */
        }

        .btnWrap {
            margin-top: 20px;
        }

        .btnWrap button {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            background-color: #28a745;
            color: #fff;
            border: none;
            border-radius: 5px;
        }

        button:disabled {
            background-color: #aaa;
            cursor: not-allowed;
        }

        .score-display, .lives-display {
            font-size: 18px;
            margin-top: 20px;
        }

        .gameOver, .warning {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.7);
            color: white;
            text-align: center;
            padding-top: 100px;
        }

        .gameOverInner, .warningInner {
            background-color: #333;
            padding: 30px;
            border-radius: 10px;
            display: inline-block;
        }

        .okBtn button {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            background-color: #28a745;
            color: #fff;
            border: none;
            border-radius: 5px;
        }

        .warning .waringText p {
            font-size: 18px;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
    
    <main id="main">
        <div class="mainInner">
            <div class="title">
                <p>두더지 게임</p>
                <p>획득 포인트 <span id="score">0p</span></p>
            </div>
            <div class="game-board">
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
                <div class="hole"><div class="mole"></div></div>
            </div>
            <div class="btnWrap">
                <button id="start-btn">게임 시작</button>
            </div>
        </div>
    </main>

    <!-- 게임 종료 팝업 -->
    <div class="gameOver">
        <div class="gameOverInner">
            <div class="gameTitle">두더지 게임</div>
            <table>
                <tr>
                    <th>참여 포인트</th>
                    <td>-10p</td>
                </tr>
                <tr>
                    <th>획득 포인트</th>
                    <td id="final-score">0p</td>
                </tr>
                <tr>
                    <th>현재 보유 포인트</th>
                    <td id="current-point">0p</td>  <!-- 현재 포인트 표시 -->
                </tr>
            </table>
            <div class="okBtn"><button onclick="restartGame()">확인</button></div>
        </div>
    </div>

    <!-- 게임 종료 경고 팝업 -->
    <div class="warning">
        <div class="warningInner">
            <div class="waringText">
                <p>게임에 참여한 포인트를 잃게 됩니다.</p>
                <p>정말 종료하시겠습니까?</p>
            </div>
            <div class="okBtn"><button onclick="closeWarning()">확인</button></div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
    <jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

<script type="text/javascript">
    const holes = document.querySelectorAll('.hole');
    const scoreDisplay = document.getElementById('score');
    const startButton = document.getElementById('start-btn');
    const gameOverPopup = document.querySelector('.gameOver');
    const finalScoreDisplay = document.getElementById('final-score');
    const currentPointsDisplay = document.getElementById('current-point');
    const warningPopup = document.querySelector('.warning');
    
    let score = 0;
    let gameInterval;
    let moleTimeout;
    let activeMole = null;
    let state = false; // 게임 상태를 관리하는 변수 (false: 게임 종료, true: 게임 진행 중)
    let gameOver = false;
    let userPoint = 0; // 서버에서 전달받은 포인트

    function checkPointsAndStartGame() {
        $.ajax({
            url: "${pageContext.request.contextPath}/games/mole/start",  // 서버의 요청 URL
            type: "POST",  // POST 요청
            dataType: "json",  // 서버에서 JSON 형식으로 응답을 받을 것
            success: function(response) {
                if (response.state === "true") {
                    alert("게임 시작!");
                    userPoint = response.updatedPoint;  // 최신 포인트 갱신
                    startGame();  // 게임 시작 함수 호출
                } else if (response.state === "false") {
                    alert("포인트 부족! 게임을 시작할 수 없습니다.");
                } else {
                    alert("서버 오류가 발생했습니다.");
                }
            },
            error: function() {
                alert("서버 오류가 발생했습니다.");
            }
        });
    }

    // 게임 시작
    function startGame() {
        score = 0;
        scoreDisplay.textContent = score;
        gameOver = false; // 게임 오버 상태 초기화
        state = true;  // 게임 진행 중으로 상태 변경
        startButton.disabled = true; // 게임 시작 후 버튼 비활성화
        gameOverPopup.style.display = 'none';  // 게임 오버 팝업 숨김

        // 게임 시작 시 두더지 나타내는 타이머 설정
        gameInterval = setInterval(() => {
            showMole();
        }, 1000);
    }

    // 두더지 표시
    function showMole() {
        if (!state || gameOver) return;

        if (activeMole) {
            clearInterval(gameInterval);
            finalScoreDisplay.textContent = `${score}p`;
            gameOverPopup.style.display = 'block';
            startButton.disabled = false;
            gameOver = true;
            state = false;
            return;
        }

        const hole = randomHole();
        const mole = hole.querySelector('.mole');
        activeMole = mole;

        mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/mole.png')`;

        moleTimeout = setTimeout(() => {
            mole.classList.remove('up');
            activeMole = null;
        }, 1500);

        mole.classList.add('up');
        mole.addEventListener('click', moleHit);
    }

    // 랜덤으로 두더지를 나타낼 구멍 선택
    function randomHole() {
        const index = Math.floor(Math.random() * holes.length);
        return holes[index];
    }

    // 두더지 클릭 시
    function moleHit(e) {
        score++;
        scoreDisplay.textContent = `${score}p`;
        activeMole.classList.remove('up');
        activeMole = null;
        clearTimeout(moleTimeout);
    }

    // 게임 종료 후 서버에 포인트 갱신 요청
    function endGame() {
        const usedPoint = 10;  // 사용된 포인트
        const winPoint = score;  // 게임에서 얻은 포인트
        const gameNum = 1;  // 게임 번호
        const result = "win";  // 결과

        $.ajax({
            url: "${pageContext.request.contextPath}/games/mole/end",  // 서버 요청 URL
            type: "POST",  // POST 요청
            dataType: "json",  // 서버에서 JSON 형식으로 응답 받음
            data: {
                usedPoint: usedPoint,
                winPoint: winPoint,
                gameNum: gameNum,
                result: result
            },
            success: function(response) {
                if (response.state === "true") {
                    $('#final-score').text(response.newPoint + "p");
                    $('#current-point').text(response.newPoint + "p");
                    alert("게임이 종료되었습니다! 사용 포인트: " + usedPoint + "p, 얻은 포인트: " + winPoint + "p");
                } else {
                    alert("게임 종료에 실패했습니다: " + response.message);
                }
            },
            error: function() {
                alert("서버 오류가 발생했습니다.");
            }
        });
    }

    startButton.addEventListener('click', checkPointsAndStartGame);
</script>
</body>
</html>
