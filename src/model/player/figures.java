
package model.player;

        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.geometry.Pos;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.Pane;
        import javafx.scene.layout.VBox;

        import java.awt.event.MouseEvent;

public class figures {

    public static VBox getAllFigures(int W_H){
        HBox Player_figure = new HBox();
        Player_figure.setAlignment(Pos.CENTER);
        ImageView IV_p = new ImageView();



        IV_p.setImage(new Image("/res/img/"+userdata.get_selected_figure()+"/"+userdata.get_selected_figure()+".png"));
        IV_p.setFitWidth(W_H);
        IV_p.setFitHeight(W_H);
        Player_figure.getChildren().add(IV_p);


        int linelength =0;
        HBox selection_figures = new HBox();

        for(int i =0; i<figures_name.values().length;i++){
            Pane pane = new Pane();
            pane.setMinSize(W_H,W_H);
            pane.setMaxSize(W_H,W_H);

            ImageView IV = new ImageView();

            IV.setImage(new Image("/res/img/"+figures_name.values()[i]+"/"+figures_name.values()[i]+".png"));
            IV.setFitHeight(W_H);
            IV.setFitWidth(W_H);
            IV.setPreserveRatio(true);
            pane.getChildren().add(IV);

            final int finalI = i;

            pane.setOnMouseClicked(event -> {
                userdata.change_selected_figure(figures_name.values()[finalI]);
                IV_p.setImage(new Image("/res/img/"+userdata.get_selected_figure()+"/"+userdata.get_selected_figure()+".png"));

            });

            pane.setOnMouseEntered(event -> {
                IV.setImage(new Image("/res/img/"+(figures_name.values()[finalI])+"/"+(figures_name.values()[finalI])+"_hover.png"));

            });
            pane.setOnMouseExited(event -> {
                IV.setImage(new Image("/res/img/"+(figures_name.values()[finalI])+"/"+(figures_name.values()[finalI])+".png"));

            });

            selection_figures.getChildren().add(pane);
            selection_figures.setAlignment(Pos.CENTER);

            linelength++;
        }
        HBox line = new HBox();
        line.setMinSize(W_H*(linelength==0?2:linelength),10);
        line.setMaxSize(W_H*(linelength==0?2:linelength),10);
        line.setStyle("-fx-background-color: rgba(255,255,255,1.0);");

        VBox box = new VBox(Player_figure,line,selection_figures);
        box.setAlignment(Pos.CENTER);

        return box;
    }
}
