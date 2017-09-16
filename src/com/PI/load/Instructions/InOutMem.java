package com.PI.load.Instructions;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bobaxix on 16.09.17.
 */
public class InOutMem extends Instruction {

    private int[] memoryBlock = {0x00000002,0x00000007};
    private int[] inputBlock = {0x00000000,0x00000000};
    private int[] outputBlock = {0x00000001,0x00000001};

    private char accessSize;
    private char accessType;
    private int baseAddress;
    private int bitAddress;
    private Matcher matcher;

    private void parseOperand(){

        Pattern pattern = Pattern.compile("([MIO])([BDW]?)(\\d).?(\\d?)");
        matcher = pattern.matcher(operand);
        matcher.matches();

            accessType = getCharFromMatch(1,0); // bit byte word
            accessSize = getCharFromMatch(2,0);
            baseAddress = getIntFromMatch(3); //
            bitAddress = getIntFromMatch(4); //bit
    }

    private char getCharFromMatch(int matchGroup, int charArrayElement){
        try{
            return matcher.group(matchGroup).toCharArray()[charArrayElement];
        }
        catch(ArrayIndexOutOfBoundsException e){
            // nic nie znalazlem moze byc okej
            return 0;
        }
    }

    private int getIntFromMatch(int matchGroup){
        try{
            return Integer.parseInt(matcher.group(matchGroup));
        }
        catch(NumberFormatException e){
            if (matcher.group(matchGroup).equals(""))
                return 0;

            return -1;
        }
    }

    private int convertAccessSizeToCode(){

        if(accessSize == 'D')
            return 0b11;
        else if(accessSize == 'W')
            return 0b10;
        else if(accessSize == 'B')
            return 0b01;
        else if(accessSize == 0)
            return 0b00;

        return -1;
    }

    private int convertBaseAdressToChipSelectCode(){

        if(accessSize == 'D')
            return 0;
        else if(accessSize == 'W')
            return (baseAddress % 2) << 1;
        else if(accessSize == 'B')
            return baseAddress % 4;
        else if(accessSize == 0)
            return baseAddress % 4;

        return -1;
    }

    private int convertAccessTypeToBaseAddressCode(){

        if(accessType == 'M')
            return memoryBlock[0];
        else if (accessType == 'I')
            return inputBlock[0];
        else if (accessType == 'O')
            return outputBlock[0];

        return -1;
    }

    private int convertBaseAdressToOffsetCode(){

            if(accessSize == 'D')
                return baseAddress;
            else if(accessSize == 'W')
                return baseAddress / 2;
            else if(accessSize == 'B')
                return baseAddress /4;
            else if(accessSize == 0)
                return baseAddress / 4;

            return -1;
    }

    @Override
    public ArrayList<Integer> generateCodeForInstruction(){

        parseOperand();
        int baseAddress = convertAccessTypeToBaseAddressCode();
        int accessType = convertAccessSizeToCode();
        int chipSelectCode = convertBaseAdressToChipSelectCode();
        int baseAddressOffsetCode = convertBaseAdressToOffsetCode();
        int fullAddress = baseAddress + baseAddressOffsetCode;

        ArrayList<Integer> codeList = new ArrayList<Integer>();
        int code = (orderCode << 24) | (accessType << 8) | (fullAddress  << 5) |
                (chipSelectCode << 3) | (bitAddress);
        codeList.add(code);
        return codeList;
    }
}
