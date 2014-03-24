package cse2311;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.DocumentException;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPrintPage;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GUI. Allows user to choose, edit, save and print a guitar tablature.
 * @author Umer Zahoor, Siraj Rauff, Waleed Azhar
 */
public class UI extends JFrame implements ActionListener, KeyListener, MouseListener, FocusListener {
    private static final long serialVersionUID = 1L;
	private static final int defaultZoom = 100;
	final int defWidth = 180, defHeight = 30;
    static JFrame frame;
    
    JTextField fileTitle, title, author, zoom;
    JPanel body, sidebar, a1, b1, livePreview, livePreview2;
	JScrollPane a2;
    JComboBox<String> fontType;
    JComboBox<Integer> fontSizeTitle, fontSizeAuthor, fontSize;
    JSlider numberSpacing, measureSpacing, lineSpacing, zoomSlide, leftMargin, rightMargin;
    JButton open, help, save, print, reset;
    JMenuItem openMenu, saveMenu, optionsMenu, printMenu, aboutMenu, helpMenu;
    
    int view = 0, indexOfHelvetica;
    double width, height;
    float defaultSpacing = 5;
    boolean opened = false, allow;
    String filePath, prevDir, defaultTitle, defaultSubtitle;
    File txtFile, output;
    Tablature t;
    Parser c = new Parser();
    Style s = new Style();
    
	PDFPage page;
    PDFFile pdfFile;
    ArrayList<Image> image = new ArrayList<Image>();
    ArrayList<JLabel> pageLabel = new ArrayList<JLabel>();
	
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //set look and feel to os's look and feel
        try	{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }
        
        //Create and set up the content pane.
        UI demo = new UI();
        frame.setJMenuBar(demo.createMenuBar());
        frame.add(demo.createBody(), BorderLayout.CENTER);
 
        //Display the window.
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    /**
     * Creates a menuBar with options to open, save, print and open Helpdoc
     * @return menuBar
     */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
 
