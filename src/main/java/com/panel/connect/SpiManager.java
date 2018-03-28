package com.panel.connect;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Spi;

public class SpiManager {

    final private GpioController gpio = GpioFactory.getInstance();
    final private GpioPinDigitalOutput reset = gpio.provisionDigitalOutputPin(
            RaspiPin.GPIO_06, "Reset", PinState.HIGH);
    final private GpioPinDigitalOutput nSS = gpio.provisionDigitalOutputPin(
            RaspiPin.GPIO_10, "nSS", PinState.HIGH);

    public int initializeSpi(){
        return Spi.wiringPiSPISetupMode(0, 500000, Spi.MODE_3);
    }

    public int sendDoubleWord(int data){

        byte[] dataBuffer = chunkDoubleWordToByteArray(data);
        Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, dataBuffer, 4);
        return foldByteArray(dataBuffer);
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

    private int foldByteArray(byte[] dataBuffer){
        int buffer = 0;
        for(int i = 0; i < 4; i++)
            buffer |= (((int)dataBuffer[i]) & 0xFF) << (i*8);
        return buffer;
    }

    public void sendAddress(Operation direction, byte address){

        int i = ((int) address);
        if(direction == Operation.WRITE)
            i |= 1 << 7;
        i = Integer.reverse(i);
        i = ((i >> 24) & 0xFF);

        byte[] dataBuffer = {(byte) i, 0, 0, 0};
        Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, dataBuffer, 1);
    }

    public int readRegister(){
        byte[] dataBuffer = {0,0,0,0};
        Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, dataBuffer, 4);
        return convert(dataBuffer);
    }

    private int convert(byte[] chunks){
        int data = (chunks[0] << 24) & 0xFF000000
                 | (chunks[1] << 16)  & 0x00FF0000
                 | (chunks[2] << 8)   & 0x0000FF00
                 |  chunks[3] & 0x000000FF;
        return Integer.reverse(data);
    }


}
