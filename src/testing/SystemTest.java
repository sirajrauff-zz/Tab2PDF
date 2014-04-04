package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import frontEnd.UserInterface;

/**
 * @author Jason
 *
 * System test class. This class tests the main functionality of the program including the GUI.
 * It does this using a robot to automatically convert a Tablature (specifically "rememberingrain.txt")
 * to a PDF called "systemtest.pdf" in the program's directory. The file is then tested for content
 * and creation time to ensure it is the correct file.
 *
 * PLEASE NOTE
 * The text "rememberingrain.txt" MUST BE in your DEFAULT DIRECTORY for this JUnit test to pass. 
 * DO NOT press any buttons or move the mouse during the execution.
 * 
 * If this test fails on your computer, adjust computerSpeed to a lower number before executing.
 */
public class SystemTest {
	final int computerSpeed = 10;
	
	/**
	 * The only test. Creates GUI, instantiates robot and gives it commands.
	 */
	@Test
	public void test() {
		UserInterface.createAndShowGUI(); //Runs the main program
		try {
			Robot test = new Robot(); //Initializes the robot which will perform the automation
			
			// Obtains the absolute file path to this project folder and adds name of the file
			String save = System.getProperty("user.dir") + "\\SystemTest.pdf"; 
			save = save.toUpperCase(Locale.CANADA); //Only works if UpperCase
			
			test.keyPress(KeyEvent.VK_ENTER);
			test.delay(500 / computerSpeed); //Delays execution to allow the GUI time to open
			
			test.keyPress(KeyEvent.VK_R); //Types in "rememberingrain.txt" for the program to convert
			test.keyPress(KeyEvent.VK_E);
			test.keyPress(KeyEvent.VK_M);
			test.keyPress(KeyEvent.VK_E);
			test.keyPress(KeyEvent.VK_M);
			test.keyPress(KeyEvent.VK_B);
			test.keyPress(KeyEvent.VK_E);
			test.keyPress(KeyEvent.VK_R);
			test.keyPress(KeyEvent.VK_I);
			test.keyPress(KeyEvent.VK_N);
			test.keyPress(KeyEvent.VK_G);
			test.keyPress(KeyEvent.VK_R);
			test.keyPress(KeyEvent.VK_A);
			test.keyPress(KeyEvent.VK_I);
			test.keyPress(KeyEvent.VK_N);
			test.keyPress('.');
			test.keyPress(KeyEvent.VK_T);
			test.keyPress(KeyEvent.VK_X);
			test.keyPress(KeyEvent.VK_T);
			
			test.keyPress(KeyEvent.VK_ENTER); //Press enter, 'open' button already selected by default
			test.delay(40000 / computerSpeed);
			
			test.keyPress(KeyEvent.VK_TAB);
			test.delay(500 / computerSpeed);
			
			test.keyPress(KeyEvent.VK_ENTER);
			test.keyRelease(KeyEvent.VK_ENTER);
			test.delay(500 / computerSpeed);
			
			/* Types in the absolute file path obtained from above
			 * as the save destination in the save dialog box */
			for (int j = 0; j < save.length(); j++) {
				if (save.charAt(j) == ':') {
					test.keyPress(KeyEvent.VK_SHIFT);
					test.keyPress(KeyEvent.VK_SEMICOLON);
					test.keyRelease(KeyEvent.VK_SHIFT);
					test.keyRelease(KeyEvent.VK_SEMICOLON);
				}
				else if(save.charAt(j) == '\\') {
					test.keyPress(KeyEvent.VK_BACK_SLASH);
					test.keyRelease(KeyEvent.VK_BACK_SLASH);
				}
				else {
					test.keyPress(save.charAt(j));
					test.keyRelease(save.charAt(j));
				}
			}
			test.delay(500 / computerSpeed);
			
			test.keyPress(KeyEvent.VK_ENTER); //Press enter, 'save' button already selected by default
			test.delay(5000 / computerSpeed);
			
			test.keyPress(KeyEvent.ALT_MASK); //Closes the program using ALT + E
			test.keyPress(KeyEvent.VK_E); 
		} catch (AWTException e) { }
		
		File check = new File("systemtest.pdf");
		long modified = check.lastModified(); //Gets the file creation date
		
		Date date = new Date(modified);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm aaa"); //Converts the long number to a readable date format
		       
		String sysFileLastMod = format.format(date); //Converts the file modified date    
		String sysCurrentDate = format.format(System.currentTimeMillis()); //Converts the systems current date

		assertTrue(check.exists()); //Checks if the file was saved successfully
		assertNotEquals(check.length(), 0); //Checks that the file actually contains data
		assertEquals(sysFileLastMod, sysCurrentDate); //Compares creation date to current date to ensure the program created it 
		check.deleteOnExit(); //Delete on exit so the test never encounters the 'already exists' dialog
	}
}