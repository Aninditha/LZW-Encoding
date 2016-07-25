package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class LZWDecoding {
	@SuppressWarnings("resource")
	public static void main(String args[]) throws IOException{
		
		//Checking if 2 command line arguments are given
		if(args.length != 2){
			System.out.println("Command line arguments missing..!!!\nEnter <filename>.lzw <bitlenght> and try again");
			System.exit(0);
		}

		//Initializing variables
		String inputFile = args[0], outputFile;
		double bitLength = Integer.parseInt(args[1]);
		double maxTableSize = Math.pow(2.0,bitLength);
		String[] table = new String[(int) maxTableSize];
		String currentString = "", newString = "";
		int index, code;
		int offset = 0;
		long length;
		
		//creating output file in the required format
		int dotIndex = inputFile.lastIndexOf('.');
		outputFile = inputFile.substring(0, dotIndex)+"_decoded.txt";
		
		try {
			//Assigning first 256 ASCII characters
			for(index=0;index<=255;index++)
				table[index] = "" + (char)index;
			index = 256;
			
			//creating a file object to read input data from a given file
			File file = new File(inputFile);
			
			InputStream readInput = new FileInputStream(inputFile);
			length = file.length();
			//Initializing a byte array
			byte[] byteIndex = new byte[(int) length];
			
			//Creating a file writer object to write data into the output file
			FileWriter writeData = new FileWriter(outputFile);
			
			//reading all the bytes from the file and storing it in a byte array
			System.out.println("Reading "+inputFile);
			System.out.println("decompressing");
			readInput.read(byteIndex, 0,(int) length);
			code = toInteger(byteIndex,offset,2);
			offset += 2;
			currentString = table[code];
			
			//writing the resulting code to the file
			writeData.write(currentString);
			writeData.flush();
			
			//while there is still data to read
		    while(offset < file.length()) {
		    	
				code = toInteger(byteIndex,offset,2);
				offset += 2;
		    	if(code >= index)
		    		newString = currentString + currentString.charAt(0);
		    	else
		    		newString = table[code];
		    	
		    	//writing the resulting code to the file
		    	writeData.write(newString);
		    	writeData.flush();
		    	
		    	//Insert data into the table
		    	if(index<maxTableSize){
		    		table[index] = currentString + newString.charAt(0);
		    		index += 1;
		    	}
		    	currentString = newString;
		    }
		} catch (ArrayIndexOutOfBoundsException e) {
			//If the given file does not exists
			System.out.println("Bit Length should atleast be 9.\nTry Again...!!!");
			System.exit(0);
		} catch (FileNotFoundException e) {
			//If the given file does not exists
			System.out.println("File Not Found\nTry Again..!!");
			System.exit(0);
		} finally{
			System.out.println("Output file "+outputFile+" created..!!");
		}
	}

	private static int toInteger(byte[] byteIndex,int start, int length) {
		
		int value = 0;
        for (int i = start; i < (start+length); i++) {
            value=value<<8;
            value=value|(byteIndex[i] & 0xFF);
        }
        return value;
	}
}
