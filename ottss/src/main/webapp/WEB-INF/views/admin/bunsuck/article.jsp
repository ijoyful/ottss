<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/write.css" type="text/css">

<c:if test="${sessionScope.member.id==dto.id || sessionScope.member.powerCode == 99}">
	<script type="text/javascript">
		function deleteOk() {
			if(confirm('게시글을 삭제 하시 겠습니까 ? ')) {
				let query = 'st_num=${dto.st_num}&${query}';
				let url = '${pageContext.request.contextPath}/admin/bunsuck/delete?' + query;
				location.href = url;
			}
		}
	</script>
</c:if>


</head>
<body>

	<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>

	<main>
		<jsp:include page="/WEB-INF/views/admin/layout/left.jsp"/>
	    <div class="wrapper">
	        <div class="body-container">    
	            <div class="body-title">
	                <h3><i class="bi bi-app"></i> 분SUCK게시판 </h3>
	            </div>
	            
	            <div class="body-main">
	                
	                <table class="table board-article">
	                    <thead>
	                        <tr>
	                            <td align="left">${dto.title}</td>
								<td align="right">
								<c:if test="${sessionScope.member.id != dto.id}">
									<span id="report" onclick="reportdialogshow();">신고</span>
								</c:if>
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
										<img src="${pageContext.request.contextPath}/uploads/bunsuck/${vo.s_fileName}" class="img-fluid img-thumbnail w-100 h-auto">
									</c:forEach>
	                            </td>
	                        </tr>
	                        <tr>
								<td colspan="2" class="text-center p-3">
									<button type="button" class="btn btn-outline-primary btnSendBoardLike" title="좋아요"><i class="bi ${isUserLike ? 'bi-hand-thumbs-up-fill':'bi-hand-thumbs-up'}"></i>&nbsp;&nbsp;<span id="likeCount">${dto.likeCount}</span></button>
								</td>
							</tr>
	                        <tr>
	                            <td colspan="2">
	                                이전글 :
	                                <c:if test="${not empty prevDto}">
	                                    <a href="${pageContext.request.contextPath}/admin/bunsuck/article?${query}&st_num=${prevDto.st_num}">${prevDto.title}</a>
	                                </c:if>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td colspan="2">
	                                다음글 :
	                                <c:if test="${not empty nextDto}">
	                                    <a href="${pageContext.request.contextPath}/admin/bunsuck/article?${query}&st_num=${nextDto.st_num}">${nextDto.title}</a>
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
	                            		<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/bunsuck/update?st_num=${dto.st_num}&page=${page}';">수정</button>
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
	                            <button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/admin/bunsuck/list?${query}';">리스트</button>
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
	
	<!-- 모달창 -->
	<div class="modal fade" id="reportModal" data-bs-keyboard="false" tabindex="-1" aria-labelledby="reportModalLable" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="myDialogModalLabel">신고</h5>
				</div>
				<div class="modal-body pt-1">
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		function login() {
			location.href = '${pageContext.request.contextPath}/admin/bunsuck/login';
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
				
				let st_num = '${dto.st_num}';
				let url = '${pageContext.request.contextPath}/admin/bunsuck/insertReply';
				let query = {st_num:st_num, content:content};
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
			let url = '${pageContext.request.contextPath}/admin/bunsuck/listReply';
			let query ='st_num=${dto.st_num}&pageNo=' + page;
			let selector = '#listReply';
			
			const fn = function(data) {
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
				
				let stc_num = $(this).attr('data-stc_num');
				let page = $(this).attr('data-pageNo');
				
				let url = '${pageContext.request.contextPath}/admin/bunsuck/deleteReply';
				let query = 'stc_num=' + stc_num;
				
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
				
				let url = '${pageContext.request.contextPath}/admin/bunsuck/insertShowLike';
				let query = 'st_num=${dto.st_num}&userLiked=' + userLiked;
				
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
		
		$(function(){
			const st_num = ${dto.st_num};
			// 카테고리 대화상자 객체
			const myModalEl = document.getElementById('reportModal');
			
			myModalEl.addEventListener('show.bs.modal', function(){
				// 모달 대화상자가 보일때
			});
		
			myModalEl.addEventListener('hidden.bs.modal', function(){
				// 모달 대화상자가 닫힐때
				location.href = '${pageContext.request.contextPath}/admin/bunsuck/article?${query}&st_num=' + st_num;
			});
		});
		
		function reportdialogshow() {
			let url = '${pageContext.request.contextPath}/admin/bunsuck/report';
			let num = ${dto.st_num};
			const fn = function(data) {
				$('#reportModal .modal-body').html(data);
				$('#reportModal').modal("show");
			};
			ajaxFun(url, 'get', {num: num}, 'text', fn);
		}
		
		function sendCancel() {
			$('#reportModal .modal-body').empty();
			$('#reportModal').modal("hide");
		}
		
		function sendOk(id, num) {
			const f = document.reportForm;
		
			if (!f.report.value) { // 신고사유가 선택되지 않았으면
				alert('신고 사유를 선택하세요.');
				return;
			}
			if (f.report.value === 'etc' && f.etc.value) {
				alert('기타 사유 선택 시, 사유를 작성하세요.');
				return;
			}
		
			let $reason = document.querySelector('input[name="report"]:checked');
			let reason = $reason.value;
			if (reason === 'etc') {
				reason = f.etc.value;
			}
		
		    let url = '${pageContext.request.contextPath}/admin/bunsuck/report';
		    const fn = function(data) {
		    	console.log(data);
		    	$('#reportModal .modal-body').empty();
		    	$('#reportModal').modal('hide');
		    	if (data.state === 'true') {
		    		alert('신고가 정상적으로 완료되었습니다!');
		    	} else if (data.msg != null) {
		    		alert(msg);
		    	}
		    };
		
		    ajaxFun(url, 'post', {id: id, num: num, reason: reason}, 'json', fn);
		}
	
	</script>

	<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>

</body>
</html>
