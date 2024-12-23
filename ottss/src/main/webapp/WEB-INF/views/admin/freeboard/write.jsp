<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">
<script type="text/javascript">
	function sendOk() {
		const f = document.freeForm;
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
		
		    f.action = '${pageContext.request.contextPath}/admin/freeboard/${mode}';
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
				<div class="body-main">
				    <div class="mainInner">
				        <div class="writeInner">
				            <form name = "freeForm" method="post" enctype="multipart/form-data">
				                <ul>
				                    <li class="listTitle">제목</li>
				                    <li class="listContent"><input type="text" name="title"></li>
				                </ul>
				                <ul>
				                    <li class="listTitle">작성자</li>
				                    <li class="listContent"><input type="text" value="${sessionScope.member.nickName}" readonly ></li>
				                </ul>
				                <ul class="content">
				                    <li class="listTitle">내용</li>
				                    <li class="listContent"><textarea name="content"></textarea></li>
				                </ul>
				                <ul>
				                    <li class="listTitle">첨부파일</li>
				                    <li class="listContent"><input type="file" name="selectFile" multiple class="form-control"></li>
				                </ul>
				                <table class="table table-borderless">
				 					<tr>
										<td class="text-center">
											<button type="button" class="btn btn-dark" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}&nbsp;<i class="bi bi-check2"></i></button>
											<button type="reset" class="btn btn-light">다시입력</button>
											<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/show/list?size=${size}';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button>
											<input type="hidden" name="size" value="${size}">
											<c:if test="${mode=='update'}">
												<input type="hidden" name="n_num" value="${dto.fb_num}">
												<input type="hidden" name="page" value="${page}">
											</c:if>
										</td>
									</tr>
								</table>
				            </form>
				        </div>
				    </div> <!-- mainInner -->
			    </div>
		    </div>
	    </div>
	</main>
	
	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>

</body>
</html>