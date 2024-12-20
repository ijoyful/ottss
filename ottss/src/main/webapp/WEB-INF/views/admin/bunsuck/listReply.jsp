<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="reply-info">
    <span class="reply-count">댓글  ${replyCount}개</span>
    <span>[목록, ${pageNo}/${total_page} 페이지]</span>
</div>

<table class="table table-borderless">
	<c:forEach var="vo" items="${listReply}">
	    <tr class="border table-light">
	        <td width="50%">
	            <div class="row reply-writer">
	                <div class="col-auto align-self-center">
	                    <div class="name">${vo.nickname}</div>
	                </div>
	            </div>
	        </td>
	        <td width="50%" align="right" class="align-middle">
	            <span>${vo.reg_date}</span> | 
	            <span class="deleteReply" data-stc_num="${vo.stc_num}" data-pageNo="${pageNo}">삭제</span> 
	        </td>
	    </tr>
	    <tr>
	        <td colspan="2" valign="top">${vo.content}</td>
	    </tr>
	</c:forEach>
</table>

<div class="page-navigation">
    ${replyCount==0?"등록된 게시글이 없습니다." : paging}
</div>
