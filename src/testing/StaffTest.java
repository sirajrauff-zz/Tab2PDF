package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import backEnd.Measure;
import backEnd.Staff;

/**
 * @author Jason
 * Test class for the Staff Class
 */
public class StaffTest {
	private Measure m, m1;
	private Staff test;
	
	/**
	 * Instantiates an instance of two measures with a value of 5f each, an ArrayList of type StringBuffer and 
	 * a new staff instance with the value of 300f which will all be used in the testing of this class.
	 */
	@Before
	public void setUp() {
		m = new Measure(5f);
		m1 = new Measure(5f);
		test = new Staff(300f);	
	}

	/**
	 * Test case for the method addStringBuffer() which adds an empty StringBuffer to the staff's last 
	 * measure. It is tested by calling the method and asserting that the size has increased by one.
	 */
	@Test
	public void testAddStringBuffer() {
		m.addLine("||----2-0------2-0------2-0-----2--*||");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		
		test.addToStaff(m);
		
		test.addStringBuffer();
		
		assertEquals(test.getLines().size(), 6);
	}
	
	/**
	 * Test case for the addToStaff(Measure m) which adds a measure to the current staff instance. This method
	 * is tested by adding the Measure m to the staff instance test and then adding what the expected staff instance
	 * test should be containing to an ArrayList result. The staff's lines are then iterated through and compared
	 * element by element to the ArrayList result
	 */
	@Test
	public void testAddToStaff() {
		m.addLine("||----2-0------2-0------2-0-----2--*||");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		
		test.addToStaff(m);
		
		ArrayList<StringBuffer> result = new ArrayList<StringBuffer>();
		
		result.add(new StringBuffer("D-||----2-0------2-0------2-0-----2--*||-D"));
		result.add(new StringBuffer("D-||---3--------2--------0-----0-0-----|-D"));
		result.add(new StringBuffer("D-|*------2-0----2-0------2-0-----2-----*|-D"));
		result.add(new StringBuffer("D-||*----2-0------2-0------2-0-----2--*|-D"));
		result.add(new StringBuffer("D-|--------2-0------2-0------2-0----2-*|-D")); 
		result.add(new StringBuffer("D-||---3--------2--------0-----0-0-----|-D"));
		
		for (int i = 0; i < result.size(); i++) {
			if (test.getLines().get(i).toString().compareTo(result.get(i).toString()) != 0) { 
				fail(); 
			}
		}
	   assertTrue(true);  
	}

	/**
	 * Test case for the method canFitAnother(Measure m) which returns true or false depending on whether
	 * the given measure can fit in the staff. This method is tested by adding a measure to staff and then
	 * asserting that the staff can fit another measure, after which it is asserted that the staff can't fit
	 * another measure.
	 */
	@Test
	public void testCanFitAnother() {
		m.addLine("||----2-0------2-0------2-0-----2--*||");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		
		/* printSpace value defined as 300f in method setup
		 * test width is roughly about 226.6f
		 * m width is roughly about 226.6f*/
		
		boolean result = test.canFitAnother(m);
		
		assertTrue(result);
		
		test.addToStaff(m);
		
		/* testWidth 226.6
		 * m1 width 151.6*/
		m1.addLine("||-----3-----1-----0-----0-||");
    	m1.addLine("||-3-----3-----3-----3------|");
		m1.addLine("*-3-----3-----3-----3---2-0-*");
		m1.addLine("||*--0-3--------3-----3-----|");
		m1.addLine("|-3-----3-----3-----3---2-0*|"); 
		m1.addLine("||--3-----3-----3-----3--3--|");
		
		result = test.canFitAnother(m1);
		assertFalse(result);
	}

	/**
	 * Test case for the method getLines() which returns an arrayList of all the measures currently in staff.
	 * This method is tested by adding a measure to staff and adding the expected strings to an ArrayList result after
	 * which both the returned ArrayLists and results are iterated through and tested for equality.
	 */
	@Test
	public void testGetLines() {
		m.addLine("||----2-0------2-0------2-0-----2--*||");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		
		test.addToStaff(m);
		
		ArrayList<StringBuffer> result = new ArrayList<StringBuffer>();
		
		result.add(new StringBuffer("D-||----2-0------2-0------2-0-----2--*||-D"));
		result.add(new StringBuffer("D-||---3--------2--------0-----0-0-----|-D"));
		result.add(new StringBuffer("D-|*------2-0----2-0------2-0-----2-----*|-D"));
		result.add(new StringBuffer("D-||*----2-0------2-0------2-0-----2--*|-D"));
		result.add(new StringBuffer("D-|--------2-0------2-0------2-0----2-*|-D")); 
		result.add(new StringBuffer("D-||---3--------2--------0-----0-0-----|-D"));
		
		for (int i = 0; i < result.size(); i++) {
			if (test.getLines().get(i).toString().compareTo(result.get(i).toString()) != 0) 
				fail(); 
		}
	    assertTrue(true);  
	}
	
