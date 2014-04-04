package backEnd;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

/**
 * @author Waleed Azhar
 */
public class FontSelector {
	final static public String Fonts[] = {
		"(FREEWIND)", "Moon Flower", "Carnevalee Freakshow",	
		"PWPerspective", "Champagne & Limousines", "Park Lane NF", "Dolce Vita", 
		"Please write me a song", "Duty Cycle",	"Prisma", "Font (el&font bubble)",	
		"Sketch_Block", "Freshman",	"budmo jiggler", "Magnolia Light", "geek",
		"Minecraftia", "gunplay rg", "Helvetica", "Times New Roman", "Courier"};
		   
   public static BaseFont getFont(int index) {
      try {
          if (Fonts[index] == "Helvetica") {
        	  return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1257, 
                  BaseFont.EMBEDDED);
          } else if (Fonts[index] == "Times New Roman") {
        	  return BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1257, 
                  BaseFont.EMBEDDED);
          } else if (Fonts[index] == "Courier") {
        	  return  BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1257, 
                  BaseFont.EMBEDDED);
          }
          return BaseFont.createFont("fonts/" + Fonts[index] + ".ttf",
                  BaseFont.IDENTITY_H,
                  BaseFont.EMBEDDED);
      } catch (DocumentException | IOException ex) {
          Logger.getLogger(FontSelector.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
   } 
}
