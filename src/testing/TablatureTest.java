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

public class TablatureTest {
	public Tablature test;
	public ArrayList<Measure> myMeasure;
	public Measure m1;
	public Measure m2;
	public PrintStream log;
	
	@Before
	public void setUp() throws Exception {
		test = new Tablature();
		myMeasure = new ArrayList<Measure>();
		m1= new Measure(0f);
		m2 = new Measure(0f);
	}
	
	@Test
	public void testTablature() {
		assertEquals(test.getTitle(), "Default");
		assertEquals(test.getSubtitle(), "Default");
	}
	
	@Test
	public void testAddLineToLastMeasure() {
		assertEquals(test.size(), 0);
		
		String g = "||*-----<5>-----------<7>----------------------------*||";
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(0), g);
		
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(1), g);
		
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(2), g);
		
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(3), g);
		
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(4), g);
		
		test.addLineToLastMeasure(g);
		assertEquals(test.getMeasures().get(0).getLines().get(5), g);
		
		test.addLineToLastMeasure(g);	
		assertEquals(test.getMeasures().get(1).getLines().get(0), g);
	}
	
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
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}
	
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
	
	@Test
	public void testGetSpacing() {
		assertEquals(test.getSpacing(), 5f, 0.0);
	}
	
	@Test
	public void testGetMeasures() {
		String g = "||*-----<5>-----------<7>----------------------------*||";
		test.addLineToLastMeasure(g);
		test.addLineToLastMeasure(g);
		test.addLineToLastMeasure(g);
		
		for (int i = 0; i <test.getMeasures().size(); i++) {
			if (test.getMeasures().get(0).getLines().get(i).compareTo(g) !=  0)
				fail();
		}
	}
	
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
	
	@Test
	public void testSetSpacing() {
		assertEquals(test.getSpacing(), 5f, 0.0);
		
		m1.addLine("*--------------------------------2---------------------*");
		m1.addLine("||*-----<5>-----------<7>----------------------------*||");
		m1.addLine("||-0-----------7-----------------------------0---------|");
		m1.addLine("*--------------------------------2---------------------*");
		
		m2.addLine("*--------------------------------2---------------------*");
		m2.addLine("||*-----<5>-----------<7>----------------------------*||");
		m2.addLine("||-0-----------7-----------------------------0---------|");
		
		myMeasure.add(m1);
		myMeasure.add(m2);
		
		test.setMeasures(myMeasure);
		test.setSpacing(70f);
		
		assertEquals(test.getSpacing(), 70f, 0.0);
		assertEquals(test.getMeasures().get(0).getSpacing(), 70f, 0.0);
		assertEquals(test.getMeasures().get(1).getSpacing(), 70f, 0.0);
	}
	
	@Test
	public void testGetTitle() {
		assertEquals(test.getTitle(), "Default");
	}
	
	@Test
	public void testSetTitle() {
		assertEquals(test.getTitle(), "Default");
		test.setTitle("Remembering rain");
		assertEquals(test.getTitle(), "Remembering rain");
	}
	
	@Test
	public void testGetSubtitle() {
		assertEquals(test.getSubtitle(), "Default");
	}
	
	@Test
	public void testSetSubtitle() {
        assertEquals(test.getSubtitle(), "Default");
		test.setSubtitle("Michaelangelo");
		assertEquals(test.getSubtitle(), "Michaelangelo");
	}
}
