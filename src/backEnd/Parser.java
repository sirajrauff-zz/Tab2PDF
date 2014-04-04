package backEnd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	String titleRegex = "[Tt][Ii][Tt][Ll][Ee]=[A-Za-z\\s0-9]+";
	String subtitleRegex = "[Ss][Uu][Bb][Tt][Ii][Tt][Ll][Ee]=[A-Za-z\\s0-9]+";
	String spacingRegex = "[Ss][Pp][Aa][Cc][Ii][Nn][Gg]=[\\s0-9.]+";
	String acceptedSymbols = "[\\x5c\\|\\-\\s,*\\+<>0-9^\\(\\)hp=gSs%ex/]+";
	String correctLine = "^(\\||\\-|[0-9])("+acceptedSymbols+"(\\s?)+"+")(\\||\\-|[0-9])" ;
	String measureSeparators = "[\\|]";
	String logPath;
	FileHandler fh;
	File temp;
	Logger logger;
	Boolean logEmpty = true;
    
	public Parser() { 
        logger = Logger.getLogger("MyLog");
        try { // This block configure the logger with handler and formatter  
        	logPath = "logs/MyLogFile " + System.currentTimeMillis() + ".log";
            fh = new FileHandler("logs/temp.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        	temp = new File ("logs/temp.log");
			temp.deleteOnExit();
        } catch (SecurityException | IOException e) { }
	}

	private String subsituteSymbols(String sections) {
		char repeatNum = '=';
		String out = sections;
		Matcher m = Pattern.compile("\\|[0-9]").matcher(sections);

		while (m.find())
			repeatNum = m.group().toString().charAt(1);
		
		out = out.replaceAll("[eBGDAE]", "|");
		out = out.replaceAll("\\|\\|\\|", "T");
		out = out.replaceAll("\\|[0-9]", "+" + repeatNum + "|");
		out = out.replaceAll("<", "-");
		out = out.replaceAll("\\s", ",");
		
		return out;
	}
	

	public Tablature readFile(File file) throws FileNotFoundException {
		Scanner s = new Scanner(file);
		Tablature returnTab = new Tablature();
        int i = 0;
        
		while (s.hasNext()) {
            i++;
			String nextLine = s.nextLine();

			if (nextLine.isEmpty())
				continue;
			else if (readHeader(returnTab, nextLine))
				continue;
			else if (nextLine.matches("\\t") || nextLine.matches("\\s+")) {
				logger.info("Line " + i + " ignored, no content:" + nextLine);
				logEmpty = false;
				continue;
			} else if (nextLine.length() < 4) {
				logger.info("Line " + i + " too short:" + nextLine);
				logEmpty = false;
				continue;
			} else if (nextLine.indexOf('-') == -1) {
				logger.info("Line " + i + " ignored, no line:" + nextLine);
				logEmpty = false;
				continue;
			} else if (nextLine.indexOf('|') == -1) {
				logger.info("Line " + i + " has no seperating characters `|`:" + nextLine);
				logEmpty = false;
				continue;
			} else if (nextLine.matches(correctLine)) {
				nextLine = subsituteSymbols(nextLine);
				StringTokenizer StrTkn = new StringTokenizer(nextLine, measureSeparators);
				if (StrTkn.countTokens() > 1)
					returnTab.addMultiMeasureLine(StrTkn);
				else if ((StrTkn.countTokens() > 0) && (StrTkn.countTokens() < 2))
					returnTab.addLineToLastMeasure(StrTkn.nextToken());
			} else {
				logger.info("Line " + i + " ignored:" + nextLine);
				logEmpty = false;
			}
			
			if (nextLine.split(correctLine).length > 0)
				nextLine = nextLine.substring(0, nextLine.lastIndexOf('|') + 1);
			
			char r = 92;
			nextLine= nextLine.replace(r, '-');
			r = ']';
			nextLine= nextLine.replace(r, ')');
			r = '[';
			nextLine= nextLine.replace(r, '(');
		}
		s.close();
		if (!logEmpty) {
            createLog();
		}
		return returnTab;
	}

	/**
	 * Copy over the temp Log to one named after program start time
	 */
	private void createLog() {
		try {
			FileInputStream tempIn = new FileInputStream(temp);
			FileOutputStream tempOut = new FileOutputStream(new File(logPath));
			FileChannel source = tempIn.getChannel();
			FileChannel destination = tempOut.getChannel();
			destination.transferFrom(source, 0, source.size());
			tempIn.close();
			tempOut.close();
			source.close();
			destination.close();
		} catch (IOException e) { }
	}

	private boolean readHeader(Tablature returnTab, String nextLine) {
		if (nextLine.matches(titleRegex)) {
			returnTab.setTitle(nextLine.substring(nextLine.indexOf("=") + 1));
			return true;
		}
		if (nextLine.matches(subtitleRegex)) {
			returnTab.setSubtitle(nextLine.substring(nextLine.indexOf("=") + 1));
			return true;
		}
		if (nextLine.matches(spacingRegex)) {
			returnTab.setSpacing(Float.valueOf(nextLine.replaceAll("[A-Za-z=]+", "")) % 10);
			return true;
		}
		return false;
	}
}
