package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;

import cse2311.Style;

public class StyleTest {

	public Style test;
	public Document document ;
	public BaseFont font;
	
	@Before
	public void setUp() throws Exception {
		document = new Document();
		//test = new Style(document); ERROR
		font = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, false);
	}

	@Test
	public void testStyle() { //Please note the DocumentException and IOexception shouldn't occur ever
		Document fail = null;
		//Style test1 = new Style(fail); ERROR
		fail("ask TA");
	}

	@Test
	public void testGetPrintSpace() {
		float realSize = document.getPageSize().getWidth() - 72f; 
		assertEquals(test.getPrintSpace(), realSize, 0.0);
	}

	@Test
	public void testGetwidth() {
		float width = font.getWidthPoint('A', 8);
		assertEquals(test.getWidth('A'), width, 0.0);
		
		width = font.getWidthPoint('F', 8);
		assertEquals(test.getWidth('F'), width, 0.0);
		
		width = font.getWidthPoint('|', 8);
		assertEquals(test.getWidth('|'), width, 0.0);
		
		width = font.getWidthPoint('*', 8);
		assertEquals(test.getWidth('*'), width, 0.0);
	}

	@Test
	public void testGetheight() {
		int my_Font_Size = 8;
		float my_height= (font.getFontDescriptor(BaseFont.ASCENT, my_Font_Size) - font.
				getFontDescriptor(BaseFont.DESCENT, my_Font_Size));
		
		assertEquals(test.getHeight(), my_height, 0.0);
	}

	/*@Test
	public void testSetwidth() {
		test.setWidth(4f);
		fail("Don't understand the width methods");
	}*/
}
