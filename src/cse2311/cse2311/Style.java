package cse2311;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;



public class Style {
	public BaseFont my_Fontface;
	private int my_Font_Size = 8;
	private float my_height;
	private float _width;
	
	public float dist_Staffs =30f;
	public float dist_Lines=7f;
	
	float leftMargin = 36f;
	float rightMargin = 36f;
	
	private float print_Space;
	
	public Document document ;

	
	public Style(Document documents ){
		try {
			my_Fontface = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, false);
			my_height= (my_Fontface.getFontDescriptor(BaseFont.ASCENT, my_Font_Size) - my_Fontface
					.getFontDescriptor(BaseFont.DESCENT, my_Font_Size));
			set_width(my_Fontface.getWidth(my_Font_Size));
			
		} catch (DocumentException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.document =documents;
		
	}

	
	
	public float getPrint_Space() {
		print_Space = document.getPageSize().getWidth() - leftMargin -rightMargin;
		return print_Space;
	}



	public float get_width(char char1) {
		_width = my_Fontface.getWidthPoint(char1, my_Font_Size);
		return _width;
		
	}



	public float get_height() {
		return (my_Fontface.getFontDescriptor(BaseFont.ASCENT, my_Font_Size) - my_Fontface
				.getFontDescriptor(BaseFont.DESCENT, my_Font_Size));
	}






	public void set_width(float _width) {
		this._width = _width;
	}
	
	


	
}
