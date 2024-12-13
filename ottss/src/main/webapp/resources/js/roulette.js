const $c = document.querySelector("canvas");
const cp = '/ottss';
const ctx = $c.getContext(`2d`);

const product = [0, 2, 3, 5, 10, 100];
const prob = [766, 90, 63, 45, 27, 9];

const colors = ["#dc0936", "#e6471d", "#f7a416", "#efe61f ", "#60b236", "#209b6c", "#169ed8", "#3f297e", "#87207b", "#be107f", "#e7167b"];
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}
const newMake = () => {
    const [cw, ch] = [$c.width / 2, $c.height / 2];
    const arc = Math.PI * 2 / 1000; // 개당 각도
	let arcSum = 0;

    for (let i = 0; i < product.length; i++) {
      ctx.beginPath();
      ctx.fillStyle = colors[i % (colors.length -1)];
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
	const $bet = $('input[name=bet]');
	let bet = $bet.value;
	let url = cp + '/games/roulette/start';
	const fn = function(data) {
		if (data.state === "false") {
			alert(data.message);
			bet = '';
			$bet.focus();
		} else {
			rotate();
		}
	}

	ajaxFunc(url, 'post', {bet: bet}, 'json', fn);
}

const rotate = () => {
	$c.style.transform = `initial`;
	$c.style.transition = `initial`;
	  
	setTimeout(() => {
		let i = 0;
		let ran = Math.floor(Math.random() * 1000);
		if (ran < 16 || ran >= 250) {
			i = 0;
		} else if (ran < 106) {
			i = 1;
		} else if (ran < 169) {
			i = 2;
		} else if (ran < 214) {
			i = 3;
		} else if (ran < 241) {
			i = 4;
		} else {
			i = 5;
		}
		
		const rotate = 3600 + (0.36 * ran + 0.18);
		
		$c.style.transform = `rotate(-${rotate}deg)`;
		$c.style.transition = `5s`;
		console.log(rotate);
		setTimeout(() => alert(`${product[i]}배에 당첨되셨습니다. 10 포인트의 ${product[i]}배인 ${10 * product[i]} 포인트를 얻습니다!`), 5000);
	}, 1);
};

newMake();