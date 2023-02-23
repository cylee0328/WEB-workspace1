<%@page import="com.kh.common.model.vo.PageInfo"%>
<%@page import="java.util.ArrayList, com.kh.board.model.vo.Board"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
   ArrayList<Board> list = (ArrayList<Board>)request.getAttribute("list");
%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.outer{
		min-height: 800px;
	}

	.list-area{
		width: 760px;
		margin: auto;
	}
</style>
</head>
<body>

	<%@ include file="../common/menubar.jsp" %>
	<div class="outer">
		<br> <h2 style="text-align: center;">사진게시판</h2> <br>
		
		<% if(loginUser != null) { %>
			<div align="right" style="width:860px">
				<a href="<%=contextPath%>/insert.th" class="btn btn-secondary">글작성</a>
				<br><br>
			</div>
		<% } %>
		<div class="list-area">
			<% for( Board b  : list  ){ %>
			<div class="thumbnail" align="center">
				
				<input type="hidden" value="<%=b.getBoardNo()  %>">
				<img src="<%=contextPath %><%= b.getTitleImg() %>" width="200px" height="150px">
				<p>
					NO.<%=b.getBoardNo()  %> <%=b.getBoardTitle()  %><br>
					조회수 : <%=b.getCount() %>
				</p>
			</div>
			<% } %>
			
		</div>
	</div>

	<script>
		$(function(){
			$(".thumbnail").click(function(){
				location.href= "<%= contextPath%>/detail.th?bno="+$(this).children().eq(0).val();				
			})
		});
	</script>

	


</body>
</html>