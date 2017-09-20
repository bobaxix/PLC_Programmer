package com.PI.load.Instructions;

import java.util.ArrayList;

/**
 * Created by bobaxix on 16.09.17.
 */
public class WithConst extends Instruction{

    int number;

    @Override
    public ArrayList<Integer> generateCodeForInstruction(){

        parseOperand();
        ArrayList<Integer> codeList = new ArrayList<Integer>();
        codeList.add(orderCode << 24);
        codeList.add(number);
        return codeList;
    }

    private void parseOperand(){

        if(operand.startsWith("#")){
            try{
                String tempOperand = operand.substring(1);
                number = Integer.parseInt(tempOperand);
            }
            catch(NumberFormatException e){
                number = 0;
            }
        }
    }
}
