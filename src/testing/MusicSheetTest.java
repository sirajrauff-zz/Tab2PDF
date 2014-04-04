package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.Test;

import backEnd.Measure;
import backEnd.MusicSheet;
import backEnd.Staff;
import backEnd.Style;
import backEnd.Tablature;

/**
 * @author Jason
 * Test Class for the MusicSheet class
 */
public class MusicSheetTest {
	private Tablature tab;
	private Style style;
	private StringTokenizer input, input2, input3, input4;
	private MusicSheet test;
	private PrintStream log;
	
	/**
	 * Instantiates instances of style and tab which are arguments for the musicSheet constructor. Tablature is
	 * added to the tab instance and a new instance of the music sheet class called test is instantiated.
	 */
	@Before
	public void setUp() {
		style = new Style();
		tab = new Tablature();
		
		input = new StringTokenizer("|s4-----------------2-----| |-----4-----4-----4-----4-| "
				+ "|---5-----5-----5-----5---| |-5-----5-----5-----------| "
				+ "|-------3-----------3-----| |-------------------------|");
		
		input2 = new StringTokenizer("|-----4-----4-----4-----4-| |s4-----------------2-----| "
				+ "|-------------------------| |-5-----5-----5-----------| "
				+ "|-------3-----------3-----| |---5-----5-----5-----5---|");
		
		input3 = new StringTokenizer("|-------------------4---7-| |s4---1-------------------| "
				+ "|-0---3--s6---------------| |---------------3---0-3---| "
				+ "|---5-------5-1-----0-----| |---5-------5-0h3---0h3---|");
		
		input4 = new StringTokenizer("|-----4-----4-----4-----4-| |s4-----------------2-----| "
				+ "|-------------------------| |-5-----5-----5-----------| "
				+ "|-------3-----------3-----| |---5-----5-----5-----5---|");

		tab.addMultiMeasureLine(input);
		tab.addMultiMeasureLine(input2);
		tab.addMultiMeasureLine(input3);
		tab.addMultiMeasureLine(input3);
		tab.addMultiMeasureLine(input2);
		tab.addMultiMeasureLine(input4);

		test = new MusicSheet(tab, style);
	}
	
	/**
	 * Test case for the method getStaffs() which returns an arrayList of staffs in music sheet. This method is 
	 * tested by asserting that the staffs returned by this method equal the expected values
 	 */
	@Test
	public void testGetStaffs() {
		String result = "|s4-----------------2-----|-----4-----4-----4-----4-|---5-----5-----5-----5---|"
				+ "-5-----5-----5-----------|-------3-----------3-----|-------------------------|";
		
		String result1 = "|-----4-----4-----4-----4-|s4-----------------2-----|-------------------------|"
				+ "-5-----5-----5-----------|-------3-----------3-----|---5-----5-----5-----5---|";
		
		String result2 = "|-------------------4---7-|s4---1-------------------|-0---3--s6---------------|"
				+ "---------------3---0-3---|---5-------5-1-----0-----|---5-------5-0h3---0h3---|";

		assertEquals(test.getStaffs().get(0).getLines().get(0).toString(), result);
		assertEquals(test.getStaffs().get(0).getLines().get(1).toString(), result1);
		assertEquals(test.getStaffs().get(0).getLines().get(2).toString(), result2);
	}

	/**
	 * Test class for MusicSheet(Tablature tab, Style s) which is the constructor for the class. This method is tested
	 * by asserting that the test instance of musicSheet contains the expected value.
	 */
	@Test
	public void testMusicSheet() {
		assertEquals(test.getStaffs().get(0).getLines().get(0).toString(), "|s4-----------------2-----|-----4-----4-----4-----4-|"
				+ "---5-----5-----5-----5---|-5-----5-----5-----------|"
				+ "-------3-----------3-----|-------------------------|");
	}
	
	/**
	 * Test case for the method for print(Object s) which prints the current argument.This method is test by switching the
	 * default output (console) to a file and then asserting that the contents of the file are equal to the expected
	 * value.
	 */
	@Test
	public void testPrint() {
		try {
			log = new PrintStream(new File("test files/music sheet/testPrint.txt"));
		} catch (FileNotFoundException e1) { }

		System.setOut(log);
		
		try {
			Scanner input = new Scanner(new File("test files/music sheet/testPrint.txt"));
			
			test.print("1243544324");
			assertEquals(input.nextLine(), "1243544324");
			
			test.print("testing");
			assertEquals(input.nextLine(), "testing");
			
			input.close();
		} catch (FileNotFoundException e) { }
	}
	
	/**
	 * Test case for the method for printStaff() which prints the current staff. This method is test by switching the
	 * default output (console) to a file and then asserting that the contents of the file are equal to the expected
	 * value.
	 */
	@Test
	public void testPrintStaff() {
		try {
			log = new PrintStream(new File("test files/music sheet/testPrintStaff.txt"));
		} catch (FileNotFoundException e1) { }
		
		System.setOut(log);
		test.getStaffs().get(0).printLines();
		
		String result = "|s4-----------------2-----|-----4-----4-----4-----4-|---5-----5-----5-----5---|"
				+ "-5-----5-----5-----------|-------3-----------3-----|-------------------------|";
		
		String result1 = "|-----4-----4-----4-----4-|s4-----------------2-----|-------------------------|"
				+ "-5-----5-----5-----------|-------3-----------3-----|---5-----5-----5-----5---|";
		
		String result2 = "|-------------------4---7-|s4---1-------------------|-0---3--s6---------------|"
				+ "---------------3---0-3---|---5-------5-1-----0-----|---5-------5-0h3---0h3---|";
		
		Scanner input;
		
		try {
			input = new Scanner (new File("test files/music sheet/testPrintStaff.txt"));
			
			assertEquals(input.nextLine(), result);
			assertEquals(input.nextLine(), result1);
			assertEquals(input.nextLine(), result2);
		} catch (FileNotFoundException e) { }	
	}

	/**
	 * Test case for setStaffs(ArrayList<Staff> myStaffs) which sets the staff in the music sheet instance. This
	 * method is tested by adding staffs to the music sheet and an arrayList setStaffs and the music sheet's staff 
	 * is asserted to be equal
	 */
	@Test
	public void testSetStaffs() {
		ArrayList<Staff> setstaffs = new ArrayList<Staff>(); 
		Staff staff = new Staff(5);
		Staff staff1 = new Staff(5);
		Staff staff2 = new Staff(5);
		
		Measure m = new Measure(5);
		m.addLine("||*-----<5>-----------<7>----------------------------*||");
		m.addLine("||-0-----------7-----------------------------0---------|");
		m.addLine("*--------------------------------2---------------------*");
		m.addLine("|-0--------------7--------------------------0---------||");
		m.addLine("|-----0----------10-------------0-------0-------5s7----|");
		
		staff.addToStaff(m);
		staff1.addToStaff(m);
		staff2.addToStaff(m);
		
		setstaffs.add(staff1);
		setstaffs.add(staff2);
		setstaffs.add(staff);
		
		test.setStaffs(setstaffs);
		
		assertSame(test.getStaffs(), setstaffs);
	}
}