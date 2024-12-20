<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/list.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
	#main .listInner ul li:nth-child(2) {width: 5%;} /* 구분 */
	#main .listInner ul li:nth-child(3) {width: 55%;} /* 제목 */
	#main .listInner ul li:nth-child(4) {width: 10%;} /* 작성자 */
	#main .listInner ul li:nth-child(5) {width: 20%;} /* 작성일 */
	#main .listInner ul li:nth-child(6) {width: 5%;} /* 조회수 */
</style>

</head>
<body>
	<!-- div.wrap 꼭 써야됩니다. 없으면 폼 망가져요~~ --> 
	<div class="wrap">
		<!-- header -->
		<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
		<!-- header End -->
		
		<!-- main -->
		<main id="main">
			<!-- div.mainInner까지 있어야 폼 안망가집니다. 안에다가 코딩 해주세용 -->
			<div class="mainInner">
				<div class="body-title">
					<h3>
						<i class="bi bi-clipboard"></i> 자유게시판
					</h3>
				</div>
				<div class="row board-list-footer" style="margin-bottom: 20px;">
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/freeboard/list';"><i class="bi bi-arrow-clockwise"></i></button>
					</div>
					<div class="col-6 text-center" style="display: flex; justify-content: center;">
						<form class="row" name="searchForm" action="${pageContext.request.contextPath}/freeboard/list" method="post" style="justify-content: center;">
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
					<div class="col text-end">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/freeboard/write';">글올리기</button>
					</div>
				</div>
				<!-- div.listInner 테이블 처럼 쓸 수 있는 ul-li 입니당 foreach 돌리실때 ul로 돌리면 끗! -->
				<div class="listInner">
                    <ul class="listTitle">
                        <li>No.</li>
                        <li>구분</li>
                        <li>제목</li>
                        <li>작성자</li>
                        <li>작성일</li>
                        <li>조회수</li>
                    </ul>
                   <c:forEach var = "dto" items="${list}" varStatus="status">
                    <ul class="listContent">
                        <li>${dataCount - (page - 1) * size - status.index}</li>
                        <li>${dto.categories}</li>
                        <li><a href="${articleUrl}&num=${dto.fb_num}" class="text-reset">${dto.title}</a></li>
                        <li>${dto.nickname}</li>
                        <li>${dto.reg_date}</li>
                        <li>${dto.hitCount}</li>
                    </ul>
                   </c:forEach>
                </div>
				<div class="page-navigation">
					${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</div>
			</div>
		</main>
		<!-- main End -->
		
		<!-- footer -->
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
		<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
		<!-- footer End -->
	</div>
</body>
</html>