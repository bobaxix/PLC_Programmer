package com.PI;
	
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import com.PI.controllers.rootLayoutController;
import com.PI.load.Order;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
		
	private Stage primaryStage;
	private BorderPane root;
	public static GpioPinDigitalOutput reset;
	private final Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		final GpioController gpio = GpioFactory.getInstance();
		reset = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Reset", PinState.HIGH);
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
			controller.setCommandsList(load());
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
	
	private Object load(){
		Object o = null;
		try{
		FileInputStream fis = new FileInputStream("commands.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		o = ois.readObject();
		ois.close();
		}
		catch(Exception e){
			System.out.println("jestem w wyj¹tku ³adowania");
			 o = generateDefaultList();
		};
		return o;
	}
	
	private HashMap<String,Order> generateDefaultList(){
		HashMap<String,Order> map = new HashMap<String,Order>();
		
		map.put("ANDN",(new Order("ANDN", (byte) 17,"IOM INSTRUCTIONS")));
		map.put("AND",(new Order("AND", (byte) 1,"IOM INSTRUCTIONS")));
		map.put("OR",(new Order("OR", (byte) 2,"IOM INSTRUCTIONS")));
		map.put("ORN",(new Order("ORN", (byte) 18,"IOM INSTRUCTIONS")));
		map.put("XOR",(new Order("XOR", (byte) 3,"IOM INSTRUCTIONS")));
		map.put("XORN",(new Order("XORN", (byte) 19,"IOM INSTRUCTIONS")));
		map.put("LD",(new Order("LD", (byte) 0,"IOM INSTRUCTIONS")));
		map.put("LDN",(new Order("LDN", (byte) 16,"IOM INSTRUCTIONS")));
		
		map.put("ST",(new Order("ST", (byte) 4,"IOM INSTRUCTIONS")));
		map.put("STN",(new Order("STN", (byte) 20,"IOM INSTRUCTIONS")));
		
		map.put("JMP",(new Order("JMP", (byte) 12,"JUMP CONTROL")));
		map.put("JMPC",(new Order("JMPC", (byte) 13,"JUMP CONTROL")));
		map.put("JMPCN",(new Order("JMPCN", (byte) 29,"JUMP CONTROL")));
		
		map.put("ANDI",(new Order("ANDI", (byte) 8,"OP WITH CONST")));
		map.put("ORI",(new Order("ORI", (byte) 9,"OP WITH CONST")));
		map.put("XORI",(new Order("XORI", (byte) 10,"OP WITH CONST")));
		map.put("LDI",(new Order("LDI", (byte) 7,"OP WITH CONST")));
		
		map.put("NOT",(new Order("NOT", (byte) 11,"NAO")));
		map.put("NOP", new Order("NOP",(byte) 24,"NAO"));		
		
		map.put("R",(new Order("R", (byte) 5,"IOM INSTRUCTIONS")));
		map.put("S",(new Order("S", (byte) 21,"IOM INSTRUCTIONS")));
		map.put("EQU",(new Order("EQU", (byte) 23,"IOM INSTRUCTIONS")));
				
		map.put("F_TRIG",(new Order("F_TRIG", (byte) 6,"IOM INSTRUCTIONS")));
		map.put("R_TRIG",(new Order("R_TRIG", (byte) 22,"IOM INSTRUCTIONS")));
		
		map.put("APB_RD",(new Order("APB_RD", (byte) 14,"APB")));
		map.put("APB_WR",(new Order("APB_WR", (byte) 30,"APB")));
		
		return map;
	}
	
	public static void main(String[] args) {  
		launch(args); 
			}
	}
