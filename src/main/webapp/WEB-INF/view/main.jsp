<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<html>
<head>
<title>메인화면</title>
<link href="<%=request.getContextPath() %>/css/main.css" rel="stylesheet" type="text/css"> 

</head>
<body>

	<div class="banner">
		<div class="container b-container1">
			<div class="">
				<div class="b-img">
					<img src="<%=request.getContextPath()%>/img/10088.png" alt="img" />
					<!-- 이미지 출처: https://www.freepik.com/free-vector/happy-women-learning-language-online-isolated-flat-vector-illustration-cartoon-female-characters-taking-individual-lessons-through-messenger-education-digital-technology-concept_10613101.htm#query=illustrations&position=3&from_view=keyword -->
				</div>
				<div class="b-text">
					<h1 class="banner-p">스터디 홈즈로 오세요!</h1>
					<br>
					<p>
						스터디 할 사람 모두 모여라~ <br> 스터디 홈즈로 스터디를 검색해보세요!<br><br> 스터디 진행자의 평판을 확인하고 <br> 나와 맞는 사람과 공부하세요
					</p>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="container py-5">
			<form class="row-vh mx-auto text-center position-relative" style="width: 100%"  
			action = "<%=request.getContextPath()%>/studymenu/offAllSearch" method="post">
				<div class=" d-flex flex-row search-div">
				<input type = "hidden" name = "part" value = "title"/>
					<input id="bar" class="form-control" type="search" placeholder="스터디 검색.." aria-label="Search" name="searchData"  />
					<button class="custom-btn">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>

				</div>
			</form>
		</div>
		<div class="container">
			<h2>최신 스터디</h2>
			<hr align="left" width="200px" style="border: 0.5px solid #c47100" />
			<div class="d-flex justify-content-between " >
			<c:forEach items="${slist}" var="i">
			    <a class="s-content" href=
			    <c:if test="${i.menuid >= 2 && i.menuid <= 7}"> 
			         "<%=request.getContextPath()%>/studymenu/offStudyMenuInfo?board_num=${i.board_num}"
			    </c:if>
			    <c:if test="${i.menuid >= 9 && i.menuid <= 14}"> 
                     "<%=request.getContextPath()%>/studymenu/onStudyMenuInfo?board_num=${i.board_num}"
                </c:if>
                <c:if test="${i.menuid >= 16 && i.menuid <= 21}"> 
                     "<%=request.getContextPath()%>/studymenu/onoffStudyMenuInfo?board_num=${i.board_num}"
                </c:if>
			   >
				<div class="container">
				             <c:if test="${i.menuid == 2 ||i.menuid == 9||i.menuid == 16}">
                                <img src="<%=request.getContextPath()%>/img/studymenu/programming.jpg" width="100%" height="200px">
                             </c:if>
                              <c:if test="${i.menuid == 3 ||i.menuid == 10||i.menuid == 17}">
                                <img src="<%=request.getContextPath()%>/img/studymenu/security.jpg" width="100%" height="200px">
                             </c:if>
                              <c:if test="${i.menuid == 4 ||i.menuid == 11||i.menuid == 18}">
                                <img src="<%=request.getContextPath()%>/img/studymenu/creative.jpg" width="100%" height="200px">
                             </c:if>
                              <c:if test="${i.menuid == 5 ||i.menuid == 12||i.menuid == 19}">
                                <img src="<%=request.getContextPath()%>/img/studymenu/marketing.jpg" width="100%" height="200px">
                             </c:if>
                              <c:if test="${i.menuid == 6 ||i.menuid == 13||i.menuid == 20}">
                                <img src="<%=request.getContextPath()%>/img/studymenu/language.jpg" width="100%" height="200px">
                             </c:if>
                              <c:if test="${i.menuid == 7 ||i.menuid == 14||i.menuid == 21}">
                                <img src="<%=request.getContextPath()%>/img/studymenu/etc.jpg"  width="100%" height="200px">
                             </c:if>
					
					<h4 class="pt-3">${i.title}</h4>
					
					<p class="my-0"><i class="fa-solid fa-user-group"></i> ${i.pernum} / ${i.count}</p>
					<p class="my-0">지역: ${i.region}</p>
					<p class="my-0">작성자: ${i.nickname}</p>
					<div class="px-0">
					&#11088;
					<c:if test="${i.point > 20}">&#11088;</c:if> 
					<c:if test="${i.point > 30}">&#11088;</c:if>
					<c:if test="${i.point > 40}">&#11088;</c:if>
					<c:if test="${i.point > 50}">&#11088;</c:if>
					</div>
				</div>
				</a>
			</c:forEach>
 
			</div>
			<br> <br>
			<div class="container pt-5">
				<div class="d-flex justify-content-between">
					<div class="col-sm" width="20%">
						<h3 style='cursor:pointer;' onclick="location.href='<%=request.getContextPath()%>/community/comBoardList?boardid=4'">공지사항</h3>
						<hr align="left" width="150px" style="border: 0.5px solid #c47100" />
						<table class="table table-sm table-borderless shadow-sm rounded boardtable">
				    		<tr class="border-bottom">
							   <th width="80%">제목</td>
	                           <th width="20%">날짜</td>
							</tr>
							<c:forEach var="i" items="${list1}">
								<tr style='cursor:pointer;' onclick="location.href='<%=request.getContextPath()%>/community/comBoardInfo?board_num=${i.board_num}'">
									<td>${i.title }</td>
									<td> <fmt:formatDate value="${i.regdate }" pattern="MM/dd"/> </td>
								</tr>
							</c:forEach>

						</table>
						<div></div>
					</div>
					<div class="col-sm" width="20%">
						<h3 style='cursor:pointer;' onclick="location.href='<%=request.getContextPath()%>/community/comBoardList?boardid=1'">질문 & 답글</h3>
						<hr align="left" width="180px" style="border: 0.5px solid #c47100" />
						<table class="table table-sm table-borderless  shadow-sm rounded boardtable">
						    <tr class="border-bottom">
                               <th width="80%">제목</td>
                               <th width="20%">날짜</td>
                            </tr>
                            <c:forEach var="i" items="${list2}">
                                <tr style='cursor:pointer;' onclick="location.href='<%=request.getContextPath()%>/community/comBoardInfo?board_num=${i.board_num}'">
                                    <td>${i.title }</td>
                                    <td> <fmt:formatDate value="${i.regdate }" pattern="MM/dd"/> </td>
                                </tr>
                            </c:forEach>
						</table>
						<div></div>
					</div>
					<div class="col-sm" width="20%">
						<h3 style='cursor:pointer;' onclick="location.href='<%=request.getContextPath()%>/community/comBoardList?boardid=2'" >자유주제</h3>
						<hr align="left" width="150px" style="border: 0.5px solid #c47100" />
						<table class="table table-sm table-borderless  shadow-sm boardtable">
						    <tr class="border-bottom">
                               <th width="80%">제목</td>
                               <th width="20%">날짜</td>
                            </tr>
                            <c:forEach var="i" items="${list3}">
                                <tr style='cursor:pointer;' onclick="location.href='<%=request.getContextPath()%>/community/comBoardInfo?board_num=${i.board_num}'">
                                    <td>${i.title }</td>
                                    <td> <fmt:formatDate value="${i.regdate }" pattern="MM/dd"/> </td>
                                </tr>
                            </c:forEach>
						</table>
						<div></div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<br />
	<br />
	<br />
	<br /> 

<script>



</script>

</body>
</html>
