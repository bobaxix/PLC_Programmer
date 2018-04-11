package com.programmer.instructions;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by bobaxix on 17.09.17.
 */
public class JumpControl implements CodeGenerator {

    private final Map<String, Integer> labels;
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public JumpControl(Map<String, Integer> labels){
        this.labels = labels;

    }
    @Override
    public ArrayList<Integer> generateCode(String operand, int orderCode, int instructionLine) {
        ArrayList<Integer> codeLine = new ArrayList<Integer>();
        int lineNumberToJump = parseOperand(operand);
        if(lineNumberToJump == -1){
            LOGGER.warning("Line "+instructionLine+": cannot find label.");
            return null;
        }
        int code = orderCode << 24 | lineNumberToJump;
        codeLine.add(code);
        return codeLine;
    }

    private int parseOperand(String operand){
        for(String label : labels.keySet()){
            if(label.equals(operand))
                return labels.get(operand);
        }
        return -1;
    }
}
