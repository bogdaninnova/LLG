package painting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.imageio.ImageIO;

import main.Calculator;
import main.Vector;

public class DrawImpuls {

	private final static int size = 1000;
	
	private Calculator c;
	
	private double xAngle; 
	private double yAngle; 
	private double zAngle;
	private String name;
	private BufferedImage bi;
	private Graphics2D g;
	

	public DrawImpuls(Calculator c, double xAngle, double yAngle, double zAngle, String name) {
		this.c = c;
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
		this.name = name;
		
		setBufferedImage(new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR));
		this.g = DrawImpuls.getBackgroundedGraphics2D(bi, Color.white);
	}
	
	public void drawTraectory() {

			wrightCircle(Color.red);
			wrightAxeOfAnisotrophia(Color.green);
			wrightSecondCircle(Color.red);
			//wrightDot(size/Math.pow(10, 6), new Vector(), Color.black);
			//wrightDot(c.r * size / Math.pow(10, 3), c.startDot, Color.black);
			
			wrightTraectory(9999, Color.blue);
			//wright(rotate(c.array), Color.BLUE);
			
			//drawAxes(true, Color.black);
			writeLegend();
			
			draw();
	}
	
	private void wrightTraectory(int flushSize, Color color) {
		LinkedList<Vector> originalList = c.getArray();
		LinkedList<Vector> list;
		while (true) {
			list = new LinkedList<Vector>();
			if (originalList.size() < flushSize) {
				wright(rotate(originalList), color);
				break;
			}
			for (int i = 0; i < flushSize; i++)
				list.add(originalList.removeFirst());
			wright(rotate(list), color);
		}
	}
	
	private LinkedList<Vector> rotate(LinkedList<Vector> list) {
		LinkedList<Vector> newArray = new LinkedList<Vector>();
		ListIterator<Vector> iterator = list.listIterator();
		Vector dot;
		while (iterator.hasNext()) 
			newArray.add(iterator.next().rotate(xAngle, yAngle, zAngle));

		return newArray;
	}
	
	private void wright(LinkedList<Vector> array, Color color) {
		
		ListIterator<Vector> iter =array.listIterator();
		Vector dot1 = iter.next();
		Vector dot2 = iter.next();

		g.setColor(color);
		while (iter.hasNext()) {
			g.drawLine((int) (size / 2 * (dot1.getX() + 1)),
					(int) (size /2 * (dot1.getY() + 1)),
					(int) (size / 2 * (dot2.getX() + 1)),
					(int) (size /2 * (dot2.getY() + 1)));
			dot2 = dot1;
			dot1 = iter.next();
		} 
	}
		
	private void wrightCircle(Color color) {
		g.setColor(color);
		g.setStroke(new BasicStroke(5.0f));
		g.drawOval(0, 0, size, size);
		g.setStroke(new BasicStroke(1.0f));
	}
	
	private void wrightSecondCircle(Color color) {
		g.setColor(color);
		g.setStroke(new BasicStroke(5.0f));
		
		LinkedList<Vector> array = new LinkedList<Vector>();
		for (double x = -1; x <= 1; x += 0.01) 
			array.add(new Vector(x, Math.sqrt(1 - x*x), 0));
		for (double x = -1; x <= 1; x += 0.01) 
			array.add(new Vector(-x, -Math.sqrt(1 - x*x), 0));
		array.add(new Vector(-1, 0, 0));
		array.add(new Vector(-1.01, Math.sqrt(1 - 1.01*1.01), 0));
		
		wright(rotate(array), color);

		g.setStroke(new BasicStroke(1.0f));
	}

	private void writeLegend() {
		BufferedImage bi2 = new BufferedImage(bi.getWidth(), (int) (bi.getHeight() * 1.3),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = DrawImpuls.getBackgroundedGraphics2D(bi2, Color.white);
		
		g2.setColor(Color.BLACK);
		Font font=new Font (Font.DIALOG, 1, (int) (0.03 * size));
		g2.setFont(font);
		
		g2.drawImage(bi, null, 0, 0);
		g2.drawString("Tetta0: " + 0 / Math.PI + " Pi, tetta: " + 0 / Math.PI + " Pi",
																						size/10, size + 3 * size/20);
		g2.drawString("w: " + 0, size/10, size + 4 * size/20);
		g2.drawString("h: [eq", size/10, size + 5 * size/20);
		
		g2.drawString("xAngle: " + xAngle / Math.PI + " Pi", size/5 + size / 2, size + 3 * size/20);
		g2.drawString("yAngle: " + yAngle / Math.PI + " Pi", size/5 + size / 2, size + 4 * size/20);
		g2.drawString("zAngle: " + zAngle / Math.PI + " Pi", size/5 + size / 2, size + 5 * size/20);
		
//		g2.drawString("Waiting: " + c.waitingTime + "periods; Repeating: " + 0
//				+ " / " + c.workingTime, size/10, size + 7 * size/20);
		
		g2.drawString(
				"Date: " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(new Date()),
				size/10, size + 8 * size/20);
		setBufferedImage(bi2);
	}
	
	private void draw() {
		try {
			ImageIO.write(bi, "PNG", new File("c:\\"+ name +".PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawLine(Vector dot1, Vector dot2, Color color) {
		g.setColor(color);

		LinkedList<Vector> array = new LinkedList<Vector>();
		array.add(dot1);
		array.add(dot2);
		array.add(new Vector());
		
		wright(rotate(array), color);
	}
	
	private void wrightAxeOfAnisotrophia(Color color) {
		g.setStroke(new BasicStroke(3.0f));
		drawLine(new Vector(0, 0).multiply(1.5),
						new Vector(0, 0).multiply(-1.5), color);
		g.setStroke(new BasicStroke(1.0f));
	}
	
	@SuppressWarnings("unused")
	private void drawAxes(boolean isNamed, Color color) {
		g.setStroke(new BasicStroke(5.0f));
		
		Vector oX = new Vector(1, 0, 0);
		Vector oY = new Vector(0, 1, 0);
		Vector oZ = new Vector(0, 0, 1);
		
		drawLine(new Vector(), oX, color);
		drawLine(new Vector(), oY, color);
		drawLine(new Vector(), oZ, color);

		g.setStroke(new BasicStroke(1.0f));
		
		if (isNamed) {
			double dim = 0.1;
			
			BufferedImage bi2 = new BufferedImage((int) (bi.getWidth() * (1 + dim)),
					(int) (bi.getHeight() * (1 + dim)), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bi2.createGraphics();
			g2.setColor(color);
			g2.setFont(new Font (Font.DIALOG, 1, (int) (0.03 * size)));
			
			g2.drawImage(bi, null, (int) (dim / 2 * bi.getWidth()), 
					(int) (dim / 2 * bi.getHeight()));
	
			int w = bi2.getWidth();
			int h = bi2.getHeight();
	
			g2.drawString("X", (int) (h / 2 * (oX.getX() * 0.98 + 1)), (int) (w /2 * (oX.getY() * 0.98 + 1)));
			g2.drawString("Y", (int) (h / 2 * (oY.getX() * 0.98 + 1)), (int) (w /2 * (oY.getY() * 0.98 + 1)));
			g2.drawString("Z", (int) (h / 2 * (oZ.getX() * 0.98 + 1)), (int) (w /2 * (oZ.getY() * 0.98 + 1)));

			setBufferedImage(bi2);
		}
	}
	
	private void setBufferedImage(BufferedImage bi) {
		this.bi = bi;
	}
	
	private static Graphics2D getBackgroundedGraphics2D(BufferedImage bi, Color color) {
		Graphics2D g = bi.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		return g;
	}
	
}
