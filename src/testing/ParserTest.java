package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

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
		fail("Not yet implemented");
	}
	
	@Test
	public void testReadFile() {
		try {
			tab = test.readFile(new File("ParserTest.txt"));
			assertEquals(tab.getMeasures().size(), 0);
			
			tab = test.readFile(new File("ParserTest1.txt"));
			assertEquals(tab.getMeasures().size(), 0);
			assertEquals(tab.getSpacing(), 5.5, 0.0);
			assertEquals(tab.getSubtitle(), " Jim Mateos");
			assertEquals(tab.getTitle(), " Remembering Rain");
			
			tab = test.readFile(new File("ParserTest2.txt"));
			assertEquals(tab.getMeasures().size(), 6);
			assertEquals(tab.getSpacing(), 5, 0.0);
			assertEquals(tab.getSubtitle(), "Ludwig van Beethoven");
			assertEquals(tab.getTitle(), "Moonlight Sonata");
			
			//PrintStream log = new PrintStream(new File("Error Parser.txt"));
			//Scanner input = new Scanner(new File("Error Parser.txt"));
			//System.setOut(log);
			
			//tab = test.readFile(new File("ParserTest3.txt"));
			//assertEquals(input.nextLine(), "UnSupported Symbol in");
		} 
		catch (FileNotFoundException e) {  e.printStackTrace(); }
	}
}
