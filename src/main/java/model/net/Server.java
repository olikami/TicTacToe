package model.net;


import controller.OnlineController;
import model.online_game;
import model.player.OnlinePlayer;
import model.player.figures_name;
import model.player.userdata;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {


    public final ServerSocket server;
    private Boolean stop = false;
    String s = "null";
    Socket socket = null;
    public String payload1 = new String();
    public String payload2 ="";
    String end = ",end";
    public int[] board_in_serverClass = {0,0,0,0,0,0,0,0,0};
    public Boolean AI_Mode = false;
    public ArrayList<String> CHAT = new ArrayList<>();




        public ArrayList<OnlinePlayer> players= new ArrayList<>();





    public Server(int port, int backlog, String bindAddr) throws IOException {
        if(port == -1) {
            AI_Mode = true;
            port=14909;
        }

        server = new ServerSocket(port, backlog, InetAddress.getByName(bindAddr));

        System.out.println("listening on port: " + server.getLocalPort());

        //create the host player.
        OnlinePlayer p = new OnlinePlayer(bindAddr,userdata.getUsername());
        p.setFigure(userdata.get_selected_figure());
        players.add(p);


    }


    public void connect() {

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


            BufferedReader rein = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));

        PrintStream ps = new PrintStream(socket.getOutputStream(),true);


        System.out.println("we are waiting for messages...");

        while ((s = rein.readLine()) != null && !stop) {
                System.out.println("we have received this message : "+s);


                //client checks available games
                if(s.equals("status")){


                    ps.println((online_game.CURRENT_PLAYERS_IN_ROOM+"/"+online_game.MAX_PLAYERS+","+ userdata.getUsername()+end));
                    System.out.println((online_game.CURRENT_PLAYERS_IN_ROOM+"/"+online_game.MAX_PLAYERS+","+ userdata.getUsername()+end));

                }
                //client checks for server availability
            if(s.equals("check")){
                ps.println("1,"+payload1+","+payload2+end);
                System.out.println("We sent: "+"1,"+payload1+","+payload2+end);
            }
            if(s.equals("getPlayers")){
                new ObjectOutputStream(socket.getOutputStream()).writeObject(players);
            }
            //client has played. we need to change the board
            if(s.equals("board")){
                int i =0;
                for (String str : ((rein.readLine().replace("[","")).replace("]","")).split(" ")) {
                    if (str.equals("")) continue;

                    board_in_serverClass[i++] = Integer.parseInt(str);
                }
                ps.println("1,"+ Arrays.toString(board_in_serverClass) +",BoardReceiveOk"+end);

            }

            if(s.equals("chat")){
                String newMessage = rein.readLine();

                if(!newMessage.equals(""))
                    CHAT.add(newMessage);

                new ObjectOutputStream(socket.getOutputStream()).writeObject(CHAT);
            }


                //client sends his move
                if(s.equals("join")){
                    ps.println("accept"+userdata.getUsername() + "," + userdata.get_selected_figure()+","+ userdata.getUsername()+end);
                     String[] sarr = rein.readLine().split(",");

                    OnlinePlayer p = new OnlinePlayer(socket.getRemoteSocketAddress().toString(),sarr[0]);

                    if(sarr.length>1)
                        p.setFigure(figures_name.valueOf(sarr[1]));

                    players.add(p);


                    System.out.println("pl names : "+sarr[0]);

                }

                ps.flush();

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


