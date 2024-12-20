<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/reset.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/admin/list.css" type="text/css">
<style type="text/css">
	#main .listInner {margin-bottom: 20px;}
	#main .listInner ul {padding: unset !important;}
	#main .listInner ul li:nth-child(1) {width: 10%;} /* 신고 번호 */
	#main .listInner ul li:nth-child(2) {width: 35%;} /* 신고 사유 */
	#main .listInner ul li:nth-child(3) {width: 15%;} /* 신고 날짜 */
	#main .listInner ul li:nth-child(4) {width: 10%;} /* 신고자 아이디 */
	#main .listInner ul li:nth-child(5) {width: 10%;} /* 신고자 닉네임 */
	#main .listInner ul li:nth-child(6) {width: 10%;} /* 신고게시판 */
	#main .listInner ul li:nth-child(7) {width: 10%;} /* 게시판 번호 */
</style>
<script type="text/javascript">
	function searchList() {
		const f = document.searchForm;
		f.submit();
	}
</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>
	
	<main id="main">
		<jsp:include page="/WEB-INF/views/admin/layout/left.jsp"/>
		<div class="wrapper">
			<div class="body-container">
				<div class="body-title">
					<h3><i class="bi bi-clipboard"></i>&nbsp;신고된 글</h3>
				</div>
				<div class="row board-list-footer" style="margin-bottom: 20px">
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin';"><i class="bi bi-arrow-clockwise"></i></button>
					</div>
					<div class="col-6 text-center">
						<form class="row" name="searchForm" action="${pageContext.request.contextPath}/admin" method="post" style="justify-content: flex-end; padding-right: calc(var(--bs-gutter-x)* .5);">
							<div class="col-auto p-1">
								<select name="schType" class="form-select">
									<option value="all" ${schType=="all"?"selected":""}>아이디 + 닉네임</option>
									<option value="userId" ${schType=="userId"?"selected":""}>아이디</option>
									<option value=nickname ${schType=="nickname"?"selected":""}>닉네임</option>
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
				</div>
				<div class="body-main">
					<div class="listInner">
						<ul class="listTitle">
							<li>신고 번호</li>
							<li>신고 사유</li>
							<li>신고 날짜</li>
							<li>신고자 아이디</li>
							<li>신고자 닉네임</li>
							<li>신고 게시판(댓글)</li>
							<li>게시판(댓글) 번호</li>
						</ul>
						<c:forEach var="list" items="${reportList}" varStatus="status">
							<ul class="listContent">
								<li>${dataCount - (page - 1) * size - status.index}</li>
								<li onclick="alert('${list.content}')">${list.report_reason}</li>
								<li>${list.report_date}</li>
								<li>${list.id}</li>
								<li>${list.nickname}</li>
								<li>${list.target_table}</li>
								<li>${list.target_num}</li>
							</ul>
						</c:forEach>
					</div>
				</div>		
				<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}</div>
			</div>
		</div>
	</main>
	
	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>
</body>
</html>