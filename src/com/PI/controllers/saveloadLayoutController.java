package com.PI.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class saveloadLayoutController {

	private static final int load = 1;
	private static final int save = 2;
	private TextArea textArea;
	private int mode;
	private int state;
	private MenuItem zap,wcz;
	@FXML
	private TextField nazwa;
	@FXML
	private Button anuluj,zapisz;
	@FXML
	private Label error;
	
	@FXML
	private void initialize(){
		nazwa.setFocusTraversable(false);
		nazwa.focusedProperty().addListener(l -> error.setVisible(false));
		anuluj.requestFocus();
		state = 0;
		error.setVisible(false);
		anuluj.setOnAction((event)-> {
			Stage stage = (Stage) anuluj.getScene().getWindow();
			stage.close();
			textArea.setDisable(false);
			zap.setDisable(false);
			wcz.setDisable(false);
		});
		
		zapisz.setOnAction((event)->{
			switch(mode){
			case(save):{
				zapisz();
				break;
			}
			case(load):{
				wczytaj();
				break;
			}
			}
		});
	}
	
	private void wczytaj() {
		try {
			StringWriter sw = new StringWriter();
			BufferedWriter bw = new BufferedWriter(sw);
			BufferedReader br = new BufferedReader(new FileReader(nazwa.getText()+".txt"));
			
			while(true){
				try{
					bw.write(br.readLine());
					bw.newLine();
				}
				catch(IOException e){
					break;
				}
				catch(NullPointerException n){
					try {
						bw.flush();
						textArea.setText(sw.toString());
						br.close();
						bw.close();
						textArea.setDisable(false);
						zap.setDisable(false);
						wcz.setDisable(false);
						Stage stage = (Stage) anuluj.getScene().getWindow();
						stage.close();
						break;
					} catch (IOException e) {
						break;
					}						
				}
			}
		} catch (FileNotFoundException e) {
			error.setVisible(true);
		}
	}
		
	public void getTextArea(TextArea textArea){
		this.textArea=textArea;
		(this.textArea).setDisable(true);
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public void setMenuItems(MenuItem z, MenuItem l){
		zap = z;
		wcz = l;
		zap.setDisable(true);
		wcz.setDisable(true);
	}
	
	public void zapisz(){
		try {
			if(nazwa.getText().trim().isEmpty()==false){
			File f = new File(nazwa.getText()+".txt");
			if(f.exists() == true){
				state++;
				System.out.println("plik instneije "+state);
			}
			else state = 0;
			if(state ==0 || state==2){
			BufferedWriter bw = new BufferedWriter(new FileWriter(nazwa.getText()+".txt"));
			BufferedReader br = new BufferedReader(new StringReader(textArea.getText()));
			while(true){
				try{
					bw.write((br.readLine()));
					bw.newLine();
				}
				catch(NullPointerException n){
					textArea.setDisable(false);
					br.close();
					bw.close();
					zap.setDisable(false);
					wcz.setDisable(false);
					Stage stage = (Stage) anuluj.getScene().getWindow();
					stage.close();
					break;
				}
				
			}
			}
			else{
				error.setText("Plik o podanej nazwie istnieje.\nKontynuowaæ?");
				error.setVisible(true);
			};
			}
			 else{
					error.setText("Podaj nazwê pliku");
					error.setVisible(true);
				}
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
}
	}
