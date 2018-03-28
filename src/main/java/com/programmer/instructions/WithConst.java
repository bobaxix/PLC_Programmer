package com.programmer.instructions;

import java.util.ArrayList;

/**
 * Created by bobaxix on 16.09.17.
 */
public class WithConst extends Instruction{

    int number;
    ArrayList<Integer> codeList;

    public WithConst(){
        isOpWithConst = true;
    }
    @Override
    public ArrayList<Integer> generateCodeForInstruction(){

        try {
            parseOperand();
            codeList = new ArrayList<Integer>();
            codeList.add(orderCode << 24);
            codeList.add(number);
        }
        catch(NullPointerException e){
            return null;
        }
        return codeList;
    }

    private void parseOperand() throws NumberFormatException{

        if(operand.startsWith("#")){
                String tempOperand = operand.substring(1);
                number = Integer.parseInt(tempOperand);
        }
    }
}
