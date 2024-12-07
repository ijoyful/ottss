<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<main id="main">
	    <div class="mainInner">
	        <div class="title">
	            <p>두더지 게임</p>
	            <p>획득 포인트 <span>100p</span></p>
	        </div>
	        <div class="gamePlay">
	            <!-- 게임 플레이 화면 넣으면 됨 -->
	        </div>
	        <div class="btnWrap">
	            <button>게임 그만하기</button>
	        </div>
	    </div> <!-- mainInner -->
	</main>
	<div class="background"></div>
	
	<!-- 게임이 정상적으로 종료 되었을 때 나오는 popup -->
	<div class="gameOver"> 
	    <div class="gameOverInner">
	        <div class="gameTitle">두더지 게임</div>
	        <table>
	            <tr>
	                <th>참여포인트</th>
	                <td>- 10p</td>
	            </tr>
	            <tr>
	                <th>획득포인트</th>
	                <td>10p</td>
	            </tr>
	            <tr>
	                <th>현재 보유 포인트</th>
	                <td>870p</td>
	            </tr>
	        </table>
	        <div class="okBtn"><button>확인</button></div>
	    </div>
	</div>
	
	<!-- 게임을 '게임 그만하기' 버튼을 눌렀을 때 나오는 Popup -->
	<div class="warning">
	    <div class="warningInner">
	        <div class="waringText">
	            <p>게임에 참여하신 포인트를 잃게 됩니다.</p>
	            <p>정말 종료하시겠습니까?</p>
	        </div>
	        <div class="okBtn"><button>확인</button></div>
	    </div>
	</div>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>