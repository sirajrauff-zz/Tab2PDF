package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;

import cse2311.Measure;
import cse2311.MusicSheet;
import cse2311.Staff;
import cse2311.Style;
import cse2311.Tablature;

public class MusicSheetTest {
	public Tablature tab;
	public Style style;
	public StringTokenizer input, input2, input3, input4, input5, input6;
	public Document document;
	public MusicSheet test;
	public PrintStream log;
	
	@Before
	public void setUp() throws Exception {
		Document document = new Document();
		//style = new Style(document); ERROR
		tab = new Tablature();
		
		input = new StringTokenizer("|s4-----------------2-----| |-----4-----4-----4-----4-| |---5-----5-----5-----5---| |-5-----5-----5-----------| |-------3-----------3-----| |-------------------------|");
		input2 = new StringTokenizer("|-----4-----4-----4-----4-| |s4-----------------2-----| |-------------------------| |-5-----5-----5-----------| |-------3-----------3-----| |---5-----5-----5-----5---|");
		input3 = new StringTokenizer("|-------------------4---7-| |s4---1-------------------| |-0---3--s6---------------| |---------------3---0-3---| |---5-------5-1-----0-----| |---5-------5-0h3---0h3---|");
		input4 = new StringTokenizer("|-----4-----4-----4-----4-| |s4-----------------2-----| |-------------------------| |-5-----5-----5-----------| |-------3-----------3-----| |---5-----5-----5-----5---|");

		tab.addMultiMeasureLine(input);
		tab.addMultiMeasureLine(input2);
		tab.addMultiMeasureLine(input3);
		
		test = new MusicSheet(tab, style);
	}
	
	@After
	public void destroy() {
		log.close();
	}
	
	@Test
	public void testMusicSheet() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetStaffs() {
		assertNotNull(test.getStaffs());

		String result = "|s4-----------------2-----|-----4-----4-----4-----4-|---5-----5-----5-----5---|-5-----5-----5-----------|-------3-----------3-----|-------------------------|";
		String result1 = "|-----4-----4-----4-----4-|s4-----------------2-----|-------------------------|-5-----5-----5-----------|-------3-----------3-----|---5-----5-----5-----5---|";
		String result2 = "|-------------------4---7-|s4---1-------------------|-0---3--s6---------------|---------------3---0-3---|---5-------5-1-----0-----|---5-------5-0h3---0h3---|";
	
		assertEquals(test.getStaffs().get(0).getLines().get(0).toString(), result);
		assertEquals(test.getStaffs().get(0).getLines().get(1).toString(), result1);
		assertEquals(test.getStaffs().get(0).getLines().get(2).toString(), result2);
	}
	
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
	
	@Test
	public void testGetSpacing() {
		assertEquals(test.getSpacing(), 5f, 0.0);
	}
	
	@Test
	public void testGetTitle() {
		assertEquals(test.getTitle(), "Default");
	}
	
	@Test
	public void testSetTitle() {
		assertEquals(test.getTitle(), "Default");
		test.setTitle("remembering rain");
		assertEquals(test.getTitle(), "remembering rain");
	}

	@Test
	public void testGetSubtitle() {
		assertEquals(test.getSubtitle(), "Default");
	}
	
	@Test
	public void testPrint() {
		try {
			log = new PrintStream(new File("testPrint.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		System.setOut(log);
		
		try {
			Scanner input = new Scanner(new File("testPrint.txt"));
			
			test.print("1243544324");
			assertEquals(input.nextLine(), "1243544324");
			
			test.print("testing");
			assertEquals(input.nextLine(), "testing");
			
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSetSubtitle() {
		 assertEquals(test.getSubtitle(), "Default");
		 test.setSubtitle("Jim Mateos");
		 assertEquals(test.getSubtitle(), "Jim Mateos");
	}
	
	@Test
	public void testPrintStaff() {
		try {
			log = new PrintStream(new File("testPrintStaff.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.setOut(log);
		test.getStaffs().get(0).printLines();
		
		String result = "|s4-----------------2-----|-----4-----4-----4-----4-|---5-----5-----5-----5---|-5-----5-----5-----------|-------3-----------3-----|-------------------------|";
		String result1 = "|-----4-----4-----4-----4-|s4-----------------2-----|-------------------------|-5-----5-----5-----------|-------3-----------3-----|---5-----5-----5-----5---|";
		String result2 = "|-------------------4---7-|s4---1-------------------|-0---3--s6---------------|---------------3---0-3---|---5-------5-1-----0-----|---5-------5-0h3---0h3---|";
		
		Scanner input;
		
		try {
			input = new Scanner (new File("testPrintStaff.txt"));
			
			assertEquals(input.nextLine(), result);
			assertEquals(input.nextLine(), result1);
			assertEquals(input.nextLine(), result2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetMyStyle() {
		assertSame(test.getMyStyle(), style);
	}
	
	@Test
	public void testSetMyStyle() {
		/*Style style2 = new Style(new Document()); ERROR
		assertSame(test.getMyStyle(), style);
		test.setMyStyle(style2);
		assertSame(test.getMyStyle(), style2);*/
	}
}
