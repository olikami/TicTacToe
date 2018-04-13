package model.net;


import model.OnlineController;
import model.player.OnlinePlayer;
import model.player.figures_name;
import model.player.userdata;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {


    public final ServerSocket server;
    private Boolean stop = false;
    String s = "null";
    Socket socket = null;
    public String payload1 ="";
    public String payload2 ="";
    String end = ",end";

    public ArrayList<OnlinePlayer> players= new ArrayList<>();




    public Server(int port, int backlog, String bindAddr) throws IOException {
        //server = new ServerSocket(port, backlog, InetAddress.getByName(bindAddr));

        server = new ServerSocket(0);
        System.out.println("listening on port: " + server.getLocalPort());

        OnlinePlayer p = new OnlinePlayer(bindAddr,userdata.getUsername());
        p.setFigure(userdata.get_selected_figure());
        players.add(p);


    }


    public void verbinde() {

        while (!stop) {
            System.out.println("ok");
            try {

                socket = server.accept();
                reinRaus(socket);
            } catch (IOException e) {
                if(e instanceof SocketException)
                System.out.println("Socket is closed");
                else
                    e.printStackTrace();
            } finally {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }

    private void reinRaus(Socket socket) throws IOException {

        ObjectOutput out = null;


        //byte[] yourBytes = bos.toByteArray();


            BufferedReader rein = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));


        System.out.println("we are waiting for messages...");

        while ((s = rein.readLine()) != null && !stop) {
                System.out.println("we have received this message : "+s);


                //client checks available games
                if(s.equals("status")){
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(OnlineController.CURRENT_PLAYERS_IN_ROOM+"/"+OnlineController.MAX_PLAYERS+","+ userdata.getUsername()+end);
                }
                //client checks for server availability
            if(s.equals("check")){
                new ObjectOutputStream(socket.getOutputStream()).writeObject("1,"+payload1+","+payload2+end);
            }
                //client sends his move
                if(s.equals("join")){
                    new ObjectOutputStream(socket.getOutputStream()).writeObject("accept"+userdata.getUsername() + "," + userdata.get_selected_figure()+","+ userdata.getUsername()+end);
                     String[] sarr = rein.readLine().split(",");

                    OnlinePlayer p = new OnlinePlayer(socket.getRemoteSocketAddress().toString(),sarr[0]);

                    if(sarr.length>1)
                        p.setFigure(figures_name.valueOf(sarr[1]));

                    players.add(p);


                    System.out.println("pl names : "+sarr[0]);

                }


            }

        }
    public void stop() {
        stop = true;
        try {
            //socket.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Try to shutdown server...");

    }
}


