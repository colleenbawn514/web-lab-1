package app.common;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
@XmlType(name = "playlist")
public class ExtendPlaylist {
    public ArrayList<ExtendTrack> tracks = new ArrayList<>();
    public String name;
    public int id;
}
