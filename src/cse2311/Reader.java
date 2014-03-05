package cse2311;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {

	public static String title;
	public static String subtitle;
	public static float spacing;

	/**
	 * Takes in a file path and returns an array containing guitar tablature 
	 * from the file specified.
	 * @param file File to be read
	 * @return Array populated with tablature
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException Length of lines in a given bar are not the same length,
	 * 									or line starts with unknown character.
	 */
	public static ArrayList<String[]> readFile(File file)
			throws FileNotFoundException, InvalidFormatException {
		ArrayList<String[]> sections = new ArrayList<String[]>();
		Scanner s = new Scanner(file);
		int barNumber = 0;
		int barLength = -1;
		boolean justPassedBlankLine = false;
		
		while (s.hasNext()) {
			String readLine = s.nextLine();
			if (readLine.toLowerCase().startsWith("title")) {
				title = readLine.substring(readLine.indexOf("=") + 1);
			} 
			else if (readLine.toLowerCase().startsWith("subtitle")){
				subtitle = readLine.substring(readLine.indexOf("=") + 1);
			} 
			else if (readLine.toLowerCase().startsWith("spacing")){
				spacing = Float.valueOf(readLine.replaceAll("[A-Za-z=]+", ""));
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
		return (sections);
	}
	
	public static ArrayList<String[]> splitInToSubSections(ArrayList<String[]> sections) throws InvalidFormatException {
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
