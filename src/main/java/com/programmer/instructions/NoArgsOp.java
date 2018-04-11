package com.programmer.instructions;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by bobaxix on 17.09.17.
 */
public class NoArgsOp implements CodeGenerator{
    Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public ArrayList<Integer> generateCode(String operand, int orderCode, int instructionLine) {
        ArrayList<Integer> codeLine = new ArrayList<Integer>();
        if(operand == null) {
            int code = orderCode << 24;
            codeLine.add(code);
            return codeLine;
        }
        else
            LOGGER.warning("Line "+instructionLine+": NAO need not argument.");

        return null;
    }
}


