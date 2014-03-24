package testing;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import cse2311.*;

public class PdfOutputCreatorTest {

	@Test
	public void testPdfOutputCreator() {
		assertTrue(true);
	}

	@Test
	public void testMakePDF() {
		try {
			PdfReader input;
			String page;
			
			Tablature tab = new Tablature();
			Parser parse = new Parser();
			PdfOutputCreator create = new PdfOutputCreator();
			File out =  new File("test files/PdfOutputCreator/pdfOutput_test1.txt");
			tab = parse.readFile(out);
			Style style = new Style();
			create.makePDF(tab, style);
			
			input = new PdfReader("Default.pdf");
			page = PdfTextExtractor.getTextFromPage(input, 1);
			
			assertTrue(new File("Default.pdf").exists());
			assertEquals(page, "Default\nDefault");
			
			Tablature tab1 = new Tablature();
			PdfOutputCreator create1 = new PdfOutputCreator();
			Parser parse1 = new Parser();
			File out1 =  new File("test files/PdfOutputCreator/pdfOutput_test2.txt");
			tab1 = parse1.readFile(out1);
			Style style1 = new Style();
			create1.makePDF(tab1, style1);
			
			input = new PdfReader("pdfOutput test2.pdf");
			page = PdfTextExtractor.getTextFromPage(input, 1);
			
			assertTrue(new File("pdfOutput test2.pdf").exists());
			assertEquals(page, "pdfOutput test2\nJim Mateos");
			
			Tablature tab2 = new Tablature();
			PdfOutputCreator create2 = new PdfOutputCreator();
			Parser parse2 = new Parser();
			File out2 =  new File("test files/PdfOutputCreator/pdfOutput_test3.txt");
			tab2 = parse2.readFile(out2);
			Style style2 = new Style();
			create2.makePDF(tab2, style2);
			
			input = new PdfReader("pdfOutput test3.pdf");
			page = PdfTextExtractor.getTextFromPage(input, 1);
			
			assertTrue(new File("pdfOutput test3.pdf").exists());
			assertEquals(page, "pdfOutput test3\n"
							+ "Ludwig van Beethoven\n"
							+ "0 5 8 5 2\n"
							+ "1 1 5 5 1 4 1\n"
							+ "2 2 2 2\n"
							+ "2 1 4 1 4\n"
							+ "3 0 3\n"
							+ "0 0");
			
			Tablature tab3 = new Tablature();
			PdfOutputCreator create3 = new PdfOutputCreator();
			Parser parse3 = new Parser();
			File out3 =  new File("test files/PdfOutputCreator/pdfOutput_test4.txt");
			tab3 = parse3.readFile(out3);
			Style style3 = new Style();
			create3.makePDF(tab3, style3);
			
			input = new PdfReader("pdfOutput test4.pdf");
			page = PdfTextExtractor.getTextFromPage(input, 1);
			
			assertTrue(new File("pdfOutput test4.pdf").exists());
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
								+ "5 0 0");
		} catch (IOException | DocumentException e) { e.printStackTrace(); }
	}
}
