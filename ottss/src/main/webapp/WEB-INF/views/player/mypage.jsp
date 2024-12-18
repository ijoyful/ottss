<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/highcharts-3d.js"></script>
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
		        type: 'bar'
		    },
		    title: {
		        text: 'Historic World Population by Region'
		    },
		    subtitle: {
		        text: 'Source: <a ' +
		            'href="https://en.wikipedia.org/wiki/List_of_continents_and_continental_subregions_by_population"' +
		            'target="_blank">Wikipedia.org</a>'
		    },
		    xAxis: {
		        categories: ['Africa', 'America', 'Asia', 'Europe'],
		        title: {
		            text: null
		        },
		        gridLineWidth: 1,
		        lineWidth: 0
		    },
		    yAxis: {
		        min: 0,
		        title: {
		            text: 'Population (millions)',
		            align: 'high'
		        },
		        labels: {
		            overflow: 'justify'
		        },
		        gridLineWidth: 0
		    },
		    tooltip: {
		        valueSuffix: ' millions'
		    },
		    plotOptions: {
		        bar: {
		            borderRadius: '50%',
		            dataLabels: {
		                enabled: true
		            },
		            groupPadding: 0.1
		        }
		    },
		    legend: {
		        layout: 'vertical',
		        align: 'right',
		        verticalAlign: 'top',
		        x: -40,
		        y: 80,
		        floating: true,
		        borderWidth: 1,
		        backgroundColor:
		            Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF',
		        shadow: true
		    },
		    credits: {
		        enabled: false
		    },
		    series: [{
		        name: 'Year 1990',
		        data: [632, 727, 3202, 721]
		    }, {
		        name: 'Year 2000',
		        data: [814, 841, 3714, 726]
		    }, {
		        name: 'Year 2021',
		        data: [1393, 1031, 4695, 745]
		    }]
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
	
	out += '<table class="table td-border">';
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
	for (; w < 6; w++) {
		out += `<td class="gray">${j}</td>`;
		j++;
	}
	out += '</tr>';
	out += '</table>'
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
				<div class="col" id="calendarLayout" style="min-height: 350px;"></div>
			</div>
		</div>
	</div>
</div>

</main>


<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>