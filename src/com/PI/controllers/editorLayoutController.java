package com.PI.controllers;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.PI.load.Order;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class editorLayoutController {
		
	private Map<String, Order> commandList;
	private ObservableList<Command> commandObservableList = FXCollections.observableArrayList();
	private ObservableList<String> choiceType = FXCollections.observableArrayList("IOM INSTRUCTIONS",
			"OP WITH CONST","NAO","JUMP CONTROL","APB");
	
	@FXML
	private TableColumn<Command,String> mnemonics,codes,types;
	        
	@FXML
	private TableView<Command> table;
	
	@FXML
	private Button save,delete,add;
	
	@FXML
	private TextField mnemonic, code;
	
	@FXML
	private ChoiceBox<String> type;
	
	@FXML
	public void initialize(){
		
		save.setOnAction((event) -> setMap());
		add.setOnAction((event)-> dodajHandler());
		delete.setOnAction((event) -> usunHandler());
		table.setEditable(true);
		mnemonics.setCellValueFactory(new PropertyValueFactory<Command,String>("key"));
		codes.setCellValueFactory(new PropertyValueFactory<Command,String>("value"));
		types.setCellValueFactory(new PropertyValueFactory<Command,String>("type"));
		type.setItems(choiceType);
		mnemonic.focusedProperty().addListener(l -> mnemonic.setStyle("-fx-text-inner-color: black;"));
		code.focusedProperty().addListener(l -> code.setStyle("-fx-text-inner-color: black;"));
		table.setItems(commandObservableList);
		table.setId("tabela");
		
		
	}
	
	private void usunHandler() {
		commandObservableList.remove(table.getSelectionModel().getSelectedItem());
		table.getSelectionModel().select(0);
	}

	private void dodajHandler() {
		String k = code.getText();
		String m = mnemonic.getText();
		if((!(m.equals(""))) && (!(k.equals("")))== true && type.getValue() != null){
			int error = 0;

			for(Command c : commandObservableList){
				
				if(c.getValue().equals(k)){
					System.out.println("Kod siê powtarza!");
					code.setStyle("-fx-text-inner-color: red;");
					error = -1;
				}
				
				if(c.getKey().equals(m)){
					System.out.println("Mnemonik siê powtarza!");
					mnemonic.setStyle("-fx-text-inner-color: red;");
					error = -1;
				}
			}
			
			int l = k.length();
			
			if(l > 2)
			{
				error = - 1;
				code.setStyle("-fx-text-inner-color: red;");
				System.out.println("String do dupy");
			}
			else {
				char[] x = k.toUpperCase().toCharArray();
				for(int i =0; i<l; i++){
					if((x[i] < '0' || x[i] > '9') && (x[i] < 'A' || x[i] > 'F')){
						code.setStyle("-fx-text-inner-color: red;");
						error = -1;
						System.out.println("B³êdny zakres");
					}
				}
			}
				
			
			if(error != -1){
				commandObservableList.add(new Command(mnemonic.getText(),code.getText().toUpperCase(),
						type.getValue()));
				mnemonic.clear();
				code.clear();
				type.setValue(null);
				table.getSelectionModel().select(commandObservableList.size()-1);
			}
		}
		else{
			mnemonic.setStyle("-fx-text-inner-color: red;");
			code.setStyle("-fx-text-inner-color: red;");
		}
	}
	

	public void getMap(Map<String, Order> commandList){
		this.commandList = commandList;
		for(Map.Entry<String,Order> mapp : commandList.entrySet()){
			Order o = mapp.getValue();
			Command m = new Command(o.getMnemonic(),o.getCode(),o.getType());
			commandObservableList.add(m);
		}			
	}
	
	private void setMap(){
		commandList.clear();
		for(Command mapa : commandObservableList){
			System.out.println(mapa.getKey());
			Order o = new Order(mapa.getKey(),converter(mapa.getValue()), mapa.getType());
			commandList.put(mapa.getKey(), o);
		}
		
		try{
		FileOutputStream fos = new FileOutputStream("commands.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(commandList);
		oos.close();
		}
		catch(Exception e){
			System.out.println(e.getClass());
		};
	}
	


	public class Command{
		private final SimpleStringProperty key;
		private final SimpleStringProperty value;
		private final SimpleStringProperty type;
		
		private Command(String key,Byte value, String type){
			this.key = new SimpleStringProperty(key);
			this.value = new SimpleStringProperty((String.format("%02X", value & 0xFF)));
			this.type = new SimpleStringProperty(type);
		}
		
		private Command(String key, String value, String type){
			this.key = new SimpleStringProperty(key);
			this.value= new SimpleStringProperty(value);
			this.type = new SimpleStringProperty(type);
		}
		
		public String getKey(){
			return key.get();
		}
		
		public String getValue(){
			return value.get();
		}
		
		public String getType(){
			return type.get();
		}
		
	}
	
	private byte converter(String s){
		int b = 0;
		int result = 0;
		System.out.println(s);
		for(int x = 0; x<s.length(); x++){
		char c = s.charAt(x);
		System.out.println(c);
		switch(c){
			case('0'):{
				b = 0;
				break;
			}
			case('1'):{
				b = 1;
				break;
			}
			case('2'):{
				b = 2;
				break;
			}
			case('3'):{
				b = 3;
				break;
			}
			case('4'):{
				b = 4;
				break;
			}
			case('5'):{
				b = 5;
				break;
			}
			case('6'):{
				b = 6;
				break;
			}
			case('7'):{
				b = 7;
				break;
			}
			case('8'):{
				b = 8;
				break;
			}
			case('9'):{
				b = 9;
				break;
			}
			case('A'):{
				b = 10;
				break;
			}
			case('B'):{
				b = 11;
				break;
			}
			case('C'):{
				b = 12;
				break;
			}
			case('D'):{
				b = 13;
				break;
			}
			case('E'):{
				b = 14;
				break;
			}
			case('F'):{
				b = 15;
				break;
			}
			default:{
				
				break;
			}
		}
			System.out.println(b);
			result = result<<4;
			result += b;
		}
		System.out.println(result);
		return (byte) result;
		}
	}

