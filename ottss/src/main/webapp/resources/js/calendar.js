let cp = '/ottss';

function calendar(data) { // data 에 들어있는 값. 출석한 날짜들이 들어있는 리스트, 년도와 달
	let list = data.list;
	let week = ['일', '월', '화', '수', '목', '금', '토'];
	let date = new Date(data.y, data.m - 1, 1); // y년도 m월 1일
	y = date.getFullYear();
	m = date.getMonth() + 1;
	w = date.getDay();

	// 시스템 오늘 날짜
	let now = new Date();
	let ny = now.getFullYear();
	let nm = now.getMonth() + 1;
	let nd = now.getDate();

	out = '<div class="subject">';
	out += `<span onclick="interCalendar(${y}, ${m-1})">&lt;</span>&nbsp;&nbsp;`;
	out += `<label>${y}년 ${m}월</label>`;
	out += `&nbsp;&nbsp;<span onclick="interCalendar(${y}, ${m+1})">&gt;</span>`;
	out += '</div>';
	
	out += '<table class="table td-border" style="width: 350px; height: 350px;">';
	out += '<tr>';
	for (let i = 0; i < week.length; i++) {
		out += `<td>${week[i]}</td>`;
	}
	out += '</tr>';
	
	// 1일 앞 부분 날짜 처리
	let preDate = new Date(y, m - 1, 0); // 이전 달의 마지막 날짜
	// let preYear = preDate.getFullYear();
	// let preMonth = preDate.getMonth() + 1;
	let preLastDay = preDate.getDate();
	let preDay = preLastDay - w;
	out += '<tr>';
	for (let i = w - 1; i >= 0; i--) {
		out += `<td class="gray">${preLastDay - i}</td>`;
	}
	let cls;
	let lastDay = (new Date(y, m, 0)).getDate();
	for (let i = 1; i <= lastDay; i++) {
		let isToday = (y ===ny) && (m === nm) && (i === nd);
		cls = isToday ? 'today': '';
		if (list.includes(i)) {
			if (isToday) {
				out += `<td class="${cls} attend" onclick="attend();">${i}</td>`;
			} else {
				out += `<td class="attend">${i}</td>`;
			}			
		} else {
			if (isToday) {
				out += `<td class="${cls}" onclick="attend();">${i}</td>`;
			} else {
				out += `<td class="">${i}</td>`;			
			}
		}
		if (i !== lastDay && ++w % 7 == 0) {
			w = 0;
			out += '</tr><tr>';
		}
	}
	
	// 마지막 날짜 뒷부분
	let j = 1;
	for (; w < 6; w++) {
		out += `<td class="gray">${j}</td>`;
		j++;
	}
	out += `</tr>`;
	out += `</table>`;
	out += `<div class="footer"><p class="todayBtn" onclick="interCalendar(${ny}, ${nm})">오늘날짜로</p></div>`;

	
	document.querySelector('#calendarLayout').innerHTML = out;
}

function ajaxFunc(url, method, formData, dataType, fn, file = false) {
	const settings = { // ajax 안에 들어가는 객체를 밖에서 선언해서 사용
		type: method
		, data: formData
		, dataType: dataType
		, success: function(data) {
			fn(data);
		}
		, beforeSend: function(jqXHR) {
			jqXHR.setRequestHeader('AJAX', true);
		}
		, complete: function() {
		}
		, error: function(jqXHR) {
			if(jqXHR.status === 403) { // 로그인이 안 된 상태에서 AJAX 접근. filter 에서 던진 에러값 == 403
				login();
				return false;
			} else if (jqXHR.status === 406) { // 문제가 생긴 경우 406 번으로 처리할 예정
				alert('요청 처리가 실패했습니다.');
				return false;
			}

			console.log(jqXHR.responseText);
		}
	}
	if (file) {
		settings.processData = false; // 파일 전송 시 필수. 서버로 보낼 데이터를 쿼리문자열로 변환 여부
		settings.contentType = false; // 파일 전송 시 필수. contentType 기본은 application/x-www-urlencoded
	}
	$.ajax(url, settings); // settings 안에 url 을 넣어도 됨
}
function interCalendar(y, m) {
	data = {y: y, m: m};
	loadCalendar(data);
}
function loadCalendar(data) {
	let url = cp + '/mypage/calendar';

	ajaxFunc(url, 'get', data, 'json', calendar);
}

window.addEventListener('load', () => {
	let now = new Date();
	let y = now.getFullYear();
	let m = now.getMonth() + 1;
	let data = {y: y, m: m};
	loadCalendar(data);
});

function attend() {
	const todaytd = document.querySelector('.today');

	if (todaytd.classList.contains('attend')) {
		alert('오늘은 이미 출석을 완료했습니다!');
		return;
	}
	alert('출석을 완료했습니다!');
	document.querySelector('.today').onclick = null;

	location.href = cp + '/mypage/attend';
}