package project;

import javafx.scene.canvas.Canvas;

public class Layer {

    private Canvas canvas;
    private String layerName;
    private boolean visible;

    public Layer(Canvas c, int num){
        this.layerName = "Layer " + num;
        this.canvas = c;
        this.visible = true;
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public String getName(){
        return layerName;
    }

    public void setLayerName(String name){
        layerName = name;
    }

    public void setVisible(){
        visible = true;
    }

    public void setInvisible(){
        visible = false;
    }

    public boolean isVisible(){
        return visible;
    }
}