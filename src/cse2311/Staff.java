package cse2311;
import java.util.ArrayList;

public class Staff {
	private ArrayList<StringBuffer> my_Lines;
	private float printSpace;
	private float width;
	private ArrayList<Integer>  repeatNum ;

	public Staff(float printSpace) {
		this.setPrintSpace(printSpace);
	}

	public void addToStaff(Measure m) {
		this.appendToLines(m);
		if (m.isRepeat())
			this.getrepeatNum().add((m.getRepeatNum()));
		this.setWidth(m.getWidth());
	}

	public ArrayList<Integer> getrepeatNum() {
		if (this.repeatNum == null){// Lazy initialization
				this.repeatNum = new ArrayList<Integer>();}
		return repeatNum;
	}

	public void setrepeatNum(ArrayList<Integer> repeatNum) {
		this.repeatNum = repeatNum;
	}
	
	public int getTopInt() {
		int i =-1;
		if(this.getrepeatNum()!= null && this.getrepeatNum().size()!= 0 ){
			i = this.getrepeatNum().get(0);
			this.getrepeatNum().remove(0);
		}
		return i;
	}
	
	private void appendToLines(Measure m) {
		int i = 0;
		for (String line : m.get_Lines()) {
			if (!(this.get_Lines().size() <= i))
				this.get_Lines().get(i).append(this.barsAdded(m.getBarType(),line));
			else {
				this.addStringBuffer();
				this.get_Lines().get(i).append(this.barsAdded(m.getBarType(),line));
			}
			i++;
		}
		this.fixBars();
	}

	private String barsAdded(String bar,String line) {
		String single = "|";
		String left = "D-|";
		String right = "|-D";
	
		if(bar == "Both")
			line= left + line+right;
		if(bar == "Right")
			line= single + line+right;
		if(bar == "Left")
			line= left + line+single;
		if(bar == "Single")
			line= single + line+single;
		
		return line;
	}

	public void addStringBuffer() {
		this.get_Lines().add(new StringBuffer());
	}

	public boolean canFitAnother(Measure m) {
		if (this.getWidth() + m.getWidth() < this.getPrintSpace())
			return true;
		return false;
	}

	private void fixBars() {
		for(int i = 0; i < this.get_Lines().size(); i++) {
			String line = this.get_Lines().get(i).toString();
			this.get_Lines().remove(i);
			this.my_Lines.add(i,this.fixedLine(line));
		}
	}

	private StringBuffer fixedLine(String line) {
		line = line.replace("T","|-|-|");
		line = line.replace("DD","D");
		line = line.replace("||","|");
		return new StringBuffer(line);
	}	

	public ArrayList<StringBuffer> get_Lines() {
		if (this.my_Lines == null)// Lazy initialization
			this.my_Lines = new ArrayList<StringBuffer>();
		return my_Lines;
	}

	public float getPrintSpace() {
		return printSpace;
	}

	public void setPrintSpace(float printSpace) {
		this.printSpace = printSpace;
	}

	public float getWidth() {
		return width;
	}

	private void setWidth(float width) {
		this.width += width;
	}

	public void printLines() {
		for (StringBuffer m : my_Lines) {
			System.out.println(m.toString());
		}
		System.out.println();
	}
}
