package cse2311;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PrintToPDF {
	float size; 

	public static void makePDF(Tablature tab) throws IOException,
			DocumentException {
		Document document = new Document(PageSize.LETTER);
		PdfWriter write = PdfWriter.getInstance(document, new FileOutputStream(
				new File(tab.getTitle() + ".pdf")));
		document.open();
		write.open();
		PdfContentByte draw = write.getDirectContent();

		printTitle(tab.getTitle(), tab.getSubtitle(), document);


		// example of adding 0+1, 2+3, 4+5 etc
		// page width 612 points
		// page height 792 points

		// first line starts at x point 36
		// currY = current y
		float currX = 0.0f;
		float currY = 680.0f;
		float lastWordX = currX;//location of last printed num/letter for arc
		float lastWordY = currY;
		
		// drawLine(0,currY, 36, currY, draw)
		// public static void DrawLine(float x, float y, float toX, float toY,
		// PdfContentByte draw)
		// lineTo(float x, float y)
		
		for (int i = 0; i < tab.data.size(); i++) {
			for (int j = 0; j < 6; j++) {
				/*
				 * String add = tab.data.get(i).get(j) + tab.data.get(i+1).get(j);
				 * Paragraph line_x = new Paragraph(add);
				 * line_x.setAlignment(Element.ALIGN_LEFT);
				 * document.add(line_x);
				 */
				drawHorLine(currX, currY, 36.0f, draw); //drawn from beginning of page to starting bar (calvin)
				currX += 36.0f;
			
				for (int z = 0; z < tab.data.get(i)[j].length(); z++) {
					char l = tab.data.get(i)[j].charAt(z);
					if (l == '-') {
						drawHorLine(currX, currY, 5.02f, draw);
						currX = currX + 5.02f;
					} else if (l == '|') {
						
						if(j < 5) //this was done to not add an extra vertical line
							drawVerLine(currX, currY-3.5f, 7.0f, draw); 
						// drawHorLine(currX, currY, 1.8f, draw);
						// drawVerLine(currX, currY, 7.0f, draw);
					} else if (l == '●'){
						drawHorLine(currX, currY, 5.02f, draw);
						text(l + "", currX, currY+1.25f, 8, draw); //aligned the text with horizontal lines (calvin)
						currX = currX + 5.02f;
					} else if (l == '/'){
						drawHorLine(currX, currY, 5.02f, draw);
						//lastWordX = currX; //set location of last num/letter
						//lastWordY = currY;
						text(l + "", currX, currY+1.25f, 8, draw); //aligned the text with horizontal lines (calvin)
						currX = currX + 5.02f;
					} else if (l == '>') {
							drawDiamond(currX + 2.0f, currY + 3.5f, draw);
							currX = currX + 5.02f;
					} else if (l == 'p'&& z < (tab.data.get(i)[j].length() - 1)
							&& tab.data.get(i)[j].charAt(z - 1) == '|'){
							createBezierCurves(draw,lastWordX+2,lastWordY-3,currX+7.02f,currY+13);
							drawHorLine(currX, currY, 5.02f, draw);
							text(l + "", currX - 1.0f, currY + 10.0f, 4, draw);
							currX = currX + 5.02f;
							 //aligned the text with horizontal lines (calvin)
							
					} else if (l == 'D') {
						if(j < 5)
							drawThick(currX, currY-4f, 5.02f, draw);
						
					} else {
						lastWordX = currX; //set location of last num/letter
						lastWordY = currY;
						text(l + "", currX, currY+1.25f, 8, draw); //aligned the text with horizontal lines (calvin)
						currX = currX + 5.02f;
					}
				}
				drawHorLine(currX, currY, 612.0f - currX, draw); //drawn from last bar to end of page (calvin)
				currX = 0.0f;
				currY = currY - 7.0f;
			}
			currX = 0.0f;
			currY = currY - 15.0f;
			if (currY <= 10) {
				document.newPage();
				currY = 750;
			}
			// document.add(new Paragraph("\n"));
		}

		document.close();
		write.close();

	}

	private static void printTitle(String title, String subtitle,
			Document document) throws DocumentException {
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

	public static void drawHorLine(float currX, float currY, float toX,
			PdfContentByte draw) {
		draw.setLineWidth(.5f);
		draw.moveTo(currX, currY + 3.3f);
		draw.lineTo(currX + toX, currY + 3.3f);
		draw.stroke();
	}

	public static void drawVerLine(float currX, float currY, float toY,
			PdfContentByte draw) {
		draw.setLineWidth(.5f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX, currY + toY);
		draw.stroke();
	}

	public static void drawDiamond(float currX, float currY, PdfContentByte draw) {
		currY = currY + (1.75f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX - 1.93f, currY - 1.93f);
		draw.stroke();
		draw.moveTo(currX, currY);
		draw.lineTo(currX + 1.93f, currY - 1.93f);
		draw.stroke();
		currY = currY - (3.5f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX + 1.93f, currY + 1.93f);
		draw.stroke();
		draw.moveTo(currX, currY);
		draw.lineTo(currX - 1.93f, currY + 1.93f);
		draw.stroke();
	}

	/*public static void drawCircle(float currX, float currY, PdfContentByte draw) {
		draw.circle(currX, currY, 1.5f);
		draw.setColorFill(BaseColor.BLACK);
		draw.fillStroke();
	}*/

	public static void drawThick(float currX, float currY, float toY,
			PdfContentByte draw) {
		draw.setLineWidth(1.8f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX, currY + toY + 2.5f);
		draw.stroke();
	}

	public static void drawDiagonal(float currX, float currY,
			PdfContentByte draw) {
		draw.moveTo(currX, currY);
		draw.lineTo(currX + 1.75f, currY + 1.75f);
		draw.stroke();
		draw.moveTo(currX, currY);
		draw.lineTo(currX - 1.75f, currY - 1.75f);
		draw.stroke();
	}

	public static void text(String text, float currX, float currY,
			int fontsize, PdfContentByte draw) throws DocumentException,
			IOException {
		BaseFont font = BaseFont.createFont("Monospace.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		draw.saveState();
		draw.beginText();
		draw.setTextMatrix(currX, currY);
		draw.setFontAndSize(font, fontsize);
		draw.showText(text);
		draw.endText();
		draw.restoreState();
	}

	public void size(float x) {
		size = x;
	}

	public boolean testsize() {
		return true;
	}
	
	public static void createBezierCurves(PdfContentByte cb, float x0, float y0,
		         float x3, float y3) {        
		cb.arc(x0 - 6.0F, y0 - 8.5F, x3 + 6.5F, y3 - 4.5F, 60, 60);
    }
}
