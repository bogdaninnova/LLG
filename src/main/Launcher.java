package main;

import bulkFileEditing.TextWriter;
import main.fields.Anisotrophia;
import main.fields.Circular;
import painting.Draw;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class Launcher {

	public static void main(String...strings) {


		//Archive.bulkWright("C:\\IDEA\\LLG\\res\\circular\\h = 0.01", "0.1 circular");

		//Archive.averrageComponents("C:\\IDEA\\LLG\\res\\circular\\h = 0.1", 0.1);
		//Archive.getMaximalDot("C:\\IDEA\\LLG\\res\\circular\\h = 0.01");


//		character(0.1);
//		character(0.3);
//		character(0.15);

		character(0.15, "circular");
		character(0.25, "circular");

	}

	public static Object[] oneParticle(double theta, double fi, double h, double w, String path) {

		CartesianCalculation c = new CartesianCalculation(
				new Anisotrophia(theta, fi),
				new Circular(w, h));
				//new Lineal( new Vector(1,0,0), w, h));

		//c.run(500, 500);
		c.run();

		new Draw(c.getArray(),
				((Anisotrophia) c.getField(Anisotrophia.class)).getAxe(),
				0.4 * Math.PI, 0.4 * Math.PI, 0, path).drawTraectory(true);

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

		ArrayList<Double> e0List = new ArrayList<Double>();
		ArrayList<Vector> m0List = new ArrayList<Vector>();
		String anis0Path = path + "/h = " + h + ";theta=" + 0.0 + ";fi=" + 0.0;
		createFolder(anis0Path);
		createFolder(anis0Path + "/" + track);
		for (double w = 0.01; w <= 2; w = (round(w + 0.01, 2))) {
			System.out.println("Field Type = " + fieldType +"; h = " + h + "theta = " + 0.0 + ", fi = " + 0.0 + ", w = " + w);
			String track0Name = anis0Path + "/" + track + "/h = "+ h +", theta = " + 0.0 + ", fi = " + 0.0 + ", w = " + w;
			Object[] result = oneParticle(0, 0, h, w, track0Name);
			e0List.add((double)result[0]);
			m0List.add((Vector) result[1]);
			TextWriter.writeDoubleList(e0List, anis0Path + "/Energy");
			TextWriter.writeTraectorysCoordinates(m0List, anis0Path + "/Average M");
		}

		double angleTheta, angleFi;
		for (double fi = 0; fi < 1; fi = round(fi + angleStep, 2))
			for (double theta = angleStep; theta < 1.0; theta = round(theta + angleStep, 2)) {

				if (((theta == 0.5) && (fi == 0)) || ((theta == 0.5) && (fi == 0.5)))
					if (fieldType.equals("lineal"))
						continue;

				angleTheta = Math.acos(2 * theta - 1);
				angleFi = 2 * Math.PI * fi;

				System.out.println(new Date());

				String anisPath = path + "/h = " + h + ";theta=" + angleTheta + ";fi=" + angleFi;
				createFolder(anisPath);
				createFolder(anisPath + "/" + track);
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


		ArrayList<Double> e1List = new ArrayList<Double>();
		ArrayList<Vector> m1List = new ArrayList<Vector>();
		String anis1Path = path + "/h = " + h + ";theta=" + 1.0 + ";fi=" + 0.0;
		createFolder(anis1Path);
		createFolder(anis1Path + "/" + track);

		for (double w = 0.01; w <= 2; w = (round(w + 0.01, 2))) {
			System.out.println("Field Type = " + fieldType +"; h = " + h + "theta = " + 1.0 + ", fi = " + 0.0 + ", w = " + w);
			String track1Name = anis1Path + "/" + track + "/h = "+ h +", theta = " + 1.0 + ", fi = " + 0.0 + ", w = " + w;
			Object[] result = oneParticle(Math.PI, 0, h, w, track1Name);
			e1List.add((double)result[0]);
			m1List.add((Vector) result[1]);
			TextWriter.writeDoubleList(e1List, anis1Path + "/Energy");
			TextWriter.writeTraectorysCoordinates(m1List, anis1Path + "/Average M");
		}
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