<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/admin/layout/staticHeader.jsp"/>

</head>
<body>

<jsp:include page="/WEB-INF/views/admin/layout/header.jsp"/>
	
<main>
	<jsp:include page="/WEB-INF/views/admin/layout/left.jsp"/>
	<div class="wrapper">
		<div class="body-container">
		    <div class="body-main">
		    	<p>신고 된 내역</p>
		    	<p>report table 보면 된다.</p>
			</div>
		</div>		
	</div>
</main>

<jsp:include page="/WEB-INF/views/admin/layout/footer.jsp"/>

<jsp:include page="/WEB-INF/views/admin/layout/staticFooter.jsp"/>
</body>
</html>