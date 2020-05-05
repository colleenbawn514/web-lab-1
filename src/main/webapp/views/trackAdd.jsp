<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Создание плейлиста</title>
</head>
<body>
<%@include file="../templates/header.jsp" %>
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
</body>
</html>