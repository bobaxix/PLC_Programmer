package com.PI.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.PI.MVC.controllers.CompileHandler;
import com.PI.load.Compilator;
import com.PI.load.Order;
import com.PI.load.Postman;
import com.PI.load.Segregation;
import com.PI.load.TextAreaTester;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class rootLayoutController {

	private BorderPane root;
	private TextArea textArea,errors;
	private ArrayList<Order> ordersList;
	private ArrayList<Integer> fullCode;
	private int error = -1;
	private CompileHandler compileHandler;

	@FXML
	private MenuItem zamknij,nowy,kompiluj,edytuj,wyslij,zapiszPlik,wczytajPlik;

	private Runnable checkTextArea;
	private Thread tester;

	private PopupWindow keyboard;

	private final Rectangle2D bounds = Screen.getPrimary().getBounds();

	private Stage stage;

	private BorderPane textPane;

	private AnchorPane pane;
	private editorLayoutController editorLayoutController;
	private int load = 1;
	private int save = 2;


	@FXML
	private Menu program;

	@FXML
	private void initialize(){
		zamknij.setOnAction((event) -> System.exit(0));
		nowy.setOnAction((event)-> textLayoutInit());

		edytuj.setOnAction((event)-> {
		    root.setCenter(pane);
        });

		wyslij.setOnAction((event) -> {

			System.out.println("Send");
			ArrayList<Integer> code = compileHandler.getFullCode();
			wyswietlanieBajtow(code);

		});
		zapiszPlik.setOnAction((event) -> zapiszwczytaj(save));
		wczytajPlik.setOnAction((event)-> zapiszwczytaj(load));
		program.setVisible(false);
		textLayoutGenerator();
		editorLayoutGenerator();
	}

	/*---------------------------------------
	 * HANDLER ZAPISYWANIA/WCZYTYWANIA PLIKU
	 ---------------------------------------*/

	private void zapiszwczytaj(int mode){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("saveloadLayout.fxml"));
		AnchorPane root;
		try {
			root = (AnchorPane) loader.load();
			Stage secondaryStage = new Stage();
			saveloadLayoutController controller = loader.getController();
			controller.getTextArea(textArea);
			controller.setMode(mode);
			controller.setMenuItems(zapiszPlik, wczytajPlik);
			secondaryStage.setScene(new Scene(root));
			secondaryStage.centerOnScreen();
			secondaryStage.setResizable(false);
			secondaryStage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void textLayoutInit(){

		textArea.setEditable(true);
		textArea.clear();
		errors.clear();
		root.setCenter(textPane);
		program.setVisible(true);
		wyslij.setDisable(false);
		checkTextArea = new  TextAreaTester(kompiluj, textArea, root);
		tester = new Thread(checkTextArea);
		tester.start();
	}

	private void textLayoutGenerator(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("textLayout.fxml"));
		try {
			textPane = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textLayoutController controller = loader.getController();
		textArea = controller.getTextArea();
		errors = controller.getErrorsArea();
		textArea.focusedProperty().addListener((ob,b,b1) -> change());


	}

	private void editorLayoutGenerator(){


		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("editorLayout.fxml"));
		try {
			pane = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editorLayoutController = loader.getController();

	}

	private synchronized void sendToPLC() {
	    System.out.println("Send");
	    ArrayList<Integer> code = compileHandler.getFullCode();

	    wyswietlanieBajtow(code);
	}

	private void change() {
		try{
	        if(keyboard==null){
	            keyboard=getPopupWindow();
	            double off = keyboard.getY();
	            keyboard.yProperty().addListener(obs->{
	                Platform.runLater(()->{
	                	errors.setPrefHeight(0);
	                	if(off != keyboard.getY()){
	                		errors.setPrefHeight(0);
	                	}
	                	else {
	                		errors.setPrefHeight(100);
	                		errors.requestFocus();
	                	}
	                    double y = bounds.getHeight()-(off-keyboard.getY());
	                    stage.setHeight(y);
	                });

	            });
	        }
		}
		catch(NullPointerException npe){};
	}


	private void wyswietlanieBajtow(ArrayList<Integer> arg){
		for(Integer x : arg){
			for(int i = 31; i>=0; i--){
				if((((x)>>i) & 0x01) == 0)
				System.out.print("0");
				else System.out.print("1");
				if(i==24) System.out.print(" ");
			}

			System.out.print("\n");
		}
	}



	public void getRoot(BorderPane root){
		this.root = root;
	}

	public void setStage(Stage stage){
		this.stage=stage;
	}

	@SuppressWarnings("unchecked")
	public void setCommandsList(ArrayList<Order> ordersList){
	    this.ordersList =  ordersList;
        compileHandler = new CompileHandler(ordersList, textArea, errors);
        kompiluj.setOnAction(compileHandler);
	}

	private PopupWindow getPopupWindow() {

		try{
	    @SuppressWarnings("deprecation")
	    final Iterator<Window> windows = Window.impl_getWindows();

	    while (windows.hasNext()) {
	        final Window window = windows.next();
	        if (window instanceof PopupWindow) {
	            if(window.getScene()!=null && window.getScene().getRoot()!=null){
	                Parent root = window.getScene().getRoot();
	                if(root.getChildrenUnmodifiable().size()>0){
	                    Node popup = root.getChildrenUnmodifiable().get(0);
	                    if(popup.lookup(".fxvk")!=null){
	                        return (PopupWindow)window;
	                    }
	                }
	            }
	        }
	    }
		}
	        catch(NullPointerException e){}
	            return null;
	}



}
