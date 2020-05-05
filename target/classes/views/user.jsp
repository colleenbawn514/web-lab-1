<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Мои плейлисты";%>
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
<%@include file="../templates/footer.jsp" %>