<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시판</title>

    <!-- Static Header Include -->
    <jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />

    <style type="text/css">
        .body-container {
            max-width: 800px;
        }
    </style>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

</head>
<body>

<header>
    <!-- Static Header Include -->
    <jsp:include page="/WEB-INF/views/layout/header.jsp" />
</header>

<main>
    <div class="container">
        <div class="body-container">    
            <div class="body-title">
                <h3><i class="bi bi-app"></i> 자랑게시판 </h3>
            </div>
            
            <div class="body-main">
                
                <table class="table board-article">
                    <thead>
                        <tr>
                            <td colspan="2" align="center">
                                <!-- 제목 부분, 동적 값 없음 -->
                            </td>
                        </tr>
                    </thead>
                    
                    <tbody>
                        <tr>
                            <td width="50%">
                                이름 : <!-- 사용자 이름 부분 -->
                            </td>
                            <td align="right">
                                <!-- 날짜 및 조회수 부분 -->
                            </td>
                        </tr>
                        
                        <tr>
                            <td colspan="2" valign="top" height="200">
                                <!-- 게시글 내용 부분 -->
                            </td>
                        </tr>
                        
                        <tr>
                            <td colspan="2">
                                이전글 :
                                <c:if test="${not empty prevDto}">
                                    <a href="#">이전 글 제목</a>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                다음글 :
                                <c:if test="${not empty nextDto}">
                                    <a href="#">다음 글 제목</a>
                                </c:if>
                            </td>
                        </tr>
                    </tbody>
                </table>
                
                <table class="table table-borderless">
                    <tr>
                        <td width="50%">
                            <!-- 수정, 삭제 버튼 부분 -->
                            <button type="button" class="btn btn-light">수정</button>
                            <button type="button" class="btn btn-light">삭제</button>
                        </td>
                        <td class="text-end">
                            <button type="button" class="btn btn-light">리스트</button>
                        </td>
                    </tr>
                </table>
                
                <div class="reply">
                    <form name="replyForm" method="post">
                        <div class='form-header'>
                            <span class="bold">댓글</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
                        </div>
                        
                        <table class="table table-borderless reply-form">
                            <tr>
                                <td>
                                    <textarea class="form-control" name="content"></textarea>
                                </td>
                            </tr>
                            <tr>
                               <td align="right">
                                    <button type="button" class="btn btn-light btnSendReply">댓글 등록</button>
                                </td>
                             </tr>
                        </table>
                    </form>
                    
                    <div id="listReply"></div>
                </div>
                
            </div>
        </div>
    </div>
</main>

<footer>
    <!-- Static Footer Include -->
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>
