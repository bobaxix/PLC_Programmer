package com.PI.load.Instructions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bobaxix on 20.09.17.
 */
public class Apb extends Instruction {

    private String[] parsedOperand;
    private Matcher matcher;
    private final String[] patterns ={
            "(OUT)$", "(IN)$",
            "(C)(\\d+)\\.(CV|CU|CD|R|S|PV|CV|QD|QU)$",
            "(T)(\\d+)\\.(TYPE|PT|IN|Q|ET)$"
    };
    ArrayList<Integer> codeLine;
    private static class CounterArgs{
        final static int CU = 0;
        final static int CD = 1;
        final static int R = 2;
        final static int S = 3;
        final static int PV = 4;
        final static int CV = 5;
        final static int QD = 6;
        final static int QU = 7;
    }

    private static class TimerArgs{
        final static int TYPE = 0;
        final static int PT = 1;
        final static int IN = 2;
        final static int Q = 3;
        final static int ET = 4;
    }

    private int[] timerAddress = {0x0000800,0x00000FFF};
    private int[] counterAddress = {0x0000000,0x000007ff};
    private int gpi = 0x00001000;
    private int gpo = 0x00001001;

    public Apb(){
        parsedOperand = new String[3];
        codeLine = new ArrayList<>();
    }

    @Override
    public ArrayList<Integer> generateCodeForInstruction(){
        boolean result = parseOperand();
        if(result){
            int address = getAddress();
            codeLine.add(orderCode << 24 | address);
        }
        return codeLine;
    }

    public ArrayList<Integer> getCodeLine(){
        return codeLine;
    }

   private boolean parseOperand(){

        boolean result = matchOperand();
        int numberOfGroups = matcher.groupCount();
        for(int i =0; i < numberOfGroups; i++)
           parsedOperand[i] = matcher.group(i+1);
        return result;
   }
    private boolean matchOperand(){

       Pattern pattern;

        for(String regularExpr : patterns){
            pattern = Pattern.compile(regularExpr);
            matcher = pattern.matcher(operand);
            if(matcher.matches())
                return true;
        }

        return false;
    }

    private int getAddress(){
        String baseAddress = parsedOperand[0];

        if(baseAddress.equals("IN"))
            return gpi;
        else if(baseAddress.equals("OUT"))
            return gpo;
        else if(baseAddress.equals("T")) {
            int address = getBaseAddress();
            return timerAddress[0] + address;
        }
        else if(baseAddress.equals("C")) {
            int address = getBaseAddress();
            return counterAddress[0] + address;
        }

        return -1;
    }

    private int getBaseAddress(){
        int baseAddressOffset = Integer.parseInt(parsedOperand[1]) * 8;
        int bitAddress = getBitAddress();

        return baseAddressOffset + bitAddress;
    }

    private int getBitAddress(){

        String bitAddress = parsedOperand[2];

        if(bitAddress.equals("TYPE"))
            return TimerArgs.TYPE;
        else if(bitAddress.equals("PT"))
            return TimerArgs.PT;
        else if(bitAddress.equals("IN"))
            return TimerArgs.IN;
        else if(bitAddress.equals("Q"))
            return TimerArgs.Q;
        else if(bitAddress.equals("ET"))
            return TimerArgs.ET;
        else if(bitAddress.equals("CU"))
            return CounterArgs.CU;
        else if(bitAddress.equals("CD"))
            return CounterArgs.CD;
        else if(bitAddress.equals("R"))
            return CounterArgs.R;
        else if(bitAddress.equals("S"))
            return CounterArgs.S;
        else if(bitAddress.equals("CV"))
            return CounterArgs.CV;
        else if(bitAddress.equals("PV"))
            return CounterArgs.PV;
        else if(bitAddress.equals("QD"))
            return CounterArgs.QD;
        else if(bitAddress.equals("QU"))
            return CounterArgs.QU;

        return 0;
    }
}
