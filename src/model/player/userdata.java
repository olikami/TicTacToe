package model.player;


import java.io.*;

import org.json.*;

import java.net.URISyntaxException;

//This data Loads, saves and define the userdata
//userdata contains: unlockd stuff, statistics, and progress
public class userdata {
    static public figures_name selected_figure = figures_name.CIRCLE;
    static String username = "";

    static public void change_selected_figure(figures_name newFigure) {
        selected_figure = newFigure;
        save();
    }

    static public figures_name get_selected_figure() {
        return selected_figure;
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

            username = obj.getString("username");
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

    //https://crunchify.com/how-to-write-json-object-to-file-in-java/
    static void save() {

        JSONObject obj = new JSONObject();

        try {

            obj.put("username", username);
            obj.put("selected_figure",selected_figure);


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
