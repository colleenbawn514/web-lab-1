package app.servlets;

import app.common.User;
import app.entities.UserLibrary;
import app.exception.UserNotFoundException;

import javax.naming.AuthenticationException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("regLogin");
        req.setAttribute("login", login);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/registration.jsp");
        requestDispatcher.forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String passwordCheck = req.getParameter("passwordCheck");
        String name = req.getParameter("name");
        boolean error = false;
        if ( UserLibrary.isExistUser(login)) {
            req.setAttribute("errorUserAlreadyExist", true);
            error = true;
        }
        System.out.println(password.equals(passwordCheck));
        System.out.println(password);
        System.out.println(passwordCheck);
        if ( !password.equals(passwordCheck)) {
            req.setAttribute("errorPasswordMismatch", true);
            error = true;
        }
        if ( password.length()<5) {
            req.setAttribute("errorPasswordShort", true);
            error = true;
        }

        if (error) {
            req.setAttribute("login", login);
            req.setAttribute("name", name);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/registration.jsp");
            requestDispatcher.forward(req, resp);
            return;
        }
        User user = UserLibrary.create(login, password, name);
        HttpSession session = req.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("user", user);
        resp.sendRedirect("/user?firstVisit");
    }

}
