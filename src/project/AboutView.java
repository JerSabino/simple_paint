package project;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutView extends BorderPane{

    private final Stage window;
    private final VBox info;
    private final BorderPane root;

    public AboutView(){
        window = new Stage();
        window.initModality(Modality.NONE);
        window.setResizable(false);

        info = new VBox();

        root = new BorderPane();

        initialize();

        Scene scene = new Scene(root);

        window.setScene(scene);
    }

    public void initialize(){
        root.setCenter(new ImageView(new Image("images/JS-logos.png")));
    }

    public void showAboutWindow(){
        window.show();
    }



}
