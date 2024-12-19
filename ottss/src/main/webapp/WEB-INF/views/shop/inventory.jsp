<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/list.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-container {
	max-width: 600px;
	}
#main .mainInner {width: 800px;}
/* #main .listInner ul li:nth-child(1) {width: 10%;} /* 상품번호 */ 
#main .listInner ul li:nth-child(1) {width: 10%;} /* 카테고리 */
#main .listInner ul li:nth-child(2) {width: 22%;} /*  상품  */
#main .listInner ul li:nth-child(3) {width: 58%;} /* 상품 설명 */
#main .listInner ul li:nth-child(4) {width: 10%;} /* 장착버튼 */
.btn {
font-size:16px;
padding : 0;

}
</style>

</head>
<body>

	<!-- div.wrap 꼭 써야됩니다. 없으면 폼 망가져요~~ --> 
	<div class="wrap">
		<!-- header -->
		<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
		<!-- header End -->
		
		<!-- main -->
		<main id="main">
			<!-- div.mainInner까지 있어야 폼 안망가집니다. 안에다가 코딩 해주세용 -->
			<div class="mainInner">
				<div id ="nickname" style="text-align: center; margin-bottom: 5px; font-size: 20px">${dto.nickname}</div>
				<!-- div.listInner 테이블 처럼 쓸 수 있는 ul-li 입니당 foreach 돌리실때 ul로 돌리면 끗! -->
				<div class="listInner">
                    <ul class="listTitle">
                        <li class = "categories">카테고리</li>
                        <li class = "name">상품이름</li>
                        <li class = "explain">상품설명</li>
                    </ul>
                    <c:choose> <%-- inventory가 비어있는 경우 메시지 출력 --%>
                        <c:when test="${empty inventory}">
                            <p>인벤토리가 비어 있습니다.</p>
                        </c:when>
                        <c:otherwise>
                    		<c:forEach var="dto" items="${inventory}">
			                   	<ul class="listContent">
			                       	<li class="categories">${dto.categories}</li>
			                       	<li class="name">${dto.item_name}</li>
			                       	<li class = "explain">${dto.item_explain}</li>  
			                       	<li>
	                    				<button type="button" class = "btn btn-sm btn-equip" style="color: white; background: #1b1f3b;" >
	                    					장착									
	                    				</button>
	                    			</li>                     
			                   	</ul>
                    		</c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
			</div>
			
		</main>
		<!-- main End -->
		
		<!-- footer -->
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
		<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
		<!-- footer End -->
	</div>
<script type="text/javascript">

$(document).ready(function() {
const equipment = {
	title : "강한",
	icon : "★",
	color : "red",
	nickname : "nickname"
};

$(".btn-equip").on("click",function() {
	if(!confirm('장착하시겠습니까?')){
		return false;
	}
	const $nickname = $("#nickname");
	
	$nickname.css("color", equipment.color);
	$nickname.html(`${equipment.icon} ${equipment.title} ${equipment.nickname}`);
	
	console.log(equipment.icon);
	console.log(equipment.title);
	console.log(equipment.nickname);
	
	});
});
</script>
</body>
</html>