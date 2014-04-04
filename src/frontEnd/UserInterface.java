package frontEnd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.DocumentException;

import backEnd.*;

/**
 * GUI. Allows user to choose, edit, save and print a guitar Tablature.
 * @author Umer Zahoor, Siraj Rauff, Waleed Azhar
 */
public class UserInterface extends JFrame implements ActionListener, KeyListener, MouseListener, WindowListener {
	/*---STATICS/FINALS-----------------------------------------------------------------*/
    private static final long serialVersionUID = 1L;
	private static final int defaultZoom = 100, textDelay = 800, zoomDelay = 500;
	private static JFrame frame;
	private static RecentOpen recentOpen;
    /*---GUI----------------------------------------------------------------------------*/
	private JTextField fileTitle, title, author, zoom;
	private JPanel body, sidebar, basicButtons;
	private JComboBox<String> fontType;
	private JComboBox<Integer> fontSizeTitle, fontSizeAuthor, fontSize;
	private JSlider numberSpacing, measureSpacing, lineSpacing, zoomSlide, leftMargin, rightMargin;
	private JButton open, help, save, print, reset;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem openMenu, saveMenu, printMenu, aboutMenu, helpMenu, exitMenu, resetMenu, 
	zoomInMenu, zoomOutMenu;
	private Timer textTimer, zoomTimer;
	/*---VARIABLES/CHANNELS-------------------------------------------------------------*/
	private int indexOfHelvetica;
	private  float defaultSpacing;
	private boolean opened = false, fileSaved = true, changesSaved = true;
	private String prevDir, defaultTitle, defaultSubtitle;
    /*---DATA---------------------------------------------------------------------------*/
	private Tablature userTab;
	private Style userStyle = new Style();
    /*---PREVIEW------------------------------------------------------------------------*/
	private PDFPanel preview;
	
	/**
     * Only used to instantiate the program
     * @param args UNUSED
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws DocumentException, IOException {
		createAndShowGUI();
	}
    
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Tab2PDF");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ImageIcon icon = createImageIcon("images/icon.png");
        frame.setIconImage(icon.getImage());
        
        //set look and feel to OS's look and feel, if it fails will just use the default Java look and feel
        try	{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

        try {
			recentOpen = new RecentOpen();
		} catch (IOException e1) { }
        
        //Create and set up the content pane.
        UserInterface demo = new UserInterface();
        frame.setJMenuBar(demo.createMenuBar());
        frame.add(demo.createBody(), BorderLayout.CENTER);
 
        //Display the window.
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Creates a JPanel with buttons for open, save, help and print
     * @return JPanel
     */
    public JPanel createB1() {
    	JPanel temp = new JPanel(new GridLayout(2,2,10,10));
    	temp.setOpaque(true);
    	
    	//create a jPanel with open, save, help and more buttons
    	ImageIcon icon = createImageIcon("images/open48.png");
    	open = new JButton("Open", icon);
    	open.setToolTipText("Open ASCII tablature to convert");
    	open.setMnemonic(KeyEvent.VK_O);
    	open.setActionCommand("open");
    	open.addActionListener(this);
    	
    	icon = createImageIcon("images/save48.png");
    	save = new JButton("Save", icon);
    	save.setToolTipText("Save this tablature as a PDF");
    	save.setMnemonic(KeyEvent.VK_S);
    	save.setActionCommand("save");
    	save.addActionListener(this);
    	save.setEnabled(false);
    	
    	icon = createImageIcon("images/help48.png");
    	help = new JButton("Help", icon);
    	help.setToolTipText("Click here for instructions to use this program");
    	help.setMnemonic(KeyEvent.VK_H);
    	help.setActionCommand("help");
    	help.addActionListener(this);
    	
    	icon = createImageIcon("images/print48.png");
    	print = new JButton("Print", icon);
    	print.setToolTipText("Print this tablature");
    	print.setMnemonic(KeyEvent.VK_P);
    	print.setActionCommand("print");
    	print.addActionListener(this);
    	print.setEnabled(false);
    	
    	//Add the text area to the content pane.
    	temp.add(open);
    	temp.add(save);
    	temp.add(help);
    	temp.add(print);
    	temp.setBorder(new TitledBorder(new EtchedBorder(), "Buttons"));
    	return temp;
    }
    
