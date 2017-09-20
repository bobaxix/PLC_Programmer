package com.PI.load;

import com.PI.load.Instructions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import com.PI.load.Instructions.Instruction;

public class Compilator {

	int[] memoryBlock = {0x00000002,0x00000007};
	int[] inputBlock = {0x00000000,0x00000000};
	int[] outputBlock = {0x00000001,0x00000001};
	int[] timerAdress = {0x0000800,0x00000FFF};
	int[] counterAdress = {0x0000000,0x000007ff};
	int gpi = 0x00001000;
	int gpo = 0x00001001;


	int max = 2147483647;
	int min = 0;
	int error = -1;

	ArrayList<Order> orderList;

	public Compilator(ArrayList<Order> orderList){
		this.orderList = orderList;
	}

	public ArrayList<Integer> compile(String code) throws IOException{

		Segregation segregation = new Segregation(orderList);

		ArrayList<Instruction> codeLinesInInstructionList = makeCodeLineList(segregation, code);

		ArrayList<Instruction> instructions = getFromCodeLinesInstructions(codeLinesInInstructionList);
		ArrayList<Instruction> labels = getFromCodeLinesLabels(codeLinesInInstructionList);
		boolean result = checkIfLabelsAreRepeated(labels);

		ArrayList<Integer> codeList = generateBinaryCode(instructions, labels);

		return codeList;
	}

	private ArrayList<Instruction> makeCodeLineList(Segregation segregation, String code) throws IOException{

		StringReader sr = new StringReader(code);
		BufferedReader br = new BufferedReader(sr);
		ArrayList<Instruction> instructions = new ArrayList<>();
		String line;
		int instructionNumber = 0;

		while((line = br.readLine()) != null){

			Instruction instruction = segregation.getInstructionObject(line, instructionNumber++);
			instructions.add(instruction);
		}

		return instructions;
	}

	private ArrayList<Instruction> getFromCodeLinesInstructions(ArrayList<Instruction> codeLines){
		ArrayList<Instruction> instructions = new ArrayList<>();

		for(Instruction instruction : codeLines){
			if(!instruction.isLabel())
				instructions.add(instruction);
		}
		return instructions;
	}

	private ArrayList<Instruction> getFromCodeLinesLabels(ArrayList<Instruction> codeLines){
		ArrayList<Instruction> labels = new ArrayList<>();

		for(Instruction instruction : codeLines){
			if(instruction.isLabel())
				labels.add(instruction);
		}
		return labels;
	}

	private boolean checkIfLabelsAreRepeated(ArrayList<Instruction> labels){

		boolean result = false;

		int labelsListSize = labels.size();
		for(int i =0; i< labelsListSize - 1; i++){
			String checkedLabel = ((Label) labels.get(i)).getLabel();
			for(int j = i+1; j< labelsListSize; j++ ){
				String comparedLabel = ((Label) labels.get(j)).getLabel();
				if(!checkedLabel.equals(comparedLabel))
					result = true;
			}
		}

		return result;
	}

	private ArrayList<Integer> generateBinaryCode(ArrayList<Instruction> instructions,
												  ArrayList<Instruction> labels){

		ArrayList<Integer> codeList = new ArrayList<>();
		for(Instruction instruction : instructions){

			if(instruction.isJump())
				((JumpControl) instruction).whereShouldJump(labels);
			codeList.addAll(instruction.generateCodeForInstruction());
		}
		return codeList;
	}
}
