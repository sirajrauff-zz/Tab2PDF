package cse2311;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public class Style {
    public BaseFont myFontface;
	private int myFontSize = 8,myTitleSize=24,mySubTitleSize=8;
	private float distSections = 30f, distLines = 7f;
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
	
	public void setFontSize(int userSize){
		myFontSize = userSize;
	}
	
	public int getFontSize() {
		return myFontSize;
	}
        public void setmyTitleSize(int userSize){
		myTitleSize = userSize;
	}
	
	public int getmyTitleSize() {
		return myTitleSize;
	}
        public void setmySubTitleSize(int userSize){
		mySubTitleSize = userSize;
	}
	
	public int getmySubTitleSize() {
		return mySubTitleSize;
	}
	
	public void setSectionDistance(float userDistance) {
		distSections = userDistance;
	}
	
	public float getSectionDistance() {
		return distSections;
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
