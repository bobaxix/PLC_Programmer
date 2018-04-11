package com.controllers;

import com.programmer.load.CodeList;
import com.programmer.load.Compiler;

import java.io.IOException;
import java.util.logging.Logger;

public class CompileHandler implements Runnable{

    private Compiler compiler;
    private String code;
    private CodeList fullCode;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public CompileHandler(Compiler compiler){
        this.compiler = compiler;
    }

    public void handle(String code) {
        this.code = code;
        run();
    }

    public CodeList getFullCode(){
        return fullCode;
    }

    @Override
    public void run() {
        if(code.isEmpty()) {
            LOGGER.warning("Any code to compile!");
            fullCode = null;
        }
        else {
            try {
                fullCode = compiler.compile(code);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (fullCode != null)
                LOGGER.info("Valid compilation!");
            else
                LOGGER.warning("Cannot compile program.");
        }
    }
}
