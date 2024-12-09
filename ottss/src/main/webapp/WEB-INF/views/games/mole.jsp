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
    <style>
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
            background: url('${pageContext.request.contextPath}/resources/images/moletest/ddang.png') no-repeat center center;
            background-size: cover;
            position: relative;
            overflow: hidden;
            cursor: pointer;
           
        }

        .mole {
            width: 100px;
            height: 100px;
            background-size: cover;
            position: absolute;
            top: 120%; /* 기본적으로 두더지가 보이지 않음 */
            left: 50%;
            transform: translateX(-50%);
            transition: top 0.3s ease;
            z-index: -1;
        }

        .mole.up {
            top: 10%; /* 두더지가 올라오는 위치 */
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

    <!-- 게임이 종료되었을 때 나오는 팝업 -->
    <div class="gameOver">
        <div class="gameOverInner">
            <div class="gameTitle">두더지 게임</div>
            <table>
                <tr>
                    <th>참여 포인트</th>
                    <td>- 10p</td>
                </tr>
                <tr>
                    <th>획득 포인트</th>
                    <td id="final-score">0p</td>
                </tr>
                <tr>
                    <th>현재 보유 포인트</th>
                    <td id="current-points">870p</td>
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

    <script>
        const holes = document.querySelectorAll('.hole');
        const scoreDisplay = document.getElementById('score');
        const startButton = document.getElementById('start-btn');
        const gameOverPopup = document.querySelector('.gameOver');
        const finalScoreDisplay = document.getElementById('final-score');
        const currentPointsDisplay = document.getElementById('current-points');
        const warningPopup = document.querySelector('.warning');
        
        let score = 0;
        let gameInterval;
        let moleTimeout;
        let activeMole = null;
        let isGameStarted = false;
        let gameOver = false;

        // 랜덤한 구멍 선택
        function randomHole() {
            return holes[Math.floor(Math.random() * holes.length)];
        }

        // 두더지를 표시하는 함수
        function showMole() {
            if (!isGameStarted || gameOver) return; // 게임이 시작되지 않거나 게임이 끝나면 두더지 표시하지 않음

            if (activeMole) {
                clearInterval(gameInterval);
                finalScoreDisplay.textContent = `${score}p`;
                gameOverPopup.style.display = 'block';
                startButton.disabled = false; // 게임 종료 후 게임 시작 버튼 활성화
                gameOver = true; // 게임 종료 플래그 설정
                return;
            }

            const hole = randomHole();
            const mole = hole.querySelector('.mole');
            activeMole = mole;

            if (Math.random() < 0.1) {
                mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/legendmore.png')`;
            } else {
                mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/more.png')`;
            }

            mole.classList.add('up');

            moleTimeout = setTimeout(() => {
                mole.classList.remove('up');
                mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/ddang.png')`;
                activeMole = null;
                showMole();
            }, 800);
        }

        // 게임 시작
        function startGame() {
            score = 0;
            scoreDisplay.textContent = score;
            gameOver = false; // 게임 오버 상태 초기화
            isGameStarted = true;
            startButton.disabled = true; // 게임 시작 후 버튼 비활성화
            gameOverPopup.style.display = 'none';  // 게임 오버 팝업 숨김

            // 게임 시작 시 두더지 나타내는 타이머 설정
            gameInterval = setInterval(() => {
                showMole();
            }, 1000);
        }

        // 두더지 클릭 이벤트
        holes.forEach(hole => {
            const mole = hole.querySelector('.mole');
            mole.addEventListener('click', () => {
                if (mole.classList.contains('up') && !gameOver) {
                    if (mole.style.backgroundImage.includes('legendmore.png')) {
                        score += 10;
                    } else {
                        score++;
                    }
                    scoreDisplay.textContent = score;

                    mole.classList.remove('up');
                    mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/${mole.style.backgroundImage.includes('legendmore.png') ? 'moredie' : 'moredie'}.png')`;

                    activeMole = null;
                    clearTimeout(moleTimeout);
                }
            });
        });

        // 게임 시작 버튼 클릭 이벤트
        startButton.addEventListener('click', startGame);

        // 게임을 재시작하는 함수
        function restartGame() {
            // 게임 상태 초기화
            score = 0;
            scoreDisplay.textContent = score;
            isGameStarted = false;
            gameOver = false;
            startButton.disabled = false;

            // 게임 오버 팝업 숨기기
            gameOverPopup.style.display = 'none';

            // 구멍을 기본 상태로 되돌리기
            holes.forEach(hole => {
                const mole = hole.querySelector('.mole');
                mole.classList.remove('up');
                mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/ddang.png')`;
            });

            // 기존에 실행된 타이머들 종료
            clearInterval(gameInterval);
            clearTimeout(moleTimeout);

            // 초기 화면으로 돌아가게 하기
        }

        // 팝업 닫기 함수
        function closeWarning() {
            warningPopup.style.display = 'none';
        }
    </script>
</body>
</html>
