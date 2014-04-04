package backEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	String measureSeparators = "|";
	String logPath;
	FileHandler fh;
	Logger logger;
	Boolean logEmpty = true;
    
	public Parser() { }

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
    	long currentTime = System.currentTimeMillis();
        logger = Logger.getLogger("MyLog");
        try { // This block configure the logger with handler and formatter  
            logger.addHandler(fh = new FileHandler("logs/MyLogFile " + currentTime + ".log"));
            fh.setFormatter(new SimpleFormatter());
        } catch (SecurityException | IOException e) { }
        
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
			}  
                            
            char r = 92;
			nextLine= nextLine.replace(r, '-');
			r = ']';
			nextLine= nextLine.replace(r, ')');
			r = '[';
			nextLine= nextLine.replace(r, '(');
                            
            if (nextLine.matches(correctLine)) {
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
		}
		
		s.close();
		logger.removeHandler(fh);
		fh.close();
		if (logEmpty)
            new File ("logs/MyLogFile " + currentTime + ".log").delete();
		return returnTab;
	}

	/**
	 * Scans the given line to check if it contains the title, subtitle or spacing
	 * @param returnTab Tablature to add title/subtitle/spacing to
	 * @param nextLine Line to be scanned
	 * @return Returns true if it finds a title/subtitle/spacing
	 */
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
