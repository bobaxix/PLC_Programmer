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

    private String accessSize;
    private char accessType;
    private int address;
    private Matcher matcher;
    private String[] parsedOperand;
    private ArrayList<Integer> codeList;

    public InOutMem(){
        parsedOperand = new String[3];
        codeList = new ArrayList<>();
    }

    private boolean parseOperand(){

        Pattern pattern = Pattern.compile("(M|I|O)(\\d+|D|W|B)\\.?(\\d?)");
        matcher = pattern.matcher(operand);
        boolean result = matcher.matches();

        return result;
    }

    private void makeReferencesToParsedOperand(){
        int numberOfGroups = matcher.groupCount();

        for(int i = 0; i < numberOfGroups; i++)
            parsedOperand[i] = matcher.group(i+1);

        accessType = parsedOperand[0].charAt(0);
        accessSize = parsedOperand[1];
        address = Integer.parseInt(parsedOperand[2]); //bit or base adress
    }

    private int convertAccessSizeToCode(){

        if(accessSize.equals("D"))
            return 0b11;
        else if(accessSize.equals("W"))
            return 0b10;
        else if(accessSize.equals("B"))
            return 0b01;
        else
            return 0b00;
    }

    private int convertBaseAddressToChipSelectCode(){

        if(accessSize.equals("D"))
            return 0;
        else if(accessSize.equals("W"))
            return (address % 2) << 1;
        else if(accessSize.equals("B"))
            return address % 4;
        else
            return Integer.parseInt(accessSize) % 4;
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

            if(accessSize.equals("D"))
                return address;
            else if(accessSize.equals("W"))
                return address / 2;
            else if(accessSize.equals("B"))
                return address /4;
            else
                return Integer.parseInt(accessSize) / 4;
    }

   private int getBitAddress(){
        if(accessSize.matches("\\d+"))
            return address;
        else
            return 0;
   }

   private boolean checkAddress(int address){

       boolean result = false;
       if(accessType == 'M')
           result = address <= memoryBlock[1];
       else if (accessType == 'I')
           result =  address <= inputBlock[1];
       else if (accessType == 'O')
           result =  address <= outputBlock[1];

       if(!result)
           LOGGER.warning("Line "+instructionLineNumber+": address out of range.");

       return result;
   }

    @Override
    public ArrayList<Integer> generateCodeForInstruction(){

        boolean result = parseOperand();
        codeList = new ArrayList<>();

        if(result){
            makeReferencesToParsedOperand();
            int baseAddress = convertAccessTypeToBaseAddressCode();
            int accessType = convertAccessSizeToCode();
            int chipSelectCode = convertBaseAddressToChipSelectCode();
            int baseAddressOffsetCode = convertBaseAdressToOffsetCode();
            int fullAddress = baseAddress + baseAddressOffsetCode;
            int bitAddress = getBitAddress();

            if(checkAddress(fullAddress)) {
                int code = (orderCode << 24) | (accessType << 8) | (fullAddress << 5) |
                        (chipSelectCode << 3) | bitAddress;
                codeList.add(code);
                return codeList;
            }
        }
        else
            LOGGER.warning("Line "+instructionLineNumber+": invalid argument");

        return null;
    }
}
