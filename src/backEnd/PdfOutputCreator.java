package backEnd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfOutputCreator {
	int fontSize = 8;
	float spacing;
	MusicSheet ms;
    String fileTitle;
	File outputLocation, temporary;
	PdfWriter write;
	Style s;
	
	public PdfOutputCreator() {	}

	private void createBezierCurves(PdfContentByte draw, float x0, float y0, float x1, char c) {
            draw.moveTo(x0 + s.getWidth(c), y0 +s.getHeight() / 2); //Start Point
            float third = ((x1+spacing)-(x0 + s.getWidth(c))) / 3.0f;
            draw.curveTo( // Control Point 1, Control Point 2, End Point
                    x0 + s.getWidth(c) + third, y0 + s.getHeight() / 1.5f, // Control Point 1
                    x0 + s.getWidth(c) + (2 * third), y0 + s.getHeight() / 1.5f, // Control Point 2
                    x1 + spacing, y0 + s.getHeight() / 2); //End Point
	}
	
	private void drawCircle(float currX, float currY, PdfContentByte draw) {
		currX += 1.7f;
		draw.circle(currX + (this.spacing - 3f) / 2, currY, 1.5f);
		draw.setColorFill(BaseColor.BLACK);
		draw.fillStroke();
	}

	private void drawDiagonal(float currX, float currY, PdfContentByte draw) {
		draw.moveTo(currX + 2.5f, currY - 2f);
		draw.lineTo(currX+spacing - 0.5f, currY + 2f);
		draw.stroke();

	}

	private void drawDiamond(float currX, float currY, PdfContentByte draw) {
		currY += 1.7f;
		currX = currX + 2.4f;
		draw.moveTo(currX + 0.175f, currY + 0.175f);
		draw.lineTo(currX - 1.93f, currY - 1.93f);
		draw.stroke();
		
		draw.moveTo(currX, currY);
		draw.lineTo(currX + 1.93f, currY - 1.93f);
		draw.stroke();
		
		currY = currY - (3.5f);
		draw.moveTo(currX - 0.175f, currY - 0.175f);
		draw.lineTo(currX + 1.93f, currY + 1.93f);
		draw.stroke();
		
		draw.moveTo(currX, currY);
		draw.lineTo(currX - 1.93f, currY + 1.93f);
		draw.stroke();
		currY -= 1.7f;
	}

	private void drawHorLine(float currX, float currY, float toX, PdfContentByte draw) {
		draw.setLineWidth(.2f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX + toX, currY);
		draw.stroke();
		draw.setLineWidth(.5f);
	}

	private void drawThick(float currX, float currY, float toY, PdfContentByte draw) {
		draw.setLineWidth(2.2f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX, currY + toY);
		draw.stroke();
	}

	private void drawVerLine(float currX, float currY, float toY, PdfContentByte draw) {
		draw.setLineWidth(.5f);
		draw.moveTo(currX + 1.5f, currY);
		draw.lineTo(currX + 1.5f, currY + toY);
		draw.stroke();
	}

	public void makePDF(Tablature userTab, Style userStyle) throws IOException, DocumentException {
		s = userStyle;
		ms = new MusicSheet(userTab, s);
		fontSize = userStyle.getFontSize();
		this.spacing = userTab.getSpacing();
		Document document = new Document(PageSize.A4);
		FileOutputStream documentStream;
		temporary = new File("temp.pdf");
		
		write = PdfWriter.getInstance(document, documentStream = new FileOutputStream(temporary));
		document.open();
		write.open();
		PdfContentByte draw = write.getDirectContent();

		float locationX = 0.0f;
		float locationY = document.top();
		float lastWordX = locationX; // location of last printed number/letter for arc
		float lastWordY = locationY;
        locationY -= printTitle(userTab.getTitle(), userTab.getSubtitle(), document);
               
		for (Staff st : ms.getStaffs()) {
			if (locationY - userStyle.getSectionDistance() - (6 * userStyle.getLineDistance()) < 0) {
				document.newPage();
				locationY = document.top();
			}

			int j = -1;
			for (StringBuffer linein : st.getLines()) {
				j++;
				String line = linein.toString(); 
				drawHorLine(locationX, locationY, userStyle.leftMargin, draw); // left margin
				locationX += userStyle.leftMargin;

				for (int z = 0; z < line.length(); z++) {
					char l = line.charAt(z);
					char m = 99;

					if (z < line.length() - 1)
						m = line.charAt(z + 1);

					if (l == '-') {
						drawHorLine(locationX, locationY, spacing, draw);
						locationX = locationX + spacing;
						
					} else if (l == '|') {
						if (j > 0) {
							if (z >= 2 && line.charAt(z - 2) == 'D')
								drawVerLine(locationX - 2.7f, locationY, userStyle.getLineDistance(), draw);
							else
								drawVerLine(locationX, locationY, userStyle.getLineDistance(), draw);
						}
						
					}  else if (l == '*') {
						drawCircle(locationX, locationY, draw);
						locationX = locationX + spacing;

					} else if (l == 's') {
						drawHorLine(locationX, locationY, spacing, draw);
						drawDiagonal(locationX, locationY, draw);
						locationX = locationX + spacing;
						
					} else if (l == '+' && j == 0) {
						String repeat = "Repeat " + st.getTopInt() + " times";
						text(repeat,locationX - 40f, locationY + 1 + userStyle.getLineDistance(), userStyle.myFontface, 7, draw);

					} else if (l == '>') {
						drawDiamond(locationX, locationY, draw);
						locationX = locationX + spacing;
						
					} else if (l == ',') {
						drawHorLine(locationX, locationY, 1f, draw);
						locationX = locationX + 1f;
						
					}  else if (l == 'p' && line.charAt(z - 1) == '|') {
						drawHorLine(locationX, locationY, spacing, draw);
						createBezierCurves(draw, lastWordX, lastWordY, locationX, l);
						text(l + "", locationX - 1.12f, locationY + 9f, userStyle.myFontface, 4, draw);
						locationX = locationX + spacing;
						
					} else if (l == 'p' || l == 'h') {
						createBezierCurves(draw, lastWordX, lastWordY, locationX, l);
						drawHorLine(locationX, locationY, spacing, draw);
						text(l + "", locationX , locationY + 9f, userStyle.myFontface, 4, draw);
						locationX = locationX + spacing;
						
					} else if (l == 'D') {
						if (j != 0)
							drawThick(locationX, locationY, userStyle.getLineDistance(), draw);
						
					} else {
						lastWordX = locationX;
						lastWordY = locationY;
						if ((l > 47 && l < 58) && (m > 47 && m < 58)) {
							locationX -= userStyle.getWidth(l) / 2;
							text(l + "", locationX + 1f, locationY, userStyle.myFontface, fontSize,draw);
							locationX += userStyle.getWidth(l);
							text(m + "", locationX- .5f, locationY,userStyle.myFontface, fontSize, draw);
							locationX += userStyle.getWidth(m);
							
							drawHorLine(locationX, locationY, (2f * spacing) - (userStyle.getWidth(l) / 2 + userStyle.getWidth(m)), draw);
                            
							locationX += userStyle.getWidth(l) / 2;
							locationX += (2f * spacing) - (userStyle.getWidth(l) + userStyle.getWidth(m));
							
							z++;                       
						} else {
							text(l + "", locationX, locationY, userStyle.myFontface, fontSize, draw);
							drawHorLine(locationX + userStyle.getWidth(l), locationY, spacing - userStyle.getWidth(l), draw);
							locationX = locationX + spacing;
						}
					}
				}
				drawHorLine(locationX, locationY, document.getPageSize().getWidth() - locationX, draw);
				locationX = 0.0f;
				if (j != 5)
					locationY = locationY - userStyle.getLineDistance();
			}
			locationX = 0.0f;
			locationY = locationY - userStyle.getSectionDistance();
		}
		temporary.deleteOnExit();
		document.close();
		write.close();
		documentStream.close();
	}

	private float printTitle(String title, String subtitle, Document document)
			throws DocumentException {
        float moveY = 0.0f;
		Paragraph Title = new Paragraph(title, new Font(s.myFontface, s.getMyTitleSize())); // Title.setLeading(3f, 0.0f);
		Title.setAlignment(1);
		document.add(Title);
        moveY += Title.getLeading();
		Paragraph subTitle = new Paragraph(subtitle, new Font(s.myFontface, s.getMySubTitleSize())); //Title.setLeading(3f, 0.0f);
        subTitle.setAlignment(1);
		document.add(subTitle);
        moveY += subTitle.getLeading();
        moveY += 20f;
        return moveY;
	}

	private void text(String text, float currX, float currY, BaseFont font, int fontsize, PdfContentByte draw) 
			throws DocumentException, IOException {
		draw.saveState();
		draw.beginText();
		draw.setTextMatrix(currX, currY - (s.getHeight() / 2.5f));
		draw.setFontAndSize(font, fontsize);
		draw.showText(text);
		draw.endText();
		draw.restoreState();
	}
}