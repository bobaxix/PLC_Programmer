package com.PI.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import com.PI.load.Instructions.InOutMem;
import com.PI.load.Instructions.Instruction;
import com.PI.load.Instructions.WithConst;
import javafx.scene.control.TextArea;

public class Segregator {

	private ArrayList<Order> orderList;

	public Segregator(ArrayList<Order> orderList){
		this.orderList = orderList;
	}

	private String[] parseInstructionLine(String line){

		String codeLine = line.split(";")[0];
		String[] parsedCodeLine = codeLine.split(" ");

		String orderName = parsedCodeLine[0].trim();
		orderName = orderName.split(":")[0];

		String operand;

		try{
			operand = parsedCodeLine[1].trim();
		}
		catch(ArrayIndexOutOfBoundsException e){
			operand = null;
		}

		String[] result = {orderName, operand};

		return result;
	}

	public Instruction getInstructionObject(String line){

		String[] parsedCodeLine = parseInstructionLine(line);
		String orderName = parsedCodeLine[0];
		String operand = parsedCodeLine[1];
		String type = "";
		int orderCode = 0;

		for(Order order : orderList){

			String mnemonic = order.getMnemonic();
			if(mnemonic.equals(orderName)){
				type = order.getType();
				orderCode = order.getCode();
			}
		}

		Instruction instruction = getInstructionObjectByType(type);
		instruction.setParameters(operand, orderCode);
		return instruction;
	}

	private Instruction getInstructionObjectByType(String type){
		switch(type){
			case("IOM INSTRUCTIONS"):{
				return new InOutMem();
			}
			case("NOP"):{

			}
			case("JUMP CONTROL"):{

			}
			case("OP WITH CONSTANS"):{
				return new WithConst();
			}
			default:{
				// ETYKIETA
				return null;
			}
		}
	}

	/*private BufferedReader br;
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
				System.out.println("B??d strumienia");
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
				System.out.println("B??d strumienia");
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
	} */

}
