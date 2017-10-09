package com.PI.load;
import java.util.ArrayList;

import com.PI.Main;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Spi;


public class Postman {

	//final GpioPinDigitalOutput reset;// = Main.reset;
	private ArrayList<Integer> code;
	private int error;
	private int checksum;
	
	public Postman(ArrayList<Integer> code){
		this.code = code;
        //this.reset = Main.reset;
		error = Spi.wiringPiSPISetupMode(0, 500000, Spi.MODE_3);
		
		if(error != -1){
			System.out.println("Inicjalizacja SPI okej");
		}
	}
	
	private int sendFourBytes(int data, boolean b){
		int checksum = 0;
	//	System.out.println("Data: "+data);
		int d = Integer.reverse(data);
	//	System.out.println("D: "+d);
		byte[] parts = new byte[4];
		parts[3] = (byte) (d);
		parts[2] = (byte) ((d>>>8));
		parts[1] = (byte) ((d>>>16));
		parts[0] = (byte) ((d>>>24));
		
		if(b == false){
			checksum += data;
		}			
		Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, parts, 4);
		if(b == true) {
			for(int i = 0;i<4;i++) checksum += (parts[i] & 0xFF) << (i*8);
			checksum = Integer.reverseBytes(checksum);
			//checksum = Integer.reverse(checksum);
		}
		return checksum;
	}
	
	
	public boolean sendCode(){
		boolean checksumProperty = false;
		if(error != -1){
			//reset.setState(PinState.LOW);
			/*--------------------------------------
			 * rodzielczo�� sleep'a - 1 ms
			 -----------------------------------------*/
		try {
			Thread.sleep(0,100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//reset.setState(PinState.HIGH);
		
		int checksumPLC;
		int l = code.size()-1;
		System.out.print("Ilosc instrukcji ("+l+"):         ");
		wyswietlanieBajtow(l);
		sendFourBytes(l,false);
		for(int i = l; i>=0 ; i-- ){
			if(i<10) System.out.print("Adres "+i+":                  ");
			else System.out.print("Adres "+i+":                ");
			wyswietlanieBajtow(code.get(i));
			checksum +=sendFourBytes(code.get(i),false);
		}
		checksumPLC = sendFourBytes(checksum,true);
		System.out.print("Suma kontrolna obliczona: ");
		wyswietlanieBajtow(Integer.reverse(checksum));
		System.out.print("Suma kontrolna otrzymana: ");
		wyswietlanieBajtow(checksumPLC);
		if(checksumPLC == Integer.reverse(checksum)) checksumProperty = true;
		else checksumProperty = false;
		}
		return checksumProperty;
	}
	
	public int getErrorState(){
		return error;
	}

	private void wyswietlanieBajtow(int x){
		
			for(int i = 31; i>=0; i--){
				if((((x)>>i) & 0x01) == 0)
				System.out.print("0");
				else System.out.print("1");
				if(i%8 == 0 && i !=0) System.out.print(" ");
			}

			System.out.print("\n");
		
	}
	
}
