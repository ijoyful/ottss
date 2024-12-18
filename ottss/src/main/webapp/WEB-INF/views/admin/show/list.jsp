<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64;iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/list.css" type="text/css">
</head>
<style type="text/css">
	#main .listInner {margin-bottom: 20px;}
	#main .listInner ul {padding: unset !important;}
	#main .listInner ul li:nth-child(1) {width: 5%;}
	#main .listInner ul li:nth-child(2) {width: 5%;}
	#main .listInner ul li:nth-child(3) {width: 50%;}
	#main .listInner ul li:nth-child(4) {width: 15%;}
	#main .listInner ul li:nth-child(5) {width: 15%;}
	#main .listInner ul li:nth-child(6) {width: 10%;}
</style>
<script type="text/javascript">
	function changeList() {
		const f = document.listForm;
		f.page.value = '1';
		f.action = '${pageContext.request.contextPath}/admin/show/list';
		f.submit();
	}
	
	function searchList() {
		const f = document.searchForm;
		f.submit();
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
				f.action = '${pageContext.request.contextPath}/admin/show/deleteList';
				f.submit();
			}
		});
		
	});
</script>

<body>
	<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>
	<main id="main">
		<jsp:include page="/WEB-INF/views/admin/layout/left.jsp"/>
		<div class="wrapper">
			<div class="body-title">
              	<h3><i class="bi bi-app"></i> 자랑게시판 </h3>
          	</div>
          	<div class="body-main">
				<div class="mainInner">
					<form name="listForm" method="post">
						<div class="row board-list-header" style="margin-bottom: 20px;">
				            <div class="col-auto me-auto">
								<button type="button" class="btn btn-light" id="btnDeleteList" title="삭제"><i class="bi bi-trash"></i></button>
				            </div>
				            <div class="col-auto">
								<c:if test="${dataCount != 0}">
									<select name="size" class="form-select" onchange="changeList();">
										<option value="5"  ${size==5 ? "selected ":""}>5개씩 출력</option>
										<option value="10" ${size==10 ? "selected ":""}>10개씩 출력</option>
										<option value="20" ${size==20 ? "selected ":""}>20개씩 출력</option>
										<option value="30" ${size==30 ? "selected ":""}>30개씩 출력</option>
										<option value="50" ${size==50 ? "selected ":""}>50개씩 출력</option>
									</select>
								</c:if>
								
								<input type="hidden" name="page" value="${page}">
								<input type="hidden" name="schType" value="${schType}">
								<input type="hidden" name="kwd" value="${kwd}">
	
							</div>
				        </div>
						<div class="listInner">
							<ul class="listTitle">
								<li class="chk">
				            		<input type="checkbox" class="form-check-input" name="chkAll" id="chkAll">
				            	</li>
								<li>No.</li>
								<li>제목</li>
								<li>작성자</li>
								<li>작성일</li>
								<li>조회수</li>
							</ul>
							<c:forEach var="dto" items="${list}" varStatus="status">
								<ul class="listContent">
									<li>
						            	<input type="checkbox" class="form-check-input" name="nums" value="${dto.st_num}">
						            </li>
									<li>${dataCount - (page - 1) * size - status.index}</li>
									<li><a href="${articleUrl}&st_num=${dto.st_num}">${dto.title}</a></li>
									<li>${dto.nickname}</li>
									<li>${dto.reg_date}</li>
									<li>${dto.hitCount}</li>
								</ul>
							</c:forEach>
						</div>
					</form>
					<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}</div>
					
					<div class="row board-list-footer">
						<div class="col">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/show/list';"><i class="bi bi-arrow-clockwise"></i></button>
						</div>
						<div class="col-6 text-center">
							<form class="row" name="searchForm" action="${pageContext.request.contextPath}/admin/show/list" method="post" style="justify-content: center;">
								<div class="col-auto p-1">
									<select name="schType" class="form-select">
										<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
										<option value="nickname" ${schType=="nickname"?"selected":""}>작성자</option>
										<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
										<option value="title" ${schType=="title"?"selected":""}>제목</option>
										<option value="content" ${schType=="content"?"selected":""}>내용</option>
									</select>
								</div>
								<div class="col-auto p-1">
									<input type="text" name="kwd" value="${kwd}" class="form-control">
								</div>
								<div class="col-auto p-1">
									<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
								</div>
							</form>
						</div>
						<div class="col text-end">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/show/write';">글올리기</button>
						</div>
					</div>				
				</div><!-- mainInner -->
			</div>
		</div>
	</main>
</body>
</html>