	/**
	 * Test case for the getPrintSpace() method which returns the print space the user specified during 
	 * initialization of the staff class.
	 */
	@Test
	public void testGetPrintSpace() { //PrintSpace equals 100f as defined in the initialization of test in the setup method
		assertEquals(test.getPrintSpace(), 300f, 0.0);
	}

	/**
	 * Test case for the method getrepeatNum() which returns an integer ArrayList containing all the 
	 * repeated number values of each measure. This method is tested by adding two measures to staff, each 
	 * with a repeating value and then asserting that the values in the returned arrayList are equal to the
	 * the expected values. 
	 */
	@Test
	public void testGetrepeatNum() {
		assertEquals(test.getrepeatNum().size(), 0);

		m.addLine("||----2-0------2-0------2-0-----2--*||+8");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		
		test.addToStaff(m);
	
		Measure m2 = new Measure(5f);
		
		m2.addLine("||----2-0------2-0------2-0-----2--*||+3");
    	m2.addLine("||---3--------2--------0-----0-0-----|");
		m2.addLine("*------2-0----2-0------2-0-----2-----*");
		m2.addLine("||*----2-0------2-0------2-0-----2--*|");
		m2.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m2.addLine("||---3--------2--------0-----0-0-----|");
		
		test.addToStaff(m2);
		
		assertSame(test.getrepeatNum().get(0) , 8);
		assertSame(test.getrepeatNum().get(1) , 3);
	}
	
	/**
	 * Test case for the method getTopInt() which returns the value of the first measure's repeated number
	 * which is the topmost value on the arrayList. When this method is called it removes the top INT. if
	 * empty, it returns -1. This method is tested by adding 3 measures each with a 
	 * repeated number and then asserting that the method returns the repeated number values for each measure
	 * in the order they were added. 
	 */
	@Test
	public void testGetTopInt() {
		m1 = new Measure(5f);
		Measure m2 = new Measure(5f);
		
		m.addLine("||----2-0------2-0------2-0-----2--*||+8");
		m.addLine("||----2-0------2-0------2-0-----2--*||");
		m.addLine("||----2-0------2-0------2-0-----2--*||");
		m.addLine("||----2-0------2-0------2-0-----2--*||");
		m.addLine("||----2-0------2-0------2-0-----2--*||");
		m.addLine("||----2-0------2-0------2-0-----2--*||");
		
    	m1.addLine("||---3--------2--------0-----0-0-----|+3");
    	m1.addLine("||---3--------2--------0-----0-0-----|");
    	m1.addLine("||---3--------2--------0-----0-0-----|");
    	m1.addLine("||---3--------2--------0-----0-0-----|");
    	m1.addLine("||---3--------2--------0-----0-0-----|");
    	m1.addLine("||---3--------2--------0-----0-0-----|");
		
    	m2.addLine("||---3--------2--------0-----0-0-----|+9");
    	m2.addLine("||---3--------2--------0-----0-0-----|");
    	m2.addLine("||---3--------2--------0-----0-0-----|");
    	m2.addLine("||---3--------2--------0-----0-0-----|");
    	m2.addLine("||---3--------2--------0-----0-0-----|");
    	m2.addLine("||---3--------2--------0-----0-0-----|");
    	
		test.addToStaff(m);
		test.addToStaff(m1);
		test.addToStaff(m2);
				
		assertEquals(test.getTopInt(), 8);
		assertEquals(test.getTopInt(), 3);
		assertEquals(test.getTopInt(), 9);
		
		assertEquals(test.getTopInt(), -1);
	}
	
	/**
	 * Test case for the method getWidth() which returns the value of the width of measures which is 
	 * calculated in the measure class. This method is tested by adding measures to staff and asserting 
	 * that the staff's width equals the theoretical width
	 */
	@Test
	public void testGetWidth() {
		m.addLine("||----2-0------2-0------2-0-----2--*||");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		
		test.addToStaff(m);
		
		assertEquals(test.getWidth(), 196.6f, 0.0);
	}

