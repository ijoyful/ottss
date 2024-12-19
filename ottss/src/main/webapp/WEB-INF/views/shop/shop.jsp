<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ottssCss/list.css" type="text/css">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-container {
	max-width: 600px;
	}
#main .mainInner {width: 800px;}
/* #main .listInner ul li:nth-child(1) {width: 10%;} /* 상품번호 */ 
#main .listInner ul li:nth-child(1) {width: 12%;} /* 카테고리 */
#main .listInner ul li:nth-child(2) {width: 22%;} /*  상품  */
#main .listInner ul li:nth-child(3) {width: 37%;} /* 상품 설명*/
#main .listInner ul li:nth-child(4) {width: 22%;} /* 가격 */
#main .listInner ul li:nth-child(5) {width: 4%;} /* 버튼 */
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
				<div style="text-align: center; margin-bottom: 20px; font-size: 25px; background: #1b1f3b; color: white;">
				POINT SHOP
				</div>
				<!-- div.listInner 테이블 처럼 쓸 수 있는 ul-li 입니당 foreach 돌리실때 ul로 돌리면 끗! -->
				<div class="listInner">
                    <ul class="listTitle">
                        <li class = "categories">카테고리</li>
                        <li class = "name">상품이름</li>
                        <li class = "explain">상품설명</li>
                        <li class = "amount">가격</li>
                        <li>&nbsp;</li>
                    </ul>
                    <c:forEach var="dto" items="${itemList}">
                    <ul class="listContent">
                        <li class="categories">${dto.categories}</li>
                        <li class="name">${dto.item_name}</li>
                        <li class = "explain">${dto.item_explain }</li>                       
                        <li class="amount">${dto.amount}</li>
	                    <li>
	                    	<button type="button" class = "btn btn-sm" style="color: white; background: #1b1f3b;" data-item-num = "${dto.item_num}">
	                    		Buy									
	                    	</button>
	                    </li>
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
<script type="text/javascript">
const currentUserId = '${sessionScope.member.id}';

function buyItem(itemNum) { // buyItem 함수 정의!
    console.log("buyItem 함수에 전달된 itemNum:", itemNum);
    console.log("사용할 userId:", currentUserId);

    if (!currentUserId) {
        console.error("userId가 유효하지 않습니다.");
        alert("로그인이 필요합니다.");
        return;
    }

    fetch('${pageContext.request.contextPath}/shop/buy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            id: currentUserId,
            itemNum: itemNum
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(`HTTP error ${response.status}: ${text}`) });
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert('구매가 완료되었습니다!');
        } else {
            alert(data.message || '구매 실패!');
        }
    })
    .catch(error => {
        console.error('오류 발생:', error);
        alert('오류 발생!');
    });
}

$(function() {
    $('.btn').click(function(event) {
        event.preventDefault();
        if (!confirm('구매하시겠습니까?')) {
            return false;
        }

        const itemNum = $(this).data('itemNum');
        console.log("클릭된 버튼의 itemNum:", itemNum);

        if (itemNum === undefined || itemNum === null || itemNum === "") {
            console.error("itemNum이 유효하지 않습니다:", itemNum);
            alert("상품 정보를 가져오는 중 오류가 발생했습니다.");
            return;
        }
        buyItem(itemNum); // buyItem 함수 호출
    });
});
</script>
</body>
</html>