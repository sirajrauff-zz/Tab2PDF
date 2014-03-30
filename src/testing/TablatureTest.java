package testing;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.junit.Before;
import org.junit.Test;
import cse2311.Measure;
import cse2311.Tablature;

/**
 * @author Jason
 * Test class for the Tablature class
 */
public class TablatureTest {
	private Tablature test;
	private ArrayList<Measure> myMeasure;
	private Measure m1, m2;
	
	/**
	 * 
	 * Instantiates a new Tablature instance called test. It also creates two instances of measure with the 
	 * spacing value set as 0f and it also instantiates a new ArrayList of type measure which will be used
	 * in testing the methods
	 */
	@Before
	public void setUp() {
		test = new Tablature();
		myMeasure = new ArrayList<Measure>();
		m1= new Measure(0f);
		m2 = new Measure(0f);
	}
	
	/**
	 * Test case for the constructor of Tablature which creates a new instance of Tablature with both
	 * the title and subtitle set as the string "Default". This method is tested by asserting the title
	 * and subtitle are equal to the string "Default"
	 */
	@Test
	public void testTablature() {
		assertEquals(test.getTitle(), "Default");
		assertEquals(test.getSubtitle(), "Default");
	}
	
	/**
	 * Test case for the method addLineToLastMeasure(String g) which adds a string/line to the current instance
	 * of tablature's last measure. This method is tested by asserting that each added line to equal to the 
	 * string in the last position of the tablature's measure
	 */
	@Test
	public void testAddLineToLastMeasure() {
		assertEquals(test.size(), 0);
		
		String g = "||*-----<5>-----------<7>----------------------------*||";
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(0), g);
		
