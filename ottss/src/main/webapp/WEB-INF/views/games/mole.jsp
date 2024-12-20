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
     
        h1 {font-size: 2.5em; text-align: center; margin-bottom: 20px; color: #FF7F00; font-weight: bold;}
        h2 {font-size: 1.5em; text-align: center;  font-weight: bold;}
	
		#main2 .mainInner {width: 1200px; margin: auto; text-align: center; padding: 80px 50px;}

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
            position: absolute;
		    top: 50%;
		    left: 40%;
		    transform: translateY(-50%);
		    background-color: #333;
		    padding: 30px;
		    width: 20%;
		    border-radius: 10px;
		    display: inline-block;
        }
        
        .gameOverInner table {width: 100%; margin: 20px 0;}
        .gameOverInner table tr {height: 30px; line-height: 30px;}

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
    
    <main id="main2">
        <div class="mainInner">
            <div class="title">
                <h1>두더지 게임</h1>
                <h2>획득 포인트 <span id="score">0p</span></h2>
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

    <div class="gameOver">
        <div class="gameOverInner">
            <div class="gameTitle">두더지 게임</div>
            <table>
                <tr>
                    <th>참여 포인트</th>
                    <td>10p</td>
                </tr>
                <tr>
                    <th>획득 포인트</th>
                    <td id="final-score">${win_point}</td>
                </tr>
                <tr>
                    <th>현재 보유 포인트</th>
                    <td id="current-point">${userPoint}</td>  <!-- 현재 포인트 표시 -->
                </tr>
            </table>
            <div class="okBtn"><button type="button" onclick="restartGame()">확인</button></div>
        </div>
    </div>

    <!-- 게임 종료 경고 팝업 -->
    <div class="warning">
        <div class="warningInner">
            <div class="waringText">
                <p>게임에 참여한 포인트를 잃게 됩니다.</p>
                <p>정말 종료하시겠습니까?</p>
            </div>
            <div class="okBtn"><button type="button" onclick="closeWarning()">확인</button></div>
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
                    // 포인트 부족 시 처리 로직 (게임 시작 불가)
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
    if (!state || gameOver) return; // 게임이 시작되지 않거나 게임이 끝나면 두더지 표시하지 않음

    if (activeMole) {
        clearInterval(gameInterval);
        finalScoreDisplay.textContent = `${score}p`;
        startButton.disabled = false; // 게임 종료 후 게임 시작 버튼 활성화
        gameOver = true;
        state = false; // 게임 종료 상태로 변경
        endGame();
        return;
    }

    const hole = randomHole();
    const mole = hole.querySelector('.mole');
    activeMole = mole;

    // 10% 확률로 legendmole 표시
    const isLegendMole = Math.random() < 0.1; // 10% 확률
    if (isLegendMole) {
        mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/legendmole.png')`;
    } else {
        mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/mole.png')`;
    }
	
    const moleDisappearTime = isLegendMole ? 500 : 800;
    moleTimeout = setTimeout(() => {
        mole.classList.remove('up');
        mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/moledie.png')`;
        activeMole = null;
        showMole();
    }, moleDisappearTime);

    mole.classList.add('up');
}

    // 랜덤으로 구멍 선택
    function randomHole() {
        return holes[Math.floor(Math.random() * holes.length)];
    }

    // 두더지 클릭 이벤트
    holes.forEach(hole => {
        const mole = hole.querySelector('.mole');
        hole.addEventListener('click', () => {
            if (mole.classList.contains('up') && !gameOver) {
                if (mole.style.backgroundImage.includes('legendmole.png')) {
                    score += 5;
                } else {
                    score++;
                }
                scoreDisplay.textContent = score;
                

                mole.classList.remove('up');
                mole.style.backgroundImage = `url('${pageContext.request.contextPath}/resources/images/moletest/moledie.png')`;

                activeMole = null;
                clearTimeout(moleTimeout);
            }
        });
    });
    // 게임 종료 후 서버에 포인트 업데이트 요청
    function endGame() {
        const entry = 10;  //사용된 포인트
        const win_point = score;  // 게임에서 얻은 포인트
        const game_num = 1;  // 게임 번호
        const result = score;  // 결과
      
        
        let url = '${pageContext.request.contextPath}/games/mole/end';
        let formData = {
        	entry: entry,
        	win_point: win_point,
        	game_num: game_num,
           	result: result
           	
        };
        
        $.ajax({
            url: url,  // 서버 요청 URL
            type: "POST",  // POST 요청
            dataType: "json",  // 서버에서 JSON 형식으로 응답 받음
            data: formData,
            success: function(response) { 
            	gameOverPopup.style.display = 'block';

                if (response.state === "true") {
                    // 게임 종료 성공 시 포인트와 메시지 출력
                    $('#final-score').text(response.win_point + "p");
                    $('#current-point').text(response.userPoint + "p");

                 } else {
                     alert("게임 종료에 실패했습니다: " + response.message);
                 }
            },
            error: function(e) {
            }
        });
        
    }

    // 게임 종료 후 버튼 클릭 시 처리
    $(document).ready(function() {
        $('.okBtn button').on('click', function() {
		//endGame();  // 게임 종료 요청
        });
    });


    // 게임 다시 시작
    function restartGame() {
        location.reload();
    }

    // 게임 종료 확인 팝업 닫기
    function closeWarning() {
        warningPopup.style.display = 'none';
    }

    // 게임 시작 버튼 클릭 이벤트
    startButton.addEventListener('click', checkPointsAndStartGame);
</script>


</body>
</html>