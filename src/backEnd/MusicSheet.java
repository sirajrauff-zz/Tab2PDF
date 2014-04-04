package backEnd;

import java.util.ArrayList;

public class MusicSheet {
	private ArrayList<Staff> myStaffs;
	private Style myStyle;
	
	public MusicSheet(Tablature tab, Style s){
		myStyle = s;
		this.makeStaffs(tab);
	}

	private void addStaff() {
		this.getStaffs().add(new Staff(myStyle.getPrintSpace()));
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

	public void print(Object s) {
		System.out.println(s);
	}

	public void printStaff() {
		for (Staff s : this.getStaffs())
			s.printLines();
	}

	public void setStaffs(ArrayList<Staff> myStaffs) {
		this.myStaffs = myStaffs;
	}
}
