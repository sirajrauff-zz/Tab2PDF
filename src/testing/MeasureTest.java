package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import backEnd.Measure;

/**
 * @author Jason
 * JUnit test case for the Measure class
 */
public class MeasureTest {
	private Measure test1;
	private ArrayList<String> setArray;
	
	/**
	 *Instantiates an instance of the measure class and sets the spacing to 5f
	 *also instantiates an ArrayList of type string which will be used in the testing
	 *of the measure class
	 */
	@Before
	public void setUp()  {
		test1 = new Measure(5f);	
		setArray = new ArrayList<String>();
	}
	
	/**
	 *Test case for the method addLine(String line), which returns true or false depending on 
	 *whether the line was added successfully. A line is successfully added if the current instance
	 *has a size less than 7. This method was tested by asserting that the addition 
	 *of the first line is true and the addition of the 7th line is false.
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
     * Test case for the method getBarType() which returns the BarType() of a set of lines. The method returns
     * Single for measures with ||--||,  Both for *---*, right ||---* and left for *---||. The third line is
     * used to determine this. The method is tested by asserting the respective bar types for the given set of 
     * measures
     */
    @Test
    public void testGetBarType() {
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
	
	/**
	 * Test case for the method getLines() which returns an arrayList of the current lines in measure.
	 */
	@Test
    public void testGetLines() {
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||---3--------2--------0-----0-0-----|");
		 test1.setMyLines(setArray);
		
		 assertTrue(test1.getLines() == setArray);
    }

	/**
     * Test case for getRepeatNum() method which returns the number of times a measure is repeated.
     * This method is tested by adding lines that are should be repeated 4 times
     * then asserting that the method returns 4. This action is performed again for a different value.
     */
    @Test
    public void testGetRepeatNum() {
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

	 /**
     * Test case for the method getSpacing() which returns the current value for spacing. In this case the current
     * value is 5f
     */
    @Test
    public void testGetSpacing() {
		assertEquals(test1.getSpacing(), 5f, 0.0);
    }

	/**
	 * Test case for the getWidth() method. 
	 */
	@Test
    public void testGetWidth() {
		test1.addLine("||*--<5>--||"); //width = 12 * 5f
		test1.addLine("||-0--7--0-|");
		test1.addLine("*--2-------*");
		test1.addLine("|-0-7--0--||");
		test1.addLine("|--0---5s7-|");
		test1.addLine("|-0----0--||"); // Bar type Both so width += 6.6
		
    	assertEquals(test1.getWidth(), 66.6f, 0.001);
    }

    /**
     * Test case for the method isRepeat() which returns either true or false if a measure is being repeated.
     * This method is tested by adding lines that deliberately indicate that they should be repeating and then 
     * asserting the method returns true. THis action is performed again for non-repeating lines.
     */
    @Test
    public void testIsRepeat() {
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
    
    /**
	 * Test case for the first constructor of the measure class. It verifies the constructor
	 * by asserting that the spacing value of this instance of the measure class is
	 * equal to 5f
	 */
	@Test
	public void testMeasure() {
		assertEquals(test1.getSpacing(), 5f, 0.0);
	}
    
    /**
	 * Test case for the second constructor of the measure class which accepts a string as a 
	 * parameter. This constructor is tested by passing a string to and then asserting that
	 * the size of this measure instance has increased to one
	 */
	@Test
	public void testMeasure2() {
		Measure test2 = new Measure("|---2-----2-----2-----2---|");
		assertEquals(test2.size(), 1);
	}

    /**
     * Test case for the method printLines() which prints out the current lines in the measure and the 
     * bar type. This method is tested by adding 6 lines to the measure and then switching the default 
     * output(console) to a file. The method is then called and the contents of the file are asserted 
     * to equal the expected output.
     */
    @Test
    public void testPrintLines() {
    	test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("||------2-0----2-0------2-0-----2---||");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
	
		PrintStream log;
		
		try {
			log = new PrintStream(new File("test files/measure/MeasuretestPrintLines().txt"));
			System.setOut(log);
			test1.printLines();
			
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

    /**
     * Test case for the setBarType() which allows the change of a BarType(). THis method is tested by adding lines
     * that have a Single BarType() and then using the method to set the BarType() to both. It is then asserted that
     * the current BarType() does not equal Single.
     */
    @Test
    public void testSetBarType() {
     	test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.addLine("||------2-0----2-0------2-0-----2---||");
		test1.addLine("||*----2-0------2-0------2-0-----2--*|");
		test1.addLine("|--------2-0------2-0------2-0----2-*|"); 
		test1.addLine("||---3--------2--------0-----0-0-----|");
		test1.setBarType("Both");
		
		assertNotSame(test1.getBarType(), "Single");
    }

    /**
	  * Test case for the setMyLines(ArrayList<String> userLines) method. This method sets the current instance
	  * of measure's lines to a user specified one. THis method is tested by using the arrayList setArray as an
	  * argument to this method and then asserting that the measure's line and setArray are the same.
	  */
	@Test
	 public void testSetLines() {
		 setArray.add("||*----2-0------2-0------2-0-----2--*|");
		 setArray.add("||*----2-0------2-0------2-0-----2--*||");
		 setArray.add("||---3--------2--------0-----0-0-----||");
		 test1.setMyLines(setArray);
		 
		 assertSame(test1.getLines(),  setArray);
	    }

    /**
     * Tests the method setRepeat() which receives as parameters booleans
     * 
     * The method is tested by first making the setRepeat true by adding lines which signal that they are repeated
     * and then using the method setRepeat to change it to false.
     */
    @Test
    public void testSetRepeat() {
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

    /**
     * Test case for the method setRepeatNum() which sets the number of times a line should be repeated. This
     * method is tested by adding lines that indicate they should be repeated 4 times and then using this method
     * to make it 8. It is then asserted that the value is now 8
     */
    @Test
    public void testSetRepeatNum() {
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
    
    /**
     * Test case for the method setSpacing which changes the value of the spacing. Changing the spacing also changes
     * the width. THis method is tested by first asserting that the width is 5f and then the method is used to 
     * change it to 67f after which is it is asserted the current spacing is 67f and the new width is as expected
     */
    @Test
    public void testSetSpacing() {
    	test1.addLine("||----2-0------2-0------2-0-----2--*||");
    	
    	assertEquals(test1.getSpacing(), 5f, 0.0);
    	test1.setSpacing(67f);
    	
    	assertEquals(test1.getSpacing(), 67f, 0.0);
    	assertEquals(test1.getWidth(), 2546.6, 0.1); //recalculates width by multiplying user input by length of string
    }
    
    /**
     * Test case for the size() method which returns the current number of lines in measure. This value
     * always falls between 0 and 6. This method is tested by gradually adding lines to measure and asserting
     * that the size increases by one with each addition. Once the 7th line is added, the size is asserted to 
     * still be 6.
     */
    @Test
    public void testSize() {
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
}