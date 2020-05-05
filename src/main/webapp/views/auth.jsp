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
            <h2>Вход в систему</h2>
            <form action="" method="POST">
                <% if (Boolean.TRUE.equals(request.getAttribute("errorLogin")) ) { %>
                     <p style="color: red;">Пользователь не найден<p>
                <% } %>
                <label for="login">Логин*</label>
                <input type="text" name="login" id="login" value="${login}" autofocus required >
                <br/>
                <button name="action" type="submit" value="login">Войти</button>
                <button name="action" type="submit" value="registration">Зарегистрироваться</button>
            </form>
		</div>
	</body>

</html>