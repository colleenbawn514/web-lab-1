<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Создание плейлиста</title>
    </head>
    <body>
        <%@include file="../templates/header.jsp" %>
        <div>
            <a href="/user">Вернуться назад</a>
            <h2>Создание плейлиста</h2>
            <form action="" method="POST">
                <label for="name">Название плейлиста*</label>
                <input type="text" name="name" id="name" required>
                <input type="submit" value="Создать">
            </form>
        </div>
    </body>
</html>