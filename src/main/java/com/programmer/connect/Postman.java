package com.programmer.connect;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.common.SpiManager;
import com.programmer.load.CodeList;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Spi;


public final class Postman {

	private static Postman instance = new Postman();
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private SpiManager spiManager;

	public static Postman getInstance(){
		return instance;
	}
	
	private Postman(){
	    spiManager = SpiManager.getInstance();
    }

	public void sendCode(CodeList code) {

        if (code != null) {

            ArrayList<Integer> codeList = code.getCodeList();
            int numberOfInstructions = codeList.get(0);
            int checksum = 0;

            spiManager.startProgramming();
            spiManager.sendDoubleWord(numberOfInstructions - 1);

            while (numberOfInstructions > 0) {
                int data = codeList.get(numberOfInstructions);
                numberOfInstructions--;
                checksum += data;
                spiManager.sendDoubleWord(data);
            }

            int checksumPLC = spiManager.sendDoubleWord(checksum);
            System.out.println("Check: "+Integer.toHexString(
                    Integer.reverse(checksum)));
            System.out.println("CheckPLC: "+Integer.toHexString(Integer.reverseBytes(
                    checksumPLC)));
            if(Integer.reverse(checksum)
                    == Integer.reverseBytes(checksumPLC))
                LOGGER.info("Device was programmed!");
            else
                LOGGER.severe("Checksum is incorrect, programming failed!");
        }
        else
            LOGGER.warning("Data to send is an empty array!");
    }
    }

