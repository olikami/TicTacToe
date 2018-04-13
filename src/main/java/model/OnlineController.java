package model;

import model.net.Client;
import model.net.Server;

import java.util.Random;

public class OnlineController {

    public static int MAX_PLAYERS = 2;
    public static int CURRENT_PLAYERS_IN_ROOM =1;
    public boolean HOST=true;
    Server server;
    Client client;
    Boolean WhoStarts = true;
    //public String[] PLAYER_IP = new int[];

    public OnlineController(Object SERVER_CLIENT, Boolean whostarts){

        //checking who is starting
        if(SERVER_CLIENT instanceof Server){

            Random r = new Random(System.currentTimeMillis());
            WhoStarts=(Boolean.valueOf(r.nextInt(1)==0?"false":"true"));

            server =((Server)SERVER_CLIENT);
            ((Server)SERVER_CLIENT).payload1 ="ready,"+WhoStarts;
        }else{
            WhoStarts = whostarts;
        }

    }
}
