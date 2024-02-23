package project;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LayersView extends BorderPane {

    private int currentIndex;
    private ListView layerList;
    private ToolBar rightToolBar, topToolBar;
    private Button addBtn, removeBtn, chooseBtn, upBtn, downBtn, visibleBtn;

    private ImageView visible, invisible;

    private final Stage window;

    public LayersView(){
        window = new Stage();
        window.initModality(Modality.NONE);
        window.setResizable(false);
        window.setOnHiding( event -> { resetList(); });

        BorderPane root = new BorderPane();
        initialize();

        root.setTop(topToolBar);
        root.setRight(rightToolBar);
        root.setCenter(layerList);

        Scene layersScene = new Scene(root, 180, 300);

        window.setScene(layersScene);
    }

    public void initializeBtns(){
        //Top Toolbar
        addBtn = new Button();
        addBtn.setTooltip(new Tooltip("Add a layer"));

        removeBtn = new Button();
        removeBtn.setTooltip(new Tooltip("Remove the selected layer"));

        visibleBtn = new Button();

        ImageView add = new ImageView(new Image("images/resized/add.png"));
        add.setFitHeight(25);
        add.setFitWidth(25);

        ImageView remove = new ImageView(new Image("images/resized/remove.png"));
        remove.setFitHeight(25);
        remove.setFitWidth(25);

        visible = new ImageView(new Image("images/resized/visible.png"));
        visible.setFitHeight(25);
        visible.setFitWidth(25);

        invisible = new ImageView(new Image("images/resized/not_visible.png"));
        invisible.setFitHeight(25);
        invisible.setFitWidth(25);

        addBtn.setGraphic(add);
        removeBtn.setGraphic(remove);
        visibleBtn.setGraphic(visible);

        //Right Toolbar
        upBtn = new Button();
        upBtn.setTooltip(new Tooltip("Move the layer down the canvas"));

        downBtn = new Button();
        downBtn.setTooltip(new Tooltip("Move the layer up the canvas"));

        ImageView up = new ImageView(new Image("images/resized/arrow_up.png"));
        up.setFitHeight(25);
        up.setFitWidth(25);

        ImageView down = new ImageView(new Image("images/resized/arrow_down.png"));
        down.setFitHeight(25);
        down.setFitWidth(25);

        upBtn.setGraphic(up);
        downBtn.setGraphic(down);

        chooseBtn = new Button("Choose");
        chooseBtn.setTooltip(new Tooltip("Choose the selected layer"));
    }

    public void initialize(){
        initializeBtns();

        //Top Toolbar
        topToolBar = new ToolBar();
        topToolBar.getItems().addAll(addBtn, removeBtn, new Separator(), visibleBtn);

        //Right Toolbar
        rightToolBar = new ToolBar();
        rightToolBar.setOrientation(Orientation.VERTICAL);

        rightToolBar.getItems().addAll(
                upBtn,
                downBtn,
                new Separator(),
                chooseBtn
        );

        layerList = new ListView<String>();
        layerList.setEditable(true);
        layerList.setCellFactory(TextFieldListCell.forListView());

        initListHandler();

        currentIndex = -1;
    }

    public void initListHandler(){
        layerList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentIndex = (int)newValue;
            System.out.println("Current index: " + currentIndex);

            if(currentIndex != -1){
                Layer layer = Main.model.layerListProperty().get((int)newValue);
                if(layer.isVisible()){
                    updateVisibleBtn(true);
                    visibleBtn.setTooltip(new Tooltip("Set layer invisible"));
                }
                else if(!layer.isVisible()){
                    updateVisibleBtn(false);
                    visibleBtn.setTooltip(new Tooltip("Set layer visible"));
                }
            }
        });
        layerList.setOnEditCommit((EventHandler<ListView.EditEvent>) event -> {
            int index = layerList.getSelectionModel().getSelectedIndex();
            Layer currentLayer = Main.model.layerListProperty().get(index);
            currentLayer.setLayerName(event.getNewValue().toString());

            resetList();
            updateList();
        });
    }

    public void updateList(){
        for(Layer l : Main.model.layerListProperty()){;
            layerList.getItems().add(l.getName());
        }
    }

    public void resetList(){
        layerList.getItems().clear();
    }

    public void closeLayersWindow(){
        resetList();
        window.close();
    }

    public void showLayersWindow(){
        updateList();
        window.show();
    }

    public Button getAddBtn(){
        return addBtn;
    }

    public Button getRemoveBtn(){
        return removeBtn;
    }

    public Button getChooseBtn(){
        return chooseBtn;
    }

    public Button getUpBtn(){
        return upBtn;
    }

    public Button getDownBtn(){
        return downBtn;
    }

    public Button getVisibleBtn(){
        return visibleBtn;
    }

    public int getCurrentIndex(){
        return currentIndex;
    }

    public Stage getStage(){
        return window;
    }

    public void updateVisibleBtn(boolean showing){
        if(showing){
            visibleBtn.setGraphic(visible);
        }
        else{
            visibleBtn.setGraphic(invisible);
        }
    }

}
