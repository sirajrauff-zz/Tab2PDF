

import java.util.ArrayList;


public  class Measure {
	
	private ArrayList<String> my_Lines;
	private String barType;
	private boolean repeat;
	private int repeatNum;
	private float spacing;
	private float width = 0;
	

	public Measure(float f){
		this.spacing = f;
	}
	
	public Measure(String firstLine){
		this.addLine(firstLine);
	}

	
	public boolean addLine(String line) {
		if(get_Lines().size()<6){
		get_Lines().add(line);
			if(this.size() == 6)
				this.corrections();
		return true;
		}
		
		return false;
	}
	private void corrections(){
		
		this.checkRepeat();
		this.checkBarType();
		this.checkLength();
		this.setWidth(this.get_Lines().get(0).length()*this.spacing);
		
	}
	
	private void checkRepeat() {
		StringBuffer temp = new StringBuffer(this.get_Lines().get(0));
		if (temp.toString().charAt(temp.length()-2) == '('){
			this.setRepeat(true);
			this.setRepeatNum(Integer.parseInt(temp.substring(temp.length()-1)));
			this.get_Lines().remove(0);
			this.get_Lines().add(0, temp.substring(0, temp.length()-1).toString());

		}else 
			this.setRepeat(false);
			
			
			
			
		
	}

	private void checkLength() {
		int longest_Length = -1 ;
		for (String s : this.get_Lines()){
			if (longest_Length == -1)
				longest_Length =s.length();
			else if(longest_Length<s.length())	
				longest_Length =s.length();
		}
		
		for (String s : this.get_Lines()){
			
			StringBuffer d = new StringBuffer(s);
			for (int i = 0; i < longest_Length - d.length() ; i++){
				   d.append("-");
				}
			s = d.toString();
			
		}
		
	}

	private void checkBarType() {
		char left = this.get_Lines().get(2).charAt(0);
		char right = this.get_Lines().get(2).charAt(this.get_Lines().get(2).length()-1);
	
		if(left==right&&right =='*'){
			this.barType = "Both";
			this.setWidth(6.6f);
		}
		
		else if(left!=right&&right =='*'){
			this.barType = "Right";
			this.setWidth(4.3f);
		}
		else if(left!=right&&left =='*'){
			this.barType = "Left";
			this.setWidth(4.3f);
			}
		else {
			this.barType ="Single";
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
		return get_Lines().size();
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
	
	public ArrayList<String> get_Lines() {
		if(this.my_Lines == null)//Lazy initialization
			this.my_Lines = new ArrayList<String>();
		return this.my_Lines;
	}

	public void setMy_Lines(ArrayList<String> my_Lines) {
		this.my_Lines = my_Lines;
	}
	public void printLines(){
		System.out.println(this.barType);
		for (String myStr : this.get_Lines()) {
			   System.out.println(myStr);
			 }
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
		if(this.getBarType()=="Both"){
			this.width = 6.6f;	
	
		}
		
		else if(this.getBarType()=="Right"){
			this.width = 4.3f;	
			
		}
		else if(this.getBarType()=="Left"){
			this.width = 4.3f;	
			
			}
		else {
			this.width = .5f;	
			
		}
		this.setWidth(this.get_Lines().get(0).length()*this.spacing);
		
	}

}
