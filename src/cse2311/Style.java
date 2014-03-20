package cse2311;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;



public class Style {
    
    	public BaseFont my_Fontface;
	public int my_Font_Size = 8;


	
	public float dist_Staffs =30f;
	public float dist_Lines=7f;
	
	float leftMargin = 36f;
	float rightMargin = 36f;
	
	
	public Document document ;

	
	public Style(Document documents){
		try {
			my_Fontface = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, false);
                        
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
                
		this.document =documents;
	}
	
	public float getPrint_Space() {
	
		return document.getPageSize().getWidth() - leftMargin -rightMargin;
	}

	public float get_width(char char1) {
		
		return my_Fontface.getWidthPoint(char1, my_Font_Size);
	}

	public float get_height() {
		return (my_Fontface.getFontDescriptor(BaseFont.ASCENT, my_Font_Size) - my_Fontface
				.getFontDescriptor(BaseFont.DESCENT, my_Font_Size));
	}

	
}
