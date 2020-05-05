package app.servlets;

import app.common.Playlist;
import app.common.Track;
import app.entities.MusicLibrary;
import app.entities.PlaylistLibrary;
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
import java.util.ArrayList;

public class PlaylistServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("playlistServlet -_-");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        System.out.println(req.getParameter("id"));
        Integer playlistId;
        try {
            playlistId = Integer.parseInt(req.getParameter("id"));

        } catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("нет айди");
            return;
        }
        Playlist playlist;
        try {
            playlist = PlaylistLibrary.get(userId, playlistId);
        } catch (PlaylistNotFoundException | UserNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("нет плейлиста");
            return;
        }
        ArrayList<Track> tracks = new ArrayList<>();
        for (int id : (ArrayList<Integer>) playlist.getTrackIds()) {
            try {
                tracks.add(MusicLibrary.get(id));
            } catch (TrackNotFoundException e) {
                e.printStackTrace();
            }
        }
        req.setAttribute("playlist", playlist);
        req.setAttribute("tracks", tracks);
        System.out.println("text");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/playlist.jsp");
        requestDispatcher.forward(req, resp);
    }
}
