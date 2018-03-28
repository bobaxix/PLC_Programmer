package com.connect;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Logger;

import com.load.CodeList;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Spi;


public final class Postman {

    final private GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput reset = gpio.provisionDigitalOutputPin(
            RaspiPin.GPIO_06, "Reset", PinState.HIGH);
	private static Postman instance = new Postman();
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static Postman getInstance(){
		return instance;
	}
	
	private Postman(){}

	private int initializeSPI(){

	    int result = Spi.wiringPiSPISetupMode(0, 500000, Spi.MODE_3);
	    if(result == -1)
            LOGGER.severe("SPI initialization failed");
	    return result;
    }
	
	private byte[] sendDoubleWord(int data){

		byte[] dataBuffer = chunkDoubleWordToByteArray(data);
		Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, dataBuffer, 4);
		return dataBuffer;
	}

	private byte[] chunkDoubleWordToByteArray(int data){

		data = Integer.reverse(data);
		byte[] chunks = new byte[4];
		chunks[3] = (byte) (data);
		chunks[2] = (byte) ((data>>>8));
		chunks[1] = (byte) ((data>>>16));
		chunks[0] = (byte) ((data>>>24));

		return chunks;
	}
	
	
	public void sendCode(CodeList code) {

        int result = initializeSPI();
        if (result != -1 && code != null) {

            ArrayList<Integer> codeList = code.getCodeList();
            int numberOfInstructions = codeList.get(0);
            int checksum = 0;

            resetDevice();
            System.out.println(codeList.toString());
            sendDoubleWord(numberOfInstructions - 1);
            System.out.println("Number of instr: "+numberOfInstructions);

            while (numberOfInstructions > 0) {
                int data = codeList.get(numberOfInstructions);
                numberOfInstructions--;
                System.out.println(Integer.toHexString(data));
                checksum += data;
                sendDoubleWord(data);
            }

            byte[] checksumPLCinChunks = sendDoubleWord(checksum);
            System.out.println("Check: "+checksum);
            int checksumPLC = calculateChecksum(checksumPLCinChunks);
            System.out.println("CheckPLC: "+checksumPLC);
            if(Integer.reverse(checksum) == checksumPLC)
                LOGGER.info("Device was programmed!");
            else
                LOGGER.severe("Checksum is incorrect, programming failed!");
        }
        else
            LOGGER.warning("Data to send is an empty array!");
    }

    private int calculateChecksum(byte[] chunks){
        int checksum = 0;
	    for(int i = 0;i<4;i++)
            checksum |= (((int)chunks[i]) & 0xFF) << (i*8);
        return Integer.reverseBytes(checksum);
    }


	private void resetDevice() {
        reset.setState(PinState.LOW);
			/*--------------------------------------
			 * rodzielczo�� sleep'a - 1 ms
			 -----------------------------------------*/
        try {
        Thread.sleep(0,100);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        reset.setState(PinState.HIGH);
    }
}

