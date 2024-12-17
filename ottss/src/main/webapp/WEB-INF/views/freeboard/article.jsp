﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<c:if test="${sessionScope.member.id==dto.id || sessionScope.member.powerCode == 99}">
	<script type="text/javascript">
		function deleteOk() {
			if(confirm('게시글을 삭제 하시 겠습니까 ? ')) {
				let query = 'fb_num=${dto.fb_num}&${query}';
				let url = '${pageContext.request.contextPath}/freeboard/delete?' + query;
				location.href = url;
			}
		}
	</script>
</c:if>

</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
	
<main>
	<div class="container">
		<div class="body-container">	
			<div class="body-title">
				<h3><i class="bi bi-clipboard"></i> 자게유시판 </h3>
			</div>
			
			<div class="body-main">
				
				<table class="table board-article">
					<thead>
						<tr>
							<td colspan="2" align="left">
								${dto.title}
							<hr>
							</td>
						</tr>
					</thead>
					
					<tbody>
						<tr>
							<td width="50%" style="margin-top: 0px;">
								닉네임 : ${dto.nickname}<hr>
							</td>
							<td align="right">
								작성일 : ${dto.reg_date} | 조회 ${dto.hitCount}<hr>
							</td>
						</tr>
						
						<tr>
							<td colspan="2" valign="top" height="200" style="border-bottom:none;">
								${dto.content}
							</td>
						</tr>
						
						<tr>
							<td colspan="2" class="text-center p-3">
								<button type="button" class="btn btn-outline-primary btnSendBoardLike" title="좋아요"><i class="bi ${insereFreeLike ? 'bi-hand-thumbs-up-fill':'bi-hand-thumbs-up'}"></i>&nbsp;&nbsp;<span id="likeCount">${dto.likeCount}</span></button>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<c:forEach var="vo" items="${listFile}" varStatus="status">
									<p class="border text-secondary mb-1 p-2">
										<i class="bi bi-folder2-open"></i>
										<a href="${pageContext.request.contextPath}/freeboard/download?fileNum=${vo.fileNum}">${vo.c_fileName}</a>
									</p>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td colspan = "2"> 다음글 :
								<c:if test="${not empty nextDTO}">
									<a href="${pageContext.request.contextPath}/freeboard/article?${query}&fb_num=${nextDTO.fb_num}">${nextDTO.title}</a>
								</c:if>
							</td>
						</tr>
						<tr>
							<td colspan = "2"> 이전글 :
								<c:if test="${not empty prevDTO}">
									<a href="${pageContext.request.contextPath}/freeboard/article?${query}&num=${prevDTO.fb_num}">${prevDTO.title}</a>
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
				
				
				<table class="table table-borderless">
					<tr>
						<td width="50%">
							 <c:choose>
                            	<c:when test="${sessionScope.member.id==dto.id}">                                                                    
                            		<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/freeboard/update?fb_num=${dto.fb_num}&page=${page}';">수정</button>
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
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/freeboard/list?${query}';">리스트</button>
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
                    
                    <div id="listReply"> 
                    	
                    </div>
                </div>
                
			</div>
		</div>
	</div>
</main>
<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/freeboard/login';
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



$(function() {
	$('.btnSendReply').click(function() {
		const $tb = $(this).closest('table');
		let content = $tb.find('textarea').val().trim();
		
		if(! content){
			$tb.find('textarea').focus();
			return false;
		}
		
		let fb_num = '${dto.fb_num}';
		let url = '${pageContext.request.contextPath}/freeboard/insertComment';
		let query = {fb_num:fb_num, content:content};
					//formDate를 객체로 처리하면 content를 인코딩하면 안된다.
					
		const fn = function(data) {
			if(data.state === 'true'){
				$tb.find('textarea').val('');
				listPage(1);
			} else {
				alert('댓글 등록이 실패 했습니다.');
			}
		};
		
		ajaxFun(url, 'post', query, 'json', fn);
	
	});

});


$(function() {
	listPage(1);
});

function listPage(page) {
	let url = '${pageContext.request.contextPath}/freeboard/listComment';
	let query ='fb_num=${dto.fb_num}&pageNo=' + page;
	let selector = '#listReply';
	
	const fn = function(data) {
		console.log(data);
		$(selector).html(data);
	};
	ajaxFun(url, 'get', query, 'text', fn);
}

//댓글 삭제
$(function() {
	$('#listReply').on('click','.deleteReply', function(){
		if(! confirm('댓글을 삭제 하시겠습니까 ?')){
			return false;
		}
		
		let stc_num = $(this).attr('data-fbc_num');
		let page = $(this).attr('data-pageNo');
		
		let url = '${pageContext.request.contextPath}/freeboard/deleteComment';
		let query = 'fbc_num=' + fbc_num;
		
		const fn = function(data){
			listPage(page);
		};
		
		ajaxFun(url, 'post', query, 'json', fn);		
	});
});

$(function() {
	$('.btnSendBoardLike').click(function() {
		const $i = $(this).find('i');
		let userLiked = $i.hasClass('bi-hand-thumbs-up-fill');
		let msg = userLiked ? '게시글 공감을 취소하시겠습니까 ? ' : '게시글에 공감하십니까 ?';
		
		if(! confirm(msg)) {
			return false;
		}
		
		let url = '${pageContext.request.contextPath}/freeboard/insertFreeLike';
		let query = 'fb_num=${dto.fb_num}&userLiked=' + userLiked;
		
		const fn = function(data) {
			let state = data.state;
			
			if(state === 'true') {
				if(userLiked){
					$i.removeClass('bi-hand-thumbs-up-fill').addClass('bi-hand-thumbs-up');
				} else {
					$i.removeClass('bi-hand-thumbs-up').addClass('bi-hand-thumbs-up-fill');					
				}
				
				let count = data.likeCount;
				$('#likeCount').text(count);
				
			} else if (state === 'liked'){
				alert('게시글 공감은 한번만 가능합니다.');
			} else {
				alert('게시글 공감 여부 처리가 실패했습니다.');
			}
			
			
		};
		
		ajaxFun(url,'post', query, 'json', fn);
		
		
		
	});
});



</script>
<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>