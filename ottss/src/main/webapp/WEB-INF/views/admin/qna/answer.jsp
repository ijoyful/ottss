<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<form name="answerForm" method="post">
	<table class="table mt-4 write-form">
		<tr>
			<td class="bg-light col-sm-2">작성자</td>
			<td> 
				<p class="form-control-plaintext">${sessionScope.member.nickName}</p>
			</td>
		</tr>
	
		<tr> 
			<td class="bg-light col-sm-2">답&nbsp;변&nbsp;내&nbsp;용</td>
			<td valign="top"> 
				<textarea name="content" class="form-control"></textarea>
			</td>
		</tr>
	</table>

	<table class="table table-borderless">
		<tr>
			<td class="text-center">
				<button type="button" class="btn btn-dark" onclick="sendOk('${sessionScope.member.id}', '${num}');">등록하기</button>
				<button type="reset" class="btn btn-light">다시입력</button>
				<button type="button" class="btn btn-light" onclick="sendCancel();">등록취소</button>
			</td>
		</tr>
	</table>
</form>
