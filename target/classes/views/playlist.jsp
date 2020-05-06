<%@ page import="java.util.Map" %>
<%@ page import="app.common.Playlist" %>
<%@ page import="app.common.Track" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%
    Playlist playlist = (Playlist) request.getAttribute("playlist");
    ArrayList<Track> tracks = (ArrayList<Track>) request.getAttribute("tracks");
%>
<%@include file="../templates/prepare.jsp" %>
<% title = playlist.getName();%>
<%@include file="../templates/header.jsp" %>
<main>
    <h5 class="subheader">Плейлист</h5>
    <div class="page-header">
        <h1 class="header"><%= playlist.getName()%></h1>
        <a class="reset-link button" href="/user/playlist/trackAdd?playlistId=<%=playlist.getId()%>">Добавьте трек</a>
    </div>
    <table>
        <thead>
            <tr>
                <td>Автор</td><td>Название</td><td>Длительность</td><td>Размер(Mb)</td>
            </tr>
        </thead>
        <tbody>
            <% if (tracks.size()==0) { %>
            <tr class="empty-row">
                <td colspan="4">
                    <span>Треков еще нет</span>
                    <br>
                    <a class="reset-link button"
                       href="/user/playlist/trackAdd?playlistId=<%=playlist.getId()%>"> добавьте первый трек</a>
                </td>
            </tr>
            <% } %>
            <%for (Track track : tracks ) { %>
                <tr>
                    <td><%=track.getAuthor()%></td>
                    <td><%=track.getName()%></td>
                    <td><%=track.getDuration()%></td>
                    <td><%=track.getSize()%></td>
                </tr>
            <% } %>
        </tbody>
    </table>
</main>
<%@include file="../templates/footer.jsp" %>