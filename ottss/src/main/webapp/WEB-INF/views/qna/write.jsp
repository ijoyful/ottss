<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<script type="text/javascript">
function sendOk() {
	const f = document.faqForm;
	let str;

	str = f.title.value.trim();
	if (!str) {
		f.title.focus();
		return;
	}
	str = f.content.value.trim();
	if (!str) {
		f.content.focus();
		return;
	}

	f.action = '${pageContext.request.contextPath}/qna/write';
	f.submit();
}
</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
	    <div class="mainInner" style="padding: 100px 0px 30px 0px">
	    	<div class="listTitle">QnA</div>
	        <div class="writeInner">
	            <form name="faqForm" method="post" enctype="multipart/form-data">
	                <ul>
	                    <li class="listTitle">제목</li>
	                    <li class="listContent"><input type="text" name="title"></li>
	                </ul>
	                <ul>
	                    <li class="listTitle">작성자</li>
	                    <li class="listContent"><input type="text" value="${sessionScope.member.nickName}" readonly></li>
	                </ul>
	                <!-- 
	                <ul>
	                    <li class="listTitle">날짜</li>
	                    <li class="listContent"><input type="date" value="2024-11-12" readonly></li>
	                </ul>
	                 -->
	                <ul class="content">
	                    <li class="listTitle">내용</li>
	                    <li class="listContent"><textarea name="content"></textarea></li>
	                </ul>
	                <ul>
	                    <li class="listTitle">첨부파일</li>
	                    <li class="listContent"><input type="file" name="selectFile" multiple class="form-control"></li>
	                </ul>
	            </form>
	        </div>
	    </div> <!-- mainInner -->
	    <table class="table table-borderless">
	 					<tr>
							<td class="text-center">
								<button type="button" class="btn btn-dark" onclick="sendOk();">등록하기&nbsp;<i class="bi bi-check2"></i></button>
								<button type="reset" class="btn btn-light">다시입력</button>
								<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/qna/list';">등록취소&nbsp;<i class="bi bi-x"></i></button>
							</td>
						</tr>
					</table>
	</main>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>