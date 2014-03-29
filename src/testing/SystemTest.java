package testing;

import static org.junit.Assert.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import cse2311.UI;

/**
 * @author Jason
 *
 * System test class. This class tests the main functionality of the program including the GUI.
 * It does this task by initializing the GUI to allow the tester to open and convert a tablature specifically
 * remembering rain and save it to workspace/cse2311. (i.e the project folder) with the file name
 * "SystemTest.pdf".
 *
 * Please note there is a time limit of 1 minute for the tester to open and save the PDF in the correct
 * directory with file name "SystemTest". 
 *
 * Anything done to contradict the above rules will result in the JUNIT continuing its execution and
 * failing
 */
public class SystemTest {

	/**
	 * System test case. Initializes the whole program and the GUI after which the thread sleeps for 1 minute
	 * to allow the tester to convert the tablature "remembering rain"
	 * and save it to the correct directory. When the thread resumes
	 * it checks if the file exists and checks the file size. It also
	 * checks the files creation time and asserts that it is equal to
	 * the current system time to the exact minute
	 */
	@Test
	public void test() {
		UI.createAndShowGUI();
		
		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) { }
		
		File check = new File("SystemTest.pdf");
		
		long modified = check.lastModified(); //Gets the file creation date               
        Date date = new Date(modified);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm aaa"); //Converts the long number to a readable date format
       
        String sysFileLastMod = format.format(date); //Converts the file modified date    
        String sysCurrentDate = format.format(System.currentTimeMillis()); //Converts the systems current date

		assertTrue(check.exists()); //Checks if file exists
		assertNotEquals(check.length(), 0); //checks if the file is not empty or have a size of 0
		assertEquals(sysFileLastMod, sysCurrentDate); //Checks the files creation date

	}
}