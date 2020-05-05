package app.servlets;

import app.common.Playlist;
import app.common.Track;
import app.common.User;
import app.entities.MusicLibrary;
import app.entities.PlaylistLibrary;
import app.entities.UserLibrary;
import app.exception.PlaylistNotFoundException;
import app.exception.TrackNotFoundException;
import app.exception.UserNotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TrackAddServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/trackAdd.jsp");
            requestDispatcher.forward(req, resp);
        }
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            Integer playlistId = null;
            try {
                playlistId = Integer.parseInt(req.getParameter("playlistId") );
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String author = req.getParameter("author");
            String name = req.getParameter("name");
            Integer duration = null;
            Double size = null;
            boolean error = false;
            try {
                size = Double.parseDouble(req.getParameter("size"));
            } catch (NumberFormatException e) {
                req.setAttribute("errorSizeType", true);
                error = true;
            }
            try {
                duration = Integer.parseInt(req.getParameter("duration"));
            } catch (NumberFormatException e) {
                req.setAttribute("errorDurationType", true);
                error = true;
            }

            if (error) {
                System.out.println("errors");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/trackAdd.jsp");
                requestDispatcher.forward(req, resp);
                return;
            }

            try {
                Track track = MusicLibrary.create(author, name, size, duration);
                HttpSession session = req.getSession();
                Integer userId = (Integer) session.getAttribute("userId");
                PlaylistLibrary.addTrack(userId, playlistId, track);
                resp.sendRedirect("/user/playlist?id="+playlistId);
            } catch (TrackNotFoundException | PlaylistNotFoundException | UserNotFoundException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        //}
    }

}
