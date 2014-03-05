package cse2311;

import java.util.ArrayList;

public class Parser {

	public Parser(){
		
	}
	
	public void parse(ArrayList<String[]> sections) {
		for (int sectionNum = 0; sectionNum < sections.size(); sectionNum++) {
			parser(sections.get(sectionNum));
		}
	}

	private void parser(String[] sec) {// remove|,|| to|
		for (int barNum = 0; barNum < 6; barNum++) {
			StringBuffer copyLine = new StringBuffer(sec[barNum]);
			for (int i = 1; i < copyLine.length(); i++) {
				if (copyLine.charAt(i) > 47 && copyLine.charAt(i) < 58
						&& copyLine.charAt(i - 1) == '|') {
					sec[6] = copyLine.substring(i, i + 1);
					copyLine.replace(i - 1, i + 1, "%");
					i--;
				} else if (copyLine.charAt(i) == copyLine.charAt(i - 1)
						&& copyLine.charAt(i) == '|') {
					copyLine.replace(i - 1, i + 1, "D");
					i--;
				} else if (copyLine.charAt(i) == '-') {
					//copyLine.replace(i, i + 1, " ");
				} else if (copyLine.charAt(i) == '<') {
					copyLine.replace(i, i + 1, "-");
				} else if (copyLine.charAt(i) == 's') {
					copyLine.replace(i, i + 1, "/");
				} else if (copyLine.charAt(i) == '*') {
					copyLine.replace(i, i + 1, "●");
				} else if (copyLine.charAt(i) == ' ') {
					copyLine.replace(i, i + 1, "");
				}
			}
			sec[barNum] = copyLine.toString();
		}
	}

	public String checkPrintString(String printString) {
		// remove|,|| to|
		StringBuffer copyLine = new StringBuffer(printString);
		for (int i = 1; i < copyLine.length(); i++) {

			if (copyLine.charAt(i) == copyLine.charAt(i - 1)
					&& copyLine.charAt(i) == '|'&& copyLine.charAt(i+1) == '|') {

				copyLine.replace(i - 1, i + 2, "T");
				i-=2;
			}else if (copyLine.charAt(i) == copyLine.charAt(i - 1)
					&& copyLine.charAt(i) == '|') {

				copyLine.replace(i - 1, i + 1, "|");
				i--;
			}
		}
		return copyLine.toString();
	}
	
	public void fixBars(ArrayList<String[]> sections) {
		String one, two;
		StringBuffer line;
		for (int i = 0; i < sections.size(); i++) {
			if (sections.get(i)[2].charAt(1) == '●') {
				one = "D";
			} else {
				one = null;
			}
			if (sections.get(i)[2].charAt(sections.get(i)[2].length()-2) == '●') {
				two = "D";
			} else {
				two = null;
			}
			for (int j = 0; j < 6; j++) {
				line = new StringBuffer(sections.get(i)[j]) ;
				if  (one != null) {
					line.replace(0, 1, "D-|");
				} else {
					line.replace(0, 1, "|");
				}
				
				if (two != null) {
					line.replace(line.length()-1, line.length(), "|-D");
				} else {
					line.replace(line.length()-1, line.length(), "|");
				}
				sections.get(i)[j] = line.toString();
			}
		}
	}

	public ArrayList<String[]> makeCorrectLengthStrings(ArrayList<String[]> sections) {

		ArrayList<String[]> returnSections = new ArrayList<String[]>();
		while (!sections.isEmpty()) {

			if (sections.size() == 0) {
				break;
			}

			String printString = "";
			int section = 0;
			int barNum = 0;

			// Paragraph sectionPrint = new Paragraph("", fonts[3]);
			// sectionPrint.setLeading(0f, 1f);
			String[] sectionPrint = new String[7];
			while ((printString.length() + sections.get(section)[barNum]
					.length()) <= 108) {
				printString = printString + sections.get(section)[barNum];
				section++;
				if (sections.size() == section) {// if reached last section
													// don't increment section and
													// move on
					break;
				}

			}

			printString = checkPrintString(printString);
			sectionPrint[0] = printString;

			for (barNum = 1; barNum < 6; barNum++) {
				printString = "";
				for (int i = 0; i < section; i++) {
					printString = printString + sections.get(i)[barNum];
				}
				printString = checkPrintString(printString);

				sectionPrint[barNum] = printString;
			}

			printString = "";

			for (int i = 0; i < section; i++) {
				sections.remove(0);
			}

			returnSections.add(sectionPrint);

		}
	return returnSections;
	}
}
