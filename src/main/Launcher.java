package main;

import bulkFileEditing.DrawQW;
import bulkFileEditing.TextWriter;
import main.fields.Anisotrophia;
import main.fields.EffectiveField;
import main.fields.Lineal;
import painting.Draw;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Launcher {

	public static void main(String...strings) {

	}


	public static void character (double h) {

		double angleStep = 0.05;

		String destination = "res/angle_step = 0.05";
		createFolder(destination);
		String path = destination + "/h = " + h;
		String track = "track";
		createFolder(path);

		ArrayList<Double> e0List = new ArrayList<Double>();
		ArrayList<Vector> m0List = new ArrayList<Vector>();
		String anis0Path = path + "/h = " + h + ";theta=" + 0.0 + ";fi=" + 0.0;
		createFolder(anis0Path);
		createFolder(anis0Path + "/" + track);
		for (double w = 0.01; w <= 2; w = (round(w + 0.01, 2))) {
			System.out.println("h = " + h + "theta = " + 0.0 + ", fi = " + 0.0 + ", w = " + w);
			String track0Name = anis0Path + "/" + track + "/h = "+ h +", theta = " + 0.0 + ", fi = " + 0.0 + ", w = " + w;
			Object[] result = oneParticle(0, 0, h, w, track0Name);
			e0List.add((double)result[0]);
			m0List.add((Vector) result[1]);
			TextWriter.writeDoubleList(e0List, anis0Path + "/Energy");
			TextWriter.writeTraectorysCoordinates(m0List, anis0Path + "/Average M");
		}

		for (double fi = 0; fi < 1; fi = round(fi + angleStep, 2))
			for (double theta = angleStep; theta < 1; theta = round(theta + angleStep, 2)) {
				System.out.println(new Date());
				if ((theta == 0.5) && (fi == 0))
					continue;
				String anisPath = path + "/h = " + h + ";theta=" + theta + ";fi=" + fi;
				createFolder(anisPath);
				createFolder(anisPath + "/" + track);
				ArrayList<Double> eList = new ArrayList<Double>();
				ArrayList<Vector> mList = new ArrayList<Vector>();
				for (double w = 0.01; w <= 2; w = (round(w + 0.01, 2))) {
					System.out.println("h = " + h + "theta = " + theta + ", fi = " + fi + ", w = " + w);
					String trackName = anisPath + "/" + track + "/h = "+ h +", theta = " + theta + ", fi = " + fi + ", w = " + w;
					Object[] result = oneParticle(theta, fi, h, w, trackName);
					eList.add((double)result[0]);
					mList.add((Vector) result[1]);
					TextWriter.writeDoubleList(eList, anisPath + "/Energy");
					TextWriter.writeTraectorysCoordinates(mList, anisPath + "/Average M");
				}
			}

		ArrayList<Double> e1List = new ArrayList<Double>();
		ArrayList<Vector> m1List = new ArrayList<Vector>();
		String anis1Path = path + "/h = " + h + ";theta=" + 1.0 + ";fi=" + 0.0;
		createFolder(anis1Path);
		createFolder(anis1Path + "/" + track);
		for (double w = 0.01; w <= 2; w = (round(w + 0.01, 2))) {
			System.out.println("h = " + h + "theta = " + 1.0 + ", fi = " + 0.0 + ", w = " + w);
			String track1Name = anis1Path + "/" + track + "/h = "+ h +", theta = " + 1.0 + ", fi = " + 0.0 + ", w = " + w;
			Object[] result = oneParticle(1, 0, h, w, track1Name);
			e1List.add((double)result[0]);
			m1List.add((Vector) result[1]);
			TextWriter.writeDoubleList(e1List, anis1Path + "/Energy");
			TextWriter.writeTraectorysCoordinates(m1List, anis1Path + "/Average M");
		}
	}


	public static void Q_w(double h, double w1, double w2) {
		Calculator c = new Calculator();
		ArrayList<Double> wList = new ArrayList<Double>();
		ArrayList<Vector> mList = new ArrayList<Vector>();
		for (double w = w1; w <= w2; w = round(w + 0.01, 2)) {
			double wSum = 0;
			Vector m_aver = new Vector();
			double counter = 0;
			for (double theta = 0.0; theta <= 1; theta = round(theta + 0.05, 2)) {
				System.out.println("\n" + new Date());
				System.out.println("h = " + h + ". W = " + w + ". Theta: " + theta);
				for (double fi = 0; fi < 1; fi = round(fi + 0.05, 2)) {

					if (theta == 0.5)
						if ((fi == 0) || (fi == 0.5) || (fi == 1)) {
							counter++;
							continue;
						}
					c.w = w;
					c.startVector = new Vector(Math.acos(2 * theta - 1), 2 * Math.PI * fi);
					Calculator.fieldsList = new EffectiveField();
					Calculator.fieldsList.add(new Anisotrophia(Math.acos(2 * theta - 1), 2 * Math.PI * fi));
					Calculator.fieldsList.add(new Lineal(new Vector(1, 0, 0), w, h));
					c.run();
					wSum += c.getEnergy();
					m_aver = m_aver.plus(c.getM_aver());
					counter++;
				}
			}
			mList.add(m_aver.multiply(1 / counter));
			wList.add(wSum / counter);

			TextWriter.writeDoubleList(wList, "Energy h = " + h + ". From w = " + w1 + " to w = " + w2);
			TextWriter.writeTraectorysCoordinates(mList, "M_aver h = " + h + ". From w = " + w1 + " to w = " + w2);
		}
	}



	public static Object[] oneParticle(double theta, double fi, double h, double w, String path) {

		//Date start = new Date();



		Calculator.fieldsList = new EffectiveField();
		Calculator.fieldsList.add(new Anisotrophia(Math.PI * theta, 2 * Math.PI * fi));
		Calculator.fieldsList.add(new Lineal(new Vector(1, 0, 0), w, h));

		Calculator c = new Calculator();
		c.w = w;
		c.startVector = new Vector(Math.PI * theta, 2 * Math.PI * fi);
		//c.run(800, 500);
		c.run();

		new Draw(c, 0.4 * Math.PI, 0.4 * Math.PI, 0, path).drawTraectory(true);

		Object[] result = {c.getEnergy(), c.getM_aver()};

		return result;
	}


	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


	public static void createFolder(String folderName) {
		File theDir = new File(folderName);
		if (!theDir.exists()) {
			theDir.mkdir();
		}
	}

	public static void averrageComponents(double h) throws NumberFormatException {

		//System.out.println(hFolder);
		String path = "res\\plain\\h = " + h;

		File folder = new File(path);
		String[] names = folder.list();

		ArrayList<Double> listX = new ArrayList<Double>();
		ArrayList<Double> listY = new ArrayList<Double>();
		ArrayList<Double> listZ = new ArrayList<Double>();
		ArrayList<Double> listE = new ArrayList<Double>();
		for (int i = 0; i < 200; i++) {
			listX.add(0d);
			listY.add(0d);
			listZ.add(0d);
			listE.add(0d);
		}
		int counter = 40;
		System.out.println();
		for(String name : names) {
			File file = new File(path + "/" + name);
			String[] cons = file.list();
			for(String c : cons) {

				if (c.contains("M_x")) {
					if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
						listX = addLists(listX, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
					} else {
						listX = addLists(listX, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
					}
				}
				if (c.contains("M_y")) {
					if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
						listY = addLists(listY, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
					} else {
						listY = addLists(listY, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
					}
				}
				if (c.contains("M_z")) {
					counter++;
					if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
						listZ = addLists(listZ, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
					} else {
						listZ = addLists(listZ, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
					}
				}

				if (c.contains("Energy")) {
					counter++;
					if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
						listE = addLists(listE, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
					} else {
						listE = addLists(listE, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
					}
				}


			}
		}
		TextWriter.writeDoubleList(multiple(listX, 1 / (double) counter), h + " Averrage X");
		TextWriter.writeDoubleList(multiple(listY, 1 / (double) counter), h + " Averrage Y");
		TextWriter.writeDoubleList(multiple(listZ, 1 / (double) counter), h + " Averrage Z");
		TextWriter.writeDoubleList(multiple(listE, 1 / (double) counter), h + " Averrage E");

	}



	private static ArrayList<Double> multiple(ArrayList<Double> list, double num) {
		ArrayList<Double> newList = new ArrayList<Double>();
		Iterator<Double> iter = list.iterator();
		while (iter.hasNext())
			newList.add(iter.next() * num);
		return newList;
	}


	private static ArrayList<Double> addLists(ArrayList<Double> list1, ArrayList<Double> list2) {

		if (list1.size() != list2.size())
			return null;
		ArrayList<Double> list = new ArrayList<Double>();
		Iterator<Double> iter1 = list1.iterator();
		Iterator<Double> iter2 = list2.iterator();
		while (iter1.hasNext())
			list.add(iter1.next() + iter2.next());
		return list;
	}

}