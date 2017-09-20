package com.PI.load.Instructions;

import java.util.ArrayList;

/**
 * Created by bobaxix on 17.09.17.
 */
public class NoArgsOp extends Instruction {

    @Override
    public ArrayList<Integer> generateCodeForInstruction(){
        ArrayList<Integer> codeLine = new ArrayList<Integer>();
        int code = orderCode << 24;
        codeLine.add(code);
        return codeLine;
    }
}
