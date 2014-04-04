package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import backEnd.Parser;
import backEnd.Tablature;

/**
 * @author Jason
 * Test class for the Parser Class
 */
public class ParserTest {
	private Parser test;
	private Tablature tab;
	
	/**
	 * Instantiates an instance of Parser called test and an instance of the class Tablature
	 * which stores the information the parser parses from the text file
	 */
	@Before
	public void setUp() throws Exception {
		test = new Parser();
		tab = new Tablature();
	}

	/**
	 * This method tests that an exception is thrown when a non-existent file is selected. This exception
	 * will never be thrown when the GUI is being used
	 */
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
	
	/**
	 * Test case for the method readFile(File input) which parses a text file and stores the information
	 * in a Tablature which it returns. This method is tested by converting 6 possible user inputs and checking
	 * if the correct results are produced
	 */
	@Test
	public void testReadFile() {
		try {
			/* Parses an empty file
			 * Number of Measures is 0
			 * Title and subtitle are set to the "Default"
			 * Spacing is set to 5f*/
			tab = test.readFile(new File("test files/parser/Empty.txt"));
			assertEquals(tab.getMeasures().size(), 0);
			assertEquals(tab.getTitle(), "Default");
			assertEquals(tab.getSubtitle(), "Default");
			
			//Parses the first text file with only a title, subtitle and spacing
			tab = test.readFile(new File("test files/parser/ParserTest1.txt"));
			assertEquals(tab.getMeasures().size(), 0);
			assertEquals(tab.getSpacing(), 5.5, 0.0);
			assertEquals(tab.getSubtitle(), " Jim Mateos");
			assertEquals(tab.getTitle(), " Remembering Rain");
		
			//Parses a 2nd text file with a title, subtitle, spacing and a few measures
			tab = test.readFile(new File("test files/parser/ParserTest2.txt"));
			assertEquals(tab.getMeasures().size(), 6);
			assertEquals(tab.getSpacing(), 5, 0.0);
			assertEquals(tab.getSubtitle(), "Ludwig van Beethoven");
			assertEquals(tab.getTitle(), "Moonlight Sonata");
			
			//Parses a 3rd text file that only contains measures with no title, subtitle, spacing
			tab = test.readFile(new File("test files/parser/ParserTest3.txt"));
			assertEquals(tab.getMeasures().size(), 7);
			assertEquals(tab.getSpacing(), 5, 0.0);
			assertEquals(tab.getSubtitle(), "Default");
			assertEquals(tab.getTitle(), "Default");
			
			/* Parses a 4th text file with completely messed up measures, improper indentation,
			 * two sets of title, spacing, and subtitle, and foreign characters */
			tab = test.readFile(new File("test files/parser/ParserTest4.txt"));
			assertEquals(tab.getSpacing(), 7, 0.0);
			assertEquals(tab.getSubtitle(), "L Beethoven");
			assertEquals(tab.getTitle(), "Moonlight");
			
			/* Parses a 5th text file with missing ending bars and improperly indented lines
			 * and lines with uneven lengths*/
			tab = test.readFile(new File("test files/parser/ParserTest5.txt"));
			assertEquals(tab.getSpacing(), 3, 0.0);
			assertEquals(tab.getSubtitle(), "Ludwig ");
			assertEquals(tab.getTitle(), "ParserTest5");
			
			/* The following is the error log output that the 4th and 5th text file should 
			 * produce.
			 * 
			 * The error output has the following out 
			 * 
			 * Date cse2311.Parser readFile, for example Mar 28, 2014 1:22:35 PM cse2311.Parser readFile
			 * 
			 * The above is omitted from the below but every empty line signifies that the above
			 * format is there
			 * 
			 * The following is the output from the 4th text file
				
				INFO: Line 9 has no separating characters `|`:-----------------------------------------------------------3-----------------------------------------------

				INFO: Line 10 has no separating characters `|`:-----------5--2-----------------------------4--2-----7--2---------------------------7--4--2----------------
				
				INFO: Line 11 has no separating characters `|`:-----1-----------11------11--------------7------------------------11--10---------7-------------------------
				
				INFO: Line 12 has no separating characters `|`:---------------------12------18--15--15---------/14-----------18--------------9--------------3--9------19--
				
				INFO: Line 14 has no separating characters `|`:--------------------------------------------------------------------------10-------------------------------
				
				INFO: Line 16 has no separating characters `|`:------------------------------------------------------------2-----0--17--20--20-------------5-------------------
				
				INFO: Line 17 has no separating characters `|`:-----2----------------6--9--8-----20------13--12--17--------------------------------------------------13--------
				
				INFO: Line 19 has no separating characters `|`:--------------------------------------15--------------9--3-----2----------------------------------15------7-----
				
				INFO: Line 20 has no separating characters `|`:---------------15--6---------------------------------------------------------------------------5----------------
				
				INFO: Line 21 ignored, no line:	fdsfsdf
				
				INFO: Line 22 ignored, no line:dfgdsfgdsgdsfg
				
				INFO: Line 23 has no separating characters `|`:-----------------------1----------------dfgdsfgdsfg---------------------------------------------20------------------
				
				INFO: Line 26 has no separating characters `|`:--17----------------------17------14---------5--2-----5--------2--8------14--19--------------16------13--
				
				INFO: Line 27 has no separating characters `|`:------------------------------------------8--------------------------------------------------------------
				
				INFO: Line 29 ignored, no line:gsdfdsg
				
				INFO: Line 30 ignored, no line:						dfgdsfgds
				
				INFO: Line 32 has no separating characters `|`:-------------------21—————————————————————\\\5–––––––––––––––––––––4——4—————————————
				
				INFO: Line 33 has no separating characters `|`:------------------------------------7--------------14---------------------------14------17------10--
				
				INFO: Line 34 has no separating characters `|`:------12————0—————————\\———————————2—————0——————————————
				
				INFO: Line 35 has no separating characters `|`:--12------18--------------------10-----10--19--------------------------------5--------------14------
				
				INFO: Line 36 has no separating characters `|`:--------------6--------------------------------19------10-----2-------------------------------------
				
				INFO: Line 37 has no separating characters `|`:------------------------21--14------------------------------------------------------18--------------
				
				INFO: Line 39 has no separating characters `|`:------------------------------------------------- -
				
				INFO: Line 40 has no separating characters `|`:-1---1-----1-----1-----1------------------------- -
				
				INFO: Line 41 has no separating characters `|`:---0-----0-----0-----0--------5-----5-----5-8---5 8
				
				INFO: Line 42 has no separating characters `|`:-2-----2-----2-----2--------5-----5-----5-----5-- -
				
				INFO: Line 43 has no separating characters `|`:-3------------------------6-----6-----6-----6---- -
				
				INFO: Line 44 has no separating characters `|`:--------------------------8---------------------- -
				
				INFO: Line 45 has no separating characters `|`:--------------------------------------------------
				
				INFO: Line 46 has no separating characters `|`:-4-----------------4----4-4---------------1-3---1-
				
				INFO: Line 47 has no separating characters `|`:-----6-----6-----6-----6------5-----5---2-----2---
				
				INFO: Line 48 has no separating characters `|`:---5-----5-----5-----5------6-----6---1-----0-----
				
				INFO: Line 49 has no separating characters `|`:-------6-----6-----6------6-----6-----------------
				
				INFO: Line 50 has no separating characters `|`:-6------------------------4-----3-----2-----------
				
				INFO: Line 53 has no separating characters `|`:------------------------------------------------- -
				
				INFO: Line 54 has no separating characters `|`:-1---1-----1-----1-----1------------------------- -
				
				INFO: Line 55 has no separating characters `|`:---0-----0-----0-----0--------5-----5-----5-8---5 8
				
				INFO: Line 56 has no separating characters `|`:------------------------------------------------- -
				
				INFO: Line 57 has no separating characters `|`:-1---1-----1-----1-----1------------------------- -
				
				INFO: Line 58 has no separating characters `|`:---0-----0-----0-----0--------5-----5-----5-8---5 8
				
				INFO: Line 61 has no separating characters `|`:-2-----2-----2-----2--------5-----5-----5-----5-- -
				
				INFO: Line 62 has no separating characters `|`:-3------------------------6-----6-----6-----6---- -
				
				INFO: Line 63 has no separating characters `|`:--------------------------8---------------------- -
				
				INFO: Line 64 has no separating characters `|`:-2-----2-----2-----2--------5-----5-----5-----5-- -
				
				INFO: Line 65 has no separating characters `|`:-3------------------------6-----6-----6-----6---- -
				
				INFO: Line 66 has no separating characters `|`:--------------------------8---------------------- -
				
				INFO: Line 67 has no separating characters `|`:--------------------------------------------------
				
				INFO: Line 68 has no separating characters `|`:-4-----------------4----4-4---------------1-3---1-
				
				INFO: Line 69 has no separating characters `|`:-----6-----6-----6-----6------5-----5---2-----2---
				
				INFO: Line 70 has no separating characters `|`:---5-----5-----5-----5------6-----6---1-----0-----
				
				INFO: Line 71 has no separating characters `|`:-------6-----6-----6------6-----6-----------------
				
				INFO: Line 72 has no separating characters `|`:-6------------------------4-----3-----2-----------
				
			* The following is from the 5th text file
				
				INFO: Line 6 ignored:	|-----1-----1-----1-----1-|-----1-----1-----1-----1-| */
		} 
		catch (FileNotFoundException e) {  }
	}
}