        //Create the menu bar.
        menuBar = new JMenuBar();
 
        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);
 
        //a group of JMenuItems
        ImageIcon icon = createImageIcon("images/open16.png");
        openMenu = new JMenuItem("Open", icon);
        openMenu.addActionListener(this);
        menu.add(openMenu);
 
        icon = createImageIcon("images/save16.png");
        saveMenu = new JMenuItem("Save", icon);
        saveMenu.addActionListener(this);
        saveMenu.setEnabled(false);
        menu.add(saveMenu);
        
        icon = createImageIcon("images/print16.png");
        printMenu = new JMenuItem("Print", icon);
        printMenu.addActionListener(this);
        printMenu.setEnabled(false);
        menu.add(printMenu);
        
        //Build second menu for options
        menu = new JMenu("Options");
        menuBar.add(menu);
        
        icon = createImageIcon("images/options16.png");
        optionsMenu = new JMenuItem("Preferences", icon);
        optionsMenu.addActionListener(this);
        menu.add(optionsMenu);
        
        //Build a third menu for Help
        menu = new JMenu("Help");
        menuBar.add(menu);
        
        icon = createImageIcon("images/help16.png");
        helpMenu = new JMenuItem("Help", icon);
        helpMenu.addActionListener(this);
        menu.add(helpMenu);
        
        aboutMenu = new JMenuItem("About");
        aboutMenu.addActionListener(this);
        menu.add(aboutMenu);
        
        return menuBar;
    }
    
    /**
     * Creates initial body to hold core buttons and JTextField
     * @return JPanel
     */
    public JPanel createBody() { //Create the content-pane-to-be.
        body = new JPanel(new BorderLayout());
        sidebar = new JPanel(new BorderLayout());
        b1 = createB1();
        
        sidebar.add(b1, BorderLayout.PAGE_START);
        body.add(sidebar, BorderLayout.LINE_START);
        body.add(createS1(), BorderLayout.SOUTH);
        return body;
    }
    
    /**
     * Creates a JPanel with buttons for open, save, help and print
     * @return JPanel
     */
    public JPanel createB1() {
    	JPanel temp = new JPanel(new GridLayout(2,2,10,10));
    	temp.setOpaque(true);
    	
    	//create a jpanel with open, save, help and more buttons
    	ImageIcon icon = createImageIcon("images/open48.png");
    	open = new JButton("Open", icon);
    	open.setToolTipText("Open ASCII tablature to convert");
    	open.addActionListener(this);
    	
    	icon = createImageIcon("images/save48.png");
    	save = new JButton("Save", icon);
    	save.setToolTipText("Save this tablature as a PDF");
    	save.addActionListener(this);
    	save.setEnabled(false);
    	
    	icon = createImageIcon("images/help48.png");
    	help = new JButton("Help", icon);
    	help.setToolTipText("Click here for instructions to use this program");
    	help.addActionListener(this);
    	
    	icon = createImageIcon("images/print48.png");
    	print = new JButton("Print", icon);
    	print.setToolTipText("Print this tablature");
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
    	author.addKeyListener(this);
    	author.setToolTipText("Set Author of Tablature");
    	JPanel authorTemp = new JPanel();
    	authorTemp.add(new JLabel("Author: "));
    	authorTemp.add(author);
    	authorTemp.add(fontSizeAuthor);
    	
    	String fonts[] = FontSelector.Fonts; //Font Box
    	fontType = new JComboBox<String>(fonts);
    	indexOfHelvetica = (Arrays.asList(fonts)).indexOf("Helvetica");
    	fontType.setSelectedIndex(indexOfHelvetica);
    	fontType.addActionListener(this);
    	fontType.setToolTipText("Set font used for the tablature");
        fontSize = new JComboBox<Integer>(fontSizes);
    	fontSize.setSelectedIndex(1);
        fontSize.addActionListener(this);
        fontSize.setToolTipText("Set font size used in tablature");
    	JPanel fontTemp = new JPanel();
    	fontTemp.add(new JLabel("    Font: "));
    	fontTemp.add(fontType);
        fontTemp.add(fontSize);
    	
    	JLabel spacingLabel = new JLabel("<html><u>Spacing"); //Spacing
    	numberSpacing = new JSlider(1, 99, (int) (t.getSpacing() * 10));
    	numberSpacing.setMinorTickSpacing(10);
    	numberSpacing.setPaintTicks(true);
    	numberSpacing.setPreferredSize(new Dimension(defWidth, defHeight));
    	numberSpacing.addMouseListener(this);
    	numberSpacing.setToolTipText("Set spacing of numbers/symbols in tablature");
    	JPanel spacingBarsTemp = new JPanel();
    	spacingBarsTemp.add(new JLabel("   Numbers:"));
    	spacingBarsTemp.add(numberSpacing);
    	
    	measureSpacing = new JSlider(140, 900, 300); //Section spacing
    	measureSpacing.setMinorTickSpacing(90);
    	measureSpacing.setPaintTicks(true);
    	measureSpacing.setPreferredSize(new Dimension(defWidth, defHeight));
    	measureSpacing.addMouseListener(this);
    	measureSpacing.setToolTipText("Set spacing between measures in the tablature");
    	JPanel spacingStaffsTemp = new JPanel();
    	spacingStaffsTemp.add(new JLabel("  Measures:"));
    	spacingStaffsTemp.add(measureSpacing);
    	
    	lineSpacing = new JSlider(20, 210, 70); //Line spacing
    	lineSpacing.setMinorTickSpacing(20);
    	lineSpacing.setPaintTicks(true);
    	lineSpacing.setPreferredSize(new Dimension(defWidth, defHeight));
    	lineSpacing.addMouseListener(this);
    	lineSpacing.setToolTipText("Set spacing between lines in a measure");
    	JPanel spacingLinesTemp = new JPanel();
    	spacingLinesTemp.add(new JLabel("     Lines:"));
    	spacingLinesTemp.add(lineSpacing);
       
        leftMargin = new JSlider(0, 160, 36); //Left margin offset
    	leftMargin.setMinorTickSpacing(16);
    	leftMargin.setPaintTicks(true);
    	leftMargin.setPreferredSize(new Dimension(defWidth, defHeight));
    	leftMargin.addMouseListener(this);
    	leftMargin.setToolTipText("Set margin from the left side of the page");
        JPanel LeftMarginStaffsTemp = new JPanel();
    	LeftMarginStaffsTemp.add(new JLabel("  Left Margin:"));
    	LeftMarginStaffsTemp.add(leftMargin);
        
        rightMargin = new JSlider(0, 160, 36); //Right margin offset
    	rightMargin.setMinorTickSpacing(16);
    	rightMargin.setPaintTicks(true);
    	rightMargin.setPreferredSize(new Dimension(defWidth, defHeight));
    	rightMargin.addMouseListener(this);
    	rightMargin.setToolTipText("Set margin from the right side of the page");
        JPanel RightMarginStaffsTemp = new JPanel();
    	RightMarginStaffsTemp.add(new JLabel("  Right Margin:"));
    	RightMarginStaffsTemp.add(rightMargin);
    	
		reset = new JButton("Reset"); //Reset button
	    reset.setToolTipText("Reset all values to default");
	    reset.addActionListener(this);

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
    	temp.add(reset);
    	return temp;
    }
    
    /**
     * Creates a JPanel with a JSlider and JTextField to change zoom of the liveprevew
     * @return JPanel
     */
    public JPanel viewBox() {
    	JPanel temp = new JPanel();
    	temp.setBorder(new TitledBorder(new EtchedBorder(), "View"));

    	zoom = new JTextField(5);
    	zoom.setText(defaultZoom + "%");
    	zoom.setToolTipText("Set level of zoom for preview");
    	zoom.addKeyListener(this);
    	zoom.addFocusListener(this);
    	
    	Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    	labelTable.put(1, new JLabel("0%"));
    	labelTable.put(101, new JLabel("100%"));
    	labelTable.put(201, new JLabel("200%"));
    	labelTable.put(301, new JLabel("300%"));
    	labelTable.put(400, new JLabel("400%"));
    	
    	zoomSlide = new JSlider(JSlider.HORIZONTAL, 1, 400, defaultZoom);
    	zoomSlide.setMajorTickSpacing(100);
    	zoomSlide.setMinorTickSpacing(50);
    	zoomSlide.setPaintTicks(true);
    	zoomSlide.setLabelTable(labelTable);
    	zoomSlide.setPaintLabels(true);
    	zoomSlide.addMouseListener(this);
    	zoomSlide.setToolTipText("Set level of zoom for preview");
    	
    	temp.add(zoom);
    	temp.add(zoomSlide);
    	temp.setPreferredSize(new Dimension((int) ((int) b1.getWidth() / 1.5 - 10) , 100));

		return temp;
    }
    
    public JScrollPane createA2() throws IOException {
    	
    	pageLabel.add(new JLabel());
    	if (image.size() > 0) {
    		for (int i = 0; i < image.size(); i++)
    			pageLabel.get(i).setIcon(new ImageIcon(image.get(i)));
    	}
    	
    	livePreview = new JPanel();
    	livePreview.setLayout(new BoxLayout(livePreview, BoxLayout.Y_AXIS));
    	
    	for (int i = 0; i < pageLabel.size(); i++)
    		livePreview.add(pageLabel.get(i));
    	
    	livePreview2 = new JPanel(new GridBagLayout());
    	livePreview2.add(livePreview);
		return new JScrollPane (livePreview2);
    }
    
    /**
     * Creates the JTextField to be used to show user status of program
     * @return JPanel with JTextField
     */
    public JPanel createS1() {
    	JPanel temp = new JPanel(new GridLayout(1,1));
    	temp.setOpaque(true);
        
        fileTitle = new JTextField("Press a button to begin");
        fileTitle.setHorizontalAlignment(JTextField.RIGHT);
        fileTitle.setOpaque(false);
        fileTitle.setBorder(null);
        temp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
        temp.add(fileTitle);
		return temp;
    }
 
    /** 
     * Returns an ImageIcon, or null if the path was invalid. 
     * */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = UI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
			Logger.getLogger(UI.class.getName()).log(Level.WARNING, "Image " + path + " not found");
            return null;
        }
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(open) || e.getSource().equals(openMenu)) {
			fileTitle.setText("Opening...");
			JFileChooser openFile = new JFileChooser();
			if (prevDir != null)
				openFile.setCurrentDirectory(new File(prevDir));
			openFile.setFileFilter(new FileNameExtensionFilter("Text documents", "txt"));	
			openFile.setAcceptAllFileFilterUsed(false);
			int returnVal = openFile.showOpenDialog(this);
			txtFile = openFile.getSelectedFile();
			if (txtFile != null)
				prevDir = txtFile.getAbsolutePath();
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					t = c.readFile(txtFile);
			        defaultTitle = t.getTitle();
			        defaultSubtitle = t.getSubtitle();
			        defaultSpacing = t.getSpacing();
					generatePDF(defaultZoom);
					
			        save.setEnabled(true);
			        print.setEnabled(true);
			        saveMenu.setEnabled(true);
			        printMenu.setEnabled(true);
			        fileTitle.setText("Opened " + t.getTitle() + ".txt");
					frame.setTitle("Tab2PDF - " + t.getTitle() + ".pdf");
				} catch (IOException e1) { 
            		JOptionPane.showMessageDialog(frame, "Cannot open file!");
            		Logger.getLogger(UI.class.getName()).log(Level.SEVERE, 
							"Could not open " + prevDir, e1);
        		}

				title.setText(defaultTitle);
				author.setText(defaultSubtitle);
				measureSpacing.setValue(300);
				lineSpacing.setValue(70);
			}
			else
				fileTitle.setText("Open cancelled.");
		}
		
		else if (e.getSource().equals(save) || e.getSource().equals(saveMenu)) {
			JFileChooser saveFile = new JFileChooser();
			output = new File(t.getTitle() + ".pdf");
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
            		
            	allow = true;
            	if (destFile.exists())
            		allow = JOptionPane.showConfirmDialog(null, "File already exists, overwrite?") == JOptionPane.OK_OPTION;
    			
            	if (allow) {	            		
            		FileChannel source = null;
 	                FileChannel destination = null;
                	try {
	                    source = new FileInputStream(new File("temp.pdf")).getChannel();
	                    destination = new FileOutputStream(destFile).getChannel();
	                    destination.transferFrom(source, 0, source.size());
	        			fileTitle.setText("Saved " + t.getTitle() + ".pdf to " + destFile.getParent());
                	} catch(Exception ex) { 
                		JOptionPane.showMessageDialog(frame, "File in use! Cannot save.");
	        			Logger.getLogger(UI.class.getName()).log(Level.WARNING, "Saving of " + t.getTitle() 
	        					+ ".pdf to " + destFile.getParent() + " failed", ex);
	        			fileTitle.setText("Save failed.");
                	} finally {
 	                    if (source != null) {
 	                        try {
 								source.close();
 							} catch (IOException e1) {
 								Logger.getLogger(UI.class.getName()).log(Level.SEVERE, 
									"FileInputStream for saving will not close", e1);
 							}
 	                    }
 	                    if (destination != null) {
 	                    	try {
 	                        	destination.close();
 	                    	} catch (IOException e1) {
 	                    		Logger.getLogger(UI.class.getName()).log(Level.SEVERE, 
									"FileOutputStream for saving will not close", e1);
 	                    	}
 	                    }
 	                }
                } else
            		fileTitle.setText("Save cancelled");	
            } else
        		fileTitle.setText("Save cancelled");		
		}
		
		else if (e.getSource().equals(help) || e.getSource().equals(helpMenu)) {
			try {
				File htmlFile = new File("help/index.html");
				java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
			} catch (IOException e1) { }
		}
		
		else if (e.getSource().equals(print) || e.getSource().equals(printMenu)) {
			fileTitle.setText("Printing...");
			PDFPrintPage pages = new PDFPrintPage(pdfFile);
			
			PrinterJob pjob = PrinterJob.getPrinterJob();
			PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
			pjob.setJobName(t.getTitle());
			Book book = new Book();
			book.append(pages, pf, pdfFile.getNumPages());
			pjob.setPageable(book);
			
			pf.setOrientation(PageFormat.PORTRAIT);
			Paper paper = new Paper();
			paper.setImageableArea(25,0,paper.getWidth() * 2,paper.getHeight());
			pf.setPaper(paper);
			
			if (pjob.printDialog() == true) {
				try {
					pjob.print();
					fileTitle.setText("Printed " + t.getTitle() + ".pdf");
				} catch (PrinterException e1) {
					JOptionPane.showMessageDialog(frame, "Failed to print " + t.getTitle() + ".pdf");
					Logger.getLogger(UI.class.getName()).log(Level.SEVERE, 
							"Printing of " + t.getTitle() + ".pdf failed.", e1);
				}
			}
		}
		
		else if (e.getSource().equals(fontSize) ||e.getSource().equals(fontSizeTitle) 
				|| e.getSource().equals(fontSizeAuthor) || e.getSource().equals(fontType)) {
			s.myFontface = FontSelector.getFont(fontType.getSelectedIndex());
            s.setFontSize(Integer.parseInt(fontSize.getSelectedItem().toString()));
            s.setMyTitleSize(Integer.parseInt(fontSizeTitle.getSelectedItem().toString()));
            s.setMySubTitleSize(Integer.parseInt(fontSizeAuthor.getSelectedItem().toString()));
            generatePDF(zoomSlide.getValue());
		}
		
		else if (e.getSource().equals(aboutMenu)) {
			ImageIcon icon = createImageIcon("images/logo.png");
			JOptionPane.showMessageDialog(frame,
			    "<html><center>Version: 3.0.6 \n"
			    + "Thank you for using our Tablature to PDF software. \n"
			    + "Group 3: Calvin Tran, Jason Kuffaur, Mohamed Nasar, Muhammad Shah, Siraj Rauf, Umer Zahoor, Waleed Azhar\n"
			    + "Special thanks to iText team, Pdf-renderer team, and Tango icon set team",
			    "About",
			    JOptionPane.INFORMATION_MESSAGE,
			    icon);
		}
		
		else if (e.getSource().equals(reset)) {
			fontSizeTitle.setSelectedIndex(10); //Reset GUI
			fontSizeAuthor.setSelectedIndex(6);
			title.setText(defaultTitle);
			author.setText(defaultSubtitle);
			fontType.setSelectedIndex(indexOfHelvetica);
			fontSize.setSelectedIndex(1);
			numberSpacing.setValue(50);
			measureSpacing.setValue(300);
			lineSpacing.setValue(70);
			leftMargin.setValue(36);
			rightMargin.setValue(36);
			zoomSlide.setValue(defaultZoom);
			zoom.setText("100%");

			t.setTitle(defaultTitle); //Reset tab
			t.setSubtitle(defaultSubtitle);
			t.setSpacing(defaultSpacing);
			s.setFontSize(8);
			s.setMyTitleSize(24);
			s.setMySubTitleSize(16);
			s.setLineDistance(7f);
			s.setMeasureDistance(30f);
            s.setLeftMargin(36f);
            s.setRightMargin(36f);
			
			generatePDF(defaultZoom);
			fileTitle.setText("Values reset to default.");
		}
	}
	
	/**
	 * Creates the PDF with user entered values then updates preview.
	 * Expands program and isntantiates live preview if no document was already open.
	 * Defaults on set values if no user entered values.
	 * @param zoomLevel Zoomlevel used for livepreview
	 */
	private void generatePDF(int zoomLevel) {
		try {
			PdfOutputCreator pdfout = new PdfOutputCreator();
			pdfout.makePDF(t, s);
		} catch (IOException | DocumentException e1) {
			e1.printStackTrace();
		}
		
		if (opened == true)
			updatePreview(zoomLevel);
		else 
			expandView();
	}

	/**
	 * Expands program to include editing options, and a live preview
	 */
	private void expandView() {
		updatePreview(defaultZoom);
		a1 = new JPanel();
		a1.setLayout(new BoxLayout(a1, BoxLayout.Y_AXIS));
		a1.add(viewBox());
		a1.add(editBox());
		sidebar.add(a1, BorderLayout.PAGE_END);
		 	
		try {
			a2 = createA2();
			body.add(a2, BorderLayout.CENTER);
		} catch (IOException e1) {
			Logger.getLogger(UI.class.getName()).log(Level.SEVERE, e1.getMessage());
		}
		
		frame.setResizable(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) (screenSize.getHeight() / 1.5);
		int width = (int) (screenSize.getWidth() / 1.5);
		if (height < 675)
			height = 675;
		if (width < 1070)
			width = 1070;
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.pack();
        frame.setLocationRelativeTo(null);
		opened = true;
	}
	
    /**
     * Updates live preview
     * @param zoomLevel Level of zoom for live preview
     */
    private void updatePreview(int zoomLevel) {
		RandomAccessFile raf;
		ByteBuffer buf;
		for (int i = 0; i < image.size(); i++)
			image.get(i).flush();
		
		pageLabel.clear();
		image.clear();

		try {
			raf = new RandomAccessFile (new File ("temp.pdf"), "r");
			byte[] b = new byte[(int) raf.length()];
			raf.read(b);
			buf = ByteBuffer.wrap(b);
			pdfFile = new PDFFile(buf);
			
			for (int i = 1; i <= pdfFile.getNumPages(); i++) {
				page = pdfFile.getPage(i);
			    Rectangle2D r2d = page.getBBox();
			    width = r2d.getWidth();
			    height = r2d.getHeight();
			    width /= 72.0;
			    height /= 72.0;
			    int res = Toolkit.getDefaultToolkit ().getScreenResolution ();
			    width *= res;
			    height *= res;
			    
			    double realZoomLevel = 100 / (double) zoomLevel;
			    image.add(page.getImage ((int) (width/realZoomLevel), (int) (height/realZoomLevel), r2d, null, true, true));
			}
		    buf.clear();
		    raf.close();
		} catch (IOException e1) { }
		
		if(image.size() > 0) {
			for (int i = 0; i < image.size(); i++) {
				ImageIcon temp = new ImageIcon(image.get(i));
				JLabel temp2 = new JLabel();
				temp2.setIcon(temp);
				pageLabel.add(temp2);
			}
		}
		
		if (livePreview == null)
			livePreview = new JPanel();
    	livePreview.setLayout(new BoxLayout(livePreview, BoxLayout.Y_AXIS));
    	if (pageLabel != null) {
    		livePreview.removeAll();
        	for (int i = 0; i < pageLabel.size(); i++) {
        		livePreview.add(pageLabel.get(i));
        		livePreview.add(new JLabel(""));
    		}
		}
    	frame.invalidate();
    	frame.validate();
    	frame.repaint();
		fileTitle.setText("Updated preview.");
    }
	
    public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
    
    @Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource().equals(title) || e.getSource().equals(author)) {
			t.setTitle(title.getText());
			t.setSubtitle(author.getText());
			generatePDF(zoomSlide.getValue());
		}
		
		else if (e.getSource().equals(zoom)) {
			String userZoom = zoom.getText().replaceAll("%", "");
			if (isInteger(userZoom) && userZoom.length() > 0 && Integer.parseInt(userZoom) != 0) {
				int temp = Integer.parseInt(userZoom);
				updatePreview(temp);
				if (temp > 400) {
					zoomSlide.setValue(400);
					zoom.setText("400%");
				}
				else
					zoomSlide.setValue(temp);
			}
			else {
				zoom.setText(defaultZoom + "%");
			}
		}
	}
	
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
	        updatePreview(zoomSlide.getValue());
		}
		else if (e.getSource().equals(numberSpacing)) {
			t.setSpacing(numberSpacing.getValue() / 10);
			generatePDF(zoomSlide.getValue());
		}
		else if (e.getSource().equals(measureSpacing)) {
			s.setMeasureDistance((float) (measureSpacing.getValue() / 10f));
			generatePDF(zoomSlide.getValue());
		}
		else if (e.getSource().equals(lineSpacing)) {
			s.setLineDistance((float) (lineSpacing.getValue() / 10));
			generatePDF(zoomSlide.getValue());
		}
		else if (e.getSource().equals(leftMargin)) {
			s.setLeftMargin((float) leftMargin.getValue());
			generatePDF(zoomSlide.getValue());
        }
		else if (e.getSource().equals(rightMargin)) {
			s.setRightMargin((float) rightMargin.getValue());
			generatePDF(zoomSlide.getValue());
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) { }

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource().equals(zoom) && !zoom.getText().contains("%")) 
			zoom.setText(zoom.getText() + "%");
	}
}