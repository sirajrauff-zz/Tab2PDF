package testing;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import cse2311.Style;

public class StyleTest {
	public Style test;
	public Document document ;
	public BaseFont font;
	
	@Before
	public void setUp() throws Exception {
		document = new Document();
		test = new Style();
		font = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, false);
	}
	
	@Test
	public void testStyle() { //Please note the documentexception and ioexception shouldn't occur ever
		try {
			assertEquals(test.myFontface, BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false));
		}  
		catch (DocumentException | IOException e) { }
	}

	@Test
	public void testGetPrintSpace() {
		float realSize = 595f - 72f; //combined size of both right and left margins are 72f
		assertEquals(test.getPrintSpace(), realSize, 0.0);
		
		test.setLeftMargin(7f);
		test.setRightMargin(20f);
		
		realSize = 595f - 27f;
		assertEquals(test.getPrintSpace(), realSize, 0.0);
	}

	@Test
	public void testGetWidth() {
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
	public void testGetLeftMargin() {
		assertEquals(test.getLeftMargin(), 36f, 0.0);
	}

	@Test
	public void testSetLeftMargin() {
		assertEquals(test.getLeftMargin(), 36f, 0.0);
		test.setLeftMargin(321f);
		assertEquals(test.getLeftMargin(), 321f, 0.0);
	}
	
	@Test
	public void testGetRightMargin() {
		assertEquals(test.getRightMargin(), 36f, 0.0);
	}
	
	@Test
	public void testSetRightMargin() {
		assertEquals(test.getRightMargin(), 36f, 0.0);
		test.setRightMargin(3211f);
		assertEquals(test.getRightMargin(), 3211f, 0.0);
	}
	
	@Test
	public void testSetFontSize() {
		assertEquals(test.getFontSize(), 8);
		test.setFontSize(354);
		assertEquals(test.getFontSize(), 354);
	}
	
	@Test
	public void testGetFontSize() {
		assertEquals(test.getFontSize(), 8);
	}
	
	@Test
	public void testSetMyTitleSize() {
		assertEquals(test.getMyTitleSize(), 24);
		test.setMyTitleSize(753);
		assertEquals(test.getMyTitleSize(), 753);
	}
	
	@Test
	public void testGetMyTitleSize() {
		assertEquals(test.getMyTitleSize(), 24);
	}
	
	@Test
	public void testSetMySubTitleSize() {
		assertEquals(test.getMySubTitleSize(), 16);
		test.setMySubTitleSize(457);
		assertEquals(test.getMySubTitleSize(), 457);
	}
	
	@Test
	public void testGetMySubTitleSize() {
		assertEquals(test.getMySubTitleSize(), 16);
	}
	
	@Test
	public void testSetMeasureDistance() {
		assertEquals(test.getSectionDistance(), 30f, 0.0);
		test.setMeasureDistance(98f);
		assertEquals(test.getSectionDistance(), 98f, 0.0);
	}
	
	@Test
	public void testGetSectionDistance() {
		assertEquals(test.getSectionDistance(), 30f, 0.0);
	}
	
	@Test
	public void testSetLineDistance() {
		assertEquals(test.getLineDistance(), 7f, 0.0);
		test.setLineDistance(32f);
		assertEquals(test.getLineDistance(), 32f, 0.0);
	}
	
	@Test
	public void testGetLineDistance() {
		assertEquals(test.getLineDistance(), 7f, 0.0);
	}
	
	@Test
	public void testGetHeight() {
		int myFontSize = 8;
		float myHeight= (font.getFontDescriptor(BaseFont.ASCENT, myFontSize) - font.
				getFontDescriptor(BaseFont.DESCENT, myFontSize));
		
		assertEquals(test.getHeight(), myHeight, 0.0);
	}
}
