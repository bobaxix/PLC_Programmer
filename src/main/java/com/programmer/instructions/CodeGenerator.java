package com.programmer.instructions;

import com.programmer.orders.Order;

import java.util.ArrayList;

public interface CodeGenerator {

    ArrayList<Integer> generateCode(String operand,
                                    int orderCode,
                                    int instructionLine);
}
