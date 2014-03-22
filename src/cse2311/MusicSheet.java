package cse2311;

import java.util.ArrayList;

public class MusicSheet {
	private ArrayList<Staff> myStaffs;
	private float mySpacing;
	private String myTitle, mySubtitle;
	private Style myStyle;
	
	public MusicSheet(Tablature tab, Style s){
		this.setMyStyle(s);
		this.setTitle(tab.getTitle());
		this.setSubtitle(tab.getSubtitle());
		mySpacing = tab.getSpacing();
		this.makeStaffs(tab);
	}

	private void makeStaffs(Tablature tab) {
		for (Measure m: tab.getMeasures()){
			if (!this.getStaffs().isEmpty() && this.getLastStaff().canFitAnother(m)) 
					this.getLastStaff().addToStaff(m);
			else {
				addStaff();
				this.getLastStaff().addToStaff(m);
			}
		}
	}
	
	private void addStaff() {
		this.getStaffs().add(new Staff(this.getMyStyle().getPrintSpace()));
	}
	
	private Staff getLastStaff() {
		if (!getStaffs().isEmpty())
			return getStaffs().get(getStaffs().size() - 1);
		else
			return null;
	}

	public ArrayList<Staff> getStaffs() {
		if(myStaffs == null)
			myStaffs = new ArrayList<Staff>();
		return myStaffs;
	}

	public void setStaffs(ArrayList<Staff> myStaffs) {
		this.myStaffs = myStaffs;
	}

	public float getSpacing() {
		return mySpacing;
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

	public void print(Object s) {
		System.out.println(s);
	}

	public void setSubtitle(String mySubtitle) {
		this.mySubtitle = mySubtitle;
	}
	public void printStaff() {
		for (Staff s : this.getStaffs())
			s.printLines();
	}

	public Style getMyStyle() {
		return myStyle;
	}

	public void setMyStyle(Style myStyle) {
		this.myStyle = myStyle;
	}
}
