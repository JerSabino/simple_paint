package project;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {

    public enum State{READY, DRAWING}
    private State state;

    private enum Mode {BRUSH, LINE, ERASER};
    private Mode currentMode;
    private GraphicsContext currentGC;
    double x, y, origX, origY;

    private final ToggleGroup toolsGroup = Main.view.getDrawingToolsGroup();

    public Controller(){
        initToolBar();
        initMouseHandler();

        currentMode = Mode.BRUSH;
        state = State.READY;
    }

    public void initToolBar(){
        //~~~~~~~~~~~~~~~~~~~ TOGGLE GROUP ~~~~~~~~~~~~~~~~~~~//
        toolsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == Main.view.getBrushBtn()){
                System.out.println("(TOGGLED BRUSH BUTTON)\n~~~~~~~~~");
                currentMode = Mode.BRUSH;
                Main.view.showSlider("BRUSH");
            }
            else if(newValue == Main.view.getLineBtn()){
                System.out.println("(TOGGLED LINE BUTTON)\n~~~~~~~~~");
                currentMode = Mode.LINE;
                Main.view.showSlider("LINE");
            }
            else if(newValue == Main.view.getEraserBtn()){
                System.out.println("(TOGGLED ERASER BUTTON)\n~~~~~~~~~~");
                currentMode = Mode.ERASER;
                Main.view.showSlider("ERASER");
            }
        });
        initButtons();
        initSliders();
    }

    public void initButtons(){
        Button layerBtn = Main.view.getLayerBtn();
        layerBtn.setOnAction(event -> {
            Stage window = Main.layersView.getStage();
            if(!window.isShowing()){
                Main.layersView.showLayersWindow();
            }
            else if(window.isShowing()){
                window.toFront();
            }
        });
    }

    public void initSliders(){
        Slider brushSlider = Main.view.getBrushSlider();
        Slider eraserSlider = Main.view.getEraserSlider();
        Slider lineSlider = Main.view.getLineSlider();

        brushSlider.valueProperty().addListener((observable, oldValue, newValue) -> Main.view.setBrushSize((double)newValue));
        eraserSlider.valueProperty().addListener((observable, oldValue, newValue) -> Main.view.setEraserSize((double)newValue));
        lineSlider.valueProperty().addListener(((observable, oldValue, newValue) -> Main.view.setLineSize((double)newValue)));
    }


    public void initMouseHandler(){
        //~~~~~~~~~~~~~~~~~~~ MOUSE EVENT HANDLER ~~~~~~~~~~~~~~~~~~~//
        Pane drawingCanvas = Main.view.getDrawingCanvas();
        drawingCanvas.addEventHandler(MouseEvent.ANY, e-> {
            switch(state){
                //**************************************** READY ****************************************//
                case READY:
                    x = e.getX();
                    y = e.getY();
                    if(e.getEventType() == MouseEvent.MOUSE_MOVED){
                        switch(currentMode){
                            case BRUSH:
                                Main.view.preview("BRUSH", x, y);
                                break;
                            case ERASER:
                                Main.view.preview("ERASER", x, y);
                                break;
                            case LINE:
                                Main.view.preview("LINE", x, y);
                                break;
                        }
                    }
                    else if(e.getEventType() == MouseEvent.MOUSE_EXITED){
                        Main.view.clearPreview();
                    }
                    //~~~~~~~~~~~~~~~~~~~ MOUSE PRESSED ~~~~~~~~~~~~~~~~~~~//
                    if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
                        currentGC = Main.view.getGraphicsContext();
                        state = State.DRAWING;
                        Main.view.clearPreview();

                        switch(currentMode){
                            case BRUSH:
                                System.out.println("DRAWING");
                                Main.view.draw(currentGC, x, y);
                                break;
                            case ERASER:
                                Main.view.erase(currentGC, x, y);
                                break;
                            case LINE:
                                origX = x;
                                origY = y;
                        }
                    }
                    break;
                case DRAWING:
                    x = e.getX();
                    y = e.getY();
                    //~~~~~~~~~~~~~~~~~~~ MOUSE DRAGGED ~~~~~~~~~~~~~~~~~~~//
                    if(e.getEventType() == MouseEvent.MOUSE_DRAGGED){
                        switch(currentMode){
                            case BRUSH:
                                System.out.println("DRAWING");
                                Main.view.draw(currentGC, x, y);
                                break;
                            case ERASER:
                                Main.view.erase(currentGC, x, y);
                                break;
                            case LINE:
                                Main.view.previewLine(origX, origY, x, y);
                                break;
                        }
                    }
                    if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
                        if(currentMode == Mode.LINE){
                            Main.view.drawLine(currentGC, origX, origY, x, y);
                        }
                        state = State.READY;
                    }
                    break;
            }
        });
    }

    public void saveFile(Image image){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(Main.getStage());

        if(file != null){
            try{
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
