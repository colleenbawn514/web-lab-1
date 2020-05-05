<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Авторизация";%>
<%@include file="../templates/header.jsp" %>
<main class="center">
    <div class="card">
        <div class="card-header" style="background-image: url('/resources/images/auth.png')">
            Вход в систему
        </div>
        <form action="" method="POST">
            <% if (Boolean.TRUE.equals(request.getAttribute("errorLogin")) ) { %>
            <p style="color: red;">Пользователь не найден<p>
                <% } %>
            <label for="login">Логин*</label>
            <input type="text" name="login" id="login" value="${login}" autofocus required >
            <br/>
            <div class="card-action">
                <button name="action" type="submit" value="login" class="button">Войти</button>
                <button name="action" type="submit" value="registration" class="button">Зарегистрироваться</button>
            </div>
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>