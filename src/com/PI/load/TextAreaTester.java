package com.PI.load;

import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class TextAreaTester implements Runnable{

	private MenuItem kompiluj;
	private TextArea textArea;
	private BorderPane root;
	
	public TextAreaTester(MenuItem kompiluj, TextArea textArea, BorderPane root){
		this.kompiluj = kompiluj;
		this.textArea = textArea;
		this.root = root;
		}
	
	@Override
	public void run() {
		while(root.getCenter() ==  textArea.getParent()){
			try{
			if(textArea.getText().isEmpty() == true){
				kompiluj.setDisable(true);
			}
			else{ kompiluj.setDisable(false);
			}
			Thread.sleep(50);
			}
			catch(NullPointerException e){
				System.out.println("NULL");
				break;
			}
			catch(InterruptedException ioe){
				System.out.println("int");
				break;
			}
		}
	}
}
