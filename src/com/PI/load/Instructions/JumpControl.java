package com.PI.load.Instructions;

import java.util.ArrayList;

/**
 * Created by bobaxix on 17.09.17.
 */
public class JumpControl extends Instruction {

    int lineNumberToJump = 0;

    public JumpControl(){
        isJump = true;
    }
    @Override
    public ArrayList<Integer> generateCodeForInstruction(){

        ArrayList<Integer> codeLine = new ArrayList<Integer>();
        int code = orderCode << 24 | lineNumberToJump;
        codeLine.add(code);
        return codeLine;
    }

    public void whereShouldJump(ArrayList<Instruction> labels){

        int lineNumber = 0;
        for(Instruction label : labels){
            if(operand.equals(((Label) label).getLabel())){
                lineNumberToJump = label.getInstructionLineNumber() - lineNumber;
            }
            lineNumber++;
        }
    }
}