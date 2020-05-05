<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../templates/prepare.jsp" %>
<% title = "Регистрация";%>
<%@include file="../templates/header.jsp" %>
<main class="center" >
    <h2>Регистрация</h2>
    <div class="card">
        <form action="" method="POST">
            <% if (Boolean.TRUE.equals(request.getAttribute("errorUserAlreadyExist")) ) { %>
            <p style="color: red;">Логин занят<p>
                <% } %>
            <label for="login">Логин*</label>
            <input type="text" name="login" id="login" value="${login}"  required>
            <br/>
                <% if (Boolean.TRUE.equals(request.getAttribute("errorPasswordMismatch")) ) { %>
            <p style="color: red;">Пароли не совпадают<p>
                <% } %>
                <% if (Boolean.TRUE.equals(request.getAttribute("errorPasswordShort")) ) { %>
            <p style="color: red;">Пароль слишком короткий (минимум 5 символов)<p>
                <% } %>
            <label for="password">Пароль*</label>
            <input type="password" name="password" id="password" autofocus required>
            <br/>
            <label for="passwordCheck">Повторите пароль*</label>
            <input type="password" name="passwordCheck" id="passwordCheck"  required>
            <br/>
            <label for="name">Имя*</label>
            <input type="name" name="name" id="name" value="${name}" required>
            <br/>
            <div class="card-action">
                <input type="submit" value="Зарегистрироваться" class="button">
            </div>
        </form>
    </div>
</main>
<%@include file="../templates/footer.jsp" %>