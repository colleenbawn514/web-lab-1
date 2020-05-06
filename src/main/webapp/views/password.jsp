<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Пароль";%>
<%@include file="../templates/header.jsp" %>
<main class="center">
    <div class="card">
        <div class="card-header" style="background-image: url('/resources/images/auth.png')">
            <span>Введите пароль</span>
        </div>
        <form  action="" method="POST">
            <div class="card-body">
                <a href="/auth">Вернуться назад</a>
                <% if (Boolean.TRUE.equals(request.getAttribute("errorPassword")) ) { %>
                <p style="color: red;">Неверный пароль<p>
                    <% } %>
                <p>Логин: ${login}</p>
                <input type="hidden" name="login" value="${login}">
                <label for="password">Пароль*</label>
                <input type="password" name="password" id="password" autofocus required>
            </div>
            <div class="card-action">
                <input type="submit" value="Войти" class="button">
            </div>
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>