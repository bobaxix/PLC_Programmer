package com.PI.MVC.controllers;

import com.PI.load.Compilator;
import com.PI.load.Order;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CompileHandler implements EventHandler {

    ArrayList<Order> orderList;
    private TextArea codeTextArea;
    private TextArea logTextArea;
    ArrayList<Integer> fullCode;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public CompileHandler(ArrayList<Order> orderList, TextArea codeTextArea, TextArea logTextArea){
        this.orderList = orderList;
        this.codeTextArea = codeTextArea;
        this.logTextArea = logTextArea;
    }

    @Override
    public void handle(Event event) {

        logTextArea.clear();
        Compilator compilator = new Compilator(orderList);

        try {
            fullCode = compilator.compile(codeTextArea.getText());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (fullCode != null)
            LOGGER.info("Valid compilation!");
        else
            LOGGER.warning("Cannot compile program.");


    }

    public ArrayList<Integer> getFullCode() {
        return fullCode;
    }
}
