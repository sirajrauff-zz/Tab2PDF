package cse2311;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public class SampleUse {

	public static void main(String[] args) throws DocumentException, InvalidFormatException, IOException {
		
		File file = new File("rememberingrain.txt");
		File file2 = new File("moonlightsonata.txt");
		//File file3 = new File("testcase.txt");
	
		Parser c = new Parser();
		Tablature guitar = c.parse(file);
		guitar.setFont(BaseFont.createFont("Monospace.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		
		Tablature guitar2 = c.parse(file2);
		guitar2.setFont(BaseFont.createFont("Monospace.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		
		//Tablature guitar3 = c.parse(file3);
		//guitar2.setFont(BaseFont.createFont("Monospace.ttf",
			//	BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		
		try {
			
			PdfOutputCreator.makePDF(guitar);
			PdfOutputCreator.makePDF(guitar2);
			//PrintToPDF.makePDF(guitar);
			System.out.println("printing");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		System.out.println(guitar.getTitle());
	}
}
