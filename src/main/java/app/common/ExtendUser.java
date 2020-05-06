package app.common;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;

@XmlType(name = "user")
@XmlRootElement

public class ExtendUser {
    public int id;
    public String login;
    public String name;
    public ArrayList<ExtendPlaylist> playlists;

}
