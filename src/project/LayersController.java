package project;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public class LayersController {
    private Button addBtn, removeBtn, upBtn, downBtn, chooseBtn, visibleBtn;

    public LayersController(){
        initButtons();
    }

    public void initButtons(){
        addBtn = Main.layersView.getAddBtn();
        removeBtn = Main.layersView.getRemoveBtn();
        chooseBtn = Main.layersView.getChooseBtn();
        upBtn = Main.layersView.getUpBtn();
        downBtn = Main.layersView.getDownBtn();
        visibleBtn = Main.layersView.getVisibleBtn();

        initButtonsHandler();
    }

    public void initButtonsHandler(){
        //Choose Btn
        chooseBtn.setOnAction(event -> {
            int selectedIndex = Main.layersView.getCurrentIndex();
            Main.view.changeLayer(selectedIndex);
            Main.layersView.closeLayersWindow();
        });

        //Add Btn
        addBtn.setOnAction(event -> {
            Main.model.addLayer();
            Main.layersView.resetList();
            Main.layersView.updateList();

            Main.view.resetCanvas();
            Main.view.setCanvas();
        });

        //Remove Btn
        removeBtn.setOnAction(event -> {
            int selectedIndex = Main.layersView.getCurrentIndex();
            if(selectedIndex != -1){
                if(Main.model.layerListProperty().size() > 1) {
                    Layer selectedLayer = Main.model.layerListProperty().get(selectedIndex);
                    Main.model.removeLayer(selectedLayer);

                    Main.view.changeLayer(0);

                    Main.layersView.resetList();
                    Main.layersView.updateList();

                    Main.view.resetCanvas();
                    Main.view.setCanvas();
                }
            }
        });

        //Up Btn
        upBtn.setOnAction(event -> {
            int selectedIndex = Main.layersView.getCurrentIndex();
            Main.model.moveLayerUp(selectedIndex);

            Main.layersView.resetList();
            Main.layersView.updateList();

            Main.view.resetCanvas();
            Main.view.setCanvas();
        });

        //Up Btn
        downBtn.setOnAction(event -> {
            int selectedIndex = Main.layersView.getCurrentIndex();
            Main.model.moveLayerDown(selectedIndex);

            Main.layersView.resetList();
            Main.layersView.updateList();

            Main.view.resetCanvas();
            Main.view.setCanvas();
        });

        visibleBtn.setOnAction(event -> {
            int selectedIndex = Main.layersView.getCurrentIndex();
            if(selectedIndex != -1){
                Layer layer = Main.model.layerListProperty().get(selectedIndex);

                if(layer.isVisible()){
                    layer.setInvisible();
                    Main.layersView.updateVisibleBtn(false);
                }
                else if(!layer.isVisible()){
                    layer.setVisible();
                    Main.layersView.updateVisibleBtn(true);
                }

                Main.view.resetCanvas();
                Main.view.setCanvas();
            }
        });
    }
}
