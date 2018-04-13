package model.net;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    //static ArrayList<ArrayList<Card>> hands;

    //Inspiration from tutorial:  https://javabeginners.de/Netzwerk/Socketverbindung.php

    private PrintStream ps;
    boolean stop = false;
    public Socket socket = null;

    int i = 0;

    public Client(String host,int port) {
        try {
            socket = new Socket(host, port);


        } catch (UnknownHostException e) {
            System.out.println("Unknown Host...");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            //e.printStackTrace();


        }
    }

    public void stop(){
        stop = true;

        if (socket != null)
            try {
                socket.close();
                System.out.println("Socket geschlossen...");
            } catch (IOException e) {
                System.out.println("Socket nicht zu schliessen...");
                e.printStackTrace();
            }
    }

        public String[] communication(String command, String payload) throws IOException, ClassNotFoundException {


            OutputStream raus = socket.getOutputStream();
            InputStream rein = socket.getInputStream();


            socket.setKeepAlive(true);

            ps = new PrintStream(raus, true);


            ps.println(command);
            ps.println(payload);
            ps.flush();


            try {
                ObjectInputStream ois = new ObjectInputStream(rein);

            String[] s = null;
            Object o;
            while ((o = ois.readObject()) != null && !stop) {

                s = ((String) o).split(",");

                for (int i =0; i<s.length;i++)
                    if(s[i].equals("end")){
                    s[i]=null;
                    return s;
                    }


            }
            }catch (EOFException e){
                e.printStackTrace();
            }catch (SocketException e){

            }
            return null;
        }


}