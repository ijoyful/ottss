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
</style>

</head>
<body>

	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
		
	<main id="main">
        <div class="mainInner">
            <div class="title_bar">
                <p>GAME'S</p>
                <div class="slide_arrow">
                    <span class="arrow prev"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-chevron-left"><polyline points="15 18 9 12 15 6"></polyline></svg></span>
                    <span class="arrow next"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-chevron-right"><polyline points="9 18 15 12 9 6"></polyline></svg></span>
                </div>
            </div>
            <div class="gameList">
                <div class="latest_wrap">
                    <div class="swiper-container latest_slide">
                        <ul class="swiper-wrapper">
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                            <li class="swiper-slide" style="background-image: url('img/game1.jpg')"><a href="#"><span>게임 1</span></a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="tableListWrap">
                <div class="tableList1">
                    <table>
                        <thead>
                            <tr>
                                <th>제목</th>
                                <th>날짜</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="tableList2">
                    <table>
                        <thead>
                            <tr>
                                <th>제목</th>
                                <th>날짜</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="tableList2">
                    <table>
                        <thead>
                            <tr>
                                <th>제목</th>
                                <th>날짜</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="tableList2">
                    <table>
                        <thead>
                            <tr>
                                <th>제목</th>
                                <th>날짜</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                            <tr>
                                <td>[공지] 삼식이네 놀이터 주의 사항</td>
                                <td>2024-12-02</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div> <!-- tableListWrap -->
        </div> <!-- mainInner -->
    </main>
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	
	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>