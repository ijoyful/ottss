<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/list.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<style type="text/css">
	#main .listInner {margin-bottom: 20px;}
	#main .listInner ul {padding: unset !important;}
	#main .listInner ul li:nth-child(1) {width: 10%;}
	#main .listInner ul li:nth-child(2) {width: 50%;}
	#main .listInner ul li:nth-child(3) {width: 15%;}
	#main .listInner ul li:nth-child(4) {width: 15%;}
	#main .listInner ul li:nth-child(5) {width: 10%;}
</style>
<script type="text/javascript">
	function changeList() {
		const f = document.listForm;
		f.page.value = '1';
		f.action = '${pageContext.request.contextPath}/notice/list';
		f.submit();
	}

</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp" />

	<main id="main">
		<div class="mainInner">
			<div class="body-title">
				<h3>
					<i class="bi bi-clipboard"></i> 공지사항
				</h3>
			</div>
			<form name="listForm" method="post">
				<div class="row board-list-header" style="margin-bottom: 20px;">
					<div class="col-auto">
						<c:if test="${dataCount != 0}">
							<select name="size" class="form-select" onchange="changeList();">
								<option value="5" ${size==5 ? "selected ":""}>5개씩 출력</option>
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
						<li>No.</li>
						<li>제목</li>
						<li>작성자</li>
						<li>작성일</li>
						<li>조회수</li>
					</ul>
					<c:forEach var="listNotice" items="${listNotice}">
						<ul class="listContent">
							<li><span style="background-color: #edc239; padding: 5px; border-radius: 5px;">공지</span></li>
							<li><a href="${articleUrl}&n_num=${listNotice.n_num}">${listNotice.title}</a></li>
							<li>${listNotice.nickname}</li>
							<li>${listNotice.reg_date}</li>
							<li>${listNotice.hitCount}</li>
						</ul>
					</c:forEach>
					<c:forEach var="list" items="${list}" varStatus="status">
						<ul class="listContent">
							<li>${dataCount-(page-1)*size-status.index}</li>
							<li>
								<c:if test="${dto.gap<1}">
									<img src="${pageContext.request.contextPath}/resources/images/new.gif">
								</c:if>
								<a href="${articleUrl}&n_num=${list.n_num}">${list.title}</a>
							</li>
							<li>${list.nickname}</li>
							<li>${list.reg_date}</li>
							<li>${list.hitCount}</li>
						</ul>
					</c:forEach>
				</div>
			</form>
			
			<div class="page-navigation">${dataCount != 0 ? paging : "등록된 게시글이 없습니다."}</div>
					
			<div class="row board-list-footer">
				<div class="col">
					<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/notice/list';"><i class="bi bi-arrow-clockwise"></i></button>
				</div>
				<div class="col-6 text-center" style="display: flex; justify-content: flex-end;">
					<form class="row" name="searchForm" action="${pageContext.request.contextPath}/notice/list" method="post" style="justify-content: center;">
						<div class="col-auto p-1">
							<select name="schType" class="form-select">
								<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
								<option value="userName" ${schType=="userName"?"selected":""}>작성자</option>
								<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
								<option value="subject" ${schType=="subject"?"selected":""}>제목</option>
								<option value="content" ${schType=="content"?"selected":""}>내용</option>
							</select>
						</div>
						<div class="col-auto p-1">
							<input type="text" name="kwd" value="${kwd}" class="form-control">
						</div>
						<div class="col-auto p-1">
							<input type="hidden" name="size" value="${size}">
							<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
						</div>
					</form>
				</div>
			</div>
		</div> <!-- mainInner -->
	</main>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>