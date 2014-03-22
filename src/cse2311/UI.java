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
import java.net.URI;
import java.net.URISyntaxException;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPrintPage;
import com.sun.pdfview.PagePanel;

public class UI extends JFrame implements ActionListener, KeyListener, MouseListener, FocusListener {
    private static final long serialVersionUID = 1L;
	private static final int defaultZoom = 100;
	final int defWidth = 180, defHeight = 30;
    static JFrame frame;
    
    JTextField fileTitle, title, author, zoom;
    JPanel body, sidebar, a1, b1, livePreview, livePreview2;
	JScrollPane a2;
    JComboBox fontType;
    JComboBox<Integer> fontSizeTitle, fontSizeAuthor;
    JSlider numberSpacing, sectionSpacing, lineSpacing, zoomSlide;
    JButton open, help, save, print;
    JMenuItem openMenu, saveMenu, optionsMenu, printMenu, aboutMenu, helpMenu;
    
    int view = 0;
    double width, height;
    float userSpacing, userSectionDistance, userLineDistance;
    boolean opened = false;
    String filePath, userTitle, userSubtitle, prevDir;
    File txtFile, userDirectory = null, output;
    Tablature t;
    MusicSheet ms;
    Parser c = new Parser();
    Style s = new Style(new Document(PageSize.A4));
    
