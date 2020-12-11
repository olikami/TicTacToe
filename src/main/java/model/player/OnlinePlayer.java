package model.player;

import java.io.Serializable;

public class OnlinePlayer implements Serializable {

    private String IPAddress;
    private figuresName figure;
    private String Name;

    public OnlinePlayer(String IP, String N) {

        this.IPAddress=IP;
        this.Name=N;
    }



    public String getIPAddress() {
        return IPAddress;
    }


    public figuresName getFigure() {
        return figure;
    }

    public void setFigure(figuresName figure) {
        this.figure = figure;
    }

    public String getName() {
        return Name;
    }



}
