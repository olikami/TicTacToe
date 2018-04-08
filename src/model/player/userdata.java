package model.player;


//This data Loads, saves and define the userdata
//userdata contains: unlockd stuff, statistics, and progress
public class userdata {
    static String username = "";
    static public figures_name selected_figure = figures_name.CIRCLE;


    static public void change_selected_figure(figures_name newFigure){
        selected_figure = newFigure;
    }

    static public figures_name get_selected_figure(){
        return selected_figure;
    }
}
