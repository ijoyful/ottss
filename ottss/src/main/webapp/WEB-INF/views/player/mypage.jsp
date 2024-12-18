<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/calendar.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/highcharts-3d.js"></script>

</head>

<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<main>
<jsp:include page="/WEB-INF/views/player/layout/left.jsp"/>
<div class="container">
	<div class="body-container">
		<div class="body-title">
			<h3><i class="bi bi-pencil-square"></i>출석 확인</h3>
		</div>
		<div class="body-main">
			<div class="row">
				<div class="col" id="line-charts" style="min-height: 350px;"></div>
				<div class="col" id="calendarLayout" style="min-height: 350px;">
				</div>
			</div>
		</div>
	</div>
</div>

</main>
<script type="text/javascript">
$(function() {
	let url = '${pageContext.request.contextPath}/mypage/attend';
	$.getJSON(url, function(data) { // GET 방식
		chartsLine(data);
	});
	function chartsLine(data) {
		let chartData = [];

		Highcharts.chart('line-charts', {
		    chart: {
		        type: 'column'
		    },
		    title: {
		        text: 'Corn vs wheat estimated production for 2023'
		    },
		    subtitle: {
		        text:
		            'Source: <a target="_blank" ' +
		            'href="https://www.indexmundi.com/agriculture/?commodity=corn">indexmundi</a>'
		    },
		    xAxis: {
		        categories: ['USA', 'China', 'Brazil', 'EU', 'Argentina', 'India'],
		        crosshair: true,
		        accessibility: {
		            description: 'Countries'
		        }
		    },
		    yAxis: {
		        min: 0,
		        title: {
		            text: '1000 metric tons (MT)'
		        }
		    },
		    tooltip: {
		        valueSuffix: ' (1000 MT)'
		    },
		    plotOptions: {
		        column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		        }
		    },
		    series: [
		        {
		            name: 'Corn',
		            data: [387749, 280000, 129000, 64300, 54000, 34300]
		        },
		        {
		            name: 'Wheat',
		            data: [45321, 140000, 10000, 140500, 19500, 113500]
		        }
		    ]
		});


	}

});

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
		cls = y === ny && m === nm && i === nd ? 'today': '';
		out += `<td class="${cls}">${i}</td>`;
		if (i !== lastDay && ++w % 7 == 0) {
			w = 0;
			out += '</tr><tr>';
		}
	}
	
	// 마지막 날짜 뒷부분
	let j = 1;
	for (let w = date.getDay(); w < 6; w++) {
		out += `<td class="gray">${j}</td>`;
		j++;
	}
	out += `</tr>`;
	out += `</table>`;
	out += `<div class="footer"><span onclick="calendar(${ny}, ${nm})">오늘날짜로</span></div>`;

	
	document.querySelector('#calendarLayout').innerHTML = out;
}

window.addEventListener('load', () => {
	let now = new Date();
	let y = now.getFullYear();
	let m = now.getMonth() + 1;
	calendar(y, m);
});
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>