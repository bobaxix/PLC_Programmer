package com.PI.MVC.controllers;

import com.PI.load.CodeList;
import com.PI.load.Compilator;
import com.PI.load.Order;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CompileHandler {

    private Compilator compilator;
    CodeList fullCode;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public CompileHandler(Compilator compilator){
        this.compilator = compilator;
    }

    public void handle(String code) {


        try {
            fullCode = compilator.compile(code);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (fullCode != null)
            LOGGER.info("Valid compilation!");
        else
            LOGGER.warning("Cannot compile program.");


    }

    public CodeList getFullCode() {
        return fullCode;
    }
}
