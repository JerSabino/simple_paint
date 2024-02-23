package project;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    public BorderPane root;
    public static Scene scene;
    public static AboutView aboutView;
    public static HelpView helpView;
    public static View view;
    public static Controller controller;
    public static Model model;
    public static LayersView layersView;
    public static LayersController layersController;
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        try{
            stage = primaryStage;

            initialize();

            scene = new Scene(root);

            loadSplashScreen();

            primaryStage.setTitle("Simple Paint");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void initialize(){
        root = new BorderPane();
        model = new Model();
        aboutView = new AboutView();
        helpView = new HelpView();
        view = new View();
        controller = new Controller();
        layersView = new LayersView();
        layersController = new LayersController();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadSplashScreen(){
        try{
            StackPane pane = new StackPane();
            pane.getChildren().add(new ImageView(new Image("images/splash.png")));

            root.setCenter(pane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            fadeOut.setOnFinished((e) -> {
                root.setCenter(view);
                stage.setWidth(1000);
                stage.setHeight(800);
            });

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Stage getStage(){
        return stage;
    }

}
