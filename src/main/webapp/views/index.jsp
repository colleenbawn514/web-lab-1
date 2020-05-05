<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Авторизация</title>
</head>
<body>
<%@include file="../templates/header.jsp" %>
<div>
    <form action="" method="POST">
        <% if (Boolean.TRUE.equals(request.getAttribute("userIsLogin")) ) { %>
            <a href="/user">Кабинет</a>
        <% } else { %>
            <a href="/auth">Войти в систему</a>
        <% }  %>
    </form>
</div>
</body>

</html>