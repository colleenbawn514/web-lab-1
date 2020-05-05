<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Мои плейлисты";%>
<%@include file="../templates/header.jsp" %>
<main>
	<h1 class="header">Добро пожаловать, ${name}</h1>
	<%

		if ( true || request.getParameter("firstVisit") != null) {
	%>
		<div class="banner" >С успешной регистрацией!</div>
	<% } %>
	<div class="playlists-container">
		<a href="/user/playlist/create" class="reset-link">
			<div class="playlist-card create-playlist">+</div>
		</a>
		<%
			Map<Integer, String> playlists = (Map<Integer, String>) request.getAttribute("playlists");
			System.out.println(playlists);
			for (int id : playlists.keySet() ) {
		%>
			<a href="/user/playlist?id=<%=id%>" class="reset-link">
				<div class="playlist-card">
					<%=playlists.get(id)%>
				</div>
			</a>
		<% } %>
	</div>
</main>
<%@include file="../templates/footer.jsp" %>