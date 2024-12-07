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
	        <div class="gameList">
	            <ul>
	                <li>
	                    <a href="gamePlay.html">
	                        <div class="imgArea" style="background-image: url('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	                <li>
	                    <a href="#">
	                        <div class="imgArea" style="background-image: url('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	                <li>
	                    <a href="#">
	                        <div class="imgArea" style="background-image: url('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	                <li>
	                    <a href="#">
	                        <div class="imgArea" style="background-image: urlurl('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	                <li>
	                    <a href="#">
	                        <div class="imgArea" style="background-image: url('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	                <li>
	                    <a href="#">
	                        <div class="imgArea" style="background-image: url('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	                <li>
	                    <a href="#">
	                        <div class="imgArea" style="background-image: url('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	                <li>
	                    <a href="#">
	                        <div class="imgArea" style="background-image: url('${pageContext.request.contextPath}/resources/images/ottssImg/cat.jpg')"></div>
	                        <div class="gameInfo">
	                            <div class="gameTitle">게임1</div>
	                            <div class="gameContent">게임 설명입니다.</div>
	                        </div>
	                    </a>
	                </li>
	            </ul>
	        </div>
	    </div> <!-- mainInner -->
	</main>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>