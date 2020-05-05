<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="app.common.User"%>
<%
    boolean isAuth = (request.getSession().getAttribute("userId") != null);
    User user = (User)request.getSession().getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=title%></title>
    <link href="https://fonts.googleapis.com/css2?family=Jost:wght@300;400;500;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>
<header>
    <div class="wrapper">
        <a href="/" class="logo"><h1>Разочарование</h1></a>
        <% if (isAuth) { %>
            <a href="/user" class="auth-button"><%=user.getName()%></a>
            <span class="divider">|</span>
            <form action="/logout" method="POST">
                <button type="submit" name="action" value="logout" class="auth-button">Выйти из аккаунта</button>
            </form>
        <% } else { %>
            <a href="/auth" class="auth-button">Войти</a>
            <span class="divider">|</span>
            <a href="/registration" class="auth-button">Зарегистрироваться</a>
        <% } %>
    </div>
</header>