package com.programmer.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.programmer.instructions.Instruction;
import com.programmer.orders.Order;

public class Segregation {

    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final ArrayList<Order> orderList;
    private Map<String, Integer> labels = new HashMap<>();

    private List<Instruction> instructions = new ArrayList<>();
	private int memoryNumber = 0;
	private int lineNumber = 0;
	private boolean result;

	public Segregation(ArrayList<Order> orderList){
	    this.orderList = orderList;
    }

    public Map<String, Integer> getLabels() {
        return labels;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public boolean parseCode(String code) throws IOException {
	    result = true;
        StringReader sr = new StringReader(code);
        BufferedReader br = new BufferedReader(sr);
        String line;

        while((line = br.readLine()) != null){

            lineNumber++;
            if(line.isEmpty())
                continue;

            if(line.startsWith(";"))
                continue;

            result = addLine(line);
        }

        lineNumber = 0;
        memoryNumber = 0;
        return result;
    }

	private String[] parseInstructionLine(String line){

		String codeLine = line.split(";")[0];
		String[] parsedCodeLine = codeLine.split(" +");

		String orderName = parsedCodeLine[0].trim();
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

	private boolean addLine(String line) {

        String[] parsedCodeLine = parseInstructionLine(line);
        String orderName = parsedCodeLine[0];
        String operand = parsedCodeLine[1];

        for(Order o : orderList)
            if(o.getMnemonic().equals(orderName)){
                if(o.getType().equals("OP WITH CONST"))
                    memoryNumber += 2;
                else
                    memoryNumber++;
                break;
            }


        if (!(orderName == null))
            orderName = orderName.trim();

        if (!(operand == null))
            operand.trim();

        if (orderName.endsWith(":")) {
            String label = orderName.split(":")[0].trim();
            if (!labels.containsKey(label)) {
                labels.put(label, memoryNumber);
            }
            else {
                LOGGER.warning("Line "+lineNumber+": same label!");
                return false;
            }

        } else {
            instructions.add(new Instruction(orderName, operand, lineNumber));
        }
        return result;
    }

}
