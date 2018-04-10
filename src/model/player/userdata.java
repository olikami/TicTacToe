package model.player;


import java.io.*;

import org.json.*;

import java.net.URISyntaxException;
import java.util.Iterator;

//This data Loads, saves and define the userdata
//userdata contains: unlockd stuff, statistics, and progress
public class userdata {


    private static figures_name selected_figure = figures_name.CIRCLE;
    static private int  playedGames ;
    static private int  winGames    ;
    static private int  loseGames   ;
    static String username = "";
    static boolean[] unlockedFigures ={true,true,false,false,false,false,false};




    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        userdata.username = username;
        save();

    }


    static public void change_selected_figure(figures_name newFigure) {
        selected_figure = newFigure;
        save();
    }

    static public figures_name get_selected_figure() {
        return selected_figure;
    }


    public static int getPlayedGames() {
        return playedGames;
    }

    public static void setPlayedGames() {
        userdata.playedGames++;
        save();
    }

    public static int getWinGames() {
        return winGames;
    }

    public static void setWinGames() {
        userdata.winGames++;
        save();

    }

    public static int getLoseGames() {
        return loseGames;
    }

    public static void setLoseGames() {
        userdata.loseGames++;
        save();

    }

    static public void loadUser() {


        String stringResult = "";
        BufferedReader sourceReader = null;
        try {
            sourceReader = new BufferedReader(new FileReader(new File(userdata.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "userdata.txt")));

            System.out.println(userdata.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "userdata.txt");

            String sourceLine = null;

            while ((sourceLine = sourceReader.readLine()) != null) {
                stringResult +=(sourceLine);


            }

            sourceReader.close();

            //parsing to a json

            JSONObject obj = null;
                obj = new JSONObject(stringResult);


            playedGames = obj.getInt("playedGames");
            winGames    = obj.getInt("winGames");
            loseGames   = obj.getInt("loseGames");
            username = obj.getString("username");
            JSONArray jarray = obj.getJSONArray("unlockedFigures");

            for(int i =0; i<jarray.length();i++)
                unlockedFigures[i] = jarray.getBoolean(i);


            selected_figure = figures_name.valueOf(obj.getString("selected_figure"));



            } catch (FileNotFoundException e) {
            //e.printStackTrace();
            save();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void unlockFigure(figures_name figure){
        unlockedFigures[figure.ordinal()] = true;
        save();
    }

    //https://crunchify.com/how-to-write-json-object-to-file-in-java/
    static void save() {

        JSONObject obj = new JSONObject();

        try {

            obj.put("username", username);
            obj.put("selected_figure",selected_figure);
            obj.put("playedGames", playedGames);
            obj.put("winGames", winGames   );
            obj.put("loseGames", loseGames  );

            JSONArray unlockedFigures_array = new JSONArray();
            for(int i =0; i<unlockedFigures.length;i++)
                unlockedFigures_array.put(unlockedFigures[i]);

            obj.put("unlockedFigures", unlockedFigures_array);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // try-with-resources statement based on post comment below :)
        try (FileWriter file = new FileWriter(userdata.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "userdata.txt")) {
            file.write(obj.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
