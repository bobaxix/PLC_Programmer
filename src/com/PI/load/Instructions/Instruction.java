package com.PI.load.Instructions;

import com.PI.load.Order;

import java.util.ArrayList;

/**
 * Created by bobaxix on 16.09.17.
 */
public abstract class Instruction {

    protected String operand;
    protected int orderCode;

    abstract public ArrayList<Integer> generateCodeForInstruction();

    public void setParameters(String operand, int orderCode){
        this.operand = operand;
        this.orderCode = orderCode;
    }

    public String getOperand(){
        return operand;
    }

    public int getOrderCode(){
        return orderCode;
    }
}
