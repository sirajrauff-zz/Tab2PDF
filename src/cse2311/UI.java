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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import cse2311.MusicSheet;
import cse2311.Parser;
import cse2311.PdfOutputCreator;
import cse2311.Style;
import cse2311.Tablature;
 
public class UI extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	float userSpacing;
	boolean opened = false;
    String filePath, userTitle, userSubtitle;
    JTextField fileTitle;
    File txtFile, userDirectory = null;
	Tablature t;
	MusicSheet ms;
    Parser c = new Parser();
	Style s = new Style(new Document(PageSize.A4));
	
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
    
    JButton open, directory, help, save;
    public  JPanel createB1() {
    	 JPanel temp = new JPanel(new GridLayout(2,2,10,10));
    	 temp.setOpaque(true);
    	 
         //create a jpanel with open, save, help and more buttons
         open = new JButton("Open Tablature"/*, icon*/);
         open.setToolTipText("Open ASCII tablature to convert");
         open.addActionListener(this);
         
         ImageIcon icon = createImageIcon("images/open48.png");
         directory = new JButton("Choose Save Directory", icon);
         directory.setToolTipText("Choose Directory to save PDF in");
         directory.addActionListener(this);
         directory.setEnabled(false);
         
         icon = createImageIcon("images/save48.png");
         save = new JButton("Save", icon);
         save.setToolTipText("Save PDF");
         save.addActionListener(this);
         save.setEnabled(false);
         
         icon = createImageIcon("images/help48.png");
         help = new JButton("Help", icon);
         help.setToolTipText("Help");
         help.addActionListener(this);
         
         //Add the text area to the content pane.
         temp.add(open);
         temp.add(directory); 
         temp.add(help);
         temp.add(save);
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
    	title.setMaximumSize(new Dimension(270,50));
          title.addFocusListener(null);
        title.addFocusListener( new FocusListener() {
                   @Override
                   public void focusGained(FocusEvent e) {
                
                    }

                   @Override
                    public void focusLost(FocusEvent e) {
                       //input validation
                         userTitle = title.getText();
			t.set_Title(userTitle);
			generatePDF();
			updatePreview();
                    
                    }

                     });
    	
    	JLabel authorLabel = new JLabel("Author: ");
    	author = new JTextField();
    	author.setMaximumSize(new Dimension(270,50));
    	author.addFocusListener( new FocusListener() {
                   @Override
                   public void focusGained(FocusEvent e) {
                     
                     
                    }

                   @Override
                    public void focusLost(FocusEvent e) {
                        //input validation
                        userSubtitle = author.getText();
			t.set_Subtitle(userSubtitle);
			generatePDF();
			updatePreview();
                    
                    }

                    });
    	
    	
    	JLabel fontLabel = new JLabel("Font");
    	fontType = new JComboBox();
    	fontSize = new JComboBox();
    	fontType.setMaximumSize(new Dimension(270,50));
    	fontSize.setMaximumSize(new Dimension(270,50));
    	
    	JLabel spacingLabel = new JLabel("Spacing: ");
    	spacing = new JSlider(1,99);
    	spacing.setMinorTickSpacing(10);
    	spacing.setPaintTicks(true);
    	spacing.addChangeListener(this);
    	
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
		if (e.getSource().equals(open)){
                    makeFileAndUpdate();
		}
		
		else if (e.getSource().equals(directory)){
			JFileChooser chooseDirectory = new JFileChooser();
                        chooseDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooseDirectory.setAcceptAllFileFilterUsed(false);
			
			int returnVal = chooseDirectory.showDialog(this, "Choose");
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				userDirectory = chooseDirectory.getSelectedFile();
				save.setEnabled(true);
			}
		}
		
		else if (e.getSource().equals(save)){
			try {
                                
				PdfOutputCreator pdfout = new PdfOutputCreator(userDirectory);
				s = new Style(new Document(PageSize.A4));
				ms = new MusicSheet(t,s);
				pdfout.makePDF(ms);
			} catch (IOException | DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if (e.getSource().equals(help)) {
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

    private void makeFileAndUpdate() throws HeadlessException {
        JFileChooser openFile = new JFileChooser();
        
        if (prevDir != null)
            openFile.setCurrentDirectory(new File(prevDir));
        openFile.setAcceptAllFileFilterUsed(false);
        openFile.setFileFilter(new FileNameExtensionFilter("Text documents", "txt"));
        
        int returnVal = openFile.showOpenDialog(this);
        
        txtFile = openFile.getSelectedFile();
        if (txtFile != null)
            prevDir = txtFile.getAbsolutePath();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                t = c.readFile(txtFile);
                s = new Style(new Document(PageSize.A4));
                ms = new MusicSheet(t,s);
                PdfOutputCreator pdfout = new PdfOutputCreator(userDirectory);
                pdfout.makePDF(ms);
                directory.setEnabled(true);
                userTitle = t.get_Title();
                userSubtitle = t.get_Subtitle();
                userSpacing = t.get_Spacing();
            } catch ( DocumentException | IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            
            if (opened)
                updatePreview();
            else
                generatePreview();
            
            title.setText(userTitle);
            this.author.setText(userSubtitle);
            this.spacing.setValue((int) (t.get_Spacing() * 10));
        }
    }
	
	private void generatePDF() {
            
		try {
			PdfOutputCreator pdfout = new PdfOutputCreator(null);
			s = new Style(new Document(PageSize.A4));
			ms = new MusicSheet(t,s);
			pdfout.makePDF(ms);
		} catch (IOException | DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void generatePreview() {
		updatePreview();
		
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
		opened = true;
                deleteFile(t.get_Title()+".pdf");
	}

	private void updatePreview() {
		RandomAccessFile raf;
		ByteBuffer buf;
		try {
			raf = new RandomAccessFile (new File (userTitle + ".pdf"), "r");
			FileChannel fc = raf.getChannel();
			buf = fc.map (FileChannel.MapMode.READ_ONLY, 0, fc.size());
			PDFFile pdfFile = new PDFFile(buf);
			
			//int numpages = pdfFile.getNumPages();

		    page = pdfFile.getPage(1);
		            
		    Rectangle2D r2d = page.getBBox();

		    width = r2d.getWidth();
		    height = r2d.getHeight();
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
                deleteFile(t.get_Title()+".pdf");
	}
	
	@Override
	public void stateChanged(ChangeEvent e) { 
		
            
                  if (e.getSource().equals(spacing)) {
                       //input validation
			userSpacing = spacing.getValue()/10;
			t.set_Spacing(userSpacing);
			generatePDF();
			updatePreview();
		}
	}
        
        public void deleteFile(String s){
            try{
 
    		File file = new File(s);
 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
 
    		e.printStackTrace();
 
    	}
        }
}