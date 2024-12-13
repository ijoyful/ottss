<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Static Header Include -->
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

    <style type="text/css">
        .body-container {
            max-width: 800px;
        }
    </style>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<!-- 에러 있어서 일단 주석  
<c:if test="${sessionScope.member.id==dto.id || sessionScope.member.powerCode == 99}">
	<script type="text/javascript">
		function deleteOk() {
			if(confirm('게시글을 삭제 하시 겠습니까 ? ')) {
				let query = 'st_num=${dto.st_num}&${query}';
				let url = '${pageContext.request.contextPath}/show/delete?' + query;
				location.href = url;
			}
		}
	</script>
</c:if>
-->

</head>
<body>

<header>
    <!-- Static Header Include -->
    <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<main>
    <div class="container">
        <div class="body-container">    
            <div class="body-title">
                <h3><i class="bi bi-app"></i> 자랑게시판 </h3>
            </div>
            
            <div class="body-main">
                
                <table class="table board-article">
                    <thead>
                        <tr>
                            <td colspan="2" align="center">
                                ${dto.title}
                            </td>
                        </tr>
                    </thead>
                    
                    <tbody>
                        <tr>
                            <td width="50%">
                                닉네임 : ${dto.nickname}
                            </td>
                            <td align="right">
                                ${dto.reg_date} | 조회 ${dto.hitCount}
                            </td>
                        </tr>
                        
                        <tr>
                            <td colspan="2" valign="top" height="200">
                                ${dto.content}
                            </td>
                        </tr>
          
                        <tr>
                            <td colspan="2" valign="top" height="200">
                                <c:forEach var="vo" items="${listFile}" varStatus="status">
									<img src="${pageContext.request.contextPath}/uploads/show/${vo.s_fileName}" class="img-fluid img-thumbnail w-100 h-auto">
								</c:forEach>
                            </td>
                        </tr>
                        
                        <tr>
                            <td colspan="2">
                                이전글 :
                                <c:if test="${not empty prevDto}">
                                    <a href="${pageContext.request.contextPath}/show/article?${query}&st_num=${prevDto.st_num}">${prevDto.title}</a>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                다음글 :
                                <c:if test="${not empty nextDto}">
                                    <a href="${pageContext.request.contextPath}/show/article?${query}&st_num=${nextDto.st_num}">${nextDto.title}</a>
                                </c:if>
                            </td>
                        </tr>
                    </tbody>
                </table>
                
                <table class="table table-borderless">
                    <tr>
                        <td width="50%">
                            <!-- 수정, 삭제 버튼 부분 -->
                            <c:choose>
                            	<c:when test="${sessionScope.member.id==dto.id}">                                                                    
                            		<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/show/update?st_num=${dto.st_num}&page=${page}';">수정</button>
                           		</c:when>
                           		<c:otherwise>
									<button type="button" class="btn btn-light" disabled>수정</button>
								</c:otherwise>
                            </c:choose>
                            
                            <c:choose>
                            	<c:when test="${sessionScope.member.id==dto.id || sessionScope.member.powerCode==99 }">
                            		<button type="button" class="btn btn-light"  onclick="deleteOk();">삭제</button>
                        		</c:when>
                        		<c:otherwise>
					    			<button type="button" class="btn btn-light" disabled>삭제</button>
					    		</c:otherwise>
                        	</c:choose>
                        
                        </td>
                        <td class="text-end">
                            <button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/show/list?${query}';">리스트</button>
                        </td>
                    </tr>
                </table>
                
                <div class="reply">
                    <form name="replyForm" method="post">
                        <div class='form-header'>
                            <span class="bold">댓글</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
                        </div>
                        
                        <table class="table table-borderless reply-form">
                            <tr>
                                <td>
                                    <textarea class="form-control" name="content"></textarea>
                                </td>
                            </tr>
                            <tr>
                               <td align="right">
                                    <button type="button" class="btn btn-light btnSendReply">댓글 등록</button>
                                </td>
                             </tr>
                        </table>
                    </form>
                    
                    <div id="listReply"></div>
                </div>
                
            </div>
        </div>
    </div>
</main>


<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/show/login';
}	



function ajaxFun(url, method, formData, dataType, fn, file=false){
	const settings = {
			type: method,
			data: formData,
			dataType: dataType,
			success: function(data){
				fn(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);
			},
			complete: function(){
				
			},
			error: function(jqXHR){
				if(jqXHR.status === 403){
					login();
					return false;
				} else if (jqXHR.status === 406){
					alert('요청 처리가 실패했습니다.');
					return false;
				}
				console.log(jqXHR.responseText);
			}
	};
	
	if(file) {
		settings.processData = false; // 파일 전송시 필수. 서버로 보낼 데이터를 쿼리문자열로 변환 여부
		settings.contentType = false; // 파일 전송시 필수. 기본은 application/x-www-urlencoded
	}

	$.ajax(url, settings);
}


//이거랑 listPage가 있어야 나온다. ajax
$(function() {
	listPage(1);
});

function listPage(page) {
	let url = '${pageContext.request.contextPath}/show/listReply';
	let query ='st_num=${dto.st_num}&pageNo=' + page;
	let selector = '#listReply';
	
	const fn = function(data) {
		$(selector).html(data);
	};
	ajaxFun(url, 'get', query, 'text', fn);
}




</script>



<footer>
    <!-- Static Footer Include -->
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>
