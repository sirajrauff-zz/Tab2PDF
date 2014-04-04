package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.junit.Test;

import backEnd.Parser;
import backEnd.PdfOutputCreator;
import backEnd.Style;
import backEnd.Tablature;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * @author Jason
 * Test class for the PdfOutputCreator class
 */
public class PdfOutputCreatorTest {
	
	/**
	 * Test case for the method testMakePDF which uses the MusicSheet, Tablature, Parser, and Style classes 
	 * to create a PDF file of a given text file Tablature. This method is tested by converting 6 Tablatures to 
	 * PDFs with each Tablature testing most possible inputs and manually asserting it is correct.
	 * 
	 * This testing case does not involve the use of a GUI, so the Tablature is not customizable
	 * 
	 * The PDF is also read and compared with what is expected in the file. Please note that due to the 
	 * current capabilities of java only the texts could be extracted from the PDFs
	 * 
	 * The output PDFs are sent to the folder test files/pdfoutputcreator.
	 */
	@Test
	public void testMakePDF() {
		try {
			PdfReader input;
			String page;
			
			/* First test case tests what happens when an empty file is converted. The PDF output
			 * Should have as its title and subtitle as the string "Default"*/
			Tablature tab = new Tablature();                                           
			Parser parse = new Parser();
			PdfOutputCreator create = new PdfOutputCreator();
			File out = new File("test files/PdfOutputCreator/pdfOutput_test1.txt");
			tab = parse.readFile(out);
			Style style = new Style();
			create.makePDF(tab, style);
			
			FileChannel source = null; //this is used to rename the temp file that is created
            FileChannel destination = null;
            
        	try {
                source = new FileInputStream(new File("temp.pdf")).getChannel();
                destination = new FileOutputStream(new File("test files/PdfOutputCreator/pdfOutput_test1.pdf")).getChannel();
                destination.transferFrom(source, 0, source.size());
        	} catch(Exception ex) { } 
			
			input = new PdfReader("test files/PdfOutputCreator/pdfOutput_test1.pdf");  //Extracts the texts from the created PDFs
			page = PdfTextExtractor.getTextFromPage(input, 1);
			input.close();

			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test1.pdf").exists());   //asserts that the PDF was created
			assertEquals(page, "Default\nDefault");
			
			/* Second test case tests what happens when the file only contains a title and subtitle. The PDF output
			 * should have as its title as "pdfOutput test2"  and subtitle as the string "Jim Mateos"*/
			Tablature tab1 = new Tablature();
			PdfOutputCreator create1 = new PdfOutputCreator();
			Parser parse1 = new Parser();
			File out1 =  new File("test files/PdfOutputCreator/pdfOutput_test2.txt");
			tab1 = parse1.readFile(out1);
			Style style1 = new Style();
			create1.makePDF(tab1, style1);
			
			source = destination = null;
            
	       	try {
               source = new FileInputStream(new File("temp.pdf")).getChannel();
               destination = new FileOutputStream(new File("test files/PdfOutputCreator/pdfOutput_test2.pdf")).getChannel();
               destination.transferFrom(source, 0, source.size());
	       	} catch(Exception ex) { } 
       	
			input = new PdfReader("test files/PdfOutputCreator/pdfOutput_test2.pdf"); //Extracts the texts from the created PDFs
			page = PdfTextExtractor.getTextFromPage(input, 1);
			input.close();
			
			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test2.pdf").exists()); //asserts that the PDF was created
			assertEquals(page, "pdfOutput test2\nJim Mateos"); //asserts that the PDF contains the expected stuff

			/* Third test case tests what happens when the file has a title, subtitle and measures that contain
				only numbers*/
			Tablature tab2 = new Tablature();
			PdfOutputCreator create2 = new PdfOutputCreator();
			Parser parse2 = new Parser();
			File out2 =  new File("test files/PdfOutputCreator/pdfOutput_test3.txt");
			tab2 = parse2.readFile(out2);
			Style style2 = new Style();
			create2.makePDF(tab2, style2);
			
			source = destination = null;
             
	       	try {
               source = new FileInputStream(new File("temp.pdf")).getChannel();
               destination = new FileOutputStream(new File("test files/PdfOutputCreator/pdfOutput_test3.pdf")).getChannel();
               destination.transferFrom(source, 0, source.size());
	       	} catch(Exception ex) { } 
			
			input = new PdfReader("test files/PdfOutputCreator/pdfOutput_test3.pdf");
			page = PdfTextExtractor.getTextFromPage(input, 1); //Extracts the texts from the created PDFs
			input.close();
			
			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test3.pdf").exists()); //asserts that the PDF was created
			assertEquals(page, "pdfOutput test3\n" //asserts that the PDF contains the expected content
					+ "Ludwig van Beethoven\n" 
					+ "0 5 8 5 2\n"
					+ "1 1 5 5 1 4 1\n"
					+ "2 2 2 2\n"
					+ "2 1 4 1 4\n"
					+ "3 0 3\n"
					+ "0 0");															
			
			/* Fourth test case tests what happens when the file has a title, subtitle and measures that has
			 * more than just numbers*/
			Tablature tab3 = new Tablature();
			PdfOutputCreator create3 = new PdfOutputCreator();
			Parser parse3 = new Parser();
			File out3 =  new File("test files/PdfOutputCreator/pdfOutput_test4.txt");
			tab3 = parse3.readFile(out3);
			Style style3 = new Style();
			create3.makePDF(tab3, style3);
			
			source = destination = null;
             
	       	try {
               source = new FileInputStream(new File("temp.pdf")).getChannel();
               destination = new FileOutputStream(new File("test files/PdfOutputCreator/pdfOutput_test4.pdf")).getChannel();
               destination.transferFrom(source, 0, source.size());
	       	} catch(Exception ex) { } 
			
			input = new PdfReader("test files/PdfOutputCreator/pdfOutput_test4.pdf");
			page = PdfTextExtractor.getTextFromPage(input, 1); //Extracts the texts from the created PDFs
			input.close();
			
			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test4.pdf").exists()); //asserts that the PDF was created
			assertEquals(page, "pdfOutput test4\n" 
					+ "Jim Mateos\n"
					+ "12 3 10 0 0 7\n"
					+ "12 12 0 10 0 0 5 7\n"
					+ "5 7 2 0 2 2 0\n"
					+ "7 2\n2 3\n"
					+ "0 7 0\n"
					+ "7 5 8 5 12 8 7 5 7\n"
					+ "9 9 5 5 10 5 7 7 3 5 5 0 9 9\n"
					+ "10 7 10 7 5 5 0 0 0 10 7 10 7\n"
					+ "9 6 0 7 7 2 2 2 9 6 0\n"
					+ "8 7 7 2 8 7\n"
					+ "0 0 0 5 0 0 0 0 0 0\n"
					+ "5 8 5 12 8 0 5\n"
					+ "5 5 10 1 1 5\n"
					+ "5 5 2 2 2 5\n"
					+ "p\n"
					+ "7 7 2 2 2 2 7 7\n"
					+ "7 0 3 3 0 0 0\n"
					+ "5 0 0"); //asserts that the PDF contains the expected stuff
			
			//Additional test cases
			Tablature tab4 = new Tablature();
			PdfOutputCreator create4 = new PdfOutputCreator();
			Parser parse4 = new Parser();
			File out4 =  new File("test files/PdfOutputCreator/pdfOutput_test5.txt");
			tab3 = parse4.readFile(out4);
			Style style4 = new Style();
			create4.makePDF(tab4, style4);
			
			//Additional test cases
			Tablature tab5 = new Tablature();
			PdfOutputCreator create5 = new PdfOutputCreator();
			Parser parse5 = new Parser();
			File out5 =  new File("test files/PdfOutputCreator/pdfOutput_test6.txt");
			tab3 = parse5.readFile(out5);
			Style style5 = new Style();
			create5.makePDF(tab5, style5);
			
			source.close();
			destination.close();
			
			assertTrue(new File("temp.pdf").delete());
			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test1.pdf").delete());
			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test2.pdf").delete());
			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test3.pdf").delete());
			assertTrue(new File("test files/PdfOutputCreator/pdfOutput_test4.pdf").delete());
		} catch (IOException | DocumentException e) { }
	}
}