package model.player;


import java.io.*;

import org.json.*;

import java.net.URISyntaxException;

//This data Loads, saves and define the userdata
//userdata contains: unlockd stuff, statistics, and progress
public class userdata {


    private static figures_name selected_figure = figures_name.CIRCLE;
    static private int  playedGames ;
    static private int wins;
    static private int loses;
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

    public static boolean[] getUnlockedFigures() {
        return unlockedFigures;
    }

    public static int getWins() {
        return wins;
    }

    public static void setWinGames() {
        userdata.wins++;
        save();

    }

    public static int getLoses() {
        return loses;
    }

    public static void setLoseGames() {
        userdata.loses++;
        save();

    }

    static String filepath ="";
    static public void loadUser() {


        String stringResult = "";
        BufferedReader sourceReader = null;
        try {

            filepath = userdata.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            int charcount=0;
            while(filepath.charAt(filepath.length()-1-charcount)!='/'){
                charcount++;
            }
            System.out.println(filepath + "userdata"+"  "+(filepath.length()-charcount));

            filepath=filepath.substring(0,(filepath.length()-charcount));

            System.out.println(filepath + "userdata");

            sourceReader = new BufferedReader(new FileReader(new File(filepath + "userdata")));


            String sourceLine = null;

            while ((sourceLine = sourceReader.readLine()) != null) {
                stringResult +=(sourceLine);


            }

            sourceReader.close();

            //parsing to a json

            JSONObject obj = null;
                obj = new JSONObject(stringResult);


            playedGames = obj.getInt("playedGames");
            wins = obj.getInt("wins");
            loses = obj.getInt("loses");
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
    public static void unlockFigure(figures_name figure){
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
            obj.put("wins", wins);
            obj.put("loses", loses);

            JSONArray unlockedFigures_array = new JSONArray();
            for(int i =0; i<unlockedFigures.length;i++)
                unlockedFigures_array.put(unlockedFigures[i]);
            obj.put("unlockedFigures", unlockedFigures_array);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // try-with-resources statement based on post comment below :)
        try (FileWriter file = new FileWriter(filepath + "userdata")) {
            file.write(obj.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
