<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Создать плейлист";%>
<%@include file="../templates/header.jsp" %>
<main>
    <div>
        <a href="/user">Вернуться назад</a>
        <h2>Создание плейлиста</h2>
        <form action="" method="POST">
            <label for="name">Название плейлиста*</label>
            <input type="text" name="name" id="name" required>
            <input type="submit" value="Создать">
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>