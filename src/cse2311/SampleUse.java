

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;


public class SampleUse {

	public static void main(String[] args) throws DocumentException, IOException {
		
		
		File file = new File("rememberingrain.txt");
		File file2 = new File("moonlightsonata.txt");
		File file3 = new File("testcase.txt");
		File file4 = new File("test2.txt");
		File file5 = new File("test3.txt");
	
		
		
		Parser c = new Parser();
		
		Tablature t = c.readFile(file2);//tab holds data


		

		//!IMPORTANT holds the doument used by pdf out creater
		Style s = new Style(new Document(PageSize.A4));
	
		//holds output preferences(has defautls) like distances and font for notes and margins;
		
		
		MusicSheet ms = new MusicSheet(t,s);// formats the data for pdf output for the given style
	
		
		PdfOutputCreator pdfout = new PdfOutputCreator("");//takes the output laction in the construter

		pdfout.makePDF(ms);//make the pdf
	
	}
	
}
