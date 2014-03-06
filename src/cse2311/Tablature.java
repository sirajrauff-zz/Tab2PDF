package cse2311;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.itextpdf.text.pdf.BaseFont;

public class Tablature {
	private float spacing;
	private Color color;
	private String title;
	private String subtitle;
	private ArrayList<String[]> data;
	private BaseFont font;
	

	
	/**
	 * Inputs a ASCII guitar tablature and converts it to an Arraylist.
	 * It then stores this with default parameters for spacing and color.
	 * This constructor leaves title and subtitle null for the user to define.
	 * @param file The ASCII Tablature
	 * @throws FileNotFoundException Throws an Exception if the file is not found.
	 * @throws InvalidFormatException 
	 */
	public Tablature() 
		{
		
		}
	public Tablature(Tablature t) 
			throws FileNotFoundException, InvalidFormatException {
		data = t.getData();
		title = t.getTitle();
		subtitle = t.getSubtitle();
		spacing = t.getSpacing();
		color = Color.black;
	}


	public ArrayList<String[]> getData() {
		return data;
	}
	public BaseFont getFont() {
		return font;
	}
	public void setFont(BaseFont font) {
		this.font = font;
	}

	public void setData(ArrayList<String[]> data) {
		this.data = data;
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
