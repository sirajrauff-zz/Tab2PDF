package cse2311;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Tablature {
	private float spacing;
	private Color color;
	private String title;
	private String subtitle;
	ArrayList<String[]> data;
	
	/**
	 * Inputs a ASCII guitar tablature and converts it to an Arraylist.
	 * It then stores this with default parameters for spacing and color.
	 * This constructor leaves title and subtitle null for the user to define.
	 * @param file The ASCII Tablature
	 * @throws FileNotFoundException Throws an Exception if the file is not found.
	 * @throws InvalidFormatException 
	 */
	public Tablature(File file) 
			throws FileNotFoundException, InvalidFormatException {
		data = Reader.readFile(file);
		title = Reader.title;
		subtitle = Reader.subtitle;
		spacing = 1;
		color = color.black;
		Parser x = new Parser();
		x.parse(data);
		data = Reader.splitInToSubSections(data);
		x.fixBars(data);
		data = x.makeCorrectLengthStrings(data);
	}

	/**
	 * Inputs a ASCII guitar tablature and converts it to an Arraylist.
	 * Also takes in parameters for spacing, title, subtitle, and color.
	 * @param file The ASCII Tablature
	 * @param userSpacing User defined spacing for the output PDF.
	 * @param userTitle User defined title for the output PDF.
	 * @param userSubtitle User defined subtitle for the output PDF.
	 * @param userColor User defined color for the output PDF.
	 * @throws FileNotFoundException Throws an Exception if the file is not found.
	 * @throws InvalidFormatException 
	 */
	public Tablature(File file, int userSpacing, String userTitle, String userSubtitle, Color userColor) 
			throws FileNotFoundException, InvalidFormatException {
		data = Reader.readFile(file);
		spacing = userSpacing;
		color = userColor;
		title = userTitle;
		subtitle = userSubtitle;
		Parser x = new Parser();
		x.parse(data);
		data = Reader.splitInToSubSections(data);
		x.fixBars(data);
		data = x.makeCorrectLengthStrings(data);
	}

	public void setSpacing(float userSpacing){
		spacing = Math.abs(userSpacing);
	}
	
	public float getSpacing(){
		return spacing;
	}
	
	public void setColor(Color userColor){
		color = userColor;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Boolean setTitle(String userTitle){
		if (title == null){
			title = userTitle;
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getTitle(){
		return title;
	}
	
	public Boolean setSubtitle(String userSubtitle){
		if (subtitle == null){
			subtitle = userSubtitle;
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getSubtitle(){
		return subtitle;
	}
	
	public void printAll() {
		for (int sectionNum = 0; sectionNum < data.size(); sectionNum++) {
			String[] temp = data.get(sectionNum);
			System.out.println(sectionNum);
			for (int barNum = 0; barNum < 6; barNum++) {
				System.out.println(temp[barNum]);
			}
		}
	}
}
