

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
		
		
		Parser c = new Parser();
		
		Tablature t = c.readFile(file);//tab holds data
		Tablature t2 = c.readFile(file2);//tab holds data
		Tablature t3 = c.readFile(file3);//tab holds data

		

		//!IMPORTANT holds the doument used by pdf out creater
		Style s = new Style(new Document(PageSize.A4));
		Style s2 = new Style(new Document(PageSize.A4));
		Style s3 = new Style(new Document(PageSize.A4));
		//holds output preferences(has defautls) like distances and font for notes and margins;
		
		
		MusicSheet ms = new MusicSheet(t,s);// formats the data for pdf output for the given style
		MusicSheet ms2 = new MusicSheet(t2,s2);
		MusicSheet ms3 = new MusicSheet(t3,s3);
		
		
		
		PdfOutputCreator pdfout = new PdfOutputCreator("");//takes the output laction in the construter
		PdfOutputCreator pdfout2 = new PdfOutputCreator("");
		PdfOutputCreator pdfout3 = new PdfOutputCreator("");
		
		pdfout.makePDF(ms);//make the pdf
		pdfout2.makePDF(ms2);
		pdfout3.makePDF(ms3);
	}
	
}
