<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Авторизация</title>
	</head>
	<body>
		<div>
		    <a href="/auth">Вернуться назад</a>
		    <h2>Введите пароль</h2>
            <form action="" method="POST">
                <% if (new Boolean(true).equals(request.getAttribute("errorPassword")) ) { %>
                    <p style="color: red;">Неверный пароль<p>
                <% } %>
                <p>Логин: ${login}</p>
                <input type="hidden" name="login" value="${login}">
                <br/>
                <label for="password">Пароль</label>
                <input type="password" name="password" id="password">
                <br/>
                <input type="submit" value="Войти">
            </form>
		</div>
	</body>

</html>