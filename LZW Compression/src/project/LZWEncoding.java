package project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class LZWEncoding {
	static byte [] byteIndex = new byte [2];
	
	public static void main(String args[]) throws IOException{
		
		//Checking if 2 command line arguments are given
		if(args.length != 2){
			System.out.println("Command line arguments missing..!!!\nEnter <filename>.txt <bitlenght> and try again");
			System.exit(0);
		}
		
		//Initializing variables
		String inputFile = args[0], outputFile;
		double bitLength = Integer.parseInt(args[1]);
		double maxTableSize = Math.pow(2.0,bitLength);
		String[] table = new String[(int) maxTableSize];
		String currentString = "";
		int index, symbol;
		
		//creating output file in the required format
		int dotIndex = inputFile.lastIndexOf('.');
		outputFile = inputFile.substring(0, dotIndex)+".lzw";
		
		try {
			//Assigning first 256 ASCII characters
			for(index=0;index<=255;index++)
				table[index] = "" + (char)index;
				
			index = 256;
			
			//creating a file object to read input data from a given file
			FileInputStream readInput = new FileInputStream(inputFile);
			
			//Creating a file writer object to write data into the output file
			FileOutputStream writeData = new FileOutputStream(outputFile);
			
			//reading data from file
			System.out.println("Reading "+inputFile);
			symbol = readInput.read();
			
			//while there is still data to read
			System.out.println("compressing...");
		    while(symbol != -1){
		    	
		    	if(Arrays.asList(table).contains((currentString)+(char)symbol))
		    		currentString = currentString + (char)symbol;
		    	else{
		    		//writing the resulting integer (binary string) to the file
		    		toByte(Arrays.asList(table).indexOf(currentString));
		    		writeData.write(byteIndex);
		    		if(index < maxTableSize){
		    			//Insert data into the table
		    			table[index] = currentString+(char)symbol;
		    			index++;
		    		}
		    		currentString = ""+(char) symbol;
		      }
		      symbol = readInput.read();
		    }
		    //writing the last integer (binary string) to the file
    		toByte(Arrays.asList(table).indexOf(currentString));
    		writeData.write(byteIndex);
    		
    		//closing file objects
    		readInput.close();
    		writeData.close();
    		
		} catch (ArrayIndexOutOfBoundsException e) {
			//If the given file does not exists
			System.out.println("Bit Length should atleast be 9.\nTry Again...!!!");
			System.exit(0);
		} catch (FileNotFoundException e) {
			//If the given file does not exists
			System.out.println("File Not Found\nTry Again..!!");
			System.exit(0);
		}
		finally{
			System.out.println("Output file "+outputFile+" created.");
		}
	}

	private static void toByte(int index) {
		byteIndex[1] = (byte) (index & 0xFF);
		byteIndex[0] = (byte) ((index >> 8) & 0xFF);
	}
}