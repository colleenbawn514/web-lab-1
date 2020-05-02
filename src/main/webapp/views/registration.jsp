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
		    <h2>Регистрация</h2>
            <form action="" method="POST">
                <label for="login">Логин</label>
                <input type="text" name="login" id="label" value="${login}">
                <br/>
                <label for="password">Пароль</label>
                <input type="password" name="password" id="password">
                <br/>
                <label for="name">Имя</label>
                <input type="name" name="name" id="name">
                <br/>
                <input type="submit" value="Зарегистрироваться">
            </form>
		</div>
	</body>

</html>