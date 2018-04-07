package model.net;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


    private final ServerSocket server;
    private Boolean stop = false;
    String s = "null";


    public Server(int port, int backlog, String bindAddr) throws IOException {
        server = new ServerSocket(port, backlog, InetAddress.getByName(bindAddr));



    }



    public void verbinde() {


        while (true) {
            if (stop)
                break;
            System.out.println("ok");
            Socket socket = null;
            try {

                socket = server.accept();
                reinRaus(socket);
            } catch (IOException e) {
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

            while ((s = rein.readLine()) != null) {
                if (stop)
                    break;

                //new ObjectOutputStream(socket.getOutputStream()).writeObject(hands);
                //raus.println(System.nanoTime() + "  that's a server message: " + s);
            /*if (!s.equals(""))
                break;*/
            }
        stop = true;

        }

/*    public ArrayList<ArrayList<Card>> getOnlineCards() {

        return hands;
    }
*/
    public void stop() {
        stop = true;
    }

    public String getOppName() {
        return s;
    }
}


