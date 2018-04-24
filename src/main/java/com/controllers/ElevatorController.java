package com.controllers;

import com.panel.transaction.BufferManager;
import com.panel.transaction.MyIntegerProperty;
import com.panel.transaction.PropertyManager;
import com.panel.view.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.nio.Buffer;
import java.util.ArrayList;

public class ElevatorController {

    @FXML
    ArrayList<ToggleButton> buttons;

    @FXML
    ArrayList<Rectangle> cabinPosition;

    @FXML
    ArrayList<Rectangle> controls;

    @FXML
    private ToggleButton serviceButton;

    @FXML
    private ArrayList<Circle> requests;

    private Runnable backToStart;
    private ViewManager vw;

    @FXML
    private void initialize(){
        for(Rectangle r : controls) {
            r.getStyleClass().add("controls");
        }
        for(ToggleButton button : buttons) {
                button.getStyleClass().add("toggleButtons");
        }
        serviceButton.getStyleClass().add("serviceToggleButton");
    }

    @FXML
    private void closeApp(){
        Platform.exit();
    }

    @FXML
    private void backToStart(){
        backToStart.run();
    }

    public void setBackToStart(Runnable r){
        backToStart = r;
    }

    public void setViewManager(ViewManager vw){
        this.vw = vw;
    }

    public void setActionsForIndicators(){
        recolorShapes(requests, Color.RED, Color.GRAY);
        recolorShapes(cabinPosition, Color.RED, Color.GREEN);
        recolorShapes(controls, Color.BLUE, Color.LIGHTSTEELBLUE);
    }

    private void recolorShapes(ArrayList<? extends Shape> shapes,
                               Paint colorOn,
                               Paint colorOff){
        for(Shape s : shapes){
            String id = s.getId();
            MyIntegerProperty property = vw.getPropertyManager().getProperty(id);
            if(property != null){
                property.addListener((obs, oldVal, newVal) -> {
                    if((int) newVal == 1)
                        s.setFill(colorOn);
                    else
                        s.setFill(colorOff);
                });
            }
        }
    }

    public void setActionsForToggleButtons(){

        serviceButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            BufferManager bm = vw.getBufferManager();
            if(newVal == true){
                buttons.forEach(button -> button.setDisable(false));
            }
            else{
                buttons.forEach(button -> {
                    button.setSelected(false);
                    button.setDisable(true);
                });
            }
            bm.setToggleBuffer(serviceButton.getId(), booleanToIntCast(newVal));
        });

        for(ToggleButton button : buttons){
            button.setDisable(true);
            button.setSelected(false);
            button.selectedProperty().addListener((obs, oldVal, newVal) -> {
                BufferManager bm = vw.getBufferManager();
                bm.setToggleBuffer(button.getId(), booleanToIntCast(newVal));
            });

        }
    }

    private int booleanToIntCast(boolean bool){
        if(bool)
            return 1;
        else
            return 0;
    }
}
