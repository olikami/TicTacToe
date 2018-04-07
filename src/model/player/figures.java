
package model.player;

        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.Pane;
        import javafx.scene.layout.VBox;

public class figures {

    public VBox getAllFigures(int W_H){
        for(int i =0; i<figures_name.values().length;i++){
            Pane pane = new Pane();
            pane.setMinSize(W_H,W_H);
            pane.setMaxSize(W_H,W_H);

            ImageView IV = new ImageView();

            IV.setImage(new Image("/res/img/"+figures_name.values()[i]+".png"));
            IV.setFitHeight(W_H);
            IV.setFitWidth(W_H);
            IV.setPreserveRatio(true);
            pane.getChildren().add(IV);

        }

    }
}
