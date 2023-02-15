<%@ page import="java.util.Date, java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	h1{ color: red;}
</style>
</head>
<body>
	<%
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일");
		String today = sdf.format(date);
	%>
	
	<h4>오늘은 <span style='color:pink;'><%= today %></span> 입니다</h4>




</body>
</html>