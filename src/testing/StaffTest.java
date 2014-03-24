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
import cse2311.Staff;

public class StaffTest {
	public Measure m, m1;
	public ArrayList<StringBuffer> myLines;
	public Staff test;
	
	@Before
	public void setUp() throws Exception {
		m = new Measure(5f);
		m1 = new Measure(5f);
		myLines = new ArrayList<StringBuffer>();
		test = new Staff(300f);
	}
	
	@Test
	public void testStaff() {
		Staff check = new Staff(5f);
		assertEquals(check.getPrintSpace(), 5f, 0.0);
	}
	
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
			if(test.getLines().get(i).toString().compareTo(result.get(i).toString()) != 0)
				fail();
		}
		assertTrue(true);  
	}
	
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
		
		assertSame(test.getrepeatNum().get(1) , 3);
	}
	
	@Test
	public void testSetrepeatNum() {
		ArrayList<Integer> repeat1 = new ArrayList<Integer>();
		repeat1.add(5);
		repeat1.add(59);
		repeat1.add(12);
		
		ArrayList<Integer> repeat2 = new ArrayList<Integer>();
		repeat2.add(84);
		repeat2.add(95);
		repeat2.add(198);
		repeat2.add(913);
		repeat2.add(193);
		repeat2.add(546);
		
		ArrayList<Integer> repeat3 = new ArrayList<Integer>();
		repeat2.add(8);
		repeat2.add(93);
		repeat2.add(11);
		repeat2.add(9);
		repeat2.add(19);
		repeat2.add(54);
		
		test.setrepeatNum(repeat1);
		assertEquals(test.getrepeatNum(), repeat1);
		
		m.addLine("||----2-0------2-0------2-0-----2--*||+8");
    	m.addLine("||---3--------2--------0-----0-0-----|+93");
		m.addLine("*------2-0----2-0------2-0-----2-----*+11");
		m.addLine("||*----2-0------2-0------2-0-----2--*|+9");
		m.addLine("|--------2-0------2-0------2-0----2-*|+19"); 
		m.addLine("||---3--------2--------0-----0-0-----|+54");
		
		test.addToStaff(m);
		
	    for (int i = 0; i < repeat3.size(); i++) 
			assertEquals(test.getrepeatNum().get(i), repeat3.get(i));
	    
		test.setrepeatNum(repeat2);
		for (int i = 0; i < repeat2.size(); i++) 
			assertEquals(test.getrepeatNum().get(i), repeat2.get(i));
	}
	
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
	
	@Test
	public void testCanFitAnother() {
		m.addLine("||----2-0------2-0------2-0-----2--*||");
    	m.addLine("||---3--------2--------0-----0-0-----|");
		m.addLine("*------2-0----2-0------2-0-----2-----*");
		m.addLine("||*----2-0------2-0------2-0-----2--*|");
		m.addLine("|--------2-0------2-0------2-0----2-*|"); 
		m.addLine("||---3--------2--------0-----0-0-----|");
		
		//printspace value defined as 300f in method setup
		//test width is roughly about 226.6f
		//m width is roughly about 226.6f
		
		boolean result = test.canFitAnother(m);
		assertTrue(result);
		test.addToStaff(m);
		
		m1.addLine("||-----3-----1-----0-----0-||"); //testwidth 226.6
    	m1.addLine("||-3-----3-----3-----3------|"); //m1 width 151.6
		m1.addLine("*-3-----3-----3-----3---2-0-*");
		m1.addLine("||*--0-3--------3-----3-----|");
		m1.addLine("|-3-----3-----3-----3---2-0*|"); 
		m1.addLine("||--3-----3-----3-----3--3--|");
		
		result = test.canFitAnother(m1);
		assertFalse(result);
		}
	
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
			if(test.getLines().get(i).toString().compareTo(result.get(i).toString()) != 0)
				fail(); 
		}
	    assertTrue(true);  
	}
	
	@Test
	public void testGetPrintSpace() { //PrintSpace equals 100f as defined in the initialization of test in the setup method
		assertEquals(test.getPrintSpace(), 300f, 0.0);
	}
	
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
			PrintStream log = new PrintStream(new File("test files/staff/testprintlines.txt"));
			System.setOut(log);
			test.printLines();
			
			Scanner input = new Scanner(new File("test files/staff/testprintlines.txt"));
			
			assertEquals(input.nextLine(), "D-||----2-0------2-0------2-0-----2--*||-D");
			assertEquals(input.nextLine(), "D-||---3--------2--------0-----0-0-----|-D");
			assertEquals(input.nextLine(), "D-|*------2-0----2-0------2-0-----2-----*|-D");
			assertEquals(input.nextLine(), "D-||*----2-0------2-0------2-0-----2--*|-D");
			assertEquals(input.nextLine(), "D-|--------2-0------2-0------2-0----2-*|-D");
			assertEquals(input.nextLine(), "D-||---3--------2--------0-----0-0-----|-D");
			
			input.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
	}
}
