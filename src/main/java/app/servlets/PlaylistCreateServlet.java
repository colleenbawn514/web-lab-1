package app.servlets;

import app.common.Playlist;
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

public class PlaylistCreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/playlistCreate.jsp");
        requestDispatcher.forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        try {
            Playlist playlist = PlaylistLibrary.create(userId, name);
             resp.sendRedirect("/user/playlist?id="+playlist.getId());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
