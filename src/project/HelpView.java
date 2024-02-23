package project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpView extends BorderPane {

    private final Stage window;
    private final VBox info;
    private final BorderPane root;

    public HelpView(){
        window = new Stage();
        window.initModality(Modality.NONE);
        info = new VBox();
        info.setAlignment(Pos.CENTER);

        root = new BorderPane();

        initialize();

        Scene scene = new Scene(root);

        window.setScene(scene);
    }

    public void initialize(){
        Label help = new Label("Welcome to Paint Simple! " +
                "This is a simple painting program with a layer system created as a project submission for CS3035 Fall 2021.\n\n" +
                "The drawing space is set to a fixed size of 1200x800.\n" +
                "The available drawing tools at your disposal are (in order):\n" +
                "- The Paint Brush\n" +
                "- The Line Tool\n" +
                "- The Eraser Tool\n\n" +
                "The size of each tool can be changed via a text box and slider on the left toolbar.\n" +
                "Colors can be picked and changed via the widget located on the bottom right side of the screen.\n\n" +
                "Layers can be managed via a 'Manage Layers' button on the bottom left side of the screen.\n" +
                "From here, you can add layers via the + button and remove layers via the - button.\n" +
                "* The 'top-most layer in the list' is the bottom layer in the canvas.\n" +
                "* The 'bottom-most layer in the list is the top layer in the canvas.\n" +
                "Visibility can also be toggled via the button with an 'eye'. It also displays whether or not a layer is visible via its icon.\n" +
                "Layers can be moved up and down in the canvas via the arrows located on the right side\n" +
                "Layers can also be named in the list.\n" +
                "Additionally, you can choose which layer you'd like to draw on.\n" +
                "The current layer that you're on will be shown just to the left of the 'Manage Layers' button.\n\n" +
                "If there are still any confusions as to what a button does during your painting session, hover over it for a helpful tooltip!\n\n" +
                "Happy Painting! :)");
        help.setWrapText(true);
        help.setMaxWidth(600);

        info.getChildren().add(help);
        info.setPadding(new Insets(5,10,5,10));
        info.setMaxSize(600,500);

        root.setLeft(new ImageView(new Image("images/help.png")));
        root.setCenter(info);
    }

    public void showHelpWindow(){
        window.show();
    }

}
