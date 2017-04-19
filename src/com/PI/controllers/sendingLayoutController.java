package com.PI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class sendingLayoutController {

	private TextArea textArea;
	private MenuItem send;
	
	@FXML
	private  Button ok;
	
	@FXML
	public void initialize(){
			ok.setOnAction((event) -> {
			Stage stage = (Stage) ok.getScene().getWindow();
			stage.close();
			textArea.setDisable(false);
			send.setDisable(false);
		});
	}
	
	
	public void getTextArea(TextArea textArea){
		this.textArea= textArea;
	}
	
	public void getWyslij(MenuItem send){
		this.send= send;
	}
	
	
}

