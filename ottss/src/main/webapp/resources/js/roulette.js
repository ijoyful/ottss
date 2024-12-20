const $c = document.querySelector("canvas");
const cp = '/ottss';
const ctx = $c.getContext(`2d`);

const product = [0, 2, 3, 5, 10, 100];
const prob = [255, 90, 255, 63, 256, 45, 27, 9];
// 0 2 0 3 0 5 10 100
const colors = ["#a3a5b0", "#767889", "#a3a5b0", "#d1d2d7", "#a3a5b0", "#484b62", "#E5E1DA", "#F72C5B"];
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}
const newMake = () => {
    const [cw, ch] = [$c.width / 2, $c.height / 2];
    const arc = Math.PI * 2 / 1000; // 개당 각도
	let arcSum = 0;

    for (let i = 0; i < prob.length; i++) {
		ctx.beginPath();
		ctx.fillStyle = colors[i];
		ctx.moveTo(cw, ch);
		let temp = prob[i] * Math.PI / 500;
		ctx.arc(cw, ch, cw, arcSum, arcSum + temp);
		ctx.fill();
		ctx.closePath();
		arcSum += temp;
    }


	ctx.fillStyle = "#fff";
	ctx.font = "18px Pretendard";
	ctx.textAlign = "center";

	for (let i = 0; i < product.length; i++) {
		const angle = (arc * prob[i] * 0.36) + (arc / 2);

		ctx.save();

		ctx.translate(
		cw + Math.cos(angle) * (cw - 50),
		ch + Math.sin(angle) * (ch - 50)
	);

	ctx.rotate(angle + Math.PI / 2);

      /*
	  product[i].forEach((text, j) => {
        ctx.fillText(text, 0, 30 * j);
      });
	  */

		ctx.restore();
	}
}

function ajaxFunc(url, method, formData, dataType, fn, file=false) {
	const settings = {
		type: method,
		data: formData,
		dataType: dataType,
		success: function(data) {
			fn(data);
		},
		beforeSend: function(jqXHR) {
			jqXHR.setRequestHeader('AJAX', true);				
		},
		complete:function() {
		},
		error: function(jqXHR) {
			if(jqXHR.status === 403) {
				login();
				return false;
			} else if(jqXHR.status === 406) {
				alert('요청 처리가 실패했습니다.');
				return false;
			}

			console.log(jqXHR.responseText);
		}
	};
	
	if(file) {
		settings.processData = false; // 파일 전송시 필수. 서버로 보낼 데이터를 쿼리문자열로 변환 여부
		settings.contentType = false; // 파일 전송시 필수. contentType. 기본은 application/x-www-urlencoded
	}
	
	$.ajax(url, settings);
}

const start = () => {
	const run = document.querySelector('.btnWrap1 button');
	const $bet = document.querySelector('input[name="bet"]');
	var bet = $bet.value;
	if (!bet) {
		alert('룰렛에 걸 포인트를 입력해주세요.');
		return;
	}
	let url = cp + '/games/roulette/start';
	const fn = function(data) {
		if (data.state === "false") {
			alert(data.message);
			$bet.value = '';
			$bet.focus();
		} else {
			run.disabled = true;
			rotate(bet, run);
		}
	}
	ajaxFunc(url, 'post', {bet: bet}, 'json', fn);
}

const rotate = (bet, run) => {
	$c.style.transform = `initial`;
	$c.style.transition = `initial`;
	  
	setTimeout(() => {
		let i = 0;
		let ran = Math.floor(Math.random() * 1000);
		if (ran < 169 || (ran >= 250 && ran < 505) || (ran >= 595 && ran < 850) || ran >= 913) {
			i = 0;
		} else if (ran < 214) {
			i = 3; // 5배
		} else if (ran < 241) {
			i = 4; // 10배
		} else if (ran < 250) {
			i = 5;
		} else if (ran < 595) {
			i = 1;
		} else {
			i = 2;
		}
		
		const rotate = 3600 + (0.36 * ran + 0.18);
		
		$c.style.transform = `rotate(-${rotate}deg)`;
		$c.style.transition = `5s`;
		var result = product[i];
		const fn = function(data) {
			if (data.state === "false") {
				alert('룰렛이 잠시 고장났다냥~ 불편을 끼쳐 미안하다냥ㅠ 조금 이따 다시 시도해달라냥!');
				location.href = cp + '/games/roulette';
			} else {
				setTimeout(() => {
					alert(`${result}배에 당첨되셨습니다. ${bet} 포인트의 ${product[i]}배인 ${bet * product[i]} 포인트를 얻습니다!`);
					location.href = cp  + '/games/roulette';					
				}
				, 5000);
			}
		}
		run.disabled = false;
		ajaxFunc(cp + '/games/roulette/end', 'post', {win_point: bet * result, bet: bet, result: result, game_num: '3'}, 'json', fn);
	}, 1);
};

newMake();