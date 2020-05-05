<%@ page import="java.util.Map" %>
<%@ page import="app.common.Playlist" %>
<%@ page import="app.common.Track" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    Playlist playlist = (Playlist) request.getAttribute("playlist");
    ArrayList<Track> tracks = (ArrayList<Track>) request.getAttribute("tracks");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Пользователь</title>
    </head>
    <body>
        <%@include file="../templates/header.jsp" %>
        <main>
            <h5>Плейлист</h5>
            <h1><%= playlist.getName()%></h1>
            <a href="/user/playlist/trackAdd?playlistId=<%=playlist.getId()%>">Добавьте трек</a>
            <table>
                <thead>
                    <tr>
                        <td>Автор</td><td>Название</td><td>Длительность</td><td>Размер(Mb)</td>
                    </tr>
                </thead>
                <tbody>
                    <% if (tracks.size()==0) { %>
                    <tr>
                        <td colspan="4">
                            <span>Треков еще нет</span>
                            <br>
                            <a href="/user/playlist/trackAdd?playlistId=<%=playlist.getId()%>">добавьте первый трек</a>
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
        <footer>Разочарование | 2020</footer>
    </body>
</html>