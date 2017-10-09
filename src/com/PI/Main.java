package com.PI;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.PI.load.Order;
import com.PI.load.OrdersLoader;
//import com.pi4j.io.gpio.GpioController;
//import com.pi4j.io.gpio.GpioFactory;
//import com.pi4j.io.gpio.GpioPinDigitalOutput;
//import com.pi4j.io.gpio.PinState;
//import com.pi4j.io.gpio.RaspiPin;

import com.PI.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

	ArrayList<Order> ordersList;
	private Stage primaryStage;
	private BorderPane root;
	//public static GpioPinDigitalOutput reset;
	private final Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		try{
			ordersList = new OrdersLoader().loadOrdersFromTxtFile();
		}
		catch(IOException e){
			System.out.println("Err");
			e.printStackTrace();
		}

		//final GpioController gpio = GpioFactory.getInstance();
		//reset = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Reset", PinState.HIGH);
		stageStart();
	}
	
	private void stageStart(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("controllers/rootLayout.fxml"));
			root = (BorderPane) loader.load();
			rootLayoutController controller = loader.getController();
			controller.getRoot(root);
			controller.setStage(primaryStage);
			controller.setCommandsList(ordersList);
			Scene scene = new Scene(root);
			primaryStage.setX(visualBounds.getMinX());
		    primaryStage.setY(visualBounds.getMinY());
		    primaryStage.setWidth(visualBounds.getWidth());
		    primaryStage.setHeight(visualBounds.getHeight());
			primaryStage.setScene(scene);
			primaryStage.setFullScreen(true);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		launch(args);
	}
}
