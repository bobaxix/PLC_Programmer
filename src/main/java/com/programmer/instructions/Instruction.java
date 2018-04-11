package com.programmer.instructions;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by bobaxix on 16.09.17.
 */
public class Instruction {

    private String operand;
    private String order;
    private int instructionLine;

    public Instruction(String order, String operand, int instructionLine){
        this.operand = operand;
        this.order = order;
        this.instructionLine = instructionLine;
    }

    public String getOrder(){
        return order;
    }
    public String getOperand(){return operand;}
    public int getInstructionLine(){return instructionLine;}


    @Override
    public String toString(){
        return operand+" "+order;
    }
}
