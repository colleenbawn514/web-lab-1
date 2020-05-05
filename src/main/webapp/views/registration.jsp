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
		    <a href="/auth">Вернуться назад</a>
		    <h2>Регистрация</h2>
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
                <input type="submit" value="Зарегистрироваться">
            </form>
		</div>
	</body>

</html>