    /**
     * Creates the content pane to be
     * @return JPanel
     */
    public JPanel createBody() { 
        body = new JPanel(new BorderLayout());
        sidebar = new JPanel(new BorderLayout());
        basicButtons = createB1();
        
        frame.addWindowListener(this);
        
        sidebar.add(basicButtons, BorderLayout.PAGE_START);
        body.add(sidebar, BorderLayout.LINE_START);
        body.add(createS1(), BorderLayout.SOUTH);
        return body;
    }
    
    /**
     * Creates a menuBar with options to open, save, print and open help documentation
     * @return menuBar
     */
    public JMenuBar createMenuBar() {
    	ImageIcon icon;
        menuBar = new JMenuBar(); //Create the menu bar.
 
        /*---FIRST MENU-------------------------------------------------------------------*/
        menu = new JMenu("File"); 
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);
        
        icon = createImageIcon("images/open16.png");
        openMenu = new JMenuItem("Open", icon);
        openMenu.setActionCommand("open");
        openMenu.addActionListener(this);
        menu.add(openMenu);
 
        icon = createImageIcon("images/save16.png");
        saveMenu = new JMenuItem("Save", icon);
        saveMenu.setActionCommand("save");
        saveMenu.addActionListener(this);
        saveMenu.setEnabled(false);
        menu.add(saveMenu);
        
        icon = createImageIcon("images/print16.png");
        printMenu = new JMenuItem("Print", icon);
        printMenu.setActionCommand("print");
        printMenu.addActionListener(this);
        printMenu.setEnabled(false);
        menu.add(printMenu);
        
		menu.addSeparator();
		recentOpen.addActionListener(this);
	    menu.add(recentOpen.getMenu());

        menu.addSeparator();
        icon = createImageIcon("images/exit16.png");
        exitMenu = new JMenuItem("Exit", icon);
        exitMenu.setActionCommand("exit");
        exitMenu.setMnemonic(KeyEvent.VK_E);
        exitMenu.addActionListener(this);
        menu.add(exitMenu);
        
        /*---SECOND MENU(options)------------------------------------------------*/
        menu = new JMenu("Edit");
        menuBar.add(menu);
        
