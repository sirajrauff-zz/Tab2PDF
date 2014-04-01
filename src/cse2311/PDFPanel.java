package cse2311;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPrintPage;

/**
 * This class allows for the creation of a JScrollPane
 * @author Umer
 */
public class PDFPanel {
	private static final int defaultZoom = 100;
	private JPanel livePreview;
	private ArrayList<JLabel> pageLabel = new ArrayList<JLabel>();
	private ArrayList<Image> image = new ArrayList<Image>();
	private PDFPage page;
	private JScrollPane preview;
	private PDFFile pdfFile;
	private double width, height;
	private File fileInView;
	
	/**
     * Creates the PDF preview
     * @param file PDF file to be previewed
     */	 
	public PDFPanel(File file) {
		fileInView = file;
		pageLabel.add(new JLabel());
    	if (image.size() > 0) {
    		for (int i = 0; i < image.size(); i++)
    			pageLabel.get(i).setIcon(new ImageIcon(image.get(i)));
    	}
    	
    	livePreview = new JPanel();
    	livePreview.setLayout(new BoxLayout(livePreview, BoxLayout.Y_AXIS));
    	
    	for (JLabel page: pageLabel)
    		livePreview.add(page);
    	
    	JPanel livePreviewContainer = new JPanel(new GridBagLayout());
    	livePreviewContainer.add(livePreview);
    	preview = new JScrollPane(livePreviewContainer);
    	refresh(defaultZoom);
    	
    	preview.revalidate();
    	preview.repaint();
	}
 
	/**
     * Updates live preview
     * @param zoomLevel Level of zoom for live preview, INT > 0
     */	    
	public void refresh(int zoomLevel) {
		RandomAccessFile raf;
		ByteBuffer buf;
		for (Image temp: image)
			temp.flush();
		
		pageLabel.clear();
		image.clear();

		try {
			raf = new RandomAccessFile (fileInView, "r");
			byte[] b = new byte[(int) raf.length()];
			raf.read(b);
			buf = ByteBuffer.wrap(b);
			pdfFile = new PDFFile(buf);
			
			for (int i = 1; i <= pdfFile.getNumPages(); i++) {
				page = pdfFile.getPage(i);
			    Rectangle2D rectangle = page.getBBox();
			    width = rectangle.getWidth();
			    height = rectangle.getHeight();
			    width /= 72.0;
			    height /= 72.0;
			    int res = Toolkit.getDefaultToolkit().getScreenResolution ();
			    width *= res;
			    height *= res;
			    
			    double realZoomLevel = 100 / (double) zoomLevel;
			    image.add(page.getImage ((int)(width/realZoomLevel), (int)(height/realZoomLevel), rectangle, null, true, true));
			}
		    buf.clear();
		    raf.close();
		} catch (IOException e1) { }
		
		if (image.size() > 0) {
			for (Image tempImage: image) 
				pageLabel.add(new JLabel(new ImageIcon(tempImage)));
		}
		
		if (livePreview == null)
			livePreview = new JPanel();
    	livePreview.setLayout(new BoxLayout(livePreview, BoxLayout.Y_AXIS));
    	
    	if (pageLabel != null) {
    		livePreview.removeAll();
        	for (JLabel temp: pageLabel) {
        		livePreview.add(temp);
        		livePreview.add(new JLabel(""));
    		}
		}
    	
    	preview.invalidate();
    	preview.validate();
    	preview.repaint();
	}
	
	/**
	 * Takes the preview and prints it, will ask to change printer settings first
	 * @throws PrinterException
	 */
	public void print() throws PrinterException {
		PDFPrintPage pages = new PDFPrintPage(pdfFile);
		
		PrinterJob printJob = PrinterJob.getPrinterJob();
		PageFormat format = PrinterJob.getPrinterJob().defaultPage();
		printJob.setJobName(fileInView.getName());
		Book book = new Book();
		book.append(pages, format, pdfFile.getNumPages());
		printJob.setPageable(book);
		
		format.setOrientation(PageFormat.PORTRAIT);
		Paper paper = new Paper();
		paper.setImageableArea(25, 0, paper.getWidth() * 2, paper.getHeight());
		format.setPaper(paper);
		
		if (printJob.printDialog() == true)
			printJob.print();
	}
	
	/**
     * Gets the actual component
     * @return The JScrollPane that is generated
     */	
	public JScrollPane getPreview() {
		return preview;
	}
}
