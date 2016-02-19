package main;

import bulkFileEditing.ExcelWriter;
import bulkFileEditing.TextWriter;
import main.fields.Anisotrophia;
import main.fields.Elliptical;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Launcher {

	public static void main(String...strings) {
//		String linealPath = "D:\\Downloads\\lineal\\lineal\\h = ";
		String circularPath = "C:\\IDEA\\LLG\\res\\lineal\\h = ";
//		String circularPath = "C:\\IDEA\\LLG\\res\\circular\\h = 0.05";
//		Archive.getMaximalDot(linealPath + 0.01);


		double h = 0.05;

	//double w = 0.69	;
//		for (double w = 0.69; w < 0.71; w = (round(w + 0.001, 3)))
//			oneParticle(Math.acos(2 * 0.85 - 1), 0.25 * 2 * Math.PI, 0.01, w, "w=" + w);


//		Archive.bulkWright(circularPath + h, "Energy", "Energy h = " + h);
//		Archive.bulkWright(circularPath + h, "M_x", "M_x h = " + h);
//		Archive.bulkWright(circularPath + h, "M_y", "M_y h = " + h);
//		Archive.bulkWright(circularPath + h, "M_z", "M_z h = " + h);
////
////
////
//		ExcelWriter ew = new ExcelWriter();
//		ew.addFewColumns("all", Archive.averrageComponents(circularPath + h));
//		ew.write("h = "  + h);




		character(0.01, "elliptical");
		character(0.05, "elliptical");
		character(0.001, "elliptical");
		character(0.005, "elliptical");

//		character(0.05, "lineal");

	}


	public static void writeInExel() {
		ExcelWriter excelWriter = new ExcelWriter();

		String linealPath = "D:\\Downloads\\lineal\\lineal\\h = ";
		String circularPath = "D:\\Downloads\\circular\\circular\\h = ";
		for (double h = 0.05; h <= 0.3;  h = round(h + 0.05, 2)) {
			//Archive.bulkWright(linealPath + h, h + " lineal");

			excelWriter.addFewColumns("h="+h, Archive.averrageComponents(linealPath + h));
		}
		excelWriter.addFewColumns("h=0.01", Archive.averrageComponents(linealPath + 0.01));
		excelWriter.write("Lineal");
	}

	public static Object[] oneParticle(double theta, double fi, double h, double w, String path) {

		CartesianCalculation c = new CartesianCalculation(
				new Anisotrophia(theta, fi),
				//new Circular(w, h));
				new Elliptical(0.5, w, h));
				//new Lineal( new Vector(1,0,0), w, h));

		//c.run(500, 500);
		c.run();

//		new Draw(c.getArray(),
//				((Anisotrophia) c.getField(Anisotrophia.class)).getAxe(),
//				0.4 * Math.PI, 0.4 * Math.PI, 0, path).drawTraectory(true);

		Object[] result = {c.getEnergy(), c.getM_aver()};

		return result;
	}


	public static void character (double h, String fieldType) {

		double angleStep = 0.05;
		String destination = "res/" + fieldType;
		createFolder(destination);
		String path = destination + "/h = " + h;
		String track = "track";
		createFolder(path);


		String anis0Path = path + "/h = " + h + ";theta=" + 0.0 + ";fi=" + 0.0;
		createFolder(anis0Path);
		createFolder(anis0Path + "/" + track);
		oneIteration(h, 0, 0, fieldType, anis0Path, track);

		double angleTheta, angleFi;
		for (double fi = 0; fi < 1; fi = round(fi + angleStep, 2))
			for (double theta = angleStep; theta < 1; theta = round(theta + angleStep, 3)) {

//				if (((theta == 0.5) && (fi == 0)) || ((theta == 0.5) && (fi == 0.5)))
//					if (fieldType.equals("lineal"))
//						continue;

				angleTheta = Math.acos(2 * theta - 1);
				angleFi = 2 * Math.PI * fi;

				System.out.println(new Date());

				String anisPath = path + "/h = " + h + ";theta=" + angleTheta + ";fi=" + angleFi;
				createFolder(anisPath);
				createFolder(anisPath + "/" + track);
				oneIteration(h, angleTheta, angleFi, fieldType, anisPath, track);
			}



		String anis1Path = path + "/h = " + h + ";theta=" + Math.PI + ";fi=" + 0.0;
		createFolder(anis1Path);
		createFolder(anis1Path + "/" + track);
		oneIteration(h, Math.PI, 0, fieldType, anis1Path, track);
	}



	public static void characterRandom (double h, String fieldType) {
		String destination = "res/" + fieldType;
		createFolder(destination);
		String path = destination + "/h = " + h;
		String track = "track";
		createFolder(path);
		int k = 400;
		Random rand = new Random();
		double angleTheta, angleFi;
		while(k --> 0) {
				angleTheta = Math.acos(2 * rand.nextDouble() - 1);
				angleFi = 2 * Math.PI * rand.nextDouble();
				System.out.println(k);
				String anisPath = path + "/h = " + h + ";theta=" + angleTheta + ";fi=" + angleFi;
				createFolder(anisPath);
				createFolder(anisPath + "/" + track);
				oneIteration(h, angleTheta, angleFi, fieldType, anisPath, track);
			}
	}



	public static void oneIteration(double h, double angleTheta, double angleFi, String fieldType, String anisPath, String track) {
		ArrayList<Double> eList = new ArrayList<Double>();
		ArrayList<Vector> mList = new ArrayList<Vector>();
		for (double w = 0.01; w <= 2; w = (round(w + 0.01, 2))) {
			System.out.println("Field Type = " + fieldType +"; h = " + h + "theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w);
			String trackName = anisPath + "/" + track + "/h = "+ h +", theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w;
			Object[] result = oneParticle(angleTheta, angleFi, h, w, trackName);
			eList.add((double)result[0]);
			mList.add((Vector) result[1]);
		}
		TextWriter.writeDoubleList(eList, anisPath + "/Energy");
		TextWriter.writeTraectorysCoordinates(mList, anisPath + "/Average M");

	}


	public static void oneIteration05(double h, double angleTheta, double angleFi, String fieldType, String anisPath, String track) {
		ArrayList<Double> eList = new ArrayList<Double>();
		ArrayList<Vector> mList = new ArrayList<Vector>();
		for (double w = 0.5; w <= 1.5; w = (round(w + 0.01, 2))) {
			System.out.println("Field Type = " + fieldType +"; h = " + h + "theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w);
			String trackName = anisPath + "/" + track + "/h = "+ h +", theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w;
			Object[] result = oneParticle(angleTheta, angleFi, h, w, trackName);
			eList.add((double)result[0]);
			mList.add((Vector) result[1]);
		}
		TextWriter.writeDoubleList(eList, anisPath + "/Energy");
		TextWriter.writeTraectorysCoordinates(mList, anisPath + "/Average M");

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
}