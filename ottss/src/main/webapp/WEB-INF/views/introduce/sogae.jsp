<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<style type="text/css">

    .sogaeInner {display: flex; justify-content: space-between;}

    .sogaeInner .imgArea {width: 35%;}
    .sogaeInner .imgArea img {width: 100%;}

    .sogaeInner .introduce {width: 60%; line-height: 1.6;}
    .sogaeInner h3 {font-size: 30px; font-weight: bold; margin-bottom: 20px;}

</style>
<body>
	<div class="wrap">
		<!-- header -->
		<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
		<!-- header End -->
		
		<!-- main -->
		<main id="main">
            <div class="mainInner">
                <div class="sogaeInner">
                    <div class="imgArea">
                        <img src="${pageContext.request.contextPath}/resources/images/ottssImg/samsik.jpg" alt="">
                    </div>
                    <div class="introduce">
                        <h3>놀이터 소개</h3>
                        <p>
                            안녕하십니까 유저 여러분 <br>
                            삼식이네 놀이터 운영을 맡은 <span style="color: fuchsia;">삼식이 집사 [박재민]</span>입니다.<br>
                            <br>
                            본 놀이터는 현금을 사용한 불법 게임 사이트에 <br>
                            지친 여러분들의 영혼의 힐링과 안식을 위해 만들어진 사이트로, <br>
                            그 어떠한 확률 조작과 먹튀 걱정이 없는 단어 그대로 '놀이터'를 지향하고 있습니다. <br>
                            자유게시판 및 분석 / 자랑 게시판을 마음껏 활용하시며 즐겁게 놀아주시면 되시겠습니다. <br>
                            <br>
                            게임에 대한 설명이나 매뉴얼은 따로 제공해드리고 있지 않습니다. <br>
                            우리가 놀이터에서 미끄럼틀 타는 법, 그네 타는 법을 따로 배우지 않듯, <br>
                            스스로 체득하는 놀이터를 지향하고 있습니다. <br>
                            혹시라도 궁금하신 부분이 있으시면 QnA 게시판에 질문하시어 <br>
                            여러분의 집단지성을 시험해보시길 바라겠습니다.<br>
                            <br>
                            단 이 놀이터는 관리자들의 놀이터들도 동시에 맡고 있으므로, <br>
                            사이트 및 게임에 대한 그 어떠한 문의 사항도 '일절' 받고 있지 않습니다. <br>
                            궁금한 사항은 QnA 게시판을 이용해주시길 바라며, <br>
                            문의 사항에 해결 시간은 관리자들도 확실히 답변드리지 못하는 점 양해해 주시길 바랍니다. <br>
                            <br>
                            모쪼록 즐겁게 노는 놀이터가 될 수 있도록 최선을 할지 말지 집가서 고민 좀 해보겠습니다.
                        </p>
                    </div>
                </div>
            </div> <!-- mainInner -->
        </main>
		<!-- main End -->
		
		<!-- footer -->
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
		<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
		<!-- footer End -->
	</div>
</body>
</html>