        icon = createImageIcon("images/reset16.png");
        resetMenu = new JMenuItem("Reset", icon);
        resetMenu.setEnabled(false);
        resetMenu.addActionListener(this);
        resetMenu.setAccelerator(KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_R, 
                java.awt.Event.CTRL_MASK));
        resetMenu.setActionCommand("reset");
        menu.add(resetMenu);
        menu.addSeparator();
        
        icon = createImageIcon("images/zoomIn16.png");
        zoomInMenu = new JMenuItem("Zoom in", icon);
        zoomInMenu.setEnabled(false);
        zoomInMenu.setActionCommand("zoom");
        zoomInMenu.addActionListener(this);
        zoomInMenu.setAccelerator(KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_EQUALS, 
                java.awt.Event.CTRL_MASK));
        menu.add(zoomInMenu);
        
        icon = createImageIcon("images/zoomOut16.png");
        zoomOutMenu = new JMenuItem("Zoom out", icon);
        zoomOutMenu.setEnabled(false);
        zoomOutMenu.setActionCommand("zoom");
        zoomOutMenu.addActionListener(this);
        zoomOutMenu.setAccelerator(KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_MINUS, 
                java.awt.Event.CTRL_MASK));
        menu.add(zoomOutMenu);
        
        menu = new JMenu("Help");
        menuBar.add(menu);
        
        /*---THIRD MENU----------------------------------------------------------*/
        icon = createImageIcon("images/help16.png");
        helpMenu = new JMenuItem("Help", icon);
        helpMenu.setActionCommand("help");
        helpMenu.addActionListener(this);
        menu.add(helpMenu);
        
        aboutMenu = new JMenuItem("About");
        aboutMenu.setActionCommand("about");
        aboutMenu.addActionListener(this);
        menu.add(aboutMenu);
        
        return menuBar;
    }
    
    /**
     * Creates the JTextField to be used to show user status of program
     * @return JPanel with JTextField
     */
    public JPanel createS1() {
    	JPanel temp = new JPanel(new GridLayout(1,1));
    	temp.setOpaque(true);
        
        fileTitle = new JTextField("Press a button to begin");
        fileTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        fileTitle.setOpaque(false);
        fileTitle.setBorder(null);
        temp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
        temp.add(fileTitle);
		return temp;
    }
 
    /**
     * Creates a JPanel with JSliders and JTextFields to modify the Tablature
     * @return JPanel
     */
    public JPanel editBox() {
	    Integer[] fontSizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48}; //Title box
		fontSizeTitle = new JComboBox<Integer>(fontSizes);
		fontSizeTitle.setSelectedIndex(10);
		fontSizeTitle.setToolTipText("Change font size of the title");
	    fontSizeTitle.addActionListener(this);
		title = new JTextField(15);
		title.setText(defaultTitle);
		title.addKeyListener(this);
		title.setToolTipText("Change title of the tablature");
		JPanel titleTemp = new JPanel();
		titleTemp.add(new JLabel("    Title: "));
		titleTemp.add(title);
		titleTemp.add(fontSizeTitle);
		
		fontSizeAuthor = new JComboBox<Integer>(fontSizes); //Author Box
		fontSizeAuthor.setSelectedIndex(6);
		fontSizeAuthor.addActionListener(this);
		fontSizeAuthor.setToolTipText("Change font size of the Author");
		author = new JTextField(15);
		author.setText(defaultSubtitle);
		author.addKeyListener(this);
		author.setToolTipText("Set Author of Tablature");
		JPanel authorTemp = new JPanel();
		authorTemp.add(new JLabel(" Author:"));
		authorTemp.add(author);
		authorTemp.add(fontSizeAuthor);
		
		String fonts[] = FontSelector.Fonts; //Font Box
		fontType = new JComboBox<String>(fonts);
		indexOfHelvetica = (Arrays.asList(fonts)).indexOf("Helvetica");
		fontType.setSelectedIndex(indexOfHelvetica);
		fontType.addActionListener(this);
		fontType.setToolTipText("Set font used for the tablature");
		fontType.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXX");    	
		
	    fontSize = new JComboBox<Integer>(fontSizes);
		fontSize.setSelectedIndex(1);
	    fontSize.addActionListener(this);
	    fontSize.setToolTipText("Set font size used in tablature");
		JPanel fontTemp = new JPanel();
		fontTemp.add(new JLabel("     Font:"));
		fontTemp.add(fontType);
	    fontTemp.add(fontSize);
		
	    fontSizeTitle.setActionCommand("font");
	    fontSizeAuthor.setActionCommand("font");
	    fontType.setActionCommand("font");
	    fontSize.setActionCommand("font");
	    
	    int change = -30;
    	JLabel spacingLabel = new JLabel("<html><u>Spacing"); //Spacing
		numberSpacing = new JSlider(1, 99, (int) (userTab.getSpacing() * 10));
		numberSpacing.setMinorTickSpacing(10);
		numberSpacing.setPaintTicks(true);
	    Dimension d = new Dimension(numberSpacing.getPreferredSize().width + change, numberSpacing.getPreferredSize().height);
	    numberSpacing.setPreferredSize(d);
		numberSpacing.addMouseListener(this);
		numberSpacing.setToolTipText("Set spacing of numbers/symbols in tablature");
		JPanel spacingBarsTemp = new JPanel();
		spacingBarsTemp.add(new JLabel("  Numbers:"));
		spacingBarsTemp.add(numberSpacing);
		
		measureSpacing = new JSlider(140, 900, 300); //Section spacing
		measureSpacing.setMinorTickSpacing(90);
		measureSpacing.setPaintTicks(true);
	    measureSpacing.setPreferredSize(d);
		measureSpacing.addMouseListener(this);
		measureSpacing.setToolTipText("Set spacing between measures in the tablature");
		JPanel spacingStaffsTemp = new JPanel();
		spacingStaffsTemp.add(new JLabel(" Measures:"));
		spacingStaffsTemp.add(measureSpacing);
		
		lineSpacing = new JSlider(20, 210, 70); //Line spacing
		lineSpacing.setMinorTickSpacing(20);
		lineSpacing.setPaintTicks(true);
	    lineSpacing.setPreferredSize(d);
		lineSpacing.addMouseListener(this);
		lineSpacing.setToolTipText("Set spacing between lines in a measure");
		JPanel spacingLinesTemp = new JPanel();
		spacingLinesTemp.add(new JLabel("        Lines:"));
		spacingLinesTemp.add(lineSpacing);
	   
	    leftMargin = new JSlider(0, 160, 36); //Left margin offset
		leftMargin.setMinorTickSpacing(16);
		leftMargin.setPaintTicks(true);
	    leftMargin.setPreferredSize(d);
		leftMargin.addMouseListener(this);
		leftMargin.setToolTipText("Set margin from the left side of the page");
	    JPanel LeftMarginStaffsTemp = new JPanel();
		LeftMarginStaffsTemp.add(new JLabel("   L Margin:"));
		LeftMarginStaffsTemp.add(leftMargin);
	    
	    rightMargin = new JSlider(0, 160, 36); //Right margin offset
		rightMargin.setMinorTickSpacing(16);
		rightMargin.setPaintTicks(true);
	    rightMargin.setPreferredSize(d);
		rightMargin.addMouseListener(this);
		rightMargin.setToolTipText("Set margin from the right side of the page");
	    JPanel RightMarginStaffsTemp = new JPanel();
		RightMarginStaffsTemp.add(new JLabel(" R Margin:"));
		RightMarginStaffsTemp.add(rightMargin);
		
		reset = new JButton("Reset"); //Reset button
	    reset.setToolTipText("Reset all values to default");
	    reset.addActionListener(this);
	    reset.setActionCommand("reset");
	    JPanel resetTemp = new JPanel();
	    resetTemp.add(reset);
	    
		JPanel temp = new JPanel(new BorderLayout());
		temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
		temp.setBorder(new TitledBorder(new EtchedBorder(), "Edit"));
		temp.add(titleTemp, temp);
		temp.add(authorTemp, temp);
		temp.add(fontTemp, temp);
		temp.add(spacingLabel, temp);
		temp.add(spacingBarsTemp, temp);
		temp.add(spacingStaffsTemp, temp);
		temp.add(spacingLinesTemp, temp);
	    temp.add(LeftMarginStaffsTemp, temp);
		temp.add(RightMarginStaffsTemp, temp);
		temp.add(resetTemp, temp);
		return temp;
	}
    
	/**
	 * Expands program to include editing options, and a live preview
	 */
	private void expandView() {
		JPanel moreOptions = new JPanel();
		moreOptions.setLayout(new BoxLayout(moreOptions, BoxLayout.Y_AXIS));
		moreOptions.add(viewBox());
		moreOptions.add(editBox());
		sidebar.add(moreOptions, BorderLayout.PAGE_END);
		 	
		preview = new PDFPanel(new File("temp.pdf"));
		body.add(preview, BorderLayout.CENTER);
		preview.refresh(defaultZoom);
		frame.setResizable(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) (screenSize.getHeight() / 1.5);
		int width = (int) (screenSize.getWidth() / 1.5);
		if (height < 685)
			height = 685;
		if (width < 1070)
			width = 1070;
		
		textTimer = new Timer(textDelay, this);
		textTimer.setActionCommand("updateValues");
		zoomTimer = new Timer(zoomDelay, this);
		zoomTimer.setActionCommand("updateZoom");
		
		frame.setVisible(false);
		frame.setPreferredSize(new Dimension(width, height));
		frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	/**
     * Creates a JPanel with a JSlider and JTextField to change zoom of the live preview
     * @return JPanel
     */
    public JPanel viewBox() {
    	JPanel temp = new JPanel();
    	temp.setBorder(new TitledBorder(new EtchedBorder(), "View"));

    	zoom = new JTextField(5);
    	zoom.setText(defaultZoom + "%");
    	zoom.setToolTipText("Set level of zoom for preview");
    	zoom.addKeyListener(this);
    	
    	Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    	labelTable.put(1, new JLabel("0%"));
    	labelTable.put(101, new JLabel("100%"));
    	labelTable.put(201, new JLabel("200%"));
    	labelTable.put(301, new JLabel("300%"));
    	labelTable.put(400, new JLabel("400%"));
    	
    	zoomSlide = new JSlider(SwingConstants.HORIZONTAL, 1, 400, defaultZoom);
    	zoomSlide.setMajorTickSpacing(100);
    	zoomSlide.setMinorTickSpacing(50);
    	zoomSlide.setPaintTicks(true);
    	zoomSlide.setLabelTable(labelTable);
    	zoomSlide.setPaintLabels(true);
    	zoomSlide.addMouseListener(this);
    	zoomSlide.setToolTipText("Set level of zoom for preview");
    	
    	temp.add(zoom);
    	temp.add(zoomSlide);
    	temp.setPreferredSize(new Dimension((int) (basicButtons.getWidth() / 1.5 - 10) , 100));

		return temp;
    }
    
	/**
	 * Creates the PDF with user entered values then updates preview.
	 * Expands program and instantiates live preview if no document was already open.
	 * Defaults on set values if no user entered values.
	 * @param zoomLevel Level of zoom used for live preview
	 */
	private void generatePDF(final int zoomLevel) {
		
		try {
   			PdfOutputCreator pdfout = new PdfOutputCreator();
   			pdfout.makePDF(userTab, userStyle);
   		} catch (IOException | DocumentException e1) {
   			System.out.println("test");
   		}
	   
		if (opened == true && preview != null) { //Expand the view, or if already expanded, refresh the preview
			preview.refresh(zoomLevel);
		}
	    else 
   			expandView();
		frame.setTitle("*" + userTab.getTitle() + ".pdf - Tab2PDF");
	}

	/**
	 * Sets variables that are changed by JTextFields. 
	 * Should only be called after a delay to avoid creating a PDF for every keystroke.
	 */
	public void grabUserValues() {
		textTimer.stop();
		
		if (userTab.getTitle() != title.getText() || userTab.getSubtitle() != author.getText()) {
			changesSaved = false;
			userTab.setTitle(title.getText());
			userTab.setSubtitle(author.getText());
		}
		
		if (zoom.getText().length() > 0) {
			try {
				int userZoom = Integer.parseInt(zoom.getText().replaceAll("%", ""));
				if (userZoom > 400)
					userZoom = 400;
				else if (userZoom < 1)
					userZoom = defaultZoom;
				zoomSlide.setValue(userZoom);
				zoom.setText(userZoom + "%");
			}
			catch (NumberFormatException nfe) {
				zoom.setText(zoomSlide.getValue() + "%");
			}
		}
		generatePDF(zoomSlide.getValue());
	}

    /**
	 * Opens a dialog box with information about the program.
	 * Provides links to source libraries that were used.
	 */
	private void openAbout() {
		URI iText, pdfRenderer, tangoIcons;
		int returnVal = JOptionPane.showOptionDialog(frame,
				"<html><center>Version: 9.1.12 \n"
				+ "Thank you for using our Tablature to PDF software. \n"
				+ "Devloped by CSE 2311 Group 3 of York University:\n"
				+ "Calvin Tran, Jason Kuffaur, Mohamed Nasar, Muhammad Shah, Siraj Rauff, Umer Zahoor and Waleed Azhar\n"
				+ "Special thanks to iText Software Corp., PDF-Renderer team, and Tango Desktop Project.",
				"About", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, createImageIcon("images/logo.png"), 
				new String[] {"iText", "PDF-Renderer", "Tango Desktop Project", "Close"}, "Close");
		try {
			iText = new URI("http://itextpdf.com/");
			pdfRenderer = new URI("https://java.net/projects/pdf-renderer/");
			tangoIcons = new URI("http://tango.freedesktop.org/");
			if (returnVal == 0)
				java.awt.Desktop.getDesktop().browse(iText);
			else if (returnVal == 1)
				java.awt.Desktop.getDesktop().browse(pdfRenderer);
			else if (returnVal == 2)
				java.awt.Desktop.getDesktop().browse(tangoIcons);
		} catch (IOException | URISyntaxException e1) { //URISyntaxException should never be thrown
			JOptionPane.showMessageDialog(frame, "Cannot open browser.");
		}
	}

	/**
	 * Opens the passed file
	 * @param userFile File to open
	 * @throws FileNotFoundException
	 */
	private void openFile(File userFile) throws FileNotFoundException {
		Parser tempParser = new Parser();
		userTab = tempParser.readFile(userFile);
		defaultTitle = userTab.getTitle();
		defaultSubtitle = userTab.getSubtitle();
		defaultSpacing = userTab.getSpacing();
		
		generatePDF(defaultZoom);
		preview.getVerticalScrollBar().setValue(0);
		fileSaved = false;
		
        if (opened)
        	reset();
        else {
        	opened = true;
			save.setEnabled(true);
			print.setEnabled(true);
			saveMenu.setEnabled(true);
			printMenu.setEnabled(true);
			resetMenu.setEnabled(true);
			zoomInMenu.setEnabled(true);
			zoomOutMenu.setEnabled(true);
        }

        try {
			recentOpen.add(userFile);
		} catch (IOException e) { }

		fileTitle.setText("Opened " + userFile.getName());
	}

	/**
	 * Attempts to open up help document. Will attempt to open up the PDF first.
	 * Should opening the PDF fail, program falls back on opening the DOC.
	 * Should this fail, it will prompt the user to download a PDF/DOC viewer.
	 */
	private void openHelp() {
		URI readerViewer, docViewer;
		if (Desktop.isDesktopSupported()) {
			try {
				File helpFile = new File("help/User_Manual.pdf");
				Desktop.getDesktop().open(helpFile);
			} catch (IOException e1) {
				try {
					File helpFile = new File("help/User_Manual.doc");
					Desktop.getDesktop().open(helpFile);
				} catch (IOException e2) {
					int returnVal = JOptionPane.showOptionDialog(frame, "Cannot open User Manual!\n"
							+ "Please install a PDF reader or Microsoft DOC viewer.",
							"Failed to open Help", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
							new String[]{"Download .pdf viewer", "Download .doc viewer", "Close"}, "close");
					try {
						readerViewer = new URI("http://get.adobe.com/reader/");
						docViewer = new URI("http://www.microsoft.com/en-ca/download/details.aspx?id=4");
						if (returnVal == 0)
							java.awt.Desktop.getDesktop().browse(readerViewer);
						else if (returnVal == 1)
							java.awt.Desktop.getDesktop().browse(docViewer);
					} catch (IOException | URISyntaxException e3) { //URISyntaxException should never be thrown
						JOptionPane.showMessageDialog(frame, "Cannot open browser");
					}
				}
			}
		}
		else
			JOptionPane.showMessageDialog(frame, "Desktop not supported on " + System.getProperty("os.name") +
					".\nContact OS manufacturer for support.");
	}

	/**
	 * Resets all values to default.
	 * Title, author and spacing are set to whatever was read in by the Tablature.
	 */
	private void reset() {
		/*-Reset Variables-*/
		userStyle.setFontSize(8);
		userStyle.setMyTitleSize(24);
		userStyle.setMySubTitleSize(16);
		userStyle.setLineDistance(7f);
		userStyle.setMeasureDistance(30f);
		userStyle.setLeftMargin(36f);
		userStyle.setRightMargin(36f);
		
		/*-Reset GUI------*/
		fontSizeTitle.setSelectedIndex(10);
		fontSizeAuthor.setSelectedIndex(6);
		title.setText(defaultTitle);
		author.setText(defaultSubtitle);
		fontType.setSelectedIndex(indexOfHelvetica);
		fontSize.setSelectedIndex(1);
		numberSpacing.setValue((int) (defaultSpacing * 10));
		measureSpacing.setValue(300);
		lineSpacing.setValue(70);
		leftMargin.setValue(36);
		rightMargin.setValue(36);
		zoomSlide.setValue(defaultZoom);
		zoom.setText("100%");

		generatePDF(defaultZoom);
		fileTitle.setText("Values reset to default.");
	}
	
	/**
	 * Prompts the user for a save location and file name.
	 * Moves temp.pdf to the new location and renames it (if needed).
	 */
	private void saveFile() {
		File output;
		JFileChooser saveFile = new JFileChooser();
		output = new File(userTab.getTitle() + ".pdf");
		saveFile.setAcceptAllFileFilterUsed(false);
		saveFile.setFileFilter(new FileNameExtensionFilter("PDF documents", "pdf"));
		saveFile.setCurrentDirectory(new File(prevDir));
		saveFile.setSelectedFile(output);
		int returnVal = saveFile.showSaveDialog(this);
		fileTitle.setText("Saving...");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File destFile = saveFile.getSelectedFile();
			if (!destFile.getAbsolutePath().endsWith(".pdf"))
				destFile = new File(destFile.getAbsolutePath().concat(".pdf"));
			
			FileChannel source, destination;
			
			boolean allow = true;
			if (destFile.exists())
				allow = JOptionPane.showConfirmDialog(null, "File already exists, overwrite?") == JOptionPane.OK_OPTION;
			
			if (allow) {	            		
				source = destination = null;
		    	try {
		            source = new FileInputStream(new File("temp.pdf")).getChannel();
		            destination = new FileOutputStream(destFile).getChannel();
		            destination.transferFrom(source, 0, source.size());
		            fileSaved = true;
		            changesSaved = true;
					fileTitle.setText("Saved " + destFile.getName() + " to " + destFile.getParent());
					frame.setTitle(userTab.getTitle() + ".pdf - Tab2PDF");
		    	} catch(Exception ex) { 
		    		JOptionPane.showMessageDialog(frame, "File in use! Cannot save.");
					fileTitle.setText("Save failed.");
		    	} finally {
		            if (source != null) {
		                try {
							source.close();
						} catch (IOException e1) { }
		            }
		            if (destination != null) {
		            	try {
		                	destination.close();
		            	} catch (IOException e1) { }
		            }
		        }
		    } else
				fileTitle.setText("Save cancelled");	
		} else
			fileTitle.setText("Save cancelled");
	}
	
	/**
	 * Attempts to save file before exit
	 */
	private void saveOnExit(){
		if (!fileSaved && opened) {
			int temp = JOptionPane.showOptionDialog(frame, "File has not been saved. Would you like to save " 
			+ userTab.getTitle() + ".pdf ?",  "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Don't Save", "Cancel"}, "Cancel");
			if (temp == JOptionPane.YES_OPTION) {
				saveFile();
				frame.dispose();
				System.exit(0);
			}
			else if (temp == JOptionPane.NO_OPTION) {
				frame.dispose();
				System.exit(0);
			}
		} else if (!changesSaved && opened) {
			int temp = JOptionPane.showOptionDialog(frame, "Changes have not been saved. Would you like to save " 
			+ userTab.getTitle() + ".pdf ?",  "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Don't Save", "Cancel"}, "Cancel");
			if (temp == JOptionPane.YES_OPTION) {
				saveFile();
				frame.dispose();
				System.exit(0);
			}
			else if (temp == JOptionPane.NO_OPTION) {
				frame.dispose();
				System.exit(0);
			}
		} else {
			frame.dispose();
			System.exit(0);
		}
	}
	
	/**
	 * Select a TXT File to open
	 */
	private void selectFile() {
		fileTitle.setText("Opening...");
		File txtFile;
		JFileChooser openFile = new JFileChooser();
		if (prevDir != null)
			openFile.setCurrentDirectory(new File(prevDir));
		openFile.setFileFilter(new FileNameExtensionFilter("Text documents", "txt"));
		int returnVal = openFile.showOpenDialog(this);
		txtFile = openFile.getSelectedFile();
		if (txtFile != null)
			prevDir = txtFile.getAbsolutePath();
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				openFile(txtFile);
			} catch (IOException e1) { 
				JOptionPane.showMessageDialog(frame, "Cannot open file!");
			}
		}
		else
			fileTitle.setText("Open cancelled.");
	}
    
    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "open") {
			if (opened && !fileSaved) {
				int temp = JOptionPane.showOptionDialog(frame, "File has not been saved. Would you like to save " 
					+ userTab.getTitle() + ".pdf ?",  "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Don't Save", "Cancel"}, "Don't Save");
				if (temp == JOptionPane.YES_OPTION) {
					saveFile();
					selectFile();
				} else if (temp == JOptionPane.NO_OPTION)
					selectFile();
			} else if (opened && !changesSaved) {
				int temp = JOptionPane.showOptionDialog(frame, "Changes not been saved. Would you like to save " 
						+ userTab.getTitle() + ".pdf ?",  "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Don't Save", "Cancel"}, "Don't Save");
					if (temp == JOptionPane.YES_OPTION) {
						saveFile();
						selectFile();
					} else if (temp == JOptionPane.NO_OPTION)
						selectFile();
			}
			else
				selectFile();
		}

		else if (e.getActionCommand() == "save")
			saveFile();
		
		else if (e.getActionCommand() == "help")
			openHelp();
		
		else if (e.getActionCommand() == "exit")
			saveOnExit();
		
		else if (e.getActionCommand() == "about")
			openAbout();
		
		else if (e.getActionCommand() == "print") {
			fileTitle.setText("Printing...");			
			try {
				preview.print();
				fileTitle.setText("Printed " + userTab.getTitle() + ".pdf");
			} catch (PrinterException e1) {
				JOptionPane.showMessageDialog(frame, "Failed to print " + userTab.getTitle() + ".pdf");
			}
		}
		
		else if (e.getActionCommand() == "font") {
			userStyle.myFontface = FontSelector.getFont(fontType.getSelectedIndex());
            userStyle.setFontSize(Integer.parseInt(fontSize.getSelectedItem().toString()));
            userStyle.setMyTitleSize(Integer.parseInt(fontSizeTitle.getSelectedItem().toString()));
            userStyle.setMySubTitleSize(Integer.parseInt(fontSizeAuthor.getSelectedItem().toString()));
            generatePDF(zoomSlide.getValue());
    		changesSaved = false;
		}
		
		else if (e.getActionCommand().startsWith("update")) {
			if (e.getActionCommand() == "updateValues") {
				fileTitle.setText("Updating title/author");
	            grabUserValues();
			}
			else
	            preview.refresh(zoomSlide.getValue());
		}
		
		else if (e.getActionCommand() == "reset") {
			userTab.setTitle(defaultTitle); //Reset tab
			userTab.setSubtitle(defaultSubtitle);
			userTab.setSpacing(defaultSpacing);
			reset();
			changesSaved = true;
		}
	
		else if (e.getActionCommand().equals("zoom")){
			if (e.getSource().equals(zoomInMenu)) 
				zoomSlide.setValue(zoomSlide.getValue() + 25);
			else if (e.getSource().equals(zoomOutMenu))
				zoomSlide.setValue(zoomSlide.getValue() - 25);
			zoom.setText(zoomSlide.getValue() + "%");
			
			if (zoomTimer.isRunning()) {
				if(zoomTimer.getInitialDelay() > 0)
					zoomTimer.restart();
			} else 
				zoomTimer.start();
		}
		
		else if (e.getActionCommand().startsWith("recent")) {
			int recentItem = Integer.parseInt(e.getActionCommand().replace("recent", ""));
			if (recentItem != 0) {
				try {
					if (opened && !fileSaved) {
						int temp = JOptionPane.showOptionDialog(frame, "File has not been saved. Would you like to save " 
							+ userTab.getTitle() + ".pdf ?",  "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Don't Save", "Cancel"}, "Don't Save");
						if (temp == JOptionPane.YES_OPTION) {
							saveFile();
							openFile(new File(recentOpen.get(recentItem)));
						} else if (temp == JOptionPane.NO_OPTION)
							openFile(new File(recentOpen.get(recentItem)));
					} else if (opened && !changesSaved) {
						int temp = JOptionPane.showOptionDialog(frame, "Changes not been saved. Would you like to save " 
								+ userTab.getTitle() + ".pdf ?",  "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Don't Save", "Cancel"}, "Don't Save");
							if (temp == JOptionPane.YES_OPTION) {
								saveFile();
								openFile(new File(recentOpen.get(recentItem)));
							} else if (temp == JOptionPane.NO_OPTION)
								openFile(new File(recentOpen.get(recentItem)));
					} else
						openFile(new File(recentOpen.get(recentItem)));
				} catch (FileNotFoundException e1) { }
			}
			else {
				reset();
				fileTitle.setText("Opened " + userTab.getTitle() + ".txt");
			}
		}
	}
    
	/** 
     * Returns an ImageIcon, or null if the path was invalid. 
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = UserInterface.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }
    
    @Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			grabUserValues();
			textTimer.stop();
		} else if (textTimer.isRunning()) {
			if(textTimer.getInitialDelay() > 0)
				textTimer.restart();
		} else 
			textTimer.start();
	}
	
	@Override
	public void keyTyped(KeyEvent e) { }
	
	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(zoomSlide)) {
			zoom.setText(zoomSlide.getValue() + "%");
	        preview.refresh(zoomSlide.getValue());
			changesSaved = false;
		}
		
		else if (e.getSource().equals(numberSpacing)) {
			userTab.setSpacing(numberSpacing.getValue() / 10);
			generatePDF(zoomSlide.getValue());
			fileTitle.setText("Updated spacing");
			changesSaved = false;
		}
		
		else if (e.getSource().equals(measureSpacing)) {
			userStyle.setMeasureDistance(measureSpacing.getValue() / 10f);
			generatePDF(zoomSlide.getValue());
			fileTitle.setText("Updated measure spacing");
			changesSaved = false;
		}
		
		else if (e.getSource().equals(lineSpacing)) {
			userStyle.setLineDistance(lineSpacing.getValue() / 10);
			generatePDF(zoomSlide.getValue());
			fileTitle.setText("Updated line spacing");
			changesSaved = false;
		}
		
		else if (e.getSource().equals(leftMargin)) {
			userStyle.setLeftMargin(leftMargin.getValue());
			generatePDF(zoomSlide.getValue());
			fileTitle.setText("Left Margin adjusted");
			changesSaved = false;
        }
		
		else if (e.getSource().equals(rightMargin)) {
			userStyle.setRightMargin(rightMargin.getValue());
			generatePDF(zoomSlide.getValue());
			fileTitle.setText("Right Margin adjusted");
			changesSaved = false;
		}
	}
    
    @Override
	public void windowActivated(WindowEvent arg0) { }
	
	@Override
	public void windowClosed(WindowEvent arg0) { }
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		saveOnExit();
	}
	
	@Override
	public void windowDeactivated(WindowEvent arg0) { }
	
	@Override
	public void windowDeiconified(WindowEvent arg0) { }
	
	@Override
	public void windowIconified(WindowEvent arg0) { }
	
	@Override
	public void windowOpened(WindowEvent arg0) { }
}