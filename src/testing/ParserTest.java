package testing;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import cse2311.Parser;
import cse2311.Tablature;

public class ParserTest {
	public Parser test;
	public Tablature tab;
	
	@Before
	public void setUp() throws Exception {
		test = new Parser();
		tab = new Tablature();
	}

	@Test
	public void testParser() {
		assertTrue(true);
	}
	
	@Test
	public void testExceptionReadFile() {
		boolean check;
		try {
			tab = test.readFile(new File("remember.txt"));
		} 
		catch (FileNotFoundException e) {
			check = true;
			assertTrue(check);
		}
	}
	
	@Test
	public void testReadFile() {
		try {
			tab = test.readFile(new File("test files/parser/ParserTest.txt"));
			assertEquals(tab.getMeasures().size(), 0);
			
			tab = test.readFile(new File("test files/parser/ParserTest1.txt"));
			assertEquals(tab.getMeasures().size(), 0);
			assertEquals(tab.getSpacing(), 5.5, 0.0);
			assertEquals(tab.getSubtitle(), " Jim Mateos");
			assertEquals(tab.getTitle(), " Remembering Rain");
			
			tab = test.readFile(new File("test files/parser/ParserTest2.txt"));
			assertEquals(tab.getMeasures().size(), 6);
			assertEquals(tab.getSpacing(), 5, 0.0);
			assertEquals(tab.getSubtitle(), "Ludwig van Beethoven");
			assertEquals(tab.getTitle(), "Moonlight Sonata");
			
			tab = test.readFile(new File("test files/parser/ParserTest3.txt"));
			assertEquals(tab.getMeasures().size(), 7);
			assertEquals(tab.getSpacing(), 9, 0.0);
			assertEquals(tab.getSubtitle(), "Ludwig van Beethoven");
			assertEquals(tab.getTitle(), "ParserTest3");
			
			tab = test.readFile(new File("test files/parser/ParserTest4.txt"));
			assertEquals(tab.getSpacing(), 7, 0.0);
			assertEquals(tab.getSubtitle(), "L Beethoven");
			assertEquals(tab.getTitle(), "Moonlight");
			
			/*
			Scanner input = new Scanner(new File("MyLogFile"));
			
			tab = test.readFile(new File("test files/parser/ParserTest3.txt"));
			assertEquals(input.nextLine(), "Mar 20, 2014 11:51:06 AM cse2311.Parser readFile");
			assertEquals(input.nextLine(), "INFO: Line 4Wrongly Formated:");
			assertEquals(input.nextLine(), "Mar 20, 2014 11:51:06 AM cse2311.Parser readFile");
			assertEquals(input.nextLine(), "INFO: Line 11Wrongly Formated:");
			assertEquals(input.nextLine(), "Mar 20, 2014 11:51:06 AM cse2311.Parser readFile");
			assertEquals(input.nextLine(), "INFO: Line 18Wrongly Formated:");
			input.close();
			 */
			fail("ask the guys and the TA");
		} 
		catch (FileNotFoundException e) { e.printStackTrace(); }
	}
}
