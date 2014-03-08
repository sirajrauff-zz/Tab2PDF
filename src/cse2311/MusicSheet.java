import java.util.ArrayList;


public class MusicSheet {
	

	private ArrayList<Staff> my_Staffs;
	
	private float my_Spacing;
	private String my_Title;
	private String my_Subtitle;

	
	
	public MusicSheet(Tablature tab){
	
		this.set_Title(tab.get_Title());
		this.set_Subtitle(tab.get_Subtitle());
		this.makeStaffs(tab);
		
		
	}



	private void makeStaffs(Tablature tab) {
		
		for (Measure m: tab.get_Measures()){
			if(this.get_Staffs().size()!=0 &&
				this.get_Last_Staff().canFitAnother(m)){
					this.get_Last_Staff().addToStaff(m);
				
			}else{
				addStaff();
				this.get_Last_Staff().addToStaff(m);
				
				
			}
		}
		
	}



	private void addStaff() {
		this.get_Staffs().add(new Staff(120f));
		
	}
	
	private Staff get_Last_Staff() {
		if (get_Staffs().size() != 0) {
			return get_Staffs().get(get_Staffs().size() - 1);
		} else
			return null;
	}





	public ArrayList<Staff> get_Staffs() {
		if(my_Staffs == null)
			my_Staffs = new ArrayList<Staff>();
		
		return my_Staffs;
	}



	public void set_Staffs(ArrayList<Staff> my_Staffs) {
		this.my_Staffs = my_Staffs;
	}



	public float get_Spacing() {
		return my_Spacing;
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

	public void print(Object s){
		System.out.println(s);
	}

	public void set_Subtitle(String my_Subtitle) {
		this.my_Subtitle = my_Subtitle;
	}
	public void printStaff(){
		for (Staff s : this.get_Staffs())
			s.printLines();
			
		
	}
	
}
