package app.common;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "track")
public class ExtendTrack {
    public String name;
    public int id;
    public double size;
    public int duration;
    public String author;

}
