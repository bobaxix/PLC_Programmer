package com.PI.load;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compilator {

	Instruction[] instruction;
	Map<String,Order> orderList;
	ArrayList<Integer> code;
	ArrayList<Label> labels;
	ArrayList<Jump> jumps;
	
	/*------------------------------------
	 * mapowanie pamiêci oraz peryferiów
	 -----------------------------------*/
	int[] memoryBlock = {0x00000002,0x00000007};
	int[] inputBlock = {0x00000000,0x00000000};
	int[] outputBlock = {0x00000001,0x00000001};
	int[] timerAdress = {0x0000800,0x00000FFF};
	int[] counterAdress = {0x0000000,0x000007ff};
	int gpi = 0x00001000;
	int gpo = 0x00001001;
	
	final static int TYPE = 0;
	final static int PT = 1;
	final static int IN = 2;
	final static int Q = 3;
	final static int ET = 4;
	
	final static int CU = 0;
	final static int CD = 1;
	final static int R = 2;
	final static int S = 3;
	final static int PV = 4;
	final static int CV = 5;
	final static int QD = 6;
	final static int QU = 7;
	
	/*---------------------------
	 * WYRA¯ENIA REGULARNE
	 --------------------------*/
	String bit_op = "\\w(\\d*)\\.(\\d*)";
	String nbit_op = "\\w{2}(\\d*)";
	String apb_op = "([cCtT])(\\d*)\\.(.*)";
	
	
	
	int instructionNumber;
	int max = 2147483647;
	int min = 0;
	int error = -1;
	
	String errors;
	StringWriter sw;
	
	public Compilator(Instruction[] i, Map<String,Order> orderList,  int instructionNumber){
		this.instruction = i;
		this.orderList = orderList;
		this.instructionNumber = instructionNumber;
		sw = new StringWriter();

	}
	
	public int getError(){
		return error;
	}
	
	public String getErrorsList(){
		return errors;
	}

	public ArrayList<Integer> compile(){
		error = 0;
		String command = null;
		String operand = null;
		String type = "nope";
		int singleCode = 0;
		code = new ArrayList<Integer>();
		jumps = new ArrayList<Jump>();
		labels = new ArrayList<Label>();
		
		for(Instruction s : instruction){
			System.out.println(s.getCommand()+" "+s.getOperand());
		}
		
		/*------------------------------------------
		 * PRZYPISANIE TYPÓW, OPERANDÓW I OPERATORÓW
		 -------------------------------------------*/
		
		for(int i = 0; i<instructionNumber; i++){
			try{
				type = "nope";
				command = null;
				operand = null;
				
				command = instruction[i].getCommand();
				System.out.println("Step "+i+": "+command);
				operand = instruction[i].getOperand();	
			}
			
			catch(NullPointerException e){
				sw.write("Line "+(i+1)+"\n");
				error = -1;
				break;
			};
			
			try{
				type = orderList.get(command).getType();
			}
			
			catch(NullPointerException e) {
					if(command.endsWith(":") == true && operand == null){
						type = "ETYKIETA";
					}
					else{
						sw.write("Line "+(i+1)+": Nie znaleziono rozkazu"+"\n");
						error = -1;
						break;
					}
			}
			
			System.out.println(type);
			
			/*------------------------------------------------
			 * ROZPOCZÊCIE 'W£AŒCIWEJ' KOMPILACJI
			 -----------------------------------------------*/
			
			switchLoop:
			switch(type){
				case("IOM INSTRUCTIONS"):{
					singleCode = (orderList.get(command).getCode())<<24;
					int adr = 0;
					int cs = 0;
					int access_type = -1;
					
					/*------------------------------
					 * access_type:
					 * 0 - bit
					 * 1 - bajt
					 * 2 - s³owo
					 * 3 - podwójne s³owo
					 --------------------------------*/
					
					char iom = 0;
					char dwb = 0;
					int mod = 4;
					int div = 0;
					int off = 0;
					int bit = 0;
					try{
						iom = operand.toUpperCase().charAt(0);
						dwb = operand.toUpperCase().charAt(1);
					}
					catch(Exception e){
						sw.write("Line "+(i+1)+": "+"Nieprawid³owy argument - \n");
						error = -1;
						break switchLoop;
					}
					
					/*-----------------------------------------------------------
					 * WYZNACZENIE TRYBU ADRESOWANIA ORAZ ZMIENNYCH POMOCNICZYCH
					-----------------------------------------------------------*/
					
					switch(dwb){
						case('D'):{ //double
							access_type = 3;
							mod = 1;
							div = 4;
							off = 0;
							break ;
						}
						case('W'):{//word
							access_type = 2;
							mod = 2;
							div = 2;
							off = 1;
							break;
						}
						case('B'):{//byte
							access_type = 1;
							mod = 4;
							div = 1;
							off = 0;
							break;
						}
						default:{
							if(Character.isDigit(dwb) == true){
								access_type = 0;
								break;
							}
							else{
								sw.write("Line "+(i+1)+": "+"Nieprawid³owy argument\n");
									break;
								}
							}
						}
					
					/*---------------------------------------------
					 * WYZNACZENIE ADRESU
					 --------------------------------------------*/
					
					switch(access_type){
					
					/*-----------------------------------
					 * ADRESOWANIE BITOWE
					 ----------------------------------*/
						case(0):{
							int a;
							int b;
							//String[] data = (operand.substring(1)).split("[.]");
							try{
								Pattern pattern = Pattern.compile("\\w(\\d*)\\.(\\d*)");
								Matcher matcher = pattern.matcher(operand);
								matcher.matches();
								//a = Integer.parseInt(data[0]);
								//b = Integer.parseInt(data[1]);
								a = Integer.parseInt(matcher.group(1));
								b = Integer.parseInt(matcher.group(2));
								bit = b;
								cs = a%4;
								if(b<0 || b>7){
									sw.write("Line "+(i+1)+": Adres spoza zakresu\n");
									error = -1;
									break switchLoop;
								}
							}
							catch(Exception e){
								sw.write("Line "+(i+1)+": "+"Nieprawid³owy argument\n");
								error = -1;
								break switchLoop;
							}
							switch(iom){
								case('I'):{
									if(a<0 || a>((inputBlock[1]-inputBlock[0]+1)*4 - 1)){
										sw.write("Line "+(i+1)+": Adres spoza zakresu\n");
										error = -1;
										break switchLoop;
									}
									adr = (inputBlock[0] + a/4);
									break;
								}
								case('Q'):{
									if(a<0 || a>((outputBlock[1]-outputBlock[0]+1)*4 - 1)){
										sw.write("Line "+(i+1)+": Adres spoza zakresu\n");
										error = -1;
										break switchLoop;
									}
									adr = (outputBlock[0] + a/4);
									break;
								}
								case('M'):{
									if(a<0 || a>((memoryBlock[1]-memoryBlock[0]+1)*4 - 1)){
										sw.write("Line "+(i+1)+": Adres spoza zakresu\n");
										error = -1;
										break switchLoop;
									}
									adr = (memoryBlock[0] + a/4);
									System.out.println("adr: "+adr);
									break;
								}
								default:{
									sw.write("Line "+(i+1)+": "+"Nieprawid³owy argument\n");
									error = -1;
									break switchLoop;
								}
							}
							break;
						}
						
						/*--------------------------------------
						 * ADRESOWANIE BYTE, WORD LUB DOUBLEWORD 
						------------------------------------- */
						
						default:{
							int a;
							try{
								Pattern pattern = Pattern.compile(nbit_op);
								Matcher matcher = pattern.matcher(operand);
								matcher.matches();
								//a = Integer.parseInt(operand.substring(2));
								a = Integer.parseInt(matcher.group(1));
								cs = (a%mod) << off;
								}
							catch(Exception e){
								sw.write("Line "+(i+1)+": "+"Nieprawid³owy argument"+e.getClass()+" "+e.getMessage()+"\n");
								error = -1;
								break switchLoop;
							}
							
							switch(iom){
							case('I'):{
								if(a<0 || a>((inputBlock[1]-inputBlock[0]+1)*4/div - 1)){
									sw.write("Line "+(i+1)+": Adres spoza zakresu\n");
									error = -1;
									break switchLoop;
								}
								adr = ((inputBlock[0] + a/mod));
								break;
							}
							case('Q'):{
								if(a<0 || a>((outputBlock[1]-outputBlock[0]+1)*4/div - 1)){
									sw.write("Line "+(i+1)+": Adres spoza zakresu\n");
									error = -1;
									break switchLoop;
								}
								adr = (outputBlock[0] + a/mod);
								break;
							}
							case('M'):{
								if(a<0 || a>((memoryBlock[1]-memoryBlock[0]+1)*4/div - 1)){
									sw.write("Line "+(i+1)+": Adres spoza zakresu\n");
									error = -1;
									break switchLoop;
								}
								adr = (memoryBlock[0] + a/mod);
								System.out.println("adr: "+adr);
								break;
							}
							default:{
								sw.write("Line "+(i+1)+": "+"Nieprawid³owy argument\n");
								error = -1;
								break switchLoop;
							}
						}
							break;
					}
						
				}
				singleCode += ((access_type<<8) + (adr<<5) + (cs<<3)) + bit;
				code.add(singleCode);
				break;
			}
					
				/*---------------------------------------------
				 * TWORZENIE INSTRUKCJI DLA INSTRUKCJI ZE STA£¥
			     ------------------------------------------- */
				
				case("OP WITH CONST"):{
					boolean isArgConst = operand.startsWith("#");
					if(isArgConst == true) operand = operand.substring(1);
					else{
						sw.write("Line "+(i+1)+": Nieprawid³owy format sta³ej\n");
						error = -1;
						break;
					}
					
					int constant = 0;
					try{
						constant = Integer.parseInt(operand);
					}
					catch(Exception e){
						sw.write("Line "+(i+1)+": Nieprawid³owy format argumentu\n");
						error = -1;
						break;
					}
											
					if(constant<min || constant>max){
						sw.write("Line "+(i+1)+": Liczba spoza zakresu\n");
						error = -1;
						break;
					}
					else{
						singleCode = (orderList.get(command).getCode())<<24;
						code.add(singleCode);
						code.add(constant);
						break;
					}
				}

				/*-----------------------------------------------
				 * TWORZENIE INSTRUKCJI DLA NO ARGUMENT OPERATION
				 ----------------------------------------------*/
				 
				case("NAO"):{ 
					singleCode = (orderList.get(command).getCode())<<24;
						code.add(singleCode);
						break;
				}
				
				/*--------------------------------------
				 * TWORZENIE INSTRUKCJI DLA JUMP CONTROL
				 * ORAZ TABLICY SKOKÓW
 				 -------------------------------------*/
				
				case("JUMP CONTROL"):{
						singleCode = (orderList.get(command).getCode())<<24;
						code.add(singleCode);
						jumps.add(new Jump(operand,code.size()));
						break;
				}
				
				/*--------------------------------------------
				 * TWORZENIE TABLICY ETYKIET
				 -------------------------------------------*/
				
				case("ETYKIETA"):{
					int x = code.size();
					labels.add(new Label((command.split(":"))[0].trim(),x,i));
					break;
				}
				
				/*--------------------------------------------
				 * TWORZENIE INSTRUKCJI DLA APB
				 -------------------------------------------*/
				
				case("APB"):{
					singleCode = (orderList.get(command).getCode())<<24;
					if(operand.equals("IN")){
						singleCode += gpi;
						code.add(singleCode);
						break;
					}
					else if(operand.equals("OUT")){
						singleCode += gpo;
						code.add(singleCode);
						break;
					}
					else{
						
						/*-------------------------------
						 *  "." jest znakiem diakrytycznym, a takie
						 *  nale¿y porównywaæ z u¿yciem [ ] 
						 --------------------------------*/

						String perip = null;
						int off = 0;
						int addr = 0;
						String offset = null;
						try{
							Pattern pattern = Pattern.compile(apb_op);
							Matcher matcher = pattern.matcher(operand);
							matcher.matches();
							perip = matcher.group(1).toUpperCase();
							addr = Integer.parseInt(matcher.group(2));
							offset = matcher.group(3).toUpperCase();
						}
						catch(Exception e){
							sw.write("Line "+(i+1)+": "+"Nieprawid³owy argument - "+e.getClass()+" "+e.getMessage()+"\n");
							error = -1;
							break;
						}
						switch(perip){
						
						/*---------------------------------
						 * OBS£UGA TIMERÓW
						 --------------------------------*/
						
						case("T"):{
							switch(offset){
								case("TYPE"):{
									off = TYPE;
									break;
								}
								case("PT"):{
									off = PT;
									break;
								}
								case("IN"):{
									off = IN;
									break;
								}
								case("Q"):{
									off = Q;
									break;
								}
								case("ET"):{
									off=ET;
									break;
								}
								default:{
									sw.write("Line "+(i+1)+": "+"Nieprawid³owy modyfikator: "+offset+"\n");
									error = -1;
									break switchLoop;
								}
							}
							
							if(addr>=((timerAdress[0] & 0x000007f8)>>3) && addr <= ((timerAdress[1] & 0x000007f8)>>3)){
								// okreœlenie Access Type
								if((off == Q || off == ET) && command.equals("APB_WR")){
									sw.write("Line "+(i+1)+": "+"Brak mo¿liwoœci zapisu pod ten adres.\n");
									error = -1;
									break switchLoop;
								}
								singleCode += ((timerAdress[0]+addr*8 & 0x0000fff8))+off;
								code.add(singleCode);
								break switchLoop;
							}
							else{
								sw.write("Line "+(i+1)+": "+"Adres spoza zakresu\n");
								error = -1;
								break switchLoop;
							}
						}
						
						/*-----------------------------
						 * OBS£UGA LICZNIKÓW
						 ----------------------------*/
						
						case("C"):{
							//off = Integer.parseInt(offset);
							
							switch(offset){
								case("CU"):{
									off=CU;
									break;
								}
								case("CD"):{
									off = CD;
									break;
								}
								case("R"):{
									off = R;
									break;
								}
								case("S"):{
									off = S;
									break;
								}
								case("PV"):{
									off = PV;
									break;
								}
								case("CV"):{
									off = CV;
									break;
								}
								case("QD"):{
									off = QD;
									break;
								}
								case("QU"):{
									off=QU;
									break;
								}								
								default:{
									sw.write("Line "+(i+1)+": "+"Nieprawid³owy modyfikator: "+offset+"\n");
									error = -1;
									break switchLoop;
								}
							}
							
							if(addr>=((counterAdress[0] & 0x000007f8)>>3) && addr <= ((counterAdress[1] & 0x000007f8)>>3)){
								if(((off >= CU && off<=PV) && command.equals("APB_RD"))||(off>=CV && command.equals("APB_WR"))){
									sw.write("Line "+(i+1)+": "+"Brak mo¿liwoœci zapisu pod ten adres.\n");
									error = -1;
									break switchLoop;
								}
							singleCode += ((counterAdress[0]+addr*8 & 0x0000fff8))+off;
							code.add(singleCode);
							break switchLoop;
							}
							else{
									System.out.println("B³¹d APB");
									sw.write("Line "+(i+1)+": "+"Adres spoza zakresu\n");
									error = -1;
									break switchLoop;
							}
						}
						default:{
							sw.write("Line "+(i+1)+": "+"Nieprawdi³owy argument\n");
							error = -1;
							break switchLoop;
						}
						}
					}

				}
				
				default:{
					sw.write("Line "+(i+1)+": "+"Nierozpoznany b³¹d\n");
					error = -1;
					break;
				}					
			}
		}
		
		/*----------------------------------
		 * KONIEC PIERWSZEJ FAZY KOMPILACJI
		 ---------------------------------*/
		
		try{
			System.out.println("Koniec petli glownej");
			System.out.println("Rozmiar tablicy skoków: "+jumps.size());
			System.out.println("Rozmiar tablicy etykiet: "+labels.size());
			
			/*-----------------------------------------------------------------
			 * ZAPEZBIECZENIE PRZED POJAWIENIEM SIÊ KILKU TAKICH SAMYCH ETYKIET
			 -----------------------------------------------------------------*/
			
		for(int i=0; i<labels.size()-1; i++){
			System.out.println("Jestem w sprawdzaniu etykiet");
			String label = labels.get(i).getLabel();
			//int nlabel = labels.get(i).getLine();
			for(int j=i+1;j<labels.size();j++){
				String llabel = labels.get(j).getLabel();
				//int nllabel = labels.get(j).getLine();
				System.out.println("Porównanie etykiet :"+label+1+" "+llabel+1);
				if(label.equals(llabel)){
					error=-1;
					sw.write("Line "+(labels.get(i).getInstrNumb()+1)+" i "+(labels.get(j).getInstrNumb()+1)+": Powtarzaj¹ce siê etykiety.\n");
				}
			}
		}
			
			/*-----------------------------------
			 * AKTUALIZACJA ADRESÓW DLA SKOKÓW
			 -----------------------------------*/
			
		for(Jump j : jumps){
			String jump = j.getJump();
			int line = j.getLine();
			System.out.println("Analizowany skok: "+jump);
			int loop = 0;
			for(Label l : labels){
				loop++;
				String label = l.getLabel();
				int lLine = l.getLine();
				System.out.println("Analizowana etykieta: "+label);
				if(jump.equals(label) == true){
					System.out.println("Znaleziona etykieta: "+label);
					int c = code.get(line-1);
					c += lLine;
					code.set(line-1, c);
					break;
				}
				else if(loop==labels.size()){
					System.out.println("Jestem w elsie");
					sw.write("Line "+(line)+": Nie znaleziono etykiety\n");
					error = -1;
				}
			}
		}
			
		}
		catch(NullPointerException n){};
		
		try {
			sw.flush();
			errors = sw.toString();
			sw.close();
		} catch (IOException e){};
		return code;
	}
	
	
	/*---------------------------------------------------
	 * KLASA WEWNÊTRZNA PRZECHOWUJ¥CA INFORMACJÊ O SKOKU
	 ---------------------------------------------------*/
	
	private class Jump{
		private String jump;
		private int line;
		
		public Jump(String jump, int line){
			this.jump = jump;
			this.line = line;
		}
		
		public String getJump(){
			return jump;
		}
		
		public int getLine(){
			return line;
		}
	}
	
	/*------------------------------------------------------
	 * KLASA WEWNÊTRZNA PRZECHOWUJACA INFORMACJÊ O ETYKIECIE 
	 ------------------------------------------------------*/
	
	private class Label{
		private String label;
		private int line;
		private int instrNumb;
		
		public Label(String label, int line, int instrNumb){
			this.label = label;
			this.line = line;
			this.instrNumb = instrNumb;
		}
		
		public int getInstrNumb(){
			return instrNumb;
		}
		
		public String getLabel(){
			return label;
		}
		
		public int getLine(){
			return line;
		}
	}
}
