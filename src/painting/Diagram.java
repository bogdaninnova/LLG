package painting;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Diagram {

	private int width = 1000;
	private int height = 1000;
	
	private int cellSizeByX = 50;
	private int cellSizeByY = 1;
	private double x_min;
	private double x_max;
	private double y_min;
	private double y_max;
	
	private ArrayList<Double> listX = new ArrayList<Double>();
	private ArrayList<Double> listY = new ArrayList<Double>();
	private ArrayList<Integer> listValue = new ArrayList<Integer>();
	
	public Diagram() {
		readInList(listX, "H_0.txt");
		readInList(listY, "TAU.txt");
		for (int i = 0; i < listX.size(); i++)
			listValue.add(100);
		setLimitValues();
		draw();
	}
	
	private void setLimitValues() {
		x_min = getMinimal(listX);
		x_max = getMaximal(listX);
		y_min = getMinimal(listY);
		y_max = getMaximal(listY);
	}
	
	private double getMinimal(List<Double> list) {
		double min = Math.pow(10, 10);
		for (double dot : list) 
			if (dot < min)
				min = dot;
		return min;
	}
	
	private double getMaximal(List<Double> list) {
		double max = -Math.pow(10, 10);
		for (double dot : list) 
			if (dot > max)
				max = dot;
		return max;
	}

	private void readInList(ArrayList<Double> list, String path) {
		File file = new File(path);
        BufferedReader in;
    	String temp;
    	try {
			in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
				while ((temp = in.readLine()) != null) 
					list.add(Double.parseDouble(temp));
				
					
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	public void draw() {
		
		double xStep = (double)width / (double)listX.size();
		double yStep = (double)height / (double)listY.size();
		
		System.out.println((x_max - x_min) / listX.size());//TODO
		System.out.println();
		System.out.println();
		System.out.println();
		
		BufferedImage bi = new BufferedImage(
				(int) ((x_max - x_min) / xStep * cellSizeByX),
				(int) ((y_max - y_min) / yStep * cellSizeByY),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = bi.createGraphics();
		
		for (int i = 0; i < listY.size(); i++) {
			
			g.setColor(new Color(listValue.get(i), 0, 0));
			
			g.fillRect(
					(int) Math.round((listX.get(i) - x_min) / xStep * cellSizeByX),
					(int) Math.round((y_max - listY.get(i)) / yStep * cellSizeByY),
					cellSizeByX, cellSizeByY);
		}

		saveInFile(bi, "Diagramm");
	}
	
	private static void saveInFile(BufferedImage bi, String name) {
		try {
			ImageIO.write(bi, "PNG", new File(name + ".PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
