package testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import backEnd.Style;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

/**
 * @author Jason
 * Test class for the Style class
 */
public class StyleTest {
	private Style test;
	private BaseFont font;
	
	/**
	 * @throws IOException - as specified in the API, this exception will never occur
	 * @throws DocumentException - as specified in the API, this exception will never occur
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		test = new Style();
		font = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, false);
	}

	/**
	 * Test case for the method getFontSize() which returns the current value of the font size. The default
	 * value for font size is 8. 
	 */
	@Test
	public void testGetFontSize() {
		assertEquals(test.getFontSize(), 8);
	}

	/**
	 * Test case for the method getHeight() which returns the height of the printable space on the page.
	 */
	@Test
	public void testGetHeight() {
		int myFontSize = 8;
		
		float myheight = (font.getFontDescriptor(BaseFont.ASCENT, myFontSize) - font.
			getFontDescriptor(BaseFont.DESCENT, myFontSize));
		
		assertEquals(test.getHeight(), myheight, 0.0);
	}

	/**
	 * Test case for the method getLeftMargin() which returns the left margin size. The default value for 
	 * it is 36f.
	 */
	@Test
	public void testGetLeftMargin() {
		assertEquals(test.getLeftMargin(), 36f, 0.0);
	}
	
	/**
	 *  Test case for the method getLineDistance() which returns the distance between lines. The default
	 * value is 7f
	 */
	@Test
	public void testGetLineDistance() {
		assertEquals(test.getLineDistance(), 7f, 0.0);
	}

	/**
	 * Test case for method getMySubtitleSize() which returns the current title size. The default size is 
	 * 16
	 */
	@Test
	public void testGetMySubTitleSize() {
		assertEquals(test.getMySubTitleSize(), 16);
	}

	/**
	 * Test case for method getMyTitleSize() which returns the current title size. The default size is 
	 * 24
	 */
	@Test
	public void testGetMyTitleSize() {
		assertEquals(test.getMyTitleSize(), 24);
	}

	/**
	 * Test case for the getPrintSpace() method which returns the printSpace which is calculated as follows
	 * 595f - leftMargin - rightMargin. This method is tested by asserting that it returns the correct value
	 * of the printSpace.
	 */
	@Test
	public void testGetPrintSpace() {
		/* left margin = 36f
		 * right margin = 36f*/
		float realSize = 595f - 72f; //combined size of both right and left margins are 72f
		assertEquals(test.getPrintSpace(), realSize, 0.0);
		
		test.setLeftMargin(7f);
		test.setRightMargin(20f);
		
		realSize = 595f - 27f;
		assertEquals(test.getPrintSpace(), realSize, 0.0);
	}

	/**
	 * Test case for the method getRightMargin() which returns the right margin size. The default value for 
	 * it is 36f
	 */
	@Test
	public void testGetRightMargin() {
		assertEquals(test.getRightMargin(), 36f, 0.0);
	}
	
	/**
	 * Test case for the method getSectionDistance() which returns the distance between measures. The default
	 * value is 30f
	 */
	@Test
	public void testGetSectionDistance() {
		assertEquals(test.getSectionDistance(), 30f, 0.0);
	}

	/**
	 * Test case for the method getWidth(char char1) which returns the width of a specified character.
	 * It is tested by asserting that the returned value is equal to the expected value (which is obtained
	 * through the iText library method)
	 */
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
	
	/**
	 * Test case for the method setFontSize(INT userSize) which changes the font size. The default value
	 * for font size is 8.
	 */
	@Test
	public void testSetFontSize() {
		assertEquals(test.getFontSize(), 8);
		test.setFontSize(354);
		assertEquals(test.getFontSize(), 354);
	}
	
	/**
	 * Test case for the method setLeftMargin(float userSize) which changes the left margin size.
	 * This method is tested by changing the margin size and asserting that it is equal to the expected
	 * value.
	 */
	@Test
	public void testSetLeftMargin() {
		assertEquals(test.getLeftMargin(), 36f, 0.0);
		test.setLeftMargin(321f);
		assertEquals(test.getLeftMargin(), 321f, 0.0);
	}
	
	/**
	 * Test case for the method setLineDistance(float userDistance) which changes the distance between each line.
	 * The default value is 7f.
	 */
	@Test
	public void testSetLineDistance() {
		assertEquals(test.getLineDistance(), 7f, 0.0);
		test.setLineDistance(32f);
		assertEquals(test.getLineDistance(), 32f, 0.0);
	}
	
	/**
	 * Test case for the method setMeasureDistance(float userDistance) which sets the distance between measures.
	 * The default value is 30f.
	 */
	@Test
	public void testSetMeasureDistance() {
		assertEquals(test.getSectionDistance(), 30f, 0.0);
		test.setMeasureDistance(98f);
		assertEquals(test.getSectionDistance(), 98f, 0.0);
	}
	
	/**
	 * Test case for the method setMySubtitleSize(INT userSize) which changes the title size. The default 
	 * size is 16.
	 */
	@Test
	public void testSetMySubTitleSize() {
		assertEquals(test.getMySubTitleSize(), 16);
		test.setMySubTitleSize(457);
		assertEquals(test.getMySubTitleSize(), 457);
	}
	
	/**
	 * Test case for the method setMyTitleSize(INT userSize) which changes the title size. The default 
	 * size is 24.
	 */
	@Test
	public void testSetMyTitleSize() {
		assertEquals(test.getMyTitleSize(), 24);
		test.setMyTitleSize(753);
		assertEquals(test.getMyTitleSize(), 753);
	}
	
	/**
	 * Test case for the method setRightMargin(float userSize) which changes the right margin size.
	 * This method is tested by changing the margin size and asserting that it is equal to the expected
	 * value.
	 */
	@Test
	public void testSetRightMargin() {
		assertEquals(test.getRightMargin(), 36f, 0.0);
		test.setRightMargin(3211f);
		assertEquals(test.getRightMargin(), 3211f, 0.0);
	}

	/**
	 * Test case for the constructor of the style class which initializes the default font type as Helvetica.
	 * This method is tested by asserting that the style instance test has the expected default font type. 
	 */
	@Test
	public void testStyle() { //Please note the DocumentException and IOException shouldn't occur ever
		try {
			assertEquals(test.myFontface, BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false));
		}  
		catch (DocumentException | IOException e) { } 
	}
}