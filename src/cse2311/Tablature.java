package cse2311;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Tablature {
	private ArrayList<Measure> myMeasure;
	private float mySpacing = 5;
	private String myTitle, mySubtitle;

	public Tablature() {
		this.mySubtitle = "Default";
		this.myTitle = "Default";
	}

	public void addLineToLastMeasure(String g) {
		if (!this.getMeasures().isEmpty() && this.getLastMeasure().size() != 6) {
			this.getLastMeasure().addLine(g);
		} else {
			this.addMeasure();
			this.addLineToLastMeasure(g);
		}
	}

	public void addMultiMeasureLine(StringTokenizer StrTkn) {
		if (this.getMeasures().size() < StrTkn.countTokens() || this.getLastMeasure().size() == 6) {
			while (StrTkn.hasMoreTokens()) {
				this.addMeasure();
				this.addLineToLastMeasure(StrTkn.nextToken());
			}
		} else {
			while (StrTkn.hasMoreTokens()) {
					this.addLineToMeasure(StrTkn.countTokens(), StrTkn.nextToken());
			}
		}
	}

	private void addMeasure() {
		this.getMeasures().add(new Measure(this.getSpacing()));
	}

	private void addLineToMeasure(int index, String inputLine) {
		if(this.getMeasures().size() >= index)
			this.getMeasures().get(getMeasures().size() - index).addLine(inputLine);
	}

	private Measure getLastMeasure() {
		if (!getMeasures().isEmpty())
			return getMeasures().get(getMeasures().size() - 1);
		else
			return null;
	}
	
	public void printMeasures() {
		for (Measure m : this.getMeasures())
			m.printLines();
	}

	public int size() {
		return this.getMeasures().size();
	}

	public float getSpacing() {
		return mySpacing;
	}

	public ArrayList<Measure> getMeasures() {
		if (this.myMeasure == null) // Lazy initialization
			this.myMeasure = new ArrayList<Measure>();
		return myMeasure;
	}

	public void setMeasures(ArrayList<Measure> myMeasure) {
		this.myMeasure = myMeasure;
	}

	public void setSpacing(float mySpacing) {
		for(Measure m : this.getMeasures())
			m.setSpacing(mySpacing);
		this.mySpacing = mySpacing;
	}

	public String getTitle() {
		return myTitle;
	}

	public void setTitle(String myTitle) {
		this.myTitle = myTitle;
	}

	public String getSubtitle() {
		return mySubtitle;
	}

	public void setSubtitle(String mySubtitle) {
		this.mySubtitle = mySubtitle;
	}
}
