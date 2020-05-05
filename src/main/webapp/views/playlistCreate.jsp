<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Создать плейлист";%>
<%@include file="../templates/header.jsp" %>
<main class="center">
    <div class="card">
        <div class="card-header" style="background-image: url('/resources/images/playlist-create.png')">
            Создание плейлиста
        </div>
        <a href="/user">Вернуться назад</a>
        <form action="" method="POST">
            <label for="name">Название плейлиста*</label>
            <input type="text" name="name" id="name" required>
            <div class="card-action">
                <input type="submit" value="Создать" class="button">
            </div>
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>