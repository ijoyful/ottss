<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<form name="reportForm" method="post">
	<table class="table mt-4 write-form">
		<tr> 
			<td class="bg-light col-sm-2">신고사유</td>
		</tr>
		<tr> 
			<td class="bg-light col-sm-2">
				<input type="radio" name="report" value="violent">폭력적 또는 혐오스러운 컨텐츠
			</td>
		</tr>
		<tr> 
			<td class="bg-light col-sm-2">
				<input type="radio" name="report" value="sexualabuse">성적인 컨텐츠
			</td>
		</tr>
		<tr> 
			<td class="bg-light col-sm-2">
				<input type="radio" name="report" value="abuse">증오 또는 악의적인 컨텐츠
			</td>
		</tr>
		<tr> 
			<td class="bg-light col-sm-2">
				<input type="radio" name="report" value="childabuse">아동 학대
			</td>
		</tr>
		<tr> 
			<td class="bg-light col-sm-2">
				<input type="radio" name="report" value="spam">스팸 또는 혼동을 야기하는 컨텐츠
			</td>
		</tr>
		<tr> 
			<td class="bg-light col-sm-2">
				<input type="radio" name="report" value="etc">기타 사유
			</td>
			<td class="bg-light col-sm-2">
				<input type="text" name="etc">
			</td>
		</tr>
	</table>
	
	<table class="table table-borderless">
		<tr>
			<td class="text-center">
				<button type="button" class="btn btn-dark" onclick="sendOk('${sessionScope.member.id}', '${num}');">신고하기</button>
				<button type="button" class="btn btn-light" onclick="sendCancel();">신고취소</button>
			</td>
		</tr>
	</table>
</form>