	/**
	 * Test case for the method printLines() which prints the formatted lines in staff. This method
	 * is tested by switching the output stream to a file and then asserting that the contents of the 
	 * file are equal to the expected output.
	 */
	@Test
	public void testPrintLines() {
		m.addLine("||----2-0------2-0------2-0-----2--*||");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		
		test.addToStaff(m);
		
		try {
			PrintStream log = new PrintStream(new File("test files/staff/test_printlines.txt"));
			System.setOut(log);
			test.printLines();
			
			Scanner input = new Scanner(new File("test files/staff/test_printlines.txt"));
			
			assertEquals(input.nextLine(), "D-||----2-0------2-0------2-0-----2--*||-D");
			assertEquals(input.nextLine(), "D-||---3--------2--------0-----0-0-----|-D");
			assertEquals(input.nextLine(), "D-|*------2-0----2-0------2-0-----2-----*|-D");
			assertEquals(input.nextLine(), "D-||*----2-0------2-0------2-0-----2--*|-D");
			assertEquals(input.nextLine(), "D-|--------2-0------2-0------2-0----2-*|-D");
			assertEquals(input.nextLine(), "D-||---3--------2--------0-----0-0-----|-D");
			
			input.close();
		} catch (FileNotFoundException e) {  }
	}

	/**
	 * Test case for the method setrepeatNum(ArrayList<Integer> userNum) which allows the user to set the 
	 * repetition value for each of staff's measures. This method is test in three stages. The first stage calls
	 * the method  and sets repeat1 and it is then asserted the arrayList in staff is the same as repeat1. The 
	 * second stage involves add measures that have repeated measures and then setting the staff's repeatNum
	 * arrayList to repeat2. It is then asserted that the repeatNum in staff is equal to the values of repeat2
	 */
	@Test
	public void testSetrepeatNum() {                 
		Measure m1 = new Measure(0f);
		Measure m2 = new Measure(0f);
		
		ArrayList<Integer> repeat1 = new ArrayList<Integer>();
		repeat1.add(5);
		repeat1.add(8);
		repeat1.add(1);
		
		ArrayList<Integer> repeat2 = new ArrayList<Integer>();
		repeat2.add(8);
		repeat2.add(9);
		repeat2.add(1);
		repeat2.add(9);
		repeat2.add(3);
		repeat2.add(4);
		
		test.setrepeatNum(repeat1);
		assertEquals(test.getrepeatNum(), repeat1);
		
		test.setrepeatNum(null);
	
		m.addLine("||----2-0------2-0------2-0-----2--*||+8");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		test.addToStaff(m);
		
		m1.addLine("||----2-0------2-0------2-0-----2--*||+5");
    	m1.addLine("||---3--------2--------0-----0-0-----|");
		m1.addLine("*------2-0----2-0------2-0-----2-----*");
		m1.addLine("||*----2-0------2-0------2-0-----2--*|");
		m1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m1.addLine("||---3--------2--------0-----0-0-----|");
		test.addToStaff(m1);
		
		m2.addLine("||----2-0------2-0------2-0-----2--*||+2");
    	m2.addLine("||---3--------2--------0-----0-0-----|");
		m2.addLine("*------2-0----2-0------2-0-----2-----*");
		m2.addLine("||*----2-0------2-0------2-0-----2--*|");
		m2.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m2.addLine("||---3--------2--------0-----0-0-----|");
		test.addToStaff(m2);
		
		assertSame(test.getrepeatNum().get(0), 8);
		assertSame(test.getrepeatNum().get(1), 5);
		assertSame(test.getrepeatNum().get(2), 2);
		
		test.setrepeatNum(repeat2);
		
	    for (int i = 0; i < repeat2.size(); i++) {
			assertEquals(test.getrepeatNum().get(i), repeat2.get(i));
		}
	}
	
	/**
	 * Test case for the Staff(float printSpace) constructor which initializes an instance of the staff class
	 * and sets the print space with the user defined value. The constructor is tested by instantiating an
	 * instance of staff with a value of 5f and asserting its print space is 5f
	 */
	@Test
	public void testStaff() {
		Staff check = new Staff(5f);
		assertEquals(check.getPrintSpace(), 5f, 0.0);
	}
}