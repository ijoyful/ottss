<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

<script type="text/javascript">
	function sendOk() {
	    const f = document.boardForm;
		let str;
		
	    str = f.title.value.trim();
	    if(!str) {
	        alert('제목을 입력하세요. ');
	        f.title.focus();
	        return;
	    }
	
	    str = f.content.value.trim();
	    if(!str) {
	        alert('내용을 입력하세요. ');
	        f.content.focus();
	        return;
	    }
	
	    f.action = '${pageContext.request.contextPath}/show/${mode}';
	    f.submit();
	}
</script>


</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp" />

	<main id="main">
		<div class="mainInner">
			<div class="writeInner">
				<form name="boardForm" method="post" enctype="multipart/form-data">
					<ul>
						<li class="listTitle">제목</li>
						<li class="listContent"><input type="text" name="title" value="${dto.title}"></li>
					</ul>
					<ul>
						<li class="listTitle">작성자</li>
						<li class="listContent"><p>${sessionScope.member.nickName}</p></li>
					</ul>
					<ul class="content">
						<li class="listTitle">내용</li>
						<li class="listContent"><textarea name="content">${dto.content}</textarea></li>
					</ul>
					<ul>
						<li class="listTitle">첨부파일</li>
						<li class="listContent"><input type="file" name="selectFile"></li>
					</ul>

					<ul>
						<li>
							<button type="button" class="btn btn-dark" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}&nbsp;<i class="bi bi-check2"></i></button>
							<button type="reset" class="btn btn-light">다시입력</button>
							<button type="button" class="btn btn-light"onclick="location.href='${pageContext.request.contextPath}/show/list';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button> 
							<c:if test="${mode=='update'}">
								<input type="hidden" name="st_num" value="${dto.st_num}">
								<input type="hidden" name="page" value="${page}">
							</c:if>
						</li>
					</ul>

				</form>
			</div>
		</div>
		<!-- mainInner -->
	</main>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>