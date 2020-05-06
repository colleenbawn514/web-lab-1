<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Добавление трека";%>
<%@include file="../templates/header.jsp" %>
<main class="center">
    <div class="card">
        <div class="card-header" style="background-image: url('/resources/images/track-add.png')">
            <span>Добавление трека</span>
        </div>
        <form action="" method="POST">
            <div class="card-body" >
                <a href="/user">Вернуться назад</a>
                <br>
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
            </div>
            <div class="card-action">
                <input type="submit" value="Создать" class="button">
            </div>
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>