package cse2311;
/**
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;

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
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
 
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
 
public class UI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
    String filePath;
    JTextField fileTitle;
    File txtFile;
	Tablature t;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    static JFrame copy;
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Tab2PDF");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //set look and feel to os's look and feel
        try	{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO automatic 
		}
        
        //Create and set up the content pane.
        UI demo = new UI();
        //frame.setJMenuBar(demo.createMenuBar());
        frame.add(demo.createBody(), BorderLayout.CENTER);
 
        //Display the window.
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        copy = frame;
    }
    
    /*JMenuItem openMenu, saveMenu, optionsMenu;
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        
 
        //Create the menu bar.
        menuBar = new JMenuBar();
 
        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);
 
        //a group of JMenuItems
        ImageIcon icon = createImageIcon("images/open16.png");
        openMenu = new JMenuItem("Open", icon);
        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        openMenu.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        openMenu.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        openMenu.addActionListener(this);
        menu.add(openMenu);
 
        icon = createImageIcon("images/save16.png");
        saveMenu = new JMenuItem("Save", icon);
        saveMenu.setMnemonic(KeyEvent.VK_B);
        saveMenu.addActionListener(this);
        menu.add(saveMenu);
  
        //Build second menu in the menu bar.
        menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");
        menuBar.add(menu);
        
        icon = createImageIcon("images/options16.png");
        optionsMenu = new JMenuItem("Preferences", icon);
        menu.add(optionsMenu);
 
        return menuBar;
    }*/
    
    JPanel body, sidebar, a1, b1;
	JScrollPane a2;
    public JPanel createBody() {
        //Create the content-pane-to-be.
        body = new JPanel(new BorderLayout());
        sidebar = new JPanel(new BorderLayout());
        b1 = createB1();
        
        sidebar.add(b1, BorderLayout.PAGE_START);
		
        body.add(sidebar, BorderLayout.LINE_START);
        body.add(createS1(), BorderLayout.SOUTH);
        
        return body;
    }
    
    JButton open, save, help;
    public  JPanel createB1() {
    	 JPanel temp = new JPanel(new GridLayout(2,2,10,10));
    	 temp.setOpaque(true);
    	 

         //create a jpanel with open, save, help and more buttons
         ImageIcon icon = createImageIcon("images/open48.png");
         open = new JButton("Open", icon);
         open.setToolTipText("Open");
         open.addActionListener(this);
         
         icon = createImageIcon("images/save48.png");
         save = new JButton("Save", icon);
         save.setToolTipText("Save");
         save.addActionListener(this);
         save.setEnabled(false);
         
         icon = createImageIcon("images/help48.png");
         help = new JButton("Help", icon);
         help.setToolTipText("Help");
         help.addActionListener(this);
         
         //Add the text area to the content pane.
         temp.add(open);
         temp.add(save); 
         temp.add(help);
         temp.setBorder(new TitledBorder(new EtchedBorder(), "Buttons"));
         return temp;
    }
    
    JTextField title, author;
    JComboBox fontType, fontSize;
    JSlider spacing;
    public JPanel createA1() {
    	JPanel temp = new JPanel(new BorderLayout());
    	temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
    	temp.setBorder(new TitledBorder(new EtchedBorder(), "Edit"));
    	JLabel titleLabel = new JLabel("Title: ");
    	title = new JTextField();
    	title.setMaximumSize(new Dimension(150,50));
    	
    	JLabel authorLabel = new JLabel("Author: ");
    	author = new JTextField();
    	author.setMaximumSize(new Dimension(150,50));
    	JLabel fontLabel = new JLabel("Font");
    	fontType = new JComboBox();
    	fontSize = new JComboBox();
    	fontType.setMaximumSize(new Dimension(150,50));
    	fontSize.setMaximumSize(new Dimension(150,50));
    	
    	JLabel spacingLabel = new JLabel("Spacing: ");
    	spacing = new JSlider();
    	
    	temp.add(titleLabel, temp);
    	temp.add(title, temp);
    	temp.add(authorLabel, temp);
    	temp.add(author, temp);
    	temp.add(fontLabel, temp);
    	temp.add(fontType, temp);
    	temp.add(fontSize, temp);
    	temp.add(spacingLabel, temp);
    	temp.add(spacing, temp);
		return temp;
    }
    
    Image image;
    JLabel livePreview;
    public JScrollPane createA2() throws IOException {
    	
    	livePreview = new JLabel();
    	if (image != null){
    		livePreview.setIcon(new ImageIcon(image));
    		livePreview.setText("");
    	}
    	livePreview.setBackground(Color.WHITE);
    	livePreview.setPreferredSize(new Dimension(544, 704));
    	JPanel temp = new JPanel(new GridBagLayout());
    	temp.add(livePreview);
		return new JScrollPane (temp);
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
    
    int view = 0;
    double width, height;
    PDFPage page;
    String prevDir;
    File output;
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(open)/* || e.getSource().equals(openMenu)*/){
			JFileChooser openFile = new JFileChooser();
			
			if (prevDir != null)
				openFile.setCurrentDirectory(new File(prevDir));
			
			openFile.setFileFilter(new FileNameExtensionFilter("Text documents", "txt"));	
			openFile.setAcceptAllFileFilterUsed(false);
			int returnVal = openFile.showOpenDialog(this);
			
			txtFile = openFile.getSelectedFile();
			if (txtFile != null)
				prevDir = txtFile.getAbsolutePath();
			//OLD PDFgenertation code
			ArrayList<String[]> sections = new ArrayList<String[]>();
			String subtitle = "", title = "";
			Float spacing;
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				//TODO add open functionality here
				//'txtFile' is the txt file being passed
				Parser c = new Parser();
				Style s = new Style(new Document(PageSize.A4));
				try {
					t = c.readFile(txtFile);
					MusicSheet ms = new MusicSheet(t,s);
					PdfOutputCreator pdfout = new PdfOutputCreator("");
					pdfout.makePDF(ms);
					title = t.get_Title();
					subtitle = t.get_Subtitle();
					spacing = t.get_Spacing();
			        save.setEnabled(true);
				} catch (IOException | DocumentException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				/*if (title != "") {
					this.title.setText(title);
				}
				if (subtitle != "") {
					this.author.setText(subtitle);
				}*/
				
				RandomAccessFile raf;
				ByteBuffer buf;
				try {
					raf = new RandomAccessFile (new File (title + ".pdf"), "r");
					FileChannel fc = raf.getChannel ();
					buf = fc.map (FileChannel.MapMode.READ_ONLY, 0, fc.size ());
					PDFFile pdfFile = new PDFFile (buf);
					
					int numpages = pdfFile.getNumPages ();

			        page = pdfFile.getPage (1);
			                
			        Rectangle2D r2d = page.getBBox ();

			        width = r2d.getWidth ();
			        height = r2d.getHeight ();
			        width /= 72.0;
			        height /= 72.0;
			        int res = Toolkit.getDefaultToolkit ().getScreenResolution ();
			        width *= res;
			        height *= res;
			        image = page.getImage ((int) (width/1.5), (int) (height/1.5), r2d, null, true, true);

			        buf.clear();
			        raf.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				}
				
				if(livePreview != null && image != null){
					livePreview.setIcon(new ImageIcon(image));
					livePreview.setPreferredSize(new Dimension(544, 704));
				}
				
				fileTitle.setText(title + ".pdf");
				
				a1 = createA1();
		        sidebar.add(a1, BorderLayout.PAGE_END);
		        
		        try {
		        	a2 = createA2();
					body.add(a2, BorderLayout.CENTER);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		        livePreview.setPreferredSize(new Dimension(544, 704));
				copy.pack();
			}

		}
		else if (e.getSource().equals(save)/* || e.getSource().equals(saveMenu)*/){
			output = new File(title.getText() + ".pdf");
			JFileChooser saveFile = new JFileChooser();
			saveFile.setFileFilter(new FileNameExtensionFilter("PDF documents", "pdf"));
			saveFile.setAcceptAllFileFilterUsed(false);
			saveFile.setCurrentDirectory(new File(prevDir));
			saveFile.setSelectedFile(output);
			int returnVal = saveFile.showSaveDialog(this);
			
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	if (JOptionPane.showConfirmDialog(null, "That files exists. Overwrite?") == JOptionPane.OK_OPTION){
	                File file = saveFile.getSelectedFile();
	                
	                //TODO add save functionality here
	                //'file' is where the user wants to save the file and the filename 
	                if(!file.exists()) {
	                	try {
							file.createNewFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                }
	
	                FileChannel source = null;
	                FileChannel destination = null;
	
	                try {
	                	try {
		                    source = new FileInputStream(output).getChannel();
		                    destination = new FileOutputStream(file).getChannel();
		                    destination.transferFrom(source, 0, source.size());
	                	}catch(Exception ex){}
	                	
	                }
	                
	                finally {
	                    if(source != null) {
	                        try {
								source.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	                    }
	                    if(destination != null) {
	                    	try {
	                        	destination.close();
	                    	} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
	                    	}
	                    }
	                }
            	}
			}
		}
		
		else if (e.getSource().equals(help)){
			try {
				java.awt.Desktop.getDesktop().browse(new URI("http://www.google.ca"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}