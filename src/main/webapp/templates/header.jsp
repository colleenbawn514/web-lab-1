<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="app.common.User"%>
<%
    boolean isAuth = (request.getSession().getAttribute("userId") != null);
    User user = (User)request.getSession().getAttribute("user");
%>
<header>
    <a href="/"><h1>Разочарование</h1></a>
    <% if (isAuth) { %>
        <a href="/user"><%=user.getName()%></a>
        <form action="/logout" method="POST">
            <button type="submit" name="action" value="logout">Выйти из аккаунта</button>
        </form>
    <% } else { %>
        <a href="/auth">Войти</a>|
        <a href="/registration">Зарегистрироваться</a>
    <% } %>
</header>