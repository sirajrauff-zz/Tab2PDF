package cse2311;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {	
	 

	public Parser(){}
	
	public Tablature parse(File file) throws FileNotFoundException, InvalidFormatException {
		Tablature returnTab = new Tablature();
		returnTab= this.readFile(file);
		Parser x = new Parser();
		x.fixBars(returnTab.getData());
		this.parser(returnTab.getData());
		returnTab.setData(this.splitInToSubSections(returnTab.getData()));
		x.fixBars(returnTab.getData());
		returnTab.setData(this.makeCorrectLengthStrings(returnTab.getData()));
		return returnTab;
		
	}


	private void parser(ArrayList<String[]> sections) {// remove|,|| to|
		for (int sectionNum = 0; sectionNum < sections.size(); sectionNum++) {
			String[] sec= sections.get(sectionNum);
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
				} else if (copyLine.charAt(i) == ' ') {
					copyLine.replace(i, i + 1, ",");
				}
				
			}
			sec[barNum] = copyLine.toString();
		}
	}
}

	private String checkPrintString(String printString) {
		// remove|,|| to|
		StringBuffer copyLine = new StringBuffer(printString);
		for (int i = 1; i < copyLine.length(); i++) {

			if (copyLine.charAt(i) == copyLine.charAt(i - 1)
					&& copyLine.charAt(i) == '|'&& copyLine.charAt(i+1) == '|') {

				copyLine.replace(i - 1, i + 2, "|-|-|");
				i-=2;
			}else if (copyLine.charAt(i) == copyLine.charAt(i - 1)
					&& copyLine.charAt(i) == '|') {

				copyLine.replace(i - 1, i + 1, "|");
				i--;
			}
		}
		return copyLine.toString();
	}
	
	private void fixBars(ArrayList<String[]> sections) {
		String one, two;
		StringBuffer line;
		for (int i = 0; i < sections.size(); i++) {
			if (sections.get(i)[2].charAt(1) == '*' ) {
				one = "D";
			} else {
				one = null;
			}
			if (sections.get(i)[2].charAt(sections.get(i)[2].length()-2) == '*') {
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

	private ArrayList<String[]> makeCorrectLengthStrings(ArrayList<String[]> sections) {

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
					.length()) <= 110) {
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
	
	

	/**
	 * Takes in a file path and returns an array containing guitar tablature 
	 * from the file specified.
	 * @param file File to be read
	 * @return Array populated with tablature
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException Length of lines in a given bar are not the same length,
	 * 									or line starts with unknown character.
	 */
	private Tablature readFile(File file)
			throws FileNotFoundException, InvalidFormatException {
		Tablature returnTab = new Tablature();
		ArrayList<String[]> sections = new ArrayList<String[]>();
		Scanner s = new Scanner(file);
		int barNumber = 0;
		int barLength = -1;
		boolean justPassedBlankLine = false;
		
		while (s.hasNext()) {
			String readLine = s.nextLine();
			if (readLine.toLowerCase().startsWith("title")) {
				returnTab.setTitle(readLine.substring(readLine.indexOf("=") + 1));
			} 
			else if (readLine.toLowerCase().startsWith("subtitle")){
				returnTab.setSubtitle(readLine.substring(readLine.indexOf("=") + 1));
			} 
			else if (readLine.toLowerCase().startsWith("spacing")){
				returnTab.setSpacing(Float.valueOf(readLine.replaceAll("[A-Za-z=]+", "")));
			}
			else if ((readLine.matches("") || readLine.startsWith("\t")) && justPassedBlankLine) {
			}
			else if ((readLine.matches("") || readLine.startsWith("\t")) && justPassedBlankLine == false) {
				if (barNumber != 6 && barNumber != 0) {
					s.close();
					throw new InvalidFormatException("Not enough lines in section " + sections.size());
				}
				justPassedBlankLine = true;
				sections.add(new String[7]);
				barNumber = 0;
				barLength = -1;
			}
			else if (readLine.startsWith("|")) {
					justPassedBlankLine = false;
					if (barNumber == 6) {
						sections.add(new String[7]);
						barLength = -1; barNumber = 0;
					}
				
					sections.get(sections.size() - 1)[barNumber] = readLine;
					if (barLength == -1) {
						barLength = readLine.length();
					}
					else {
						if (barLength != readLine.length()) {
							s.close();
							throw new InvalidFormatException("Line " + (barNumber + 1) + "'s length is not consistent in section " + sections.size());	
						}
					}
					barNumber++;
			} 
			else {
				s.close();
				throw new InvalidFormatException("Unexpected input in section " + sections.size() + " line: " + (barNumber + 1));	
			} 
		}
		s.close();
		returnTab.setData(sections);
		return returnTab;
	}
	
	private ArrayList<String[]> splitInToSubSections(ArrayList<String[]> sections) throws InvalidFormatException {
		ArrayList<String[]> temporary = new ArrayList<String[]>();		
		for (int sectionNum = 0; sectionNum < sections.size(); sectionNum++) {
			String[] temp = sections.get(sectionNum);
			int stringLoc, startIndex, doubleIndex, percentIndex, tempMin;
			while (!(temp[5].length() == 0)) {
				for (int barNum = 0; barNum < 6; barNum++) {
					StringBuffer copyLine = new StringBuffer(temp[barNum]);
					StringBuffer indexing = new StringBuffer(copyLine);
					indexing.delete(0,1);
					startIndex = indexing.indexOf("|");
					doubleIndex = indexing.indexOf("D"); 
					percentIndex = indexing.indexOf("%"); 
					
					if ((startIndex < 0) && (doubleIndex < 0) && (percentIndex < 0)){
						throw new InvalidFormatException("End of line " + (barNum + 1) + " in section " + temporary.size() + " not found");
					}
					if (startIndex < 0)
						startIndex = Integer.MAX_VALUE;
					if (doubleIndex < 0)
						doubleIndex = Integer.MAX_VALUE;
					if (percentIndex < 0)
						percentIndex = Integer.MAX_VALUE;
					tempMin = Math.min(startIndex, doubleIndex);
					stringLoc = Math.min(tempMin, percentIndex) + 1; 
					
					if (barNum == 0) {
						temporary.add(new String[7]);
					}
					if (stringLoc == copyLine.length() - 1) {
						temporary.get(temporary.size() - 1)[barNum] = copyLine.substring(0, stringLoc + 1);
						copyLine.delete(0, stringLoc + 1);
						temp[barNum] = copyLine.toString();
					} else {
						temporary.get(temporary.size() - 1)[barNum] = copyLine.substring(0, stringLoc+1);
						copyLine.delete(0, stringLoc);
						temp[barNum] = copyLine.toString();			
					}
				}
			}
		}
		return temporary;
	}
}
