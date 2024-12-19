function calendar(y, m) {

	let week = ['일', '월', '화', '수', '목', '금', '토'];
	let date = new Date(y, m - 1, 1); // y년도 m월 1일
	y = date.getFullYear();
	m = date.getMonth() + 1;
	w = date.getDay();
	
	// 시스템 오늘 날짜
	let now = new Date();
	let ny = now.getFullYear();
	let nm = now.getMonth() + 1;
	let nd = now.getDate();
	
	out = '<div class="subject">';
	out += `<span onclick="calendar(${y}, ${m - 1})">&lt;</span>&nbsp;&nbsp;`;
	out += `<label>${y}년 ${m}월</label>`;
	out += `&nbsp;&nbsp;<span onclick="calendar(${y}, ${m + 1})">&gt;</span>`;
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
		if (isToday) {			
			out += `<td class="${cls}" onclick="attend();">${i}</td>`;			
		} else {
			out += `<td class="">${i}</td>`;			
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
	out += `<div class="footer"><span onclick="calendar(${ny}, ${nm})">오늘날짜로</span></div>`;

	
	document.querySelector('#calendarLayout').innerHTML = out;
}

function attend() {
	alert('출석');
}

window.addEventListener('load', () => {
	let now = new Date();
	let y = now.getFullYear();
	let m = now.getMonth() + 1;
	calendar(y, m);

	let attendtd = document.querySelector('.today');
	attendtd.addEventListener('click', () => {
	});
});