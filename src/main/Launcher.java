package main;

import bulkFileEditing.FolderEditor;
import bulkFileEditing.TextWriter;
import main.fields.*;
import painting.Draw;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class Launcher {

	public static void main(String...strings) {
//		Calculation.print();
		double theta = Math.PI / 4;
		double phi = 0;
		double h = 0.1;
		double w = 1;

		oneParticle("circular", theta, phi, h, w, "hall\\Decart", true);

		SphericalCalculation spherival = new SphericalCalculation(theta, phi, h, w);
		spherival.run(0, 10);
		new Draw(spherival.getArray(), new Vector(theta, phi),
				0.4 * Math.PI, 0.4 * Math.PI, 0, "hall\\Spherical").drawTraectory(true);

		StochasticCalculation sc = new StochasticCalculation();
		sc.run(0, 10000);
		new Draw(sc.getArray(), new Vector(theta, phi),
				0.4 * Math.PI, 0.4 * Math.PI, 0, "hall\\Stochas").drawTraectory(true);

		CheckStochasticCalculation csc = new CheckStochasticCalculation();
		csc.run(0, 10000);
		new Draw(csc.getArray(), new Vector(theta, phi),
				0.4 * Math.PI, 0.4 * Math.PI, 0, "hall\\Stochas2").drawTraectory(true);



//		Field circular = new Circular(1, 1);
//
//		System.out.println(circular.getValue(null, Math.PI).getX());
//		System.out.println(circular.getValue(null, Math.PI).getY());
//		System.out.println(circular.getValue(null, Math.PI).getZ());
//		System.out.println();
//		System.out.println(circular.getDerivative(null, Math.PI).getX());
//		System.out.println(circular.getDerivative(null, Math.PI).getY());
//		System.out.println(circular.getDerivative(null, Math.PI).getZ());

//		character(0.25, "circular", false);
//		character(0.15, "circular", false);
//		character(0.3, "circular", false);
//		character(0.2, "circular", false);
//


//		String path1 = "angle=1000\\circular\\h = 0.15";
//		String path2 = "angle=1000\\circular\\h = 0.2";
//		String path3 = "angle=1000\\circular\\h = 0.25";
//		String path4 = "angle=1000\\circular\\h = 0.3";
//
//		ExcelWriter ew = new ExcelWriter();
//
//		ew.addFewColumns("0.15", Archive.averrageComponents(path1));
//		ew.addFewColumns("0.2", Archive.averrageComponents(path2));
//		ew.addFewColumns("0.25", Archive.averrageComponents(path3));
//		ew.addFewColumns("0.3", Archive.averrageComponents(path4));
//
//		ew.write("Step=1000");


//		oneIteration(0.3, 0.9272952180016121, 0, "circular", "circular", "track", false);



	}

	public static Object[] oneParticle(String fieldType, double theta, double fi, double h, double w, String path, boolean isDraw) {
		CartesianCalculation c;
		switch (fieldType) {
			case "circular" :
				c = new CartesianCalculation(new Anisotropy(theta, fi), new Circular(w, h));
				break;
			case "lineal" :
				c = new CartesianCalculation(new Anisotropy(theta, fi), new Lineal( new Vector(1,0,0), w, h));
				break;
			case "elliptical" :
				c = new CartesianCalculation(new Anisotropy(theta, fi), new Elliptical(0.5, w, h));
				break;
			default:
				throw new IllegalArgumentException();
		}

		c.run(0, 10);
		//c.run();

		if (isDraw)
			new Draw(c.getArray(),
					((Anisotropy) c.getField(Anisotropy.class)).getAxe(),
					0.4 * Math.PI, 0.4 * Math.PI, 0, path).drawTraectory(true);

		return new Object[]{c.getEnergy(), c.getM_aver()};
	}


	public static void character (double h, String fieldType, boolean isDraw) {

		double angleStep = 0.001;
		String destination = "angle=1000/" + fieldType;
		FolderEditor.createFolder(destination);
		String path = destination + "/h = " + h;
		String track = "track";
		FolderEditor.createFolder(path);

		String anis0Path = path + "/h = " + h + ";theta=" + 0.0 + ";fi=" + 0.0;
		FolderEditor.createFolder(anis0Path);
		if (isDraw)
			FolderEditor.createFolder(anis0Path + "/" + track);
		oneIteration(h, 0, 0, fieldType, anis0Path, track, isDraw);

		double angleTheta, angleFi;
		double fi = 0;//for (double fi = 0; fi < 1; fi = round(fi + angleStep, 2))
			for (double theta = angleStep; theta < 1; theta = round(theta + angleStep, 3)) {

//				if (((theta == 0.5) && (fi == 0)) || ((theta == 0.5) && (fi == 0.5)))
//					if (fieldType.equals("lineal"))
//						continue;

				angleTheta = Math.acos(2 * theta - 1);
				angleFi = 2 * Math.PI * fi;

				System.out.println(new Date());

				String anisPath = path + "/h = " + h + ";theta=" + angleTheta + ";fi=" + angleFi;
				FolderEditor.createFolder(anisPath);
				if (isDraw)
					FolderEditor.createFolder(anisPath + "/" + track);
				oneIteration(h, angleTheta, angleFi, fieldType, anisPath, track, isDraw);
			}

		String anis1Path = path + "/h = " + h + ";theta=" + Math.PI + ";fi=" + 0.0;
		FolderEditor.createFolder(anis1Path);
		if (isDraw)
			FolderEditor.createFolder(anis1Path + "/" + track);
		oneIteration(h, Math.PI, 0, fieldType, anis1Path, track, isDraw);
	}

	public static void oneIteration(double h, double angleTheta, double angleFi, String fieldType, String anisPath, String track, boolean isDraw) {
		ArrayList<Double> eList = new ArrayList<>();
		ArrayList<Vector> mList = new ArrayList<>();
		for (double w = 0.1; w <= 2; w = (round(w + 0.01, 2))) {
			System.out.println("Field Type = " + fieldType +"; h = " + h + "theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w);
			String trackName = anisPath + "/" + track + "/h = "+ h +", theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w;
			Object[] result = oneParticle(fieldType, angleTheta, angleFi, h, w, trackName, isDraw);
			eList.add((double) result[0]);
			mList.add((Vector) result[1]);
		}
		TextWriter.writeDoubleList(eList, anisPath + "/Energy");
		TextWriter.writeTraectorysCoordinates(mList, anisPath + "/Average M");
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}