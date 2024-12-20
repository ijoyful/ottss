<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
	#main {margin: unset;}
	
	.pointWrap {display: flex; justify-content: space-between;}
	.pointWrap p {font-weight: bold; font-size: 20px; color: deeppink;}
	
	.table {width: 100%; text-align: center;}
	.table tr {height: 30px; line-height: 30px;}
	.table .thead tr td {background-color: #1B1F3B; color: #fff;}
	
	.table tbody tr td {border-bottom: 1px solid #eee;}
	
	.title {margin-bottom: 20px; text-align: right;}
</style>
</head>

<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<main id="main">
		<jsp:include page="/WEB-INF/views/player/layout/left.jsp"/>
		<div class="mainInner">
			<div class="body-container">	
				<div class="body-title">
					<h3><i class="bi bi-pencil-square"></i> 포인트 이용내역 </h3>
				</div>
		
				<div class="body-main">
					<div class="pointWrap">
						<p>잔여 포인트: ${left_pt}</p>
						<select class="mode">
							<option value="all" selected>전체</option>
							<option value="game">게임</option>
							<option value="shop">상점</option>
							<option value="used">포인트 소비</option>
							<option value="win">포인트 획득</option>
						</select>
					</div>
		
		
					<div class="mt-4 mb-1 wrap-inner">
						<div class="title">
							<div class="col ps-0 fw-bold text-primary item-count">목록 0개</div>
						</div>
		
						<table class="table">
							<colgroup>
					        	<col style="width: 17.5%" />
					        	<col style="width: 17.5%" />
					        	<col style="width: 17.5%" />
					        	<col style="width: 17.5%" />
					        	<col style="width: 30%" />
							</colgroup>
							<thead class="thead">
								<tr>
									<td>유형</td>
									<td>종류</td>
									<td>포인트</td>
									<td>남은 포인트</td>
									<td>날짜</td>
								</tr>
							</thead>
							<tbody class="list-content" data-pageNo="0" data-totalPage="0">
							</tbody>
						</table>
		
						<div class="sentinel" data-loading="false"></div>
							<%-- 무한 스크롤 관찰용 --%>
					</div>
		
				</div>
			</div>
		</div>
	
	</main>
	
	<script type="text/javascript">
		function login() {
			location.href = '${pageContext.request.contextPath}/member/login';
		}
		function ajaxFunc(url, method, formData, dataType, fn, file = false) {
			const sentinelNode = document.querySelector('.sentinel');
			const settings = { // ajax 안에 들어가는 객체를 밖에서 선언해서 사용
				type: method
				, data: formData
				, dataType: dataType
				, success: function(data) {
					fn(data);
				}
				, beforeSend: function(jqXHR) {
					sentinelNode.setAttribute('data-loading', 'true');
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
		
		const listNode = document.querySelector('.list-content');
		const sentinelNode = document.querySelector('.sentinel');
		
		function loadContent(page, mode) {
			let url = '${pageContext.request.contextPath}/mypage/pointhistory';
			let query = 'pageNo=' + page + '&mode=' + mode;
			const fn = function(data) {
				addNewContent(data);
			}
			ajaxFunc(url, 'get', query, 'json', fn);
		}
		
		function addNewContent(data) {
			let dataCount = parseInt(data.dataCount);
			let pageNo = parseInt(data.pageNo);
			let total_page = parseInt(data.total_page);
		
			const itemCount = document.querySelector('.item-count');
		
			listNode.setAttribute('data-pageNo', pageNo);
			listNode.setAttribute('data-totalPage', total_page);
		
			itemCount.innerHTML = '목록 ' + dataCount + '개';
			sentinelNode.style.display = 'none';
		
			if (dataCount === 0) {
				listNode.innerHTML = '';
				return;
			}
		
			let htmlText;
			for (let item of data.list) {
				let num = item.pt_num;
				let categories = item.categories === 10 ? '소비' : '획득';
				let category = item.category;
				let used_pt = item.point;
				let left_pt = item.left_point;
				let pt_date = item.pt_date;
		
				htmlText = '<tr class="item-content">';
				htmlText += '	<td>';
				htmlText += categories;
				htmlText += '	</td><td>';
				htmlText += category;
				htmlText += '	</td><td>';
				htmlText += used_pt;
				htmlText += '	</td><td>';
				htmlText += left_pt;
				htmlText += '	</td><td>';
				htmlText += pt_date;
				htmlText += '	</td>';
				htmlText += '</tr>';
		
				listNode.insertAdjacentHTML('beforeend', htmlText);
			}
		
			if (pageNo < total_page) {
				sentinelNode.setAttribute('data-loading', 'false');
				sentinelNode.style.display = 'block';
				io.observe(sentinelNode); // 관찰 대상(요소) 등록
			}
		
		}
		
		// 인터 섹션 옵저버를 이용한 무한 스크롤
		const ioCallback = (entries, io) => {
			entries.forEach((entry) => {
				if (entry.isIntersecting) {
					// 관찰 대상의 교차(겹치는) 상태: 화면에 보이는 상태
					let loading = sentinelNode.getAttribute('data-loading');
					if (loading !== 'false') {
						return;
					}
		
					io.unobserve(entry.target); // 기존 관찰하던 요소는 더이상 관찰하지 않음
		
					let pageNo = parseInt(listNode.getAttribute('data-pageNo'));
					let total_page = parseInt(listNode.getAttribute('data-totalPage'));
					let mode = $('.mode option:selected').val();
		
					$('.mode').change(function() {
						mode = $(this).val(); // 선택된 option의 값 가져오기
					});
		
					if (pageNo === 0 || pageNo < total_page) {
						pageNo++;
						loadContent(pageNo, mode);
					}
				}
			});
		}
		
		const io = new IntersectionObserver(ioCallback); // 관찰자 초기화
		io.observe(sentinelNode); // 관찰대상(요소) 등록
		
		$(function() { // select onchange 이벤트 등록
			$('.mode').change(function() {
				$('.list-content').empty();
				let url = '${pageContext.request.contextPath}/mypage/pointhistory';
				let query = 'pageNo=1&mode=' + $(this).val();
				const fn = function(data) {
					$('.list-content').empty();
					addNewContent(data);
				}
				ajaxFunc(url, 'get', query, 'json', fn);
			});
		});
	</script>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>