package com.PI.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class textLayoutController {


	@FXML
	private TextArea textArea,errors;
	
	@FXML
	private Button minus,plus;
	
	@FXML
	public void initialize(){
		minus.setOnAction((event)-> minusHandler());
		plus.setOnAction((event)-> plusHandler());
	}

	private void plusHandler() {
		double i = (textArea.getFont()).getSize();
		i = i+2;
		textArea.setFont(Font.font(i));
	}

	private void minusHandler() {
		double i = (textArea.getFont()).getSize();
		i = i-2;
		textArea.setFont(Font.font(i));
		
			}

	public TextArea getTextArea(){
		return textArea;
	}
	public TextArea getErrorsArea(){
		return errors;
	}
}
