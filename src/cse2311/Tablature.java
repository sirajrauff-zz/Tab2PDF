

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Tablature {

	private ArrayList<Measure> my_Measure;

	private float my_Spacing;

	private String my_Title;

	private String my_Subtitle;

	public Tablature() {
		this.my_Spacing = 5f;
		this.my_Subtitle = "Default";
		this.my_Title = "Default";

	}

	public void addLineToLastMeasure(String g) {
		if (this.get_Measures().size() != 0
				&& this.get_Last_Measure().size() != 6) {
			this.get_Last_Measure().addLine(g);
		} else {
			this.addMeasure();
			this.addLineToLastMeasure(g);
		}

	}

	public void addMultiMeasureLine(StringTokenizer StrTkn) {

		if (this.get_Measures().size() == 0
				|| this.get_Last_Measure().size() == 6) {
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
		this.get_Measures().add(new Measure());

	}

	private void addLineToMeasure(int index, String s) {
		this.get_Measures().get(get_Measures().size() - index).addLine(s);

	}

	private Measure get_Last_Measure() {
		if (get_Measures().size() != 0) {
			return get_Measures().get(get_Measures().size() - 1);
		} else
			return null;
	}

	public int size() {
		return this.get_Measures().size();
	}

	public float get_Spacing() {
		return my_Spacing;

	}

	public ArrayList<Measure> get_Measures() {
		if (this.my_Measure == null)// Lazy initialization
			this.my_Measure = new ArrayList<Measure>();

		return my_Measure;
	}

	public void set_Measures(ArrayList<Measure> my_Measure) {
		this.my_Measure = my_Measure;
	}

	public void set_Spacing(float my_Spacing) {
		this.my_Spacing = my_Spacing;

	}

	public String get_Title() {
		return my_Title;

	}

	public void set_Title(String my_Title) {
		this.my_Title = my_Title;

	}

	public String get_Subtitle() {
		return my_Subtitle;
	}

	public void set_Subtitle(String my_Subtitle) {
		this.my_Subtitle = my_Subtitle;

	}

}
