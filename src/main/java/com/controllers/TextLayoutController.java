package com.controllers;


import com.programmer.connect.LoadSaveData;
import com.programmer.connect.Postman;
import com.programmer.load.Compiler;
import com.programmer.load.CodeList;
import com.programmer.logging.MyLogger;
import com.programmer.orders.Order;
import com.programmer.path.ProjectPath;
import javafx.application.Platform;
import javafx.beans.value.ObservableDoubleValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class TextLayoutController {


	@FXML
	private TextArea textArea,errors;

	@FXML
	private BorderPane programmingPane;

	private FileChooserLayoutController fclc;

	private GridPane fileChooserPane;

	private CompileHandler compileHandler;

	private Runnable backToStart;

	private ArrayList<Order> orderList;
	private PopupWindow keyboard;

    private Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@FXML
	public void initialize(){

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(
		        "/fxml/programmer/file_chooser_layout.fxml"));
		try {
			MyLogger.setup(errors);
			fileChooserPane = loader.load();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		fclc = loader.getController();
		textArea.getParent().getScene();
		fclc.setTextField(textArea);
		fclc.setBackToPrevious(() -> {
			BorderPane root = (BorderPane)fileChooserPane.getParent();
			root.setCenter(programmingPane);
			errors.clear();
		});

		textArea.focusedProperty().addListener((ob,b,b1) -> change());
	}

	@FXML
	private void plusHandler() {
		double i = (textArea.getFont()).getSize();
		i = i+2;
		textArea.setFont(Font.font(i));
	}

	@FXML
	private void minusHandler() {
		double i = (textArea.getFont()).getSize();
		i = i-2;
		textArea.setFont(Font.font(i));
	}

	@FXML
    private void saveButtonHandle(){
	    try {
	        if(ProjectPath.getProjectPath().getLastFilename() == null)
	            throw new IOException();
            LoadSaveData.saveProject(new File(ProjectPath.getProjectPath().getFullPath()),
                    textArea.getText());
        }
        catch(IOException e){
	        LOGGER.info("Cannot save file, try \"Save as...\"");
        };
    }

    @FXML
    private void newButtonHandle(){
	    ProjectPath.getProjectPath().cleanPath();
	    clear();
    }

	@FXML
	private void saveAsButtonHandle(){
		BorderPane root = (BorderPane) programmingPane.getParent();
		root.setCenter(fileChooserPane);
		fclc.setActualOperation(Operation.SAVE);
	}

	@FXML
	private void loadButtonHandle(){
		BorderPane root = (BorderPane) programmingPane.getParent();
		root.setCenter(fileChooserPane);
		fclc.setActualOperation(Operation.LOAD);
	}

	@FXML
	private void compileProgram(){
		errors.clear();
		compileHandler = new CompileHandler(new Compiler(orderList));
		compileHandler.handle(textArea.getText());

	}

	public void setOrderList(ArrayList<Order> orderList){
		this.orderList = orderList;
	}

	public void clear(){
	    textArea.clear();
	    errors.clear();
    }

	@FXML
	private void sendToPlc()
    {   CodeList codeList = compileHandler.getFullCode();
		Postman postman = Postman.getInstance();
		postman.sendCode(codeList);
	}

	@FXML
	private void backButtonHandle(){
		backToStart.run();
		clear();
		ProjectPath.getProjectPath().cleanPath();
	}

	public void setBackButtonHandle(Runnable r){
		backToStart = r;
	}
	public void setProject(String sb){
	    textArea.setText(sb);
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

	private void change() {
		try{
			if(keyboard==null){
				keyboard = getPopupWindow();
				double textAreaHeight = textArea.getHeight();
				double keyboardDown = keyboard.getY();

				keyboard.yProperty().addListener(obs->{
					Platform.runLater(()->{
						// x = <0 -> close; 243 - > open>
						double x = keyboardDown - keyboard.getY();
						double y = x -100;
						if(x < 100) {
							errors.setMaxHeight(100 - x);
							textArea.setMaxHeight(textAreaHeight);
						}
						else {
							textArea.setMaxHeight(textAreaHeight - y);
							errors.setMaxHeight(0);
						}
					});

				});
			}
		}
		catch(NullPointerException npe){};
	}


}

