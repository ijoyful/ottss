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
	<jsp:include page="/WEB-INF/views/player/layout/left.jsp"/>
	<div class="mainInner">
		<div class="wrapper">
			<div class="body-container">
			    <div class="body-main">
			    	<p> 마이 페이지 입니다. </p>
				</div>
			</div>		
		</div>
	</div>
</main>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>


<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>