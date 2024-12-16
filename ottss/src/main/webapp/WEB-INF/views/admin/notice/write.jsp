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
<style type="text/css">
	#main .writeInner ul {padding: unset !important;}
</style>
<script type="text/javascript">
	function sendOk() {
	    const f = document.noticeForm;
		let str;
		
	    str = f.title.value.trim();
	    if(!str) {
	        alert('제목을 입력하세요.');
	        f.title.focus();
	        return;
	    }
	
	    str = f.content.value.trim();
	    if(!str) {
	        alert('내용을 입력하세요.');
	        f.content.focus();
	        return;
	    }
	
	    f.action = '${pageContext.request.contextPath}/admin/notice/${mode}';
	    f.submit();
	}
	
	<c:if test="${mode=='update'}">
		function deleteFile(fileNum) {
			if(! confirm('파일을 삭제 하시겠습니까 ? ')) {
				return;
			}
			
			let qs = 'n_num=${dto.n_num}&fileNum=' + fileNum + '&page=${page}&size=${size}';
			let url = '${pageContext.request.contextPath}/admin/notice/deleteFile?' + qs;
			location.href = url;
		}
	</c:if>
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
				            <form name="noticeForm" method="post" enctype="multipart/form-data">
				                <ul>
				                    <li class="listTitle">제목</li>
				                    <li class="listContent"><input type="text" name="title" value="${dto.title}"></li>
				                </ul>
				                <ul>
				                    <li class="listTitle">공지 여부</li>
				                    <li class="listContent" style="justify-content: flex-start; padding-left: 20px;">
				                    	<input type="checkbox" name="notice_status" id="notice" value="1" style="width: auto;" ${dto.notice_status==1 ? "checked":""}>
										<label class="form-check-label" for="notice">&nbsp;&nbsp;공지</label>
				                    </li>
				                </ul>
				                <ul>
				                    <li class="listTitle">작성자</li>
				                    <li class="listContent"><input type="text" value="${sessionScope.member.nickName}" readonly></li>
				                </ul>
				                <ul class="content">
				                    <li class="listTitle">내용</li>
				                    <li class="listContent"><textarea name="content">${dto.content}</textarea></li>
				                </ul>
				                <ul>
				                    <li class="listTitle">첨부파일</li>
				                    <li class="listContent"><input type="file" name="selectFile" multiple class="form-control"></li>
				                </ul>
				                
				                <c:if test="${mode=='update'}">
									<c:forEach var="vo" items="${listFile}">
										<ul>
											<li class="bg-light col-sm-2">첨부된파일</li>
											<li>
												<p class="form-control-plaintext">
													<a href="javascript:deleteFile('${vo.file_num}')"><i class="bi bi-trash"></i></a>
													${vo.c_filename}
												</p>
											</li>
										</ul>
									</c:forEach>
								</c:if>
				                
				                <table class="table table-borderless">
				 					<tr>
										<td class="text-center">
											<button type="button" class="btn btn-dark" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}&nbsp;<i class="bi bi-check2"></i></button>
											<button type="reset" class="btn btn-light">다시입력</button>
											<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list?size=${size}';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button>
											<input type="hidden" name="size" value="${size}">
											<c:if test="${mode=='update'}">
												<input type="hidden" name="n_num" value="${dto.n_num}">
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