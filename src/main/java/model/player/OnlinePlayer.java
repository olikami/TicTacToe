package model.player;

import sun.security.x509.IPAddressName;

public class OnlinePlayer {

    private String IPAddress;
    private figures_name figure;
    private String Name;

    public OnlinePlayer(String IP, String N){

        this.IPAddress=IP;
        this.Name=N;
    }



    public String getIPAddress() {
        return IPAddress;
    }


    public figures_name getFigure() {
        return figure;
    }

    public void setFigure(figures_name figure) {
        this.figure = figure;
    }

    public String getName() {
        return Name;
    }



}
