<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-container {
	max-width: 800px;
	}
#main .listInner ul li:nth-child(1) {width: 10%;} /* 상품번호 */
#main .listInner ul li:nth-child(2) {width: 10%;} /* 카테고리 */
#main .listInner ul li:nth-child(3) {width: 30%;} /*  상품  */
#main .listInner ul li:nth-child(4) {width: 60%;} /* 상품 설명*/
#main .listInner ul li:nth-child(5) {width: 20%;} /* 가격 */
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
				<!-- div.listInner 테이블 처럼 쓸 수 있는 ul-li 입니당 foreach 돌리실때 ul로 돌리면 끗! -->
				<div class="listInner">
                    <ul class="listTitle">
                        <li class = "num">No.</li>
                        <li class = "categories">카테고리</li>
                        <li class = "name">상품이름</li>
                        <li class = "explain">상품설명</li>
                        <li class = "amount">가격</li>
                    </ul>
                    <c:forEach var="dto" items="${itemList}">
                    <ul class="listContent">
                        <li class="num">${dto.item_num}</li>
                        <li class="categories">${dto.categories}</li>
                        <li class="name">${dto.item_name}</li>
                        <li class = "explain">${dto.item_explain }</li>                       
                        <li class="amount">${dto.amount}</li>
                    </ul>
                    </c:forEach>
                </div>
			</div>
			
		</main>
		<!-- main End -->
		
		<!-- footer -->
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
		<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
		<!-- footer End -->
	</div>
</body>
</html>