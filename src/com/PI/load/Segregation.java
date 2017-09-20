package com.PI.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import com.PI.load.Instructions.*;
import com.PI.load.Instructions.Instruction;
import javafx.scene.control.TextArea;

public class Segregation {

	private ArrayList<Order> orderList;

	public Segregation(ArrayList<Order> orderList){
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

	public Instruction getInstructionObject(String line, int lineNumber){

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
		instruction.setParameters(orderName, orderCode, operand, lineNumber);
		return instruction;
	}

	private Instruction getInstructionObjectByType(String type){
		switch(type){
			case("IOM INSTRUCTIONS"):
				return new InOutMem();
			case("NAO"):
				return new NoArgsOp();
			case("JUMP CONTROL"):
				return new JumpControl();
			case("OP WITH CONSTANS"):
				return new WithConst();
			case("APB"):
				return new Apb();
			default:
				return new Label();
		}
	}
}
