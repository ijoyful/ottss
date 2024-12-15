<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="reply-info">
    <span class="reply-count">댓글</span>
    <span>[목록/페이지]</span>
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
	            <span class="deleteReply">삭제</span> |
	            <span class="notifyReply">신고</span>
	        </td>
	    </tr>
	    <tr>
	        <td colspan="2" valign="top">${vo.content}</td>
	    </tr>
	
	    <tr>
	        <td>
	            <button type="button" class="btn btn-light btnReplyAnswerLayout">답글 <span>답글 개수</span></button>
	        </td>
	        <td align="right">
	            <button type="button" class="btn btn-light btnSendReplyLike" title="좋아요">
	                <i class="bi bi-hand-thumbs-up" style="color: red"></i> <span>좋아요 개수</span>
	            </button>
	            <button type="button" class="btn btn-light btnSendReplyLike" title="싫어요">
	                <i class="bi bi-hand-thumbs-down" style="color: black"></i> <span>싫어요 개수</span>
	            </button>
	        </td>
	    </tr>
	
	    <tr class="reply-answer">
	        <td colspan="2">
	            <div class="border rounded">
	                <div id="listReplyAnswer" class="answer-list"></div>
	                <div>
	                    <textarea class="form-control m-2"></textarea>
	                </div>
	                <div class="text-end pe-2 pb-1">
	                    <button type="button" class="btn btn-light btnSendReplyAnswer">답글 등록</button>
	                </div>
	            </div>
	        </td>
	    </tr>
</c:forEach>
</table>

<div class="page-navigation">
    ${replyCount==0?"등록된 게시글이 없습니다." : paging}
</div>
