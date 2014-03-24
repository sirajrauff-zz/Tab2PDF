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
	private Measure test;
	private ArrayList<String> setArray;
	
	@Before
	public void setUp()  {
		test = new Measure(5f);
	    new ArrayList<String>();
	    setArray = new ArrayList<String>();
	}
	
	@Test
	public void testMeasure() {
		assertEquals(test.getSpacing(), 5f, 0.0);
	}

	@Test
	public void testMeasure2() {
		Measure test2 = new Measure("|---2-----2-----2-----2---|");
		assertEquals(test2.size(), 1);
	}
	 
	@Test
	public void testAddLine() {
		assertTrue(test.addLine("*--------------------------------2---------------------*"));
		
		test.addLine("||*-----<5>-----------<7>----------------------------*||");
		test.addLine("||-0-----------7-----------------------------0---------|");
		test.addLine("*--------------------------------2---------------------*");
		test.addLine("|-0--------------7--------------------------0---------||");
		test.addLine("|-----0----------10-------------0-------0-------5s7----|");
		
		assertFalse(test.addLine("|---10-------------10-----0-------0-------5s7----7-----|"));
	}
	
	/**
	 * Tests the getWidth method of the measure class.
	 * This is done by adding lines to the measure and comparing the program's calculate value of width with the 
	 * actual value.
	 */
	@Test
    public void testGetWidth() {
		test.addLine("||*--<5>--||"); //width = 12 * 5f
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||"); // Bar type Both so width += 6.6
    	assertEquals(test.getWidth(), 66.6f, 0.001);
    }
	
	 /**
	 * Tests the method setLines.
	 * This is done by adding lines to setArray and setting this array to the measure instance test.
	 * The original arrayList setArray is tested for equality with test's arrayList
	 */
	@Test
	 public void testSetLlines() {
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||*----2-0------2-0------2-0-----2--*||");
		 setArray.add("||---3--------2--------0-----0-0-----||");
		 test.setMyLines(setArray);
			
		 assertSame(test.getLines(),  setArray);
	    }
	 
	@Test
    public void testGetLines() {
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||---3--------2--------0-----0-0-----|");
		 test.setMyLines(setArray);
		 assertTrue(test.getLines() == setArray);
    }
    
    @Test
    public void testSize() {
    	setArray.add("||*----2-0------2-0------2-0-----2--*|");
		setArray.add("||*----2-0------2-0------2-0-----2--*|");
		setArray.add("||---3--------2--------0-----0-0-----|");
		test.setMyLines(setArray);
    	
		assertEquals(test.size(), 3);
		
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test.size(), 4);
		
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test.size(), 5);
		
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test.size(), 6);
		
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		assertEquals(test.size(), 6);
    }
    
    /**
     * Tests the method setRepeat() which receives as parameters booleans
     * The method is tested by first making the setRepeat true by adding lines which signal that they are repeated
     * and then using the method setRepeat to change it to false.
     */
    @Test
    public void testSetRepeat() {
    	test.addLine("||*--<5>--||+4");    
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||"); 
    	assertTrue(test.isRepeat());
    	
    	test.setRepeat(false);
    	assertFalse(test.isRepeat());
    	
    	test.setMyLines(null);
    	test.addLine("||*--<5>--||");    
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||");
		assertFalse(test.isRepeat());
		
		test.setRepeat(true);
    	assertTrue(test.isRepeat());
    }
    
    @Test
    public void testIsRepeat() {
    	test.addLine("||*--<5>--||+4");    
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||"); 
    	assertTrue(test.isRepeat());
    	
    	test.setMyLines(null);
    	test.addLine("||*--<5>--||");    
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||");
		assertFalse(test.isRepeat());
    }
    
    @Test
    public void testGetRepeatNum() {
    	test.addLine("||*--<5>--||+4");    
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||"); 
    	assertEquals(test.getRepeatNum(), 4);
    	
    	test.setMyLines(null);
    	test.addLine("||*--<5>--||+9");    
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||");
		assertEquals(test.getRepeatNum(), 9);
    }

    @Test
    public void testSetRepeatNum() {
    	test.addLine("||*--<5>--||+4");    
		test.addLine("||-0--7--0-|");
		test.addLine("*--2-------*");
		test.addLine("|-0-7--0--||");
		test.addLine("|--0---5s7-|");
		test.addLine("|-0----0--||"); 
    	assertEquals(test.getRepeatNum(), 4);
    	
    	test.setRepeatNum(8);
    	assertEquals(test.getRepeatNum(), 8);
    }
    
    @Test
    public void testGetBarType() {
    	test.addLine("||----2-0------2-0------2-0-----2--*||");
    	test.addLine("||---3--------2--------0-----0-0-----|");
		test.addLine("*------2-0----2-0------2-0-----2---*");
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		test.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test.addLine("||---3--------2--------0-----0-0-----|");
		assertEquals(test.getBarType(), "Both");
		
		test.setMyLines(null);
		test.addLine("||----2-0------2-0------2-0-----2--*||");
    	test.addLine("||---3--------2--------0-----0-0-----|");
		test.addLine("||------2-0----2-0------2-0-----2---*");
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		test.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test.addLine("||---3--------2--------0-----0-0-----|");
		assertEquals(test.getBarType(), "Right");
		
		test.setMyLines(null);
		test.addLine("||----2-0------2-0------2-0-----2--*||");
    	test.addLine("||---3--------2--------0-----0-0-----|");
		test.addLine("||------2-0----2-0------2-0-----2---||");
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		test.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test.addLine("||---3--------2--------0-----0-0-----|");
		assertEquals(test.getBarType(), "Single");
		
		test.setMyLines(null);
		test.addLine("||----2-0------2-0------2-0-----2--*||");
    	test.addLine("||---3--------2--------0-----0-0-----|");
		test.addLine("*------2-0----2-0------2-0-----2---||");
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		test.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test.addLine("||---3--------2--------0-----0-0-----|");
		assertEquals(test.getBarType(), "Left");
    }
    
    @Test
    public void testSetBarType() {
     	test.addLine("||----2-0------2-0------2-0-----2--*||");
    	test.addLine("||---3--------2--------0-----0-0-----|");
		test.addLine("||------2-0----2-0------2-0-----2---||");
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		test.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test.addLine("||---3--------2--------0-----0-0-----|");
		test.setBarType("Both");
		
		assertNotSame(test.getBarType(), "Single");
    }
    
    @Test
    public void testPrintLines() {
    	test.addLine("||----2-0------2-0------2-0-----2--*||");
    	test.addLine("||---3--------2--------0-----0-0-----|");
		test.addLine("||------2-0----2-0------2-0-----2---||");
		test.addLine("||*----2-0------2-0------2-0-----2--*|");
		test.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test.addLine("||---3--------2--------0-----0-0-----|");
		
		PrintStream log;
		try {
			log = new PrintStream(new File("test files/measure/MeasuretestPrintLines().txt"));
			System.setOut(log);
			test.printLines();
			
			Scanner input = new Scanner(new File("test files/measure/MeasuretestPrintLines().txt"));
			
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
    public void testGetSpacing() {
    	assertEquals(test.getSpacing(), 5f, 0.0);
    }
    
    @Test
    public void testSetSpacing() {
    	test.addLine("||----2-0------2-0------2-0-----2--*||");
    	
    	assertEquals(test.getSpacing(), 5f, 0.0);
    	test.setSpacing(67f);
    	
    	assertEquals(test.getSpacing(), 67f, 0.0);
    	assertEquals(test.getWidth(), 2546.6, 0.1); //recalculates width by multiplying user input by length of string
    }
}
    

