package app.client;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientConnection {
    //public TrackManagerRemote track;


    public ClientConnection() {
        try {
            Registry registry = LocateRegistry.getRegistry(null, 80);
            //this.track = (TrackManagerRemote) registry.lookup("tracksLibrary");


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    //public boolean isConnected() {
        //return  this.track != null ;
    //}
}
