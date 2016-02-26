package painting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;

import main.Vector;

public class Draw {

	private final static int size = 1000;
	
	private List<Vector> list;
	
	private double xAngle; 
	private double yAngle; 
	private double zAngle;
	private String name;
	private BufferedImage bi;
	private Graphics2D g;

	private ArrayList<Vector> lastList = new ArrayList<Vector>();
	private Vector easyAxe;
	
	public Draw(List<Vector> list, Vector easyAxe, double xAngle, double yAngle, double zAngle, String name) {
		this.list = list;
		this.easyAxe = easyAxe;
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
		this.name = name;
				
		setBufferedImage(new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR));
		this.g = getBackgroundedGraphics2D(bi, Color.white);
	}
	
	public BufferedImage drawTraectory(boolean isSave) {

			wrightCircle(Color.red);
			wrightAxeOfAnisotrophia(Color.green);
			wrightSecondCircle(Color.red);

			wright(rotate(list), Color.BLUE);			
			drawAxes(true, Color.black);
			//writeLegend();
			
			if (isSave)
				save(bi, new File(name +".PNG"));
			
			return bi;
	}
	
	public void update() {
		setBufferedImage(new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR));
		g = getBackgroundedGraphics2D(bi, Color.white);
		//wrightCircle(Color.red);
		//wrightAxeOfAnisotrophia(Color.green);
		//wrightSecondCircle(Color.red);
		//drawAxes(false, Color.black);
		
		
	}
	
	
	public void drawAnimationTraectory() {

		ListIterator<Vector> iter = list.listIterator();
		
		int counter = 0;
		int imageCounter = 0;
		ArrayList<Vector> tempList = new ArrayList<Vector>();
		
		while (iter.hasNext()) {
			Vector temp = iter.next();
			tempList.add(new Vector(temp));
			lastList.add(new Vector(temp));
			if (counter++ == 50) {
				System.out.println(++imageCounter);
				counter = 0;
				update();
				wright(rotate(lastList), Color.CYAN);
				g.setStroke(new BasicStroke(2.0f));
				drawLine(tempList.get(tempList.size() - 1).clone().rotate(xAngle, yAngle, zAngle), Color.BLUE);
				g.setStroke(new BasicStroke(1.0f));
				
				save(bi, new File(new Date().getTime() +".PNG"));
				tempList.clear();
			}
		}
	}
	
	
	public void drawAnimationTraectoryWithoutTracing() {

		ListIterator<Vector> iter = list.listIterator();
		
		int counter = 0;
		int imageCounter = 0;
		LinkedList<Vector> tempList = new LinkedList<Vector>();
		
		for (Vector vec : list)
			lastList.add(vec);
		
		while (iter.hasNext()) {
			Vector temp = iter.next();
			tempList.add(new Vector(temp));
			if (counter++ == 100) {
				System.out.println(++imageCounter);
				counter = 0;
				update();
				wright(rotate(list), Color.CYAN);
				g.setStroke(new BasicStroke(2.0f));
				drawLine(tempList.getLast().rotate(xAngle, yAngle, zAngle), Color.BLUE);
				g.setStroke(new BasicStroke(1.0f));
				
				save(bi, new File(new Date().getTime() +".PNG"));
				tempList.clear();
			}
		}
	}
		
	
	private void wrightTraectory(int flushSize, Color color) {
		List<Vector> originalList = list;
		ArrayList<Vector> list;
		while (true) {
			list = new ArrayList<Vector>();
			if (originalList.size() < flushSize) {
				wright(originalList, color);
				break;
			}
			
			for (Vector v : originalList.subList(0, flushSize))
				list.add(v.clone());
			originalList = originalList.subList(flushSize, originalList.size() - 1);

			wright(list, color);
		}
	}
	
	
		
	private ArrayList<Vector> rotate(List<Vector> list2) {
		ArrayList<Vector> newArray = new ArrayList<Vector>();
		ListIterator<Vector> iterator = list2.listIterator();
		Vector dot;
		while (iterator.hasNext()) 
			newArray.add(iterator.next().rotate(xAngle, yAngle, zAngle));

		return newArray;
	}
	

	
	private void wright(List<Vector> array, Color color) {
		
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
		
		ArrayList<Vector> array = new ArrayList<Vector>();
		for (double x = -1; x <= 1; x += 0.01) 
			array.add(new Vector(x, Math.sqrt(1 - x*x), 0));
		for (double x = -1; x <= 1; x += 0.01) 
			array.add(new Vector(-x, -Math.sqrt(1 - x*x), 0));
		array.add(new Vector(-1, 0, 0));
		array.add(new Vector(-1.01, Math.sqrt(1 - 1.01*1.01), 0));
		
		wright(rotate(array), color);

		g.setStroke(new BasicStroke(1.0f));
	}

	@SuppressWarnings("unused")
	private void wrightDot(double r, Vector dot, Color color) {
		g.setStroke(new BasicStroke(7.0f));
		ArrayList<Vector> array = new ArrayList<Vector>();
		for (double x = dot.getX() - r; x <= dot.getX() + r; x += r/4)
			for (double y = dot.getY() - r; y <= dot.getY() + r; y += r/4)
				for (double z = dot.getZ() - r; z <= dot.getZ() + r; z += r/4)
					if (Math.pow(x - dot.getX(), 2) + 
							Math.pow(y - dot.getY(), 2) + 
							Math.pow(z - dot.getZ(), 2) < r*r)
						array.add(new Vector(x, y, z));

		wright(rotate(array), color);
		g.setStroke(new BasicStroke(1.0f));
	}
	
	private void writeLegend() {
		BufferedImage bi2 = new BufferedImage(bi.getWidth(), (int) (bi.getHeight() * 1.3),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = Draw.getBackgroundedGraphics2D(bi2, Color.white);
		
		g2.setColor(Color.BLACK);
		Font font=new Font (Font.DIALOG, 1, (int) (0.03 * size));
		g2.setFont(font);
		
		g2.drawImage(bi, null, 0, 0);
		
		double w = 0;
		double h = 0;
		
//		if (c.fieldsList.isContain(Circular.class)) {
//			w = ((Circular)  c.fieldsList.get(Circular.class)).getW();
//			h = ((Circular)  c.fieldsList.get(Circular.class)).getH();
//		}

		
		g2.drawString("w: " + w, size/10, size + 4 * size/20);
		g2.drawString("h: " + h, size/10, size + 5 * size/20);
		
		g2.drawString("xAngle: " + xAngle / Math.PI + " Pi", size/5 + size / 2, size + 3 * size/20);
		g2.drawString("yAngle: " + yAngle / Math.PI + " Pi", size/5 + size / 2, size + 4 * size/20);
		g2.drawString("zAngle: " + zAngle / Math.PI + " Pi", size/5 + size / 2, size + 5 * size/20);
		
//		g2.drawString("Waiting: " + c.waitingTime +"; Repeating: "
//			+ c.getObserverValue(PeriodCounter.class)+ " / " + c.workingTime,
//			size/10, size + 7 * size/20);
		
		g2.drawString(
				"Date: " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(new Date()),
				size/10, size + 8 * size/20);
		setBufferedImage(bi2);
	}
	
	public static void save(BufferedImage bi, File file) {
		try {
			ImageIO.write(bi, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawLine(Vector dot, Color color) {
		g.setColor(color);

		ArrayList<Vector> array = new ArrayList<Vector>();
		array.add(dot);
		array.add(new Vector());
		array.add(new Vector());

		wright(array, color);
	}
	
	private void wrightAxeOfAnisotrophia(Color color) {
		if (easyAxe != null) {
			g.setStroke(new BasicStroke(3.0f));
			drawLine(easyAxe.rotate(xAngle, yAngle, zAngle).multiply(-1.5), color);
			drawLine(easyAxe.rotate(xAngle, yAngle, zAngle).multiply(1.5), color);
			g.setStroke(new BasicStroke(1.0f));
		}
	}
	
	private void drawAxes(boolean isNamed, Color color) {
		g.setStroke(new BasicStroke(5.0f));
		
		Vector oX = new Vector(1, 0, 0).rotate(xAngle, yAngle, zAngle);
		Vector oY = new Vector(0, 1, 0).rotate(xAngle, yAngle, zAngle);
		Vector oZ = new Vector(0, 0, 1).rotate(xAngle, yAngle, zAngle);
				
		drawLine(oX, color);
		drawLine(oY, color);
		drawLine(oZ, color);

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
	
	public static Graphics2D getBackgroundedGraphics2D(BufferedImage bi, Color color) {
		Graphics2D g = bi.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		return g;
	}

}
