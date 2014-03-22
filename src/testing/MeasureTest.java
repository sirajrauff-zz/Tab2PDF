package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import cse2311.Measure;

/**
 * JUnit test case for the Measure class
 */
public class MeasureTest {
	private Measure test1;
	private ArrayList<String> setArray;
	
	/**
	 * Instantiates a new instance of the measure class called test1 with spacing set to 5f.
	 * Creates an arrayList called setArray which will be used in cases when the setter method of measure is implemented.
	 */
	@Before
	public void setUp()  {
		test1 = new Measure(5f);
	    new ArrayList<String>();
	    setArray = new ArrayList<String>();
	}
	
	/**
	 * JUnit testcase for the method addLine(), which return true a line has be successfully added and false otherwise
	 * When lines 6 or more lines are added, it returns false
	 * tested by adding 5 lines and asserting false on the 6th line added.
	 */
	@Test
	public void testAddLine() {
		assertTrue(test1.addLine("*--------------------------------2---------------------*"));
		
		test1.addLine("||*-----<5>-----------<7>----------------------------*||");
		test1.addLine("||-0-----------7-----------------------------0---------|");
		test1.addLine("*--------------------------------2---------------------*");
		test1.addLine("|-0--------------7--------------------------0---------||");
		test1.addLine("|-----0----------10-------------0-------0-------5s7----|");
		
		assertFalse(test1.addLine("|---10-------------10-----0-------0-------5s7----7-----|"));
	}
	
	/**
	 * Tests the get_width method of the measure class.
	 * 
	 * This is done by adding lines to the measure and comparing the program's calculate value of width with the 
	 * actual value.
	 */
	@Test
    public void testGetWidth(){
		test1.addLine("||*--<5>--||");    //width = 12 * 5f
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||"); // Bar type Both so width += 6.6
		
    	assertEquals(test1.getWidth(), 66.6f, 0.001);
    }
	
	 /**
	 * Tests the method set_lines.
	 * 
	 * This is done by adding lines to set_Array and setting this array to the measure instance test.
	 * The original arrayList set_Array is tested for equality with test's arrayList
	 */
	@Test
	public void testSetlines(){
		    
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||*----2-0------2-0------2-0-----2--*||");
		 setArray.add("||---3--------2--------0-----0-0-----||");
			 
		 test1.setMyLines(setArray);
		 assertSame(test1.getLines(), setArray);
	    }
	 
	 @Test
	 public void testGet_lines(){
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||---3--------2--------0-----0-0-----|");
		 
		 test1.setMyLines(setArray);
		 assertTrue(test1.getLines() == setArray);
    }
    
    @Test
    public void testSize(){
    	setArray.add("||*----2-0------2-0------2-0-----2--*|");
		setArray.add("||*----2-0------2-0------2-0-----2--*|");
		setArray.add("||---3--------2--------0-----0-0-----|");
		 
		test1.setMyLines(setArray);
		assertEquals(test1.size(), 3);
		
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test1.size(), 4);
		
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test1.size(), 5);
		
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test1.size(), 6);
		
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test1.size(), 6);
    }
    
    /**
     * Tests the method setRepeat() which receives as parameters booleans
     * 
     * The method is tested by first making the setRepeat true by adding lines which signal that they are repeated
     * and then using the method setRepeat to change it to false.
     */
    @Test
    public void testSetRepeat(){
    	
    	test1.addLine("||*--<5>--||+4");    
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||"); 
		
    	assertTrue(test1.isRepeat());
    	
    	test1.setRepeat(false);
    	assertFalse(test1.isRepeat());
    	
    	test1.setMyLines(null);
    	
    	test1.addLine("||*--<5>--||");    
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||");
		
		assertFalse(test1.isRepeat());
		
		test1.setRepeat(true);
    	assertTrue(test1.isRepeat());
    }
    
    @Test
    public void testIsRepeat(){
    	
    	test1.addLine("||*--<5>--||+4");    
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||"); 
		
    	assertTrue(test1.isRepeat());
    	
    	test1.setMyLines(null);
    	
    	test1.addLine("||*--<5>--||");    
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||");
		
		assertFalse(test1.isRepeat());
    }
    
    @Test
    public void testGetRepeatNum(){
    	
    	test1.addLine("||*--<5>--||+4");    
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||"); 
		
    	assertEquals(test1.getRepeatNum(), 4);
    	
    	test1.setMyLines(null);
    	
    	test1.addLine("||*--<5>--||+9");    
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||");
		
		assertEquals(test1.getRepeatNum(), 9);
    }

    @Test
    public void testSetRepeatNum(){
    	
    	test1.addLine("||*--<5>--||+4");    
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||"); 
    	assertEquals(test1.getRepeatNum(), 4);
    	
    	test1.setRepeatNum(8);
    	assertEquals(test1.getRepeatNum(), 8);
    }
    
    @Test
    public void testGetBarType(){
    	test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("*------2-0----2-0------2-0-----2---*");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
		
		assertEquals(test1.getBarType(), "Both");
		
		test1.setMyLines(null);
		test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("||------2-0----2-0------2-0-----2---*");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
		
		assertEquals(test1.getBarType(), "Right");
		
		test1.setMyLines(null);
		test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("||------2-0----2-0------2-0-----2---||");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
		
		assertEquals(test1.getBarType(), "Single");
		
		test1.setMyLines(null);
		test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("*------2-0----2-0------2-0-----2---||");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
		
		assertEquals(test1.getBarType(), "Left");
    }
    
    @Test
    public void testSetBarType(){
    	
     	test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("||------2-0----2-0------2-0-----2---||");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
		
		test1.setBarType("Both");
		assertNotSame(test1.getBarType(), "Single");
    }
    
    @Test
    public void testPrintLines(){
    	test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("||------2-0----2-0------2-0-----2---||");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
		
		PrintStream log;
		try {
			log = new PrintStream(new File("MeasuretestPrintLines().txt"));
			System.setOut(log);
			
			test1.printLines();
			Scanner input = new Scanner(new File("MeasuretestPrintLines().txt"));
			
			assertEquals(input.nextLine(), "Single");
			assertEquals(input.nextLine(), "||----2-0------2-0------2-0-----2--*||");
			assertEquals(input.nextLine(), "||---3--------2--------0-----0-0-----|");
			assertEquals(input.nextLine(), "||------2-0----2-0------2-0-----2---||");
			assertEquals(input.nextLine(), "||*----2-0------2-0------2-0-----2--*|");
			assertEquals(input.nextLine(), "|--------2-0------2-0------2-0----2-*|");
			assertEquals(input.nextLine(), "||---3--------2--------0-----0-0-----|");
			input.close();
		} catch (FileNotFoundException e) { }
    }
    
    @Test
    public void testgetSpacing() {
    	assertEquals(test1.getSpacing(), 5f, 0.0);
    }
    
    @Test
    public void testsetSpacing() {
    	test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	
    	assertEquals(test1.getSpacing(), 5f, 0.0);
    	test1.setSpacing(67f);
    	
    	assertEquals(test1.getSpacing(), 67f, 0.0);
    	assertEquals(test1.getWidth(), 2546.6, 0.1);     //recalculates width by multiplying user input by length of string
    }
}