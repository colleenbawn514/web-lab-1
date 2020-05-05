<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Пароль";%>
<%@include file="../templates/header.jsp" %>
<main class="center">
    <h2>Введите пароль</h2>
    <div class="card">
        <a href="/auth">Вернуться назад</a>
        <form action="" method="POST">
            <% if (Boolean.TRUE.equals(request.getAttribute("errorPassword")) ) { %>
            <p style="color: red;">Неверный пароль<p>
                <% } %>
            <p>Логин: ${login}</p>
            <input type="hidden" name="login" value="${login}">
            <br/>
            <label for="password">Пароль*</label>
            <input type="password" name="password" id="password" autofocus required>
            <br/>
            <div class="card-action">
                <input type="submit" value="Войти" class="button">
            </div>
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>