package com.programmer.instructions;

import java.util.Map;

public class CodeFactory {

    private final Map<String, Integer> labels;

    public CodeFactory(Map<String, Integer> labels) {
        this.labels = labels;
    }

    public CodeGenerator codeGenerator(String type){
        switch(type){
            case "IOM INSTRUCTIONS":
                return new InOutMem();
            case "OP WITH CONST":
                return new WithConst();
            case "NAO":
                return new NoArgsOp();
            case "APB":
                return new Apb();
            case "JUMP CONTROL":
                return new JumpControl(labels);

            default:
                return null;
        }
    }
}