		g = "||*-----<5>-----------<1>----------------------------*||";
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(1), g);
		
		g = "||*-----<5>-----------<3>----------------------------*||";
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(2), g);
		
		g = "||*-----<5>-----------<9>----------------------------*||";
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(3), g);
		
		g = "||*-----<5>-----------<5>----------------------------*||";
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(4), g);
		
		g = "||*-----<5>-----------<6>----------------------------*||";
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(5), g);
		
		g = "||*-----<5>-----------<8>----------------------------*||";
		test.addLineToLastMeasure(g);	
		assertEquals(test.getMeasures().get(1).getLines().get(0), g);
	}
	
	/**
	 * Test case for the method addMultiMeasureLine(StringTokenizer StrTkn) which accepts a stringTokenizer
	 * and splits it into individual lines which are then added to the tablature's measure. The StringTokenizer
	 * is delimited by spaces. This method is tested by passing it a StringTokenizer and asserting that the 
	 * tablature's measure contains the appropriately separated lines.
	 */
	@Test
	public void testAddMultiMeasureLine() {
		StringTokenizer sample = new StringTokenizer("||*-----<5>-----------<7>----------------------------*|| "
				+ "|-0--------------7--------------------------0---------| "
				+ "||*-----<5>-----------<7>----------------------------*|| ");
		
		String one = "||*-----<5>-----------<7>----------------------------*||";
		String two = "|-0--------------7--------------------------0---------|";
		String three = "||*-----<5>-----------<7>----------------------------*||";
		
		test.addMultiMeasureLine(sample);
		
		assertEquals(test.getMeasures().get(0).getLines().get(0), one);
		assertEquals(test.getMeasures().get(1).getLines().get(0), two);
		assertEquals(test.getMeasures().get(2).getLines().get(0), three);
	}

	/**
	 * Test case for the method printMeasures() which prints the all the measures currently in Tablature.
	 * This method is tested by first switching the output stream to a file and then asserting that the 
	 * contents of the file are equal to the expected value.
	 */
	@Test
	public void testPrintMeasures() {
		try {
			PrintStream log = new PrintStream(new File("test files/tablature/printMeasures.txt"));
			System.setOut(log);
			
			String g = "||*-----<5>-----------<7>----------------------------*||";
			
			test.addLineToLastMeasure(g);			
			test.addLineToLastMeasure(g);
			test.addLineToLastMeasure(g);			
			test.addLineToLastMeasure(g);			
			test.addLineToLastMeasure(g);		
			test.addLineToLastMeasure(g);	
			
			test.printMeasures();
			
			Scanner input = new Scanner(new File("test files/tablature/printMeasures.txt"));
			
			assertEquals(input.nextLine(), "Single");
			assertEquals(input.nextLine(), g);
			assertEquals(input.nextLine(), g);
			assertEquals(input.nextLine(), g);
			assertEquals(input.nextLine(), g);
			assertEquals(input.nextLine(), g);
			assertEquals(input.nextLine(), g);
			
			input.close();
		}
		catch (FileNotFoundException e) { }
	}
	
	/**
	 * Test case for the size() method which returns the current number of measures in Tablature. This method
	 * is tested by adding one measure to the tablature and then asserting the size is equal to 1. Another measure 
	 * is then added and the current size is asserted to be 2
	 */
	@Test
	public void testSize() {
		assertEquals(test.size(), 0);
		
		String g = "||*-----<5>-----------<7>----------------------------*||";
		
		test.addLineToLastMeasure(g);
		test.addLineToLastMeasure(g);
		test.addLineToLastMeasure(g);
		test.addLineToLastMeasure(g);
		test.addLineToLastMeasure(g);
		
		assertEquals(test.size(), 1);
		
		test.addLineToLastMeasure(g);
		test.addLineToLastMeasure(g);
		
		assertEquals(test.size(), 2);
	}
	
	/**
	 * Test case for the method getSpacing() which returns the current spacing value. The default value
	 * for the spacing is 5f. This method is tested by asserting it returns 5f
	 */
	@Test
	public void testGetSpacing() {
			assertEquals(test.getSpacing(), 5f, 0.0);
	}
	
	/**
	 * Test case for the method getMeasures() which returns an arrayList of all the measures currently
	 * in Tablature. This method is tested by iterating through the returned arrayList and asserting that
	 * each element in the arrayList is equal to the expected value.
	 */
	@Test
	public void testGetMeasures() {
		String g = "||*-----<5>-----------<7>----------------------------*||";
		test.addLineToLastMeasure(g);
		
		g = "||*-----<4>-----------<10>----------------------------*||";
		test.addLineToLastMeasure(g);
		
		g = "||*-----<3>-----------<0>----------------------------*||";
		test.addLineToLastMeasure(g);
		
		
		assertEquals(test.getMeasures().get(0).getLines().get(0), "||*-----<5>-----------<7>----------------------------*||");
		assertEquals(test.getMeasures().get(0).getLines().get(1), "||*-----<4>-----------<10>----------------------------*||");
		assertEquals(test.getMeasures().get(0).getLines().get(2), "||*-----<3>-----------<0>----------------------------*||");
	}
	
	/**
	 * Test case for the method setMeasures(ArrayList<Measure> myMeasure) which sets an arrayList of measures
	 * as the tablature's measure. This method is tested by adding measures m1 and m2 to the arrayList myMeasure
	 * and myMeasure is then used as an argument for this method. The tablature's measures are then asserted to 
	 * be the same as myMeasures
	 */
	@Test
	public void testSetMeasures() {
		m1.addLine("*--------------------------------2---------------------*");
		m1.addLine("||*-----<5>-----------<7>----------------------------*||");
		m1.addLine("||-0-----------7-----------------------------0---------|");
		m1.addLine("*--------------------------------2---------------------*");
		m1.addLine("|-0--------------7--------------------------0---------||");
		m1.addLine("|-----0----------10-------------0-------0-------5s7----|");
		
		m2.addLine("*--------------------------------2---------------------*");
		m2.addLine("||*-----<5>-----------<7>----------------------------*||");
		m2.addLine("||-0-----------7-----------------------------0---------|");
		m2.addLine("*--------------------------------2---------------------*");
		m2.addLine("|-0--------------7--------------------------0---------||");
		m2.addLine("|-----0----------10-------------0-------0-------5s7----|");
		
		myMeasure.add(m1);
		myMeasure.add(m2);
		test.setMeasures(myMeasure);
		
		assertSame(test.getMeasures(), myMeasure);
	}

	/**
	 * Test case for the setSpacing(float mySpacing) which sets the spacing for each and every measure
	 * currently in tablature. This method is tested by adding the measures m1 and m2 to tablature and then 
	 * setting tablature's spacing to 70f. The spacing of m1 and m2 in tablature is asserted to equal 70f
	 */
	@Test
	public void testSetSpacing() {
		assertEquals(test.getSpacing(), 5f, 0.0);
		
		m1.addLine("*--------------------------------2---------------------*");
		m1.addLine("||*-----<5>-----------<7>----------------------------*||");
		m1.addLine("||-0-----------7-----------------------------0---------|");
		m1.addLine("*--------------------------------2---------------------*");
		myMeasure.add(m1);
		
		m2.addLine("*--------------------------------2---------------------*");
		m2.addLine("||*-----<5>-----------<7>----------------------------*||");
		m2.addLine("||-0-----------7-----------------------------0---------|");
		myMeasure.add(m2);
		
		test.setMeasures(myMeasure);
		test.setSpacing(70f);
		
		assertEquals(test.getSpacing(), 70f, 0.0);
		assertEquals(test.getMeasures().get(0).getSpacing(), 70f, 0.0);
		assertEquals(test.getMeasures().get(1).getSpacing(), 70f, 0.0);
	}

	/**
	 * Test case for the method getTitle() which returns the title of the Tablature
	 */
	@Test
	public void testGetTitle() {
		assertEquals(test.getTitle(), "Default");
	}
	
	/**
	 * Test case for the method setTitle(String myTitle) which changes the tablature's title to the 
	 * string myTitle
	 */
	@Test
	public void testSetTitle() {
		assertEquals(test.getTitle(), "Default");
		test.setTitle("Remembering rain");
		assertEquals(test.getTitle(), "Remembering rain");
	}
	
	/**
	 * Test case for the method getTitle() which returns the title of the Tablature
	 */
	@Test
	public void testGetSubtitle() {
		assertEquals(test.getSubtitle(), "Default");
	}

	/**
	 * Test case for the method setSubtitle(String mySbutitle) which changes the tablature's subtitle to the 
	 * string mySutitle
	 */
	@Test
	public void testSetSubtitle() {
        assertEquals(test.getSubtitle(), "Default");
		test.setSubtitle("Michaelangelo");
		assertEquals(test.getSubtitle(), "Michaelangelo");
	}
}