package project;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;

public class View extends BorderPane {

    private Canvas canvas, preview;
    private GraphicsContext currentGC, previewGC;
    private double brushSize, eraserSize, lineSize;
    private Slider brushSlider, eraserSlider, lineSlider;
    private TextField brushTextBox, eraserTextBox, lineTextBox;
    private Label currentLayerLbl;


    private ToolBar leftToolBar, bottomToolBar;
    private MenuBar menuBar;
    private ToggleGroup drawingToolsGroup;
    private ColorPicker colorPicker;

    private ScrollPane drawingSpace;
    private StackPane drawingCanvas;

    private ToggleButton brushBtn, lineBtn, eraserBtn;
    private Button layerBtn;

    public View(){

        brushSize = 3;
        eraserSize = 3;
        lineSize = 3;

        initToolbars();
        initCanvas();
        setCanvas();

        this.setLeft(leftToolBar);
        this.setTop(menuBar);
        this.setBottom(bottomToolBar);
    }

    public void initToolbars(){
        //~~~~~~ Menu Bar ~~~~~~//
        Menu menu = new Menu("Menu");
        MenuItem help = new MenuItem("Help");
        MenuItem about = new MenuItem("About");

        about.setOnAction(event -> Main.aboutView.showAboutWindow());
        help.setOnAction(event -> Main.helpView.showHelpWindow());

        Menu fileMenu = new Menu("File");
        MenuItem save = new MenuItem("Save Image");

        save.setOnAction(event -> saveImage());

        Menu editMenu = new Menu("Edit");
        MenuItem clearAll = new MenuItem("Clear everything");
        MenuItem clearLayer = new MenuItem("Clear current layer");

        clearAll.setOnAction(event -> clearAllLayers());
        clearLayer.setOnAction(event -> clearCurrentLayer());

        menu.getItems().addAll(help, about);
        fileMenu.getItems().add(save);
        editMenu.getItems().addAll(clearAll, clearLayer);

        menuBar = new MenuBar(menu, fileMenu, editMenu);

        //~~~~~~ Bottom Bar ~~~~~~//
        bottomToolBar = new ToolBar();
        bottomToolBar.setPadding(new Insets(5, 5, 5, 5));

        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);

        layerBtn = new Button("Manage Layers", new ImageView(new Image("images/resized/layers.png")));

        currentLayerLbl = new Label("Current Layer: Layer 1");

        final Pane rightSpacer = new Pane();
        HBox.setHgrow(
                rightSpacer,
                Priority.ALWAYS
        );

        bottomToolBar.getItems().addAll(
                colorPicker,
                rightSpacer,
                currentLayerLbl,
                new Separator(),
                layerBtn);

        //~~~~~~ Left Toolbar ~~~~~//
        leftToolBar = new ToolBar();
        drawingToolsGroup = new ToggleGroup();

        brushBtn = new ToggleButton();
        lineBtn = new ToggleButton();
        eraserBtn = new ToggleButton();

        // Drawing Tools Toggle Group
        drawingToolsGroup.getToggles().addAll(
          brushBtn,
          lineBtn,
          eraserBtn
        );

        // Button Graphics
        brushBtn.setGraphic(new ImageView(new Image("images/resized/brush.png")));
        lineBtn.setGraphic(new ImageView(new Image("images/resized/line.png")));
        eraserBtn.setGraphic(new ImageView(new Image("images/resized/eraser.png")));

        // Button Tooltips
        brushBtn.setTooltip(new Tooltip("Brush"));
        lineBtn.setTooltip(new Tooltip("Line"));
        eraserBtn.setTooltip(new Tooltip("Eraser"));

        initSliders();
        initTextBoxes();

