<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Пользователь</title>
	</head>
	<body>
		<%@include file="../templates/header.jsp" %>
		<main>
			<h1>Добро пожаловать, ${name}</h1>
			<a href="/user/playlist/create">Создать плейлист</a>
			<%
				if ( request.getParameter("firstVisit") != null) {
			%>
				<h4 style="color: #00F;">С успешной регистрацией!</h4>
			<% } %>
			<%
				Map<Integer, String> playlists = (Map<Integer, String>) request.getAttribute("playlists");
				System.out.println(playlists);
				for (int id : playlists.keySet() ) {
			%>
				<a href="/user/playlist?id=<%=id%>"><%=playlists.get(id)%></a>
			<% } %>
		</main>
		<footer>Разочарование | 2020</footer>
	</body>
</html>