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
					<h3><i class="bi bi-clipboard"></i>&nbsp;회원 리스트</h3>
				</div>
				<div class="row board-list-footer" style="margin-bottom: 20px">
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/player/list';"><i class="bi bi-arrow-clockwise"></i></button>
					</div>
					<div class="col-6 text-center">
						<form class="row" name="searchForm" action="${pageContext.request.contextPath}/admin/player/list" method="post" style="justify-content: flex-end; padding-right: calc(var(--bs-gutter-x)* .5);">
							<div class="col-auto p-1">
								<select name="schType" class="form-select">
									<option value="all" ${schType=="all"?"selected":""}>아이디 + 이름</option>
									<option value="userId" ${schType=="userId"?"selected":""}>아이디</option>
									<option value="userName" ${schType=="userName"?"selected":""}>이름</option>
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
			    	<ul class="listTitle">
		                <li>ID</li>
		                <li>이름</li>
		                <li>닉네임</li>
		                <li>생년월일</li>
		                <li>전화번호</li>
		                <li>이메일</li>
		                <li>가입 날짜</li>
		                <li>보유 포인트</li>
		                <li>권한 번호</li>
		                <li>차단</li>
		            </ul>
		            <c:forEach var="list" items="${list}">
			            <ul class="listContent">
			                <li class="userId" data-userId = "${list.id}">${list.id}</li>
			                <li>${list.name}</li>
			                <li>${list.nickName}</li>
			                <li>${list.birth}</li>
			                <li>${list.tel1}-${list.tel2}-${list.tel3}</li>
			                <li>${list.email1}@${list.email2}</li>
			                <li>${list.reg_date}</li>
			                <li>${list.point}</li>
			                <li>${list.powercode}</li>
			                <li>
			                	<c:choose>
			                		<c:when test="${list.block == 0}">
			                			<button class="blockBtn" data-block = "${list.block}">차단</button>
			                		</c:when>
			                		<c:otherwise>
			                			<button class="blockBtn" data-block = "${list.block}">차단 해제</button>
			                		</c:otherwise>
			                	</c:choose>
		                	</li>
			            </ul>
		            </c:forEach>
				</div>
	            <div class="page-navigation">
					${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</div>
			</div>		
		</div>
	</main>
	
	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>
</body>
<script type="text/javascript">

	function toggleUserBlock(userId, block, current) {
		
		$.ajax ({
			
			url : "${pageContext.request.contextPath}/admin/player/toggleBlock",
			type : "POST",
			data : {
				id : userId,
				block : block
			},
			dataType : 'json',
			success : function(response) {
				const button = $('.blockBtn');
				
				if(block === 0) {
					current.text("차단 해제");
					current.data("block", 1);
				} else {
					current.text("차단");
					current.data("block", 0);
				}
			},
			error : function() {
				alert("에러가 발생했습니다. 다시 시도해주세요");
			}
			
		});
		
	};
	
	$(function() {
		$('.body-main').on('click', '.blockBtn', function() {
			
			const userId = $(this).closest('.listContent').find('.userId').data("userid");
			const block = $(this).data("block");
			
			toggleUserBlock(userId, block, $(this));
		});
	});

</script>
</html>