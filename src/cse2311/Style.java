import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;



public class Style {
	public BaseFont my_Fontface;
	public int my_Font_Size = 8;
	public float my_height;
	public float my_width;
	public float my_CenterOffSet;
	
	public float dist_Staffs =7f;
	public float dist_Lines=7f;
	
	float leftMargin = 36f;
	float rightMargin = 36f;
	
	float print_Space;
	
	public Document document = new Document(PageSize.LETTER);

	
	public Style(){
		try {
			my_Fontface = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, false);
			my_height= (my_Fontface.getFontDescriptor(BaseFont.ASCENT, my_Font_Size) - my_Fontface
					.getFontDescriptor(BaseFont.DESCENT, my_Font_Size));
			my_width  = my_Fontface.getWidth( my_Font_Size);
			my_CenterOffSet = my_height/2;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public float getPrint_Space() {
		
		print_Space = document.getPageSize().getWidth() - leftMargin -rightMargin;
		return print_Space;
	}


	
}
