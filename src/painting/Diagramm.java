package painting;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

public class Diagramm {

	private final int cellSize;
	private double h_min = 100;
	private double h_max = 0;
	private double w_min = 100;
	private double w_max = 0;
	
	private LinkedList<DiagrammDot> list;
	private double stepH, stepW;
	
	public Diagramm(String path) {
		this.stepH = 0.01;//TODO automathic
		this.stepW = 0.01;//TODO automathic
		this.cellSize = 10;
		
		readElementsList(path);
		setMinAndMax();
	}
	
	private void readElementsList(String path) {

		File file = new File(path);
        File files[] = file.listFiles();
        BufferedReader in;
        LinkedList<DiagrammDot> list = new LinkedList<DiagrammDot>();
        
        double w, h;
        String a;
        
        try {
	        for(int i = 0; i < files.length; i++) {
	        	h = 0;
	        	a = files[i].getName();
	        	a = a.substring(4, a.length() - 4);//deleting "w = " and ".txt"
	        	w = Double.parseDouble(a);

	        	in = new BufferedReader(new FileReader(files[i].getAbsoluteFile()));
	            try {
	                while ((a = in.readLine()) != null) {
	                	h += stepH;
	                	list.add(new DiagrammDot(w, h, Integer.parseInt(a)));
	                }
	            } finally {
	                in.close();
	            }
	        }
        } catch(IOException e) {
        	System.out.println("IO ERROR");
        }
        this.list = list;
	}
	
	private void setMinAndMax() {
		ListIterator<DiagrammDot> iterator =list.listIterator();
		DiagrammDot element;
		
		while (iterator.hasNext()) {
			element = iterator.next();
			if (element.w < w_min) w_min = element.w;
			if (element.w > w_max) w_max = element.w;
			if (element.h < h_min) h_min = element.h;
			if (element.h > h_max) h_max = element.h;
		}
	}
	
	public void draw() {

		ListIterator<DiagrammDot> iterator =list.listIterator();
		DiagrammDot element;
		
		BufferedImage bi = new BufferedImage(
				(int) ((w_max - w_min) / stepW * cellSize),
				(int) ((h_max - h_min) / stepH * cellSize),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = bi.createGraphics();
		
		while (iterator.hasNext()) {
			element = iterator.next();
			g.setColor(new Color(element.altitude + 100, 0, 0));//TODO set colors
			g.fillRect(
					(int) Math.round((element.w - w_min) / stepW * cellSize),
					(int) Math.round((h_max - element.h) / stepH * cellSize),
					cellSize, cellSize);
		}

		saveInFile(addLegend(bi), "Diagramm");
	}
	
	private BufferedImage addLegend(BufferedImage bi) {
		BufferedImage bi2 = new BufferedImage(
				(int) (bi.getWidth() * 1.1),
				(int) (bi.getHeight() * 1.1),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi2.createGraphics();
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, bi2.getWidth(), bi2.getHeight());
		g2.drawImage(bi, null, (int) (0.05 * bi.getWidth()), (int) (0.05 * bi.getHeight()));
		g2.setColor(Color.black);
		
		writeScale(bi, g2);
		
		/*g2.setColor(Color.BLACK);
		Font font=new Font (Font.DIALOG, 1, (int) (0.03 / stepW * cellSize));
		g2.setFont(font);
		
		g2.drawString(String.valueOf(w_min), (int) (0.05 * bi.getWidth()), (int) (0.04 * bi.getHeight()));
		g2.drawString(String.valueOf(w_max), (int) (1 * bi.getWidth()), (int) (0.04 * bi.getHeight()));

		g2.drawString(String.valueOf(w_min), (int) (0.05 * bi.getWidth()), (int) (0.04 * bi.getHeight()));
		g2.drawString(String.valueOf(w_max), (int) (1 * bi.getWidth()), (int) (0.04 * bi.getHeight()));
	*/
		return bi2;
	}
	
	private void writeScale(BufferedImage bi, Graphics2D g) {//TODO 1
		int line = 10;
		int count = 10;
	
		g.setFont(new Font (Font.DIALOG, 1, (int) (0.02 / stepW * cellSize)));
		
		double x, y;
		
		for (int i = 0; i <= count; i++) {
			x = round(w_min + i * (w_max - w_min) / count);
			y = round(h_min + i * (h_max - h_min) / count);
			
			g.drawLine(
					(int) (0.05 * bi.getWidth() + i * bi.getWidth() / count),
					(int) (0.05 * bi.getHeight()),
					(int) (0.05 * bi.getWidth() + i * bi.getWidth() / count),
					(int) (0.05 * bi.getHeight() - line));
			g.drawString(Double.toString(x),
					(int) (0.05 * bi.getWidth() + i * bi.getWidth() / count - line),
					(int) (0.05 * bi.getHeight() - line));
			g.drawLine(
					(int) (0.05 * bi.getWidth() + i * bi.getWidth() / count),
					(int) (1.05 * bi.getHeight()),
					(int) (0.05 * bi.getWidth() + i * bi.getWidth() / count),
					(int) (1.05 * bi.getHeight() + line));
			g.drawString(Double.toString(x),
					(int) (0.05 * bi.getWidth() + i * bi.getWidth() / count - line),
					(int) (1.05 * bi.getHeight() + line + (int) (0.02 / stepW * cellSize)));
			g.drawLine(
					(int) (0.05 * bi.getWidth()),
					(int) (0.05 * bi.getHeight() + i * bi.getHeight() / count),
					(int) (0.05 * bi.getWidth() - line),
					(int) (0.05 * bi.getHeight()) + i * bi.getHeight() / count);
			g.drawString(Double.toString(y),
					(int) (1.05 * bi.getWidth() + line + (int) (0.005 / stepW * cellSize)),
					(int) (0.05 * bi.getHeight()) + (int) (0.01 / stepW * cellSize) + i * bi.getHeight() / count);
			g.drawLine(
					(int) (1.05 * bi.getWidth()),
					(int) (0.05 * bi.getHeight() + i * bi.getHeight() / count),
					(int) (1.05 * bi.getWidth() + line),
					(int) (0.05 * bi.getHeight()) + i * bi.getHeight() / count);
			g.drawString(Double.toString(y),
					(int) (0.05 * bi.getWidth() - line - (int) (0.05 / stepW * cellSize)),
					(int) (0.05 * bi.getHeight()) + (int) (0.01 / stepW * cellSize) + i * bi.getHeight() / count);

		}
	}
	
	private double round(double x) {
		int a = (int) (x * 1000);
		x = (double) a;
		return x/1000;
	}
	
	private static void saveInFile(BufferedImage bi, String name) {
		try {
			ImageIO.write(bi, "PNG", new File("c:\\java\\" + name + ".PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
