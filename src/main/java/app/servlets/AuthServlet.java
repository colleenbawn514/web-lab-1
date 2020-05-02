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
import java.io.IOException;
import java.io.PrintWriter;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/auth.jsp");
        requestDispatcher.forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String action = req.getParameter("action");

        if ("registration".equals(action)) {
            resp.sendRedirect("/registration");
            return;
        }

        boolean isExistUser = false;
        isExistUser = UserLibrary.isExistUser(login);
        System.out.println(password);
        if (isExistUser) {
            if (password == null) {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/password.jsp");
                req.setAttribute("login", login);
                requestDispatcher.forward(req, resp);
            } else {
                User user = null;
                try {
                    user = UserLibrary.get(login, password);
                    resp.sendRedirect("/user");
                }catch (UserNotFoundException | AuthenticationException e){
                    RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/password.jsp");
                    req.setAttribute("login", login);
                    req.setAttribute("errorPassword", true);
                    requestDispatcher.forward(req, resp);
                }
            }


        } else {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/auth.jsp");
            req.setAttribute("login", login);
            req.setAttribute("errorLogin", true);
            requestDispatcher.forward(req, resp);
        }
    }
}
