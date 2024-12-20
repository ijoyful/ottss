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
<style type="text/css">
	#main {margin: unset;}
</style>
</head>

<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<main id="main">
		<jsp:include page="/WEB-INF/views/player/layout/left.jsp"/>
		<div class="mainInner">
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
		function ajaxFun(url, method, formData, dataType, fn, file=false) {
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
		$(function() {
			let url = '${pageContext.request.contextPath}/mypage/attendlist';
			ajaxFun(url, 'get', {year: 2024}, 'json', chartsLine);
			function chartsLine(data) {
				let chartData = [];
				let att_date = [];
				let attendCount = [];
				for (let item of data.list) {
					let obj = [item.att_date, item.attendCount];
					chartData.push(obj);
					att_date.push(item.att_date);
					attendCount.push(item.attendCount);
				}
		
				Highcharts.chart('line-charts', {
				    chart: {
				        type: 'column'
				    },
				    title: {
				        text: data.nickname + '님의 ' + data.year + ' 년 월별 출석 횟수'
				    },
				    xAxis: {
				        categories: att_date,
				        crosshair: true,
				        accessibility: {
				            description: 'Countries'
				        }
				    },
				    yAxis: {
				        min: 0,
				        title: {
				            text: '출석 횟수'
				        }
				    },
				    plotOptions: {
				        column: {
				            pointPadding: 0.2,
				            borderWidth: 0
				        }
				    },
				    series: [
				        {
				            name: '출석 횟수',
				            data: attendCount
				        }
				    ]
				});
		
		
			}
		
		});
	</script>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
	<script src="${pageContext.request.contextPath}/resources/js/calendar.js"></script>
</body>
</html>