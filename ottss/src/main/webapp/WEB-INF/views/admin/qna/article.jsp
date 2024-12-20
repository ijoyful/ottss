<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">
<script type="text/javascript">
	
function deleteBoard() {
	if(confirm('게시글을 삭제하시겠습니까?')) {
		let query = 'num=${dto.faq_num}&${query}';
		let url = '${pageContext.request.contextPath}/admin/qna/delete?' + query;
		location.href = url;
	}
}

function ajaxFun(url, method, formData, dataType, fn, file=false){
	const settings = {
			type: method,
			data: formData,
			dataType: dataType,
			success: function(data){
				fn(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);
			},
			complete: function(){
				
			},
			error: function(jqXHR){
				if(jqXHR.status === 403){
					login();
					return false;
				} else if (jqXHR.status === 406){
					alert('요청 처리가 실패했습니다.');
					return false;
				}
				console.log(jqXHR.responseText);
			}
	};
	
	if(file) {
		settings.processData = false; // 파일 전송시 필수. 서버로 보낼 데이터를 쿼리문자열로 변환 여부
		settings.contentType = false; // 파일 전송시 필수. 기본은 application/x-www-urlencoded
	}

	$.ajax(url, settings);
}

function answerdialogshow() {
	let url = '${pageContext.request.contextPath}/admin/qna/answer';
	let num = ${dto.faq_num};
	const fn = function(data) {
		$('#answerModal .modal-body').html(data);
		$('#answerModal').modal("show");
	};
	ajaxFun(url, 'get', {num: num}, 'text', fn);
}

function sendCancel() {
	$('#answerModal .modal-body').empty();
	$('#answerModal').modal("hide");
}

function sendOk(id, num) {
	const f = document.answerForm;
	let answer = f.content.value;

	if (!answer) { // 신고사유가 선택되지 않았으면
		alert('답변을 입력하세요.');
		return;
	}

    let url = '${pageContext.request.contextPath}/admin/qna/answer';
    
    const fn = function(data) {
    	$('#answerModal .modal-body').empty();
    	$('#answerModal').modal('hide');
    	if (data.state === 'true') {
    		alert('답변이 정상적으로 등록되었습니다.');
    	}
    };
    
    ajaxFun(url, 'post', {id: id, num: num, a_content: answer}, 'json', fn);
}

$(function(){
	$('#chkAll').click(function(){
		$('form input[name=nums]').prop('checked', $(this).is(':checked'));
	});
	
	$('form input[name=nums]').click(function(){
		let b = $('form input[name=nums]').length === $('form input[name=nums]:checked').length;
		$('#chkAll').prop('checked', b );
	});

	$('#btnDeleteList').click(function(){
		let cnt = $('form input[name=nums]:checked').length;
		if(cnt === 0) {
			alert('삭제할 게시글을 선택하세요.');
			return false;
		}
		
		if(confirm('선택한 게시글을 삭제 하시겠습니까 ? ')) {
			const f = document.listForm;
			f.action = '${pageContext.request.contextPath}/admin/qna/deleteList';
			f.submit();
		}
	});

	const num = ${dto.faq_num};
	// 카테고리 대화상자 객체
	const myModalEl = document.getElementById('answerModal');
	
	myModalEl.addEventListener('show.bs.modal', function(){
		// 모달 대화상자가 보일때
	});

	myModalEl.addEventListener('hidden.bs.modal', function(){
		// 모달 대화상자가 닫힐때
		location.href = '${pageContext.request.contextPath}/admin/qna/article?${query}&num=' + num;
	});

});
	
</script>
</head>
<body>

	<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>
	
	<main id="main">
		<jsp:include page="/WEB-INF/views/admin/layout/left.jsp"/>
		<div class="wrapper">
			<div class="body-container">
				<div class="body-title">
					<h3><i class="bi bi-clipboard"></i> QnA </h3>
				</div>
			    <div class="body-main">
		    	    <div class="mainInner">
				        <div class="writeInner">
			                <ul>
			                    <li class="listTitle">제목</li>
			                    <li class="listContent">${dto.q_title}</li>
			                </ul>
			                <ul>
			                    <li class="listTitle">작성자</li>
			                    <li class="listContent">${dto.q_nickname}</li>
			                </ul>
			                <ul>
			                    <li class="listTitle">질문 날짜</li>
			                    <li class="listContent">${dto.question_date}</li>
			                </ul>
			                <ul>
			                    <li class="listTitle">조회</li>
			                    <li class="listContent">${dto.hitCount}</li>
			                </ul>
			                <ul class="content">
			                    <li class="listTitle">내용</li>
			                    <li class="listContent"><textarea readonly>${dto.q_content}</textarea></li>
			                </ul>
			                <ul>
			                    <li class="listTitle">첨부파일</li>
			                    <li class="listContent">
			                    	<c:forEach var="vo" items="${listFile}" varStatus="status">
			                    		<a href="${pageContext.request.contextPath}/faq/download?fileNum=${vo.fileNum}">${vo.c_fileName}</a>
			                    	</c:forEach>
			                    </li>
			                </ul>
			                <ul>
			                    <li class="listTitle">이전글</li>
			                    <li class="listContent">
			                    	<c:if test="${not empty nextDTO}">
										<a href="${pageContext.request.contextPath}/admin/qna/article?${query}&num=${nextDTO.faq_num}">${nextDTO.q_title}</a>
									</c:if>
			                    </li>
			                </ul>
			                <ul>
			                    <li class="listTitle">다음글</li>
			                    <li class="listContent">
			                    	<c:if test="${not empty prevDTO}">
										<a href="${pageContext.request.contextPath}/admin/qna/article?${query}&num=${prevDTO.faq_num}">${prevDTO.q_title}</a>
									</c:if>
			                    </li>
			                </ul>
					        <table class="table board-article">
								<c:if test="${not empty dto.a_nickname}">
									<thead>
										<tr>
											<td colspan="2" align="left">${dto.q_title}</td>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td width="50%">닉네임 : ${dto.a_nickname}</td>
											<td align="right">답변일자: ${dto.answer_date}</td>
										</tr>
										
										<tr>
											<td colspan="2" valign="top" height="200" style="border-bottom: none;">${dto.a_content}</td>
										</tr>
									</tbody>
								</c:if>
							</table>
				        </div>
				        <table class="table table-borderless">
							<tr>
								<td width="50%">
					    			<button type="button" class="btn btn-light" onclick="deleteBoard();">삭제</button>
								</td>
								<td class="text-end">
									<c:if test="${empty dto.a_nickname}">
									<button type="button" class="btn btn-light" onclick="answerdialogshow();">답변하기</button>
									</c:if>
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/qna/list?${query}';">리스트</button>
								</td>
							</tr>
						</table>
				    </div> <!-- mainInner -->
				</div>
			</div>		
		</div>
	</main>

	<!-- 모달창 -->
	<div class="modal fade" id="answerModal" data-bs-keyboard="false" tabindex="-1" aria-labelledby="reportModalLable" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="myDialogModalLabel">신고</h5>
				</div>
				<div class="modal-body pt-1">
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>
</body>
</html>