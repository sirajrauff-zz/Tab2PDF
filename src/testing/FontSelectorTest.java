package testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import backEnd.FontSelector;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

/**
 * @author Jason
 * Test class for the FontSelector Class
 */
public class FontSelectorTest {
	
	/**
	 * Test case for method getFont(INT index) which returns a font type given an index. This method is tested
	 * by calling the method and asserting that it returns the correct font type.
	 */
	@Test
	public void testGetFont() {
		try {
			assertEquals(FontSelector.getFont(18), BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1257, BaseFont.EMBEDDED));
			assertEquals(FontSelector.getFont(19), BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1257, BaseFont.EMBEDDED));
			assertEquals(FontSelector.getFont(20), BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1257, BaseFont.EMBEDDED));
			
			assertEquals(FontSelector.getFont(3), BaseFont.createFont("fonts/" + "PWPerspective" + ".ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
			assertEquals(FontSelector.getFont(6), BaseFont.createFont("fonts/" + "Dolce Vita" + ".ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
			assertEquals(FontSelector.getFont(14), BaseFont.createFont("fonts/" + "Magnolia Light" + ".ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
			assertEquals(FontSelector.getFont(17), BaseFont.createFont("fonts/" + "gunplay rg" + ".ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		} catch (DocumentException | IOException e) { }
	}
}