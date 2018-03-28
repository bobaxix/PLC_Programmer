package com.app;

import java.io.IOException;
import java.util.ArrayList;

import com.controllers.RootLayout;
import com.orders.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

	public static ArrayList<Order> ordersList;
	private Stage primaryStage;
	private GridPane gridPane;
	private final Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		try{
			OrdersLoader loader = OrdersLoader.getInstance();
			ordersList = loader.loadOrdersFromTxtFile();
		}
		catch(IOException e){
			System.out.println("Err");
			e.printStackTrace();
		}

		stageStart();
	}

	@Override
	public void stop(){
		try{
			OrdersLoader loader = OrdersLoader.getInstance();
			loader.saveOrdersToTxtFile(ordersList);
			System.out.println("Stop app");
		}
		catch(IOException e){

		}

	}
	
	private void stageStart(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/root_layout.fxml"));
			BorderPane borderPane =  loader.load();
			borderPane.setFocusTraversable(false);
			RootLayout controller = loader.getController();
			controller.getRoot(borderPane);
			controller.setCommandsList(ordersList);

			Scene scene = new Scene(borderPane);
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
