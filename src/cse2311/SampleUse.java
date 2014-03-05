package cse2311;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

public class SampleUse {

	public static void main(String[] args) throws FileNotFoundException, DocumentException, InvalidFormatException {
		
		File file = new File("rememberingrain.txt");
		//File file = new File("moonlightsonata.txt");
		//File file = new File("testcase.txt");
		
		Tablature guitar = new Tablature(file);
		//guitar.printAll();
		
		try {
			PrintToPDF.makePDF(guitar);
			System.out.println("printing");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		System.out.println(Reader.title);
	}
}
