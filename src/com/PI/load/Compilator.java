package com.PI.load;

import com.PI.load.Instructions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.PI.load.Instructions.Instruction;

public class Compilator {

	private boolean error = false;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private ArrayList<Order> orderList;

	public Compilator(ArrayList<Order> orderList){
		this.orderList = orderList;
	}

	public CodeList compile(String code) throws IOException{

		Segregation segregation = new Segregation(orderList);

		ArrayList<Instruction> codeLinesInInstructionList = makeCodeLineList(segregation, code);

		ArrayList<Instruction> instructions = getFromCodeLinesInstructions(codeLinesInInstructionList);
		ArrayList<Instruction> labels = getFromCodeLinesLabels(codeLinesInInstructionList);

		error = checkIfLabelsAreRepeated(labels);
        System.out.println("Before: "+error);

		if(!error)
			return generateBinaryCode(instructions, labels);

		return null;
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

			if(instruction == null)
			    error = true;
		}

		return instructions;
	}

	private ArrayList<Instruction> getFromCodeLinesInstructions(ArrayList<Instruction> codeLines){
		ArrayList<Instruction> instructions = new ArrayList<>();

		for(Instruction instruction : codeLines){
			try {
				if (!instruction.isLabel())
					instructions.add(instruction);
			}
			catch(NullPointerException e){
				//TO DO:
				//make exception
				error = true;
			}
		}
		return instructions;
	}

	private ArrayList<Instruction> getFromCodeLinesLabels(ArrayList<Instruction> codeLines){
		ArrayList<Instruction> labels = new ArrayList<>();

		for(Instruction instruction : codeLines){
			try {
				if (instruction.isLabel())
					labels.add(instruction);
			}
			catch(NullPointerException e){
				error = true;
			}
		}
		return labels;
	}

	private boolean checkIfLabelsAreRepeated(ArrayList<Instruction> labels){

		int labelsListSize = labels.size();
		for(int i =0; i< labelsListSize - 1; i++){
			String checkedLabel = ((Label) labels.get(i)).getLabel();
			for(int j = i+1; j< labelsListSize; j++ ){
				String comparedLabel = ((Label) labels.get(j)).getLabel();
                if(checkedLabel.equals(comparedLabel)) {
                    LOGGER.warning("Repeated labels in lines " + labels.get(i).getInstructionLineNumber() + "" +
                            " and " + labels.get(j).getInstructionLineNumber());
                    return true;
                }
			}
		}

		return error;
	}

	private CodeList generateBinaryCode(ArrayList<Instruction> instructions,
												  ArrayList<Instruction> labels){

		CodeList codeList = new CodeList();
		for(Instruction instruction : instructions){

			if(instruction.isJump())
				((JumpControl) instruction).whereShouldJump(labels);

			ArrayList<Integer> codeLine = instruction.generateCodeForInstruction();
            if(!codeLine.isEmpty())
				codeList.addCompiledCodeLine(codeLine);
			else
				error = true;
		}

		if(error)
		    return null;

		return codeList;
	}

}
