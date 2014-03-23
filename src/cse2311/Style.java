package cse2311;

import java.io.IOException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public class Style {
    public BaseFont myFontface;
	private int myFontSize = 8, myTitleSize = 24, mySubTitleSize = 16;
	private float distMeasures = 30f, distLines = 7f;
	float leftMargin = 36f, rightMargin = 36f;

	public Style() {
		try {
			myFontface = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public float getPrintSpace() {
		return 595f - leftMargin - rightMargin;
	}
	
	public void setleftMargin(float userSize) {
		 leftMargin= userSize;
	}
	
	public float getleftMargin() {
		return leftMargin;
        }
        
        public void setrightMargin(float userSize) {
		 rightMargin= userSize;
	}
	
	public float getrightMargin() {
		return rightMargin;
        }
        
        public void setFontSize(int userSize) {
		myFontSize = userSize;
	}
	
	public int getFontSize() {
		return myFontSize;
	}
	
    public void setMyTitleSize(int userSize) {
    	myTitleSize = userSize;
	}
	
	public int getMyTitleSize() {
		return myTitleSize;
	}
    
	public void setMySubTitleSize(int userSize) {
		mySubTitleSize = userSize;
	}
	
	public int getMySubTitleSize() {
		return mySubTitleSize;
	}
	
	public void setMeasureDistance(float userDistance) {
		distMeasures = userDistance;
	}
	
	public float getSectionDistance() {
		return distMeasures;
	}
	
	public void setLineDistance(float userDistance) {
		distLines = userDistance;
	}
	
	public float getLineDistance() {
		return distLines;
	}

	public float getWidth(char char1) {
		return myFontface.getWidthPoint(char1, myFontSize);
	}

	public float getHeight() {
		return (myFontface.getFontDescriptor(BaseFont.ASCENT, myFontSize) - myFontface
				.getFontDescriptor(BaseFont.DESCENT, myFontSize));
	}
}
