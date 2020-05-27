package app.servlets;

import app.common.User;
import app.entities.PlaylistLibrary;
import app.entities.UserLibrary;
import app.exception.UserNotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("userServlet on-_-");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        User user;
        try {
             user = UserLibrary.get(userId);
        } catch (UserNotFoundException e) {
            resp.sendRedirect("/auth");
            return;
        }
        Map<Integer, String> playlistsInfo = null;
        try {
            playlistsInfo = PlaylistLibrary.getAll(user.getId());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        req.setAttribute("playlists", playlistsInfo);

        req.setAttribute("name", user.getName());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/user.jsp");
        requestDispatcher.forward(req, resp);
    }
}