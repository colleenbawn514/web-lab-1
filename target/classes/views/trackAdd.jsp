<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Добавление трека";%>
<%@include file="../templates/header.jsp" %>
<main>
    <div>
        <a href="/user">Вернуться назад</a>
        <h2>Добавление трека</h2>
        <form action="" method="POST">
            <label for="author">Исполнитель*</label>
            <input type="text" name="author" id="author" value="${author}" required>
            <br>
            <label for="name">Название*</label>
            <input type="text" name="name" id="name" value="${name}" required>
            <br>
            <% if (Boolean.TRUE.equals(request.getAttribute("errorSizeType")) ) { %>
            <p style="color: red;">Размер должен быть числом<p>
                <% } %>
            <label for="size">Размер*</label>
            <input type="number" name="size" id="size" value="${size}" required>
            <br>
                <% if (Boolean.TRUE.equals(request.getAttribute("errorDurationType")) ) { %>
            <p style="color: red;">Длительность должна быть числом<p>
                <% } %>
            <label for="duration">Длительность*</label>
            <input type="number" name="duration" id="duration" value="${duration}" step="0.001" required>
            <input type="submit" value="Создать">
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>