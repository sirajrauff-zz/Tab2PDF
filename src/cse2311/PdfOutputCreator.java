
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

	String outputLocation = "";

	public PdfOutputCreator(String outputLocation) {

		this.outputLocation = outputLocation;
	}

	public void makePDF(MusicSheet ms) throws IOException, DocumentException {
		s = ms.getMy_Style();
		this.spacing = ms.get_Spacing();
		Document document = s.document;

		PdfWriter write = PdfWriter.getInstance(document, new FileOutputStream(
				new File(outputLocation + ms.get_Title() + ".pdf")));
		document.open();
		write.open();
		PdfContentByte draw = write.getDirectContent();

		printTitle(ms.get_Title(), ms.get_Subtitle(), document);

		float currX = 0.0f;
		float currY = s.document.top() - 70;
		float lastWordX = currX;// location of last printed num/letter for arc
		float lastWordY = currY;

		for (Staff st : ms.get_Staffs()) {

			if (currY - s.dist_Staffs - (6 * s.dist_Lines) < 0) {
				document.newPage();
				currY = s.document.top();
			}

			int j = -1;
			for (StringBuffer linein : st.get_Lines()) {
				j++;
				String line = linein.toString();
				// right margin
				drawHorLine(currX, currY, s.leftMargin, draw);
				currX += s.leftMargin;

				for (int z = 0; z < line.length(); z++) {

					char l = line.charAt(z);
					char m = 99;

					if (z < line.length() - 1)
						m = line.charAt(z + 1);

					if (l == '-') {
						drawHorLine(currX, currY, ms.get_Spacing(), draw);
						currX = currX + ms.get_Spacing();
					} else if (l == '|') {

						if (j > 0) {
							drawVerLine(currX, currY, s.dist_Lines, draw);

						}

					} else if (l == '*') {
						drawCircle(currX, currY, draw);
						currX = currX + ms.get_Spacing();

					} else if (l == '/') {
						drawHorLine(currX, currY, ms.get_Spacing(), draw);
						drawDiagonal(currX, currY, draw);
						currX = currX + ms.get_Spacing();
					} else if (l == '(') {
						String repeat = "REAPEAT " + st.getTopInt() + " TIMES";
						text(repeat,
								currX - (repeat.length() * ms.get_Spacing()),
								currY + s.dist_Lines, s.my_Fontface, 8, draw);

					} else if (l == '>') {
						drawDiamond(currX, currY, draw);
						currX = currX + ms.get_Spacing();
					}else if (l == ',') {
						drawHorLine(currX, currY, 1f, draw);
						currX = currX + 1f;
					}  else if (l == 'p' && z < (line.length() - 1)
							&& line.charAt(z - 1) == '|') {

						createBezierCurves(draw, lastWordX + 2, lastWordY - 3,
								currX + 7.02f, currY + 13);
						drawHorLine(currX, currY, 5.02f, draw);
						text(l + "", currX - 1.0f, currY + 10.0f,
								s.my_Fontface, 4, draw);

						currX = currX + ms.get_Spacing();

					} else if (l == 'p' || l == 'h') {
						createBezierCurves(draw, lastWordX + 2, lastWordY - 3,
								currX + 7.02f, currY + 13);
						drawHorLine(currX, currY, 5.02f, draw);
						text(l + "", currX - 1.0f, currY + 10.0f,
								s.my_Fontface, 4, draw);

						currX = currX + ms.get_Spacing();
					} else if (l == 'D') {
						if (j != 0)
							drawThick(currX, currY, s.dist_Lines, draw);

					} else {
						lastWordX = currX;
						lastWordY = currY;
						text(l + "", currX, currY, s.my_Fontface, fontSize,draw);

						if ((l > 47 && l < 58) && (m > 47 && m < 58)) {

							text(m + "", currX + s.get_width(l), currY,
									s.my_Fontface, fontSize, draw);
							drawHorLine(
									currX + s.get_width(l) + s.get_width(m),
									currY,
									(2f * ms.get_Spacing())
											- (s.get_width(l) + s.get_width(m)),
									draw);
							currX = currX + ms.get_Spacing();
							currX = currX + ms.get_Spacing();
							z++;
							
						}else {
							
							drawHorLine(currX + s.get_width(l), currY,ms.get_Spacing() - s.get_width(l), draw);
							currX = currX + ms.get_Spacing();
						
						}
					}
				}

				drawHorLine(currX, currY, document.getPageSize().getWidth()
						- currX, draw);

				currX = 0.0f;
				currY = currY - s.dist_Lines;
			}

			currX = 0.0f;
			currY = currY - s.dist_Staffs;

		}

		document.close();
		write.close();

	}
//currX =currX+ (ms.get_Spacing()-s.get_width(l)) + (ms.get_Spacing()-s.get_width(line.charAt(z-2))) + (ms.get_Spacing()-ms.get_Spacing()/5);
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

	private void drawHorLine(float currX, float currY, float toX,
			PdfContentByte draw) {
		draw.setLineWidth(.5f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX + toX, currY);
		draw.stroke();
	}

	private void drawVerLine(float currX, float currY, float toY,
			PdfContentByte draw) {
		draw.setLineWidth(.5f);
		draw.moveTo(currX, currY);
		draw.lineTo(currX, currY + toY);
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

	private void drawThick(float currX, float currY, float toY,
			PdfContentByte draw) {
		draw.setLineWidth(1.8f);
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

	private void text(String text, float currX, float currY, BaseFont font,
			int fontsize, PdfContentByte draw) throws DocumentException,
			IOException {

		draw.saveState();
		draw.beginText();
		draw.setTextMatrix(currX, currY - (s.get_height() / 2.5f));
		draw.setFontAndSize(font, fontsize);
		draw.showText(text);
		draw.endText();
		draw.restoreState();
	}

	private void createBezierCurves(PdfContentByte cb, float x0, float y0,
			float x3, float y3) {
		cb.arc(x0 - 6.0F, y0 - 8.5F, x3 + 6.5F, y3 - 4.5F, 60, 60);
	}

}