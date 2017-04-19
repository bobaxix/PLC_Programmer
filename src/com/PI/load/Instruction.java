package com.PI.load;

public class Instruction {

	private String command;
	private String operand;
	
	public Instruction(String command, String operand){
		this.command = command;
		this.operand = operand;
	}
	
	public String getCommand(){
		return command;
	}
	
	public String getOperand(){
		return operand;
	}
}
