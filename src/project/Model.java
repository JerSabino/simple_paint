package project;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;

public class Model {
    private final SimpleListProperty<Layer> layerListProperty;
    private int layerNum;

    public Model(){
        ArrayList<Layer> list = new ArrayList<>();

        ObservableList<Layer> observableList = FXCollections.observableArrayList(list);
        layerListProperty = new SimpleListProperty<>(observableList);

        layerNum = 2;
    }

    public void addLayer(){
        Canvas newCanvas = new Canvas();
        newCanvas.setHeight(1000);
        newCanvas.setWidth(1200);

        Layer layer = new Layer(newCanvas, layerNum);
        layerListProperty.add(layer);

        layerNum++;
    }

    public void removeLayer(Layer layer){
        layerListProperty.remove(layer);
    }

    public void moveLayerUp(int index){
        if(index-1 > -1){
            Collections.swap(layerListProperty(), index, index-1);
        }
    }

    public void moveLayerDown(int index){
        if(index != -1 && index+1 < layerListProperty.size()){
            Collections.swap(layerListProperty(), index, index+1);
        }
    }

    public SimpleListProperty<Layer> layerListProperty(){ return layerListProperty; }
}
