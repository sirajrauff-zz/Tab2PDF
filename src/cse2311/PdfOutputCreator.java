package cse2311;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfOutputCreator {
	int fontSize = 8;
	float spacing;
	Style s;
	File outputLocation;
	PdfWriter write;
	
	public PdfOutputCreator() {	}

	public void makePDF(MusicSheet ms) throws IOException, DocumentException {		
		s = ms.getMyStyle();
		fontSize = s.getFontSize();
		this.spacing = ms.getSpacing();
		Document document = s.document;
		FileOutputStream documentStream;
		//System.out.print(System.getProperty("os.name", "generic").toLowerCase());
		write = PdfWriter.getInstance(document, documentStream = new FileOutputStream(new File(ms.getTitle() + ".pdf")));
		
		document.open();
		write.open();
		PdfContentByte draw = write.getDirectContent();

		printTitle(ms.getTitle(), ms.getSubtitle(), document);

		float locationX = 0.0f;
		float locationY = s.document.top() - 70;
		float lastWordX = locationX;// location of last printed num/letter for arc
		float lastWordY = locationY;

		for (Staff st : ms.getStaffs()) {
			if (locationY - s.getSectionDistance() - (6 * s.getLineDistance()) < 0) {
				document.newPage();
				locationY = s.document.top();
			}

			int j = -1;
			for (StringBuffer linein : st.getLines()) {
				j++;
				String line = linein.toString();
				// left margin
				drawHorLine(locationX, locationY, s.leftMargin, draw);
				locationX += s.leftMargin;

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
								drawVerLine(locationX - 2.7f, locationY, s.getLineDistance(), draw);
							else
								drawVerLine(locationX, locationY, s.getLineDistance(), draw);
						}
						
					}  else if (l == '*') {
						drawCircle(locationX, locationY, draw);
						locationX = locationX + spacing;

					} else if (l == '/') {
						drawHorLine(locationX, locationY, spacing, draw);
						drawDiagonal(locationX, locationY, draw);
						locationX = locationX + spacing;
						
					} else if (l == '+' && j == 0) {
						String repeat = "Repeat " + st.getTopInt() + " times";
						text(repeat,locationX - 40f, locationY + 1 + s.getLineDistance(), s.myFontface, 7, draw);

					} else if (l == '>') {
						drawDiamond(locationX, locationY, draw);
						locationX = locationX + spacing;
						
					} else if (l == ',') {
						drawHorLine(locationX, locationY, 1f, draw);
						locationX = locationX + 1f;
						
					}  else if (l == 'p' && line.charAt(z - 1) == '|') {
						drawHorLine(locationX, locationY, spacing, draw);
						createBezierCurves(draw, lastWordX, lastWordY, locationX);
						text(l + "", locationX - 1.12f, locationY + 9f, s.myFontface, 4, draw);
						locationX = locationX + spacing;
						
					} else if (l == 'p' || l == 'h') {
						createBezierCurves(draw, lastWordX, lastWordY, locationX);
						drawHorLine(locationX, locationY, spacing, draw);
						text(l + "", locationX + 1, locationY + 9f, s.myFontface, 4, draw);
						locationX = locationX + spacing;
						
					} else if (l == 'D') {
						if (j != 0)
							drawThick(locationX, locationY, s.getLineDistance(), draw);
						
					} else {
						lastWordX = locationX;
						lastWordY = locationY;
						if ((l > 47 && l < 58) && (m > 47 && m < 58)) {
							locationX -= s.getWidth(l) / 2;
							text(l + "", locationX + 1f, locationY, s.myFontface, fontSize,draw);
							locationX += s.getWidth(l);
							text(m + "", locationX- .5f, locationY,s.myFontface, fontSize, draw);
							locationX += s.getWidth(m);
							
							drawHorLine(locationX, locationY, (2f * spacing) - (s.getWidth(l) / 2 + s.getWidth(m)), draw);
                            
							locationX += s.getWidth(l) / 2;
							locationX += (2f * spacing) - (s.getWidth(l) + s.getWidth(m));
							
							z++;                       
						} else {
							text(l + "", locationX, locationY, s.myFontface, fontSize, draw);
							drawHorLine(locationX + s.getWidth(l), locationY, spacing - s.getWidth(l), draw);
							locationX = locationX + spacing;
						}
					}
				}
				drawHorLine(locationX, locationY, document.getPageSize().getWidth() - locationX, draw);
				locationX = 0.0f;
				if (j != 5)
					locationY = locationY - s.getLineDistance();
			}
			locationX = 0.0f;
			locationY = locationY - s.getSectionDistance();
		}
		document.close();
		write.close();
		documentStream.close();
	}
	
	/*currX = currX + (spacing - s.getwidth(l)) + (spacing - s.getwidth(line.charAt(z - 2))) 
					+ (ms.get_Spacing() - spacing / 5);*/
	private void printTitle(String title, String subtitle, Document document)
			throws DocumentException {
		Font[] fonts = {
				new Font(),
				/* new Font(fontfamily, size, type, color) */
				new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD,
						new BaseColor(/* Red */0, /* Green */0, /* Blue */0)),
				new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL,
						new BaseColor(/* Red */0, /* Green */0, /* Blue */0)), };

		Paragraph Title = new Paragraph(title, fonts[1]);
		Title.setAlignment(1);

		document.add(Title);
		Paragraph subTitle = new Paragraph(subtitle, fonts[2]);

		subTitle.setAlignment(1);
		document.add(subTitle);
	}

	private void drawHorLine(float currX, float currY, float toX, PdfContentByte draw) {
		draw.setLineWidth(.2f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX + toX, currY);
		draw.stroke();
		draw.setLineWidth(.5f);
	}

	private void drawVerLine(float currX, float currY, float toY, PdfContentByte draw) {
		draw.setLineWidth(.5f);
		draw.moveTo(currX + 1.5f, currY);
		draw.lineTo(currX + 1.5f, currY + toY);
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

	private void drawCircle(float currX, float currY, PdfContentByte draw) {
		currX += 1.7f;
		draw.circle(currX + (this.spacing - 3f) / 2, currY, 1.5f);
		draw.setColorFill(BaseColor.BLACK);
		draw.fillStroke();
	}

	private void drawThick(float currX, float currY, float toY, PdfContentByte draw) {
		draw.setLineWidth(2.2f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX, currY + toY);
		draw.stroke();
	}

	private void drawDiagonal(float currX, float currY, PdfContentByte draw) {
		draw.moveTo(currX, currY);
		draw.lineTo(currX, currY);
		draw.stroke();
		draw.moveTo(currX, currY);
		draw.lineTo(currX, currY);
		draw.stroke();
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

	private void createBezierCurves(PdfContentByte cb, float x0, float y0, float x1) {
		cb.arc(x0 + 2f, y0 + 5f, x1 + 7f, y0 - 1.5f, 40, 100);
	}
}