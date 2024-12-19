<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64;iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/list.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
</head>

<script type="text/javascript">
	function searchList() {
		const f = document.searchForm;
		f.submit();
	}
</script>


<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	<div class="wrap">

		<main id="main">
			<div class="mainInner">
				
				<div class="body-title">
                	<h3><i class="bi bi-app"></i> 자랑게시판 </h3>
            	</div>
            	
            	<div class="row board-list-footer" style="margin-bottom: 20px;">
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/show/list';"><i class="bi bi-arrow-clockwise"></i></button>
					</div>
					<div class="col-6 text-center">
						<form class="row" name="searchForm" action="${pageContext.request.contextPath}/show/list" method="post" style="justify-content: center;">
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
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/show/write';">글올리기</button>
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
					<c:forEach var="dto" items="${list}" varStatus="status">
						<ul class="listContent">
							<li>${dataCount - (page - 1) * size - status.index}</li>
							<li><a href="${articleUrl}&st_num=${dto.st_num}">${dto.title}</a></li>
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
			<!-- mainInner -->
		</main>

		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
		<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
	</div>
</body>
</html>