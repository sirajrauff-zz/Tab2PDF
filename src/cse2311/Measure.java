package cse2311;

import java.util.ArrayList;

public  class Measure {
	private ArrayList<String> myLines;
	private String barType;
	private boolean repeat;
	private int repeatNum;
	private float spacing, width = 0;

	public Measure(float f) {
		this.spacing = f;
	}
	
	public Measure(String firstLine) {
		this.addLine(firstLine);
	}
	
	public boolean addLine(String line) {
		if(getLines().size() < 6){
			getLines().add(line);
				if(this.size() == 6)
					this.corrections();
			return true;
		}
		return false;
	}
	
	private void corrections() {
		this.checkRepeat();
		this.checkBarType();
		this.checkLength();//doesn't work perfectly
		this.setWidth(this.getLines().get(0).length() * this.spacing);
	}
	
	private boolean checkRepeat() {
		StringBuffer temp = new StringBuffer(this.getLines().get(0));
		if(temp.length() < 2){
			this.setRepeat(false);
			return false;
		}
		if (((temp.length()!= 0) && (temp.toString().charAt(temp.length() - 2) == '+'))) {
			this.setRepeat(true);
			if (((temp.toString().charAt(temp.length()-1) > 47) && (temp.toString().charAt(temp.length() - 1) < 58))) {
				this.setRepeatNum(Integer.parseInt(temp.substring(temp.length() - 1)));
				this.getLines().remove(0);
				this.getLines().add(0, temp.substring(0, temp.length() - 1).toString());
			}
		} 
		else 
			this.setRepeat(false);
		return true;
	}

	private void checkLength() {
		int longestLength = -1 ;
		for (String s : this.getLines()) {
			if (longestLength == -1)
				longestLength = s.length();
			else if (longestLength > s.length())	
				longestLength = s.length();
		}
		
		ArrayList<String> temp = new  ArrayList<String>() ;
		for (String s : this.getLines()) {
			if(s.charAt(s.length() - 1) =='+')
				temp.add(s.substring(0, longestLength) + "+");
			else
				temp.add(s.substring(0, longestLength));
		}
		this.setMyLines(temp);
	}

	private void checkBarType() {
		char left = this.getLines().get(2).charAt(0);
		char right = this.getLines().get(2).charAt(this.getLines().get(2).length()-1);
	
		if (left == right && right == '*') {
			this.barType = "Both";
			this.setWidth(6.6f);
		}
		
		else if (left != right && right == '*') {
			this.barType = "Right";
			this.setWidth(4.3f);
		}
		else if (left != right && left == '*') {
			this.barType = "Left";
			this.setWidth(4.3f);
		}
		else {
			this.barType = "Single";
			this.setWidth(.5f);
		}
	}
	
	public float getWidth() {
		return width;
	}

	private void setWidth(float width) {
		this.width += width;
	}

	public boolean isRepeat() {
		return repeat;
	}
	
	public int size() {
		return getLines().size();
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public int getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(int repeatNum) {
		this.repeatNum = repeatNum;
	}
	
	public ArrayList<String> getLines() {
		if (this.myLines == null) //Lazy initialization
			this.myLines = new ArrayList<String>();
		return this.myLines;
	}

	public void setMyLines(ArrayList<String> myLines) {
		this.myLines = myLines;
	}
	
	public void printLines() {
		System.out.println(this.barType);
		for (String myStr : this.getLines())
			   System.out.println(myStr);
		System.out.println();
	}

	public String getBarType() {
		return barType;
	}

	public void setBarType(String barType) {
		this.barType = barType;
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
		this.recalculateWidth();
	}

	private void recalculateWidth() {
		if ("Both".equals(this.getBarType()))
			this.width = 6.6f;	
		else if ("Right".equals(this.getBarType())) 
			this.width = 4.3f;
		else if ("Left".equals(this.getBarType()))
			this.width = 4.3f;
		else
			this.width = .5f;
		this.setWidth(this.getLines().get(0).length() * this.spacing);
	}
}