	PDFPage page;
    PDFFile pdfFile;
    ArrayList<Image> image = new ArrayList<Image>();
    ArrayList<JLabel> pageLabel = new ArrayList<JLabel>();
	
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
    }
    
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
    
    public JPanel createBody() { //Create the content-pane-to-be.
        body = new JPanel(new BorderLayout());
        sidebar = new JPanel(new BorderLayout());
        b1 = createB1();
        
        sidebar.add(b1, BorderLayout.PAGE_START);
        body.add(sidebar, BorderLayout.LINE_START);
        body.add(createS1(), BorderLayout.SOUTH);
        return body;
    }
    
    public  JPanel createB1() {
    	 JPanel temp = new JPanel(new GridLayout(2,2,10,10));
    	 temp.setOpaque(true);
    	 
         //create a jpanel with open, save, help and more buttons
    	 ImageIcon icon = createImageIcon("images/open48.png");
         open = new JButton("Open", icon);
         open.setToolTipText("Open ASCII tablature to convert");
         open.addActionListener(this);
                  
         icon = createImageIcon("images/save48.png");
         save = new JButton("Save", icon);
         save.setToolTipText("Save PDF");
         save.addActionListener(this);
         save.setEnabled(false);
         
         icon = createImageIcon("images/help48.png");
         help = new JButton("Help", icon);
         help.setToolTipText("Help");
         help.addActionListener(this);
         
         icon = createImageIcon("images/print48.png");
         print = new JButton("Print", icon);
         print.setToolTipText("Print opened music sheet");
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
    
    public JPanel editBox() {
    	JPanel temp = new JPanel(new BorderLayout());
    	temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
    	temp.setBorder(new TitledBorder(new EtchedBorder(), "Edit"));
    	
    	title = new JTextField(15);
    	Integer[] fontSizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
    	fontSizeTitle = new JComboBox<Integer>(fontSizes);
    	fontSizeTitle.setSelectedIndex(10);
    	
    	JPanel titleTemp = new JPanel();
    	titleTemp.add(new JLabel("    Title: "));
    	titleTemp.add(title);
    	titleTemp.add(fontSizeTitle);
    	
    	title.addKeyListener(this);
    	fontSizeTitle.addActionListener(this);
    	
    	author = new JTextField(15);
    	fontSizeAuthor = new JComboBox<Integer>(fontSizes);
    	fontSizeAuthor.setSelectedIndex(6);
    	
    	JPanel authorTemp = new JPanel();
    	authorTemp.add(new JLabel("Author: "));
    	authorTemp.add(author);
    	authorTemp.add(fontSizeAuthor);
    	
    	fontSizeAuthor.addActionListener(this);
    	author.addKeyListener(this);
    	
    	String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    	fontType = new JComboBox(fonts);
    	//fontType.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXX");
    	int indexOfArial = (Arrays.asList(fonts)).indexOf("Arial");
    	fontType.setSelectedIndex(indexOfArial);
    	
    	JPanel fontTemp = new JPanel();
    	fontTemp.add(new JLabel("    Font: "));
    	fontTemp.add(fontType);
    	fontType.addActionListener(this);
    	
    	JLabel spacingLabel = new JLabel("<html><u>Spacing");
    	numberSpacing = new JSlider(1, 99, (int) userSpacing * 10);
    	numberSpacing.setMinorTickSpacing(10);
    	numberSpacing.setPaintTicks(true);
    	numberSpacing.setPreferredSize(new Dimension(defWidth, defHeight));
    	numberSpacing.addMouseListener(this);
    	
    	JPanel spacingBarsTemp = new JPanel();
    	spacingBarsTemp.add(new JLabel("   Numbers:"));
    	spacingBarsTemp.add(numberSpacing);
    	
    	sectionSpacing = new JSlider(140, 900, 300);
    	sectionSpacing.setMinorTickSpacing(30);
    	sectionSpacing.setPaintTicks(true);
    	sectionSpacing.setPreferredSize(new Dimension(defWidth, defHeight));
    	sectionSpacing.addMouseListener(this);
    	
    	JPanel spacingStaffsTemp = new JPanel();
    	spacingStaffsTemp.add(new JLabel("  Sections:"));
    	spacingStaffsTemp.add(sectionSpacing);
    	
    	lineSpacing = new JSlider(20, 210, 70);
    	lineSpacing.setMinorTickSpacing(10);
    	lineSpacing.setPaintTicks(true);
    	lineSpacing.setPreferredSize(new Dimension(defWidth, defHeight));
    	lineSpacing.addMouseListener(this);
    	
    	JPanel spacingLinesTemp = new JPanel();
    	spacingLinesTemp.add(new JLabel("     Lines:"));
    	spacingLinesTemp.add(lineSpacing);
    	
    	temp.add(titleTemp, temp);
    	temp.add(authorTemp, temp);
    	temp.add(fontTemp, temp);
    	temp.add(spacingLabel, temp);
    	temp.add(spacingBarsTemp, temp);
    	temp.add(spacingStaffsTemp, temp);
    	temp.add(spacingLinesTemp, temp);
		return temp;
    }
    
    public JPanel viewBox() {
    	JPanel temp = new JPanel();
    	temp.setBorder(new TitledBorder(new EtchedBorder(), "View"));

    	zoom = new JTextField(5);
    	zoom.setText(defaultZoom + "%");
    	
    	zoomSlide = new JSlider(JSlider.HORIZONTAL, 0, 400, defaultZoom);
    	zoomSlide.setMajorTickSpacing(100);
    	zoomSlide.setMinorTickSpacing(50);
    	zoomSlide.setPaintTicks(true);
    	zoomSlide.setPaintLabels(true);
    	
    	zoomSlide.addMouseListener(this);
    	zoom.addKeyListener(this);
    	zoom.addFocusListener(this);
    	
    	temp.add(zoom);
    	temp.add(zoomSlide);
    	temp.setPreferredSize(new Dimension((int) ((int) b1.getWidth()/1.5 - 10) , 100));

		return temp;
    }
    
    public JScrollPane createA2() throws IOException {
    	
    	pageLabel.add(new JLabel());
    	if (image.size() > 0){
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
    
    public JPanel createS1() {
    	JPanel temp = new JPanel(new GridLayout(1,1));
    	temp.setOpaque(true);
        
        fileTitle = new JTextField("");
        fileTitle.setHorizontalAlignment(JTextField.RIGHT);
        fileTitle.setOpaque(false);
        fileTitle.setBorder(null);
        temp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
        temp.add(fileTitle);
		return temp;
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = UI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(open) || e.getSource().equals(openMenu)) {
			JFileChooser openFile = new JFileChooser();

			fileTitle.setText("Opening...");
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
			        userTitle = t.getTitle();
			        userSubtitle = t.getSubtitle();
			        userSpacing = t.getSpacing();
			        userSectionDistance = 30f;
			        userLineDistance = 7f;
					generatePDF();
					
			        save.setEnabled(true);
			        print.setEnabled(true);
			        saveMenu.setEnabled(true);
			        printMenu.setEnabled(true);
			        fileTitle.setText("Opened " + userTitle + ".txt");
					frame.setTitle("Tab2PDF - " + userTitle + ".pdf");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
				if (opened == true)
					updatePreview(defaultZoom);
				else {
					expandView();
				}

				title.setText(userTitle);
				author.setText(userSubtitle);
				sectionSpacing.setValue((int) userSectionDistance * 10);
				lineSpacing.setValue((int) userLineDistance * 10);
			}
			else {
				fileTitle.setText("Cannot open selected file.");
			}
		}
		else if (e.getSource().equals(save) || e.getSource().equals(saveMenu)) {
			JFileChooser saveFile = new JFileChooser();
			output = new File(t.getTitle() + ".pdf");
			saveFile.setFileFilter(new FileNameExtensionFilter("PDF documents", "pdf"));
			saveFile.setAcceptAllFileFilterUsed(false);
			saveFile.setCurrentDirectory(new File(prevDir));
			saveFile.setSelectedFile(output);
			int returnVal = saveFile.showSaveDialog(this);
			fileTitle.setText("Saving...");
			
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File destFile = saveFile.getSelectedFile();
            	if (!destFile.getAbsolutePath().endsWith(".pdf"))
            		destFile = new File(destFile.getAbsolutePath().concat(".pdf"));
            		
            	boolean allow = true;
            	if (destFile.exists()){
            		allow = JOptionPane.showConfirmDialog(null, "That files exists. Overwrite?") == JOptionPane.OK_OPTION;
            	}
    			
            	if (allow) {	            		
            		FileChannel source = null;
 	                FileChannel destination = null;
                	try {
	                    source = new FileInputStream(output).getChannel();
	                    destination = new FileOutputStream(destFile).getChannel();
	                    destination.transferFrom(source, 0, source.size());

	        			fileTitle.setText("Saved " + userTitle + " to " + destFile.getParent());
                	} catch(Exception ex) { 
                	} finally {
 	                    if(source != null) {
 	                        try {
 								source.close();
 							} catch (IOException e1) {
 								e1.printStackTrace();
 							}
 	                    }
 	                    if(destination != null) {
 	                    	try {
 	                        	destination.close();
 	                    	} catch (IOException e1) {
 								e1.printStackTrace();
 	                    	}
 	                    }
 	                }
                }
            	else
            		fileTitle.setText("Save failed");
            } 			
		}
		else if (e.getSource().equals(help) || e.getSource().equals(helpMenu)) {
			try {
				File htmlFile = new File("help/index.html");
				java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
				} catch (PrinterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			fileTitle.setText("Printed " + userTitle + ".pdf");
		}
		else if (e.getSource().equals(fontSizeTitle) || e.getSource().equals(fontSizeAuthor) || e.getSource().equals(fontType)) {
			fileTitle.setText(fontType.getSelectedItem().toString());
			// TODO Waleed set font here, remember to use something like userFont the same way we do spacing
			// 		since we're instantiating the Style object every time we generate a PDF.
			//		E.g. set userFont = some font, then in generate PDF s.setFont() (add this method?) = userFont;
		}
		else if (e.getSource().equals(aboutMenu)){
			ImageIcon icon = createImageIcon("images/logo.png");
			JOptionPane.showMessageDialog(frame,
				    "<html><center>Version: 3.0.6 \n"
				    + "Thank you for using our Tablature to PDF software, which is far superior than Group 1 and Group 2 \n"
				    + "Group 3: Calvin Tran, Jason Kuffaur, Mohamed Nasar, Muhammad Shah, Siraj Rauf, Umer Zahoor, Waleed Azhar\n"
				    + "Special thanks to iText team, Pdf-renderer team, and Tango icon set team",
				    "About",
				    JOptionPane.INFORMATION_MESSAGE,
				    icon);
		}
	}
	
	private void generatePDF() {
		try {
			PdfOutputCreator pdfout = new PdfOutputCreator();
			s = new Style(new Document(PageSize.A4));
			s.setLineDistance(userLineDistance);
			s.setSectionDistance(userSectionDistance);
			ms = new MusicSheet(t, s);
			pdfout.makePDF(ms);
		} catch (IOException | DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		frame.setResizable(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) (screenSize.getHeight() / 1.5);
		int width = (int) (screenSize.getWidth() / 1.5);
		frame.setPreferredSize(new Dimension(width, height));
		frame.pack();
		opened = true;
	}
	
    private void updatePreview(int zoomLevel) {
		RandomAccessFile raf;
		ByteBuffer buf;
		for (int i = 0; i < image.size(); i++)
			image.get(i).flush();
		
		pageLabel.clear();
		image.clear();

		try {
			raf = new RandomAccessFile (new File (userTitle + ".pdf"), "r");
			byte[] b = new byte[(int) raf.length()];
			raf.read(b);
			buf = ByteBuffer.wrap(b);
			pdfFile = new PDFFile(buf);
			
			for (int i = 1; i <= pdfFile.getNumPages(); i++){
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
		
		if(image.size() > 0){
			for (int i = 0; i < image.size(); i++){
				ImageIcon temp = new ImageIcon(image.get(i));
				JLabel temp2 = new JLabel();
				temp2.setIcon(temp);
				pageLabel.add(temp2);
			}
		}
		
		if (livePreview == null)
			livePreview = new JPanel();
    	livePreview.setLayout(new BoxLayout(livePreview, BoxLayout.Y_AXIS));
    	if (pageLabel != null){
    		livePreview.removeAll();
        	for (int i = 0; i < pageLabel.size(); i++){
        		livePreview.add(pageLabel.get(i));
        		livePreview.add(new JLabel(""));
    		}
		}
    	frame.invalidate();
    	frame.validate();
    	frame.repaint();
    }
	
    @Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource().equals(title) || e.getSource().equals(author)) {
			userTitle = title.getText();
			userSubtitle = author.getText();
			t.setTitle(userTitle);
			t.setSubtitle(userSubtitle);
			generatePDF();
			updatePreview(zoomSlide.getValue());
		}
		else if (e.getSource().equals(zoom)) {
			String temp = zoom.getText().replaceAll("%", "");
			int zTemp;
			if (temp.length() > 0) {
				zTemp = Integer.parseInt(temp);
				if (zTemp != 0)
					updatePreview(zTemp);
				else
					zoom.setText(defaultZoom + "%");
				
				if (zTemp > 400)
					zoomSlide.setValue(400);
				else
					zoomSlide.setValue(zTemp);
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
		if (e.getSource().equals(zoomSlide)){
			if (zoomSlide.getValue() == 0)
				zoomSlide.setValue(1);
			zoom.setText(zoomSlide.getValue() + "%");
	        updatePreview(zoomSlide.getValue());
		}
		else if (e.getSource().equals(numberSpacing)){
			userSpacing = numberSpacing.getValue() / 10;
			t.setSpacing(userSpacing);
			generatePDF();
			updatePreview(zoomSlide.getValue());
		}
		else if (e.getSource().equals(sectionSpacing)){
			userSectionDistance = ((float) sectionSpacing.getValue()) / 10f;
			generatePDF();
			updatePreview(zoomSlide.getValue());
		}
		else if (e.getSource().equals(lineSpacing)) {
			userLineDistance = ((float) lineSpacing.getValue()) / 10;
			generatePDF();
			updatePreview(zoomSlide.getValue());
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