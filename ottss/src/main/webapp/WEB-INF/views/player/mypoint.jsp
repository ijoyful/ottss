<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>

<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<main>
<jsp:include page="/WEB-INF/views/player/layout/left.jsp"/>
</main>


<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>