package app.servlets;

import app.common.*;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

public class UserExportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        try {
            User user = UserLibrary.get(userId);
            ExtendUser extendUser = new ExtendUser();
            extendUser.playlists = new ArrayList<>();
            extendUser.id=user.getId();
            extendUser.login=user.getLogin();
            extendUser.name=user.getName();

            for (int playlistId : user.getPlaylistIds()) {
                ExtendPlaylist extendPlaylist = new ExtendPlaylist();
                extendPlaylist.tracks = new ArrayList<>();
                Playlist playlist = PlaylistLibrary.get(user.getId(), playlistId);

                for (int trackId : (ArrayList<Integer>) playlist.getTrackIds() ) {
                    ExtendTrack extendTrack = new ExtendTrack();
                    Track track = MusicLibrary.get(trackId);
                    extendTrack.author = track.getAuthor();
                    extendTrack.name = track.getName();
                    extendTrack.duration = track.getDuration();
                    extendTrack.size = track.getSize();
                    extendTrack.id = track.getId();
                    extendPlaylist.tracks.add(extendTrack);
                }

                extendPlaylist.name = playlist.getName();
                extendPlaylist.id = playlist.getId();
                extendUser.playlists.add(extendPlaylist) ;
            }
            PrintWriter out = resp.getWriter();
            out.println(this.toXml(extendUser));
        } catch (UserNotFoundException | PlaylistNotFoundException | TrackNotFoundException | JAXBException e) {
            PrintWriter out = resp.getWriter();
            out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    private String toXml ( ExtendUser user) throws JAXBException {
        StringWriter writer = new StringWriter();

        //создание объекта Marshaller, который выполняет сериализацию
        JAXBContext context = JAXBContext.newInstance(ExtendUser.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // сама сериализация
        marshaller.marshal(user, writer);

        //преобразовываем в строку все записанное в StringWriter
        String result = writer.toString();
        System.out.println(result);
        return result;
    }

}
