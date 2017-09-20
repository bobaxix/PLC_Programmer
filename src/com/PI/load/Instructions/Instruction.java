package com.PI.load.Instructions;

import com.PI.load.Order;

import java.util.ArrayList;

/**
 * Created by bobaxix on 16.09.17.
 */
public abstract class Instruction {

    protected String operand;
    protected String order;
    protected int orderCode;
    protected int instructionLineNumber;
    protected boolean isLabel = false;
    protected boolean isJump = false;

    abstract public ArrayList<Integer> generateCodeForInstruction();

    public void setParameters(String order, int orderCode, String operand, int instructionLineNumber){
        this.operand = operand;
        this.orderCode = orderCode;
        this.order = order;
        this.instructionLineNumber = instructionLineNumber;
    }

    public String getOperand(){
        return operand;
    }

    public int getOrderCode(){
        return orderCode;
    }

    public int getInstructionLineNumber(){
        return  instructionLineNumber;
    }

    public String getOrder(){
        return order;
    }

    public boolean isLabel(){
        return isLabel;
    }

    public boolean isJump(){
        return isJump;
    }
}
