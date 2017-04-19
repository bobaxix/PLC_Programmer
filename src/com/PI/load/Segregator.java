package com.PI.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javafx.scene.control.TextArea;

public class Segregator {
	private BufferedReader br;
	private String code;
	private int instructionNumber;
	
	public Segregator(TextArea textArea){
		code = textArea.getText();
		instructionNumber = getNumberOfLines();
	}
	
	private int getNumberOfLines(){
		int i = 0;
		br = new BufferedReader(new StringReader(code));
		while(true){
			String codeLine;
			try{
				codeLine = br.readLine();
			}
			catch(IOException ioe){
				System.out.println("B³¹d strumienia");
				break;
			}
			if(codeLine == null) break;
			else i++;
			
		}		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return i;
	}
	
	public int getInstructionNumber(){
		return instructionNumber;
	}
	
	public Instruction[] segregate(){

		Instruction[] instructions = new Instruction[instructionNumber];
		String command;
		String operand;

		br = new BufferedReader(new StringReader(code));
		
		for(int i =0; i<instructionNumber; i++){
			
			String codeLine;
			try{
				codeLine = br.readLine();
			}
			catch(IOException ioe){
				System.out.println("B³¹d strumienia");
				break;
			}
			
			try{
				codeLine = (codeLine.split(";"))[0];
			}
			catch(ArrayIndexOutOfBoundsException e) {
				break;
			}
			
			try{
				command = codeLine.substring(0,codeLine.indexOf(" ")).trim();
				operand = codeLine.substring(codeLine.indexOf(" ")).trim();
				instructions[i] = new Instruction(command,operand);
			}
			catch(StringIndexOutOfBoundsException sioe){
				
				try{
					command = codeLine.substring(0,codeLine.indexOf("	")).trim();
					operand = codeLine.substring(codeLine.indexOf("	")).trim();
					instructions[i] = new Instruction(command,operand);
				}
				catch(StringIndexOutOfBoundsException sio){
					command = codeLine;
					instructions[i] = new Instruction(command,null);
				}
			}

			
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return instructions;
	}

}