        addButtons();
        leftToolBar.setOrientation(Orientation.VERTICAL);
        leftToolBar.getItems().addAll(brushSlider, brushTextBox);
    }

    public void addButtons(){
        //Size Label
        Label sizeLbl = new Label("Size:");
        leftToolBar.getItems().addAll(
                brushBtn,
                new Separator(),
                lineBtn,
                new Separator(),
                eraserBtn,
                new Separator(),
                sizeLbl
        );
    }

    public void initTextBoxes(){
        brushTextBox = new TextField();
        eraserTextBox = new TextField();
        lineTextBox = new TextField();

        brushTextBox.setEditable(true);
        eraserTextBox.setEditable(true);
        lineTextBox.setEditable(true);

        brushTextBox.setPrefWidth(50);
        eraserTextBox.setPrefWidth(50);
        lineTextBox.setPrefWidth(50);

        brushTextBox.setText("5.0");
        eraserTextBox.setText("5.0");
        lineTextBox.setText("5.0");

        brushTextBox.setOnAction(event -> {
            double value = Double.parseDouble(brushTextBox.getText());
            if(value > 200){
                brushSlider.setValue(200);
            }
            else if(value< 5){
                brushSlider.setValue(5);
            }
            else{
                brushSlider.setValue(value);
            }
        });

        eraserTextBox.setOnAction(event -> {
            double value = Double.parseDouble(eraserTextBox.getText());
            if(value > 200){
                eraserSlider.setValue(200);
            }
            else if(value< 0){
                eraserSlider.setValue(5);
            }
            else{
                eraserSlider.setValue(value);
            }
        });

        lineTextBox.setOnAction(event -> {
            double value = Double.parseDouble(lineTextBox.getText());
            if(value > 200){
                lineSlider.setValue(200);
            }
            else if(value< 0){
                lineSlider.setValue(5);
            }
            else{
                lineSlider.setValue(value);
            }
        });

        //brushTextBox.textProperty().bind(brushSlider.valueProperty().asString("%.1f"));
        //eraserTextBox.textProperty().bind(eraserSlider.valueProperty().asString("%.1f"));
        //lineTextBox.textProperty().bind(lineSlider.valueProperty().asString("%.1f"));
    }

    public void initCanvas() {
        drawingSpace = new ScrollPane();
        drawingCanvas = new StackPane();
        drawingCanvas.setPrefSize(1200,1000);
        drawingCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        preview = new Canvas();
        preview.setHeight(1000);
        preview.setWidth(1200);
        previewGC = preview.getGraphicsContext2D();

        canvas = new Canvas();
        canvas.setHeight(1000);
        canvas.setWidth(1200);

        drawingSpace.setContent(drawingCanvas);
        currentGC = canvas.getGraphicsContext2D();

        Layer layer = new Layer(canvas, 1);
        Main.model.layerListProperty().add(layer);
    }

    public void resetCanvas(){
        drawingCanvas.getChildren().clear();
        System.out.println("Cleared canvas");
    }

    public void setCanvas(){
        int num = 1;
        for(Layer l : Main.model.layerListProperty()){
            System.out.println("Added layer " + num);
            num++;
            if(l.isVisible()){
                drawingCanvas.getChildren().add(l.getCanvas());
            }
        }
        drawingCanvas.getChildren().add(preview);
        drawingSpace.setContent(drawingCanvas);
        this.setCenter(drawingSpace);
    }

    public void initSliders(){
        brushSlider = new Slider();
        brushSlider.setMin(5);
        brushSlider.setMax(200);
        brushSlider.setOrientation(Orientation.VERTICAL);
        brushSlider.setShowTickLabels(true);
        brushSlider.setShowTickMarks(true);
        brushSlider.setPrefHeight(250);

        eraserSlider = new Slider();
        eraserSlider.setMin(5);
        eraserSlider.setMax(200);
        eraserSlider.setOrientation(Orientation.VERTICAL);
        eraserSlider.setShowTickLabels(true);
        eraserSlider.setShowTickMarks(true);
        eraserSlider.setPrefHeight(250);

        lineSlider = new Slider();
        lineSlider.setMin(5);
        lineSlider.setMax(200);
        lineSlider.setOrientation(Orientation.VERTICAL);
        lineSlider.setShowTickLabels(true);
        lineSlider.setShowTickMarks(true);
        lineSlider.setPrefHeight(250);
    }

    public void draw(GraphicsContext gc, double x, double y){
        gc.save();
        gc.setFill(colorPicker.getValue());
        gc.fillOval(x-(brushSize/2), y-(brushSize/2), brushSize, brushSize);
        gc.restore();
    }

    public void preview(String mode, double x, double y){
        clearPreview();
        previewGC.save();
        switch(mode){
            case "BRUSH":
                previewGC.setFill(colorPicker.getValue());
                previewGC.fillOval(x-(brushSize/2), y-(brushSize/2), brushSize, brushSize);
                break;
            case "ERASER":
                previewGC.setFill(Color.WHITE);
                previewGC.fillRect(x-(eraserSize/2), y-(eraserSize/2), eraserSize, eraserSize);
                break;
            case "LINE":
                previewGC.setFill(colorPicker.getValue());
                previewGC.fillRect(x-(lineSize/2), y-(lineSize/2), lineSize, lineSize);
        }
        previewGC.restore();
    }

    public void previewLine(double origX, double origY, double targetX, double targetY){
        clearPreview();

        previewGC.setStroke(colorPicker.getValue());
        previewGC.setLineWidth(lineSize);

        previewGC.strokeLine(origX, origY, targetX, targetY);
    }

    public void drawLine(GraphicsContext gc, double origX, double origY, double targetX, double targetY){
        clearPreview();

        gc.setStroke(colorPicker.getValue());
        gc.setLineWidth(lineSize);

        gc.strokeLine(origX, origY, targetX, targetY);
    }

    public void clearPreview(){
        previewGC.clearRect(0,0,preview.getWidth(),preview.getHeight());
    }

    public void erase(GraphicsContext gc, double x, double y){
        gc.clearRect(x-(eraserSize/2), y-(eraserSize/2), eraserSize, eraserSize);
    }

    public void showSlider(String mode){
        leftToolBar.getItems().removeAll(brushSlider, eraserSlider, lineSlider, brushTextBox, eraserTextBox, lineTextBox);
        switch(mode){
            case "BRUSH":
                leftToolBar.getItems().addAll(brushSlider, brushTextBox);
                break;
            case "LINE":
                leftToolBar.getItems().addAll(lineSlider, lineTextBox);
                break;
            case "ERASER":
                leftToolBar.getItems().addAll(eraserSlider, eraserTextBox);
                break;
        }
    }

    public void clearAllLayers(){
        for(Layer l : Main.model.layerListProperty()){
            GraphicsContext tempGC = l.getCanvas().getGraphicsContext2D();
            tempGC.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        }
    }

    public void clearCurrentLayer(){
        currentGC.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
    }

    public ToggleGroup getDrawingToolsGroup(){
        return drawingToolsGroup;
    }

    public ToggleButton getBrushBtn(){
        return brushBtn;
    }

    public ToggleButton getLineBtn(){
        return lineBtn;
    }

    public ToggleButton getEraserBtn(){
        return eraserBtn;
    }

    public Slider getBrushSlider(){
        return brushSlider;
    }

    public Slider getEraserSlider(){
        return eraserSlider;
    }

    public Slider getLineSlider(){
        return lineSlider;
    }

    public Button getLayerBtn(){
        return layerBtn;
    }

    public void setBrushSize(double value){
        brushSize = value;
        brushTextBox.setText(String.format("%.1f", value));
    }

    public void setEraserSize(double value){
        eraserSize = value;
        eraserTextBox.setText(String.format("%.1f", value));
    }

    public void setLineSize(double value){
        lineSize = value;
        lineTextBox.setText(String.format("%.1f", value));
    }

    public GraphicsContext getGraphicsContext(){
        return currentGC;
    }

    public Pane getDrawingCanvas(){
        return drawingCanvas;
    }

    public void changeLayer(int index){
        Layer selectedLayer = Main.model.layerListProperty().get(index);
        currentGC = selectedLayer.getCanvas().getGraphicsContext2D();
        System.out.println("Changed to " + selectedLayer.getName());

        currentLayerLbl.setText("Current Layer: " + selectedLayer.getName());
    }

    public void saveImage(){
        Canvas c = new Canvas(1200,1000);
        GraphicsContext gc = c.getGraphicsContext2D();

        WritableImage wim = new WritableImage(1200,1000);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        //Draw white background first
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1200,1000);

        //Draw all other layers
        for(Layer l : Main.model.layerListProperty()){
            Canvas currLayer = l.getCanvas();
            Image snapshot = currLayer.snapshot(params, wim);
            gc.drawImage(snapshot, 0, 0);
        }

        Image img = c.snapshot(new SnapshotParameters(), wim);
        Main.controller.saveFile(img);
    }
}

