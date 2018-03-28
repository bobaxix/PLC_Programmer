package com.controllers;


import com.programmer.connect.Postman;
import com.programmer.load.Compiler;
import com.programmer.load.CodeList;
import com.programmer.logging.MyLogger;
import com.programmer.orders.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

public class textLayoutController {


	@FXML
	private TextArea textArea,errors;

	@FXML
	private BorderPane programmingPane;


	private FileChooserLayoutController fclc;

	private GridPane fileChooserPane;

	private CodeList codeList;

	private Runnable backToStart;

	private ArrayList<Order> orderList;

	
	@FXML
	public void initialize(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/programmer/file_chooser_layout.fxml"));
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
		CompileHandler compileHandler = new CompileHandler(new Compiler(orderList));
		codeList = compileHandler.handle(textArea.getText());
	}

	public  void setOrderList(ArrayList<Order> orderList){
		this.orderList = orderList;
	}

	@FXML
	private void sendToPlc(){
		Postman postman = Postman.getInstance();
		postman.sendCode(codeList);
	}

	@FXML
	private void backButtonHandle(){
		backToStart.run();
	}

	public void setBackButtonHandle(Runnable r){
		backToStart = r;
	}
	public void setProject(StringBuilder sb){
		textArea.setText(sb.toString());
	}
}

