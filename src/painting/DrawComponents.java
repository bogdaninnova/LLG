package painting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import main.CartesianCalculation;
import main.Vector;
import main.fields.Impuls;

public class DrawComponents {

	private final static int sizeW = 2000;
	private final static int sizeH = 1000;

	public static void draw(CartesianCalculation c, String name) {
		BufferedImage bi = new BufferedImage(sizeW, sizeH,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = Draw.getBackgroundedGraphics2D(bi, Color.white);

		 g.setStroke(new BasicStroke(3));
		wright(getComponentsList(c.getArray(), "x"), g, Color.BLUE);
		wright(getComponentsList(c.getArray(), "y"), g, Color.RED);
		wright(getComponentsList(c.getArray(), "z"), g, Color.GREEN);

		//if (c.impulsArray.size() != 0) wright(getComponentsList(c.impulsArray, "x"), g, Color.BLACK);

		g.setColor(Color.BLACK);
		g.drawLine(0, sizeH/2, sizeW, sizeH/2);

		String isTaoSigma = "+";

		double h = 0;
		
		if (c.fieldsList.isContain(Impuls.class))
			h =  ((Impuls) c.fieldsList.get(Impuls.class)).getH();

		Draw.save(bi, new File(name + ".png"));
	}

	private static LinkedList<Double> getComponentsList(List<Vector> list, String coord) {
		LinkedList<Double> result = new LinkedList<Double>();

		switch (coord) {
			case "x" : for (Vector d : list) result.add(d.getX()); break;
			case "y" : for (Vector d : list) result.add(d.getY()); break;
			case "z" : for (Vector d : list) result.add(d.getZ()); break;
		}
		return result;
	}

	public static void wright(List<Double> list, String name) {
		BufferedImage bi = new BufferedImage(sizeW, sizeH,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = Draw.getBackgroundedGraphics2D(bi, Color.white);
		g.setStroke(new BasicStroke(3));
		wright(list, g, Color.BLACK);
		Draw.save(bi, new File(name + ".png"));
	}

	private static void wright(List<Double> list, Graphics2D g, Color color) {

		ListIterator<Double> iter = list.listIterator();
		double y1 = iter.next();
		double y2 = iter.next();

		double step = (double) sizeW / (double) list.size();

		double x1 = 0;
		double x2 = step;

		g.setColor(color);
		while (iter.hasNext()) {
			g.drawLine(
					(int) x1, (int) Math.abs(sizeH /2 * (y1 - 1)),
					(int) x2, (int) Math.abs(sizeH /2 * (y2 - 1)));

			x1 = x2;
			x2 += step;

			y1 = y2;
			y2 = iter.next();

		}
	}


	public static void normal(List<Double> list, String name) {

		List<Double> tempList = new ArrayList<Double>();
		List<Double> newList = new ArrayList<Double>();

		double min = getMin(list);
		double max = getMax(list);

		double normal = sizeH / (max - min);


		for (double e : list)
			tempList.add(e * normal);

		double maxTemp = getMax(tempList);


		//final int div = list.size() / 2;
		//int counter = 0;

		for (double e : tempList)
		//	if (++counter > div)
				newList.add(-e + maxTemp);

		BufferedImage bi = new BufferedImage(sizeW, sizeH,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = Draw.getBackgroundedGraphics2D(bi, Color.white);
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.BLACK);



		ListIterator<Double> iter = newList.listIterator();
		double y1 = iter.next();
		double y2 = iter.next();

		double step = (double) sizeW / (double) newList.size();

		double x1 = 0;
		double x2 = step;


		while (iter.hasNext()) {
			g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

			x1 = x2;
			x2 += step;

			y1 = y2;
			y2 = iter.next();

		}


		Draw.save(bi, new File(name + "(max: " +max+ ", min: " +min+ ").png"));
	}

	private static double getMax(List<Double> list) {
		double max = - 8888;
		for (double e : list)
			if (e > max) max = e;
		return max;
	}

	private static double getMin(List<Double> list) {
		double min = 8888;
		for (double e : list)
			if (e < min) min = e;
		return min;
	}


}
