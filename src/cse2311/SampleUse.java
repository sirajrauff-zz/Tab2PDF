

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
		
		Tablature t = c.readFile(file);
		Style s = new Style();
		MusicSheet ms = new MusicSheet(t);
		
		PdfOutputCreator.makePDF(ms, s);
	
		
	}
	
}
