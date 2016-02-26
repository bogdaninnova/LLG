package main;

import bulkFileEditing.FolderEditor;
import bulkFileEditing.TextWriter;
import main.fields.Anisotropy;
import main.fields.Circular;
import main.fields.Elliptical;
import main.fields.Lineal;
import painting.Draw;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class Launcher {

	public static void main(String...strings) {


		//String fieldType = "circular";
		String fieldType = "elliptical";
		//String fieldType = "lineal";

		String path = "C:\\IDEA\\LLG\\res\\" + fieldType + "\\h = ";
		double h = 0.001;

		//Archive.createGraphics(path + h);

//		double[] array = new double[]{0.001, 0.005, 0.01, 0.05, 0.1, 0.2, 0.3};
//
//		for (double d : array)
//			FolderEditor.deleteFiles(path + d, ".png", "track");


//		Archive.bulkWright(path + h, "Energy", "Energy, field type = " + fieldType + ", h = " + h);
//		Archive.bulkWright(circularPath + h, "M_x", "M_x h = " + h);
//		Archive.bulkWright(circularPath + h, "M_y", "M_y h = " + h);
		Archive.bulkWright(path + h, "M_z", "M_z h = " + h);

//		ExcelWriter ew = new ExcelWriter();
//		ew.addFewColumns("h = "  + h, Archive.averrageComponents(path + h));
//		ew.write(fieldType + "; h = "  + h);


		character(0.4, fieldType, true);
		character(0.5, fieldType, true);
		character(0.6, fieldType, true);

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

		//c.run(500, 500);
		c.run();

		if (isDraw)
			new Draw(c.getArray(),
					((Anisotropy) c.getField(Anisotropy.class)).getAxe(),
					0.4 * Math.PI, 0.4 * Math.PI, 0, path).drawTraectory(true);

		return new Object[]{c.getEnergy(), c.getM_aver()};
	}


	public static void character (double h, String fieldType, boolean isDraw) {

		double angleStep = 0.05;
		String destination = "res/" + fieldType;
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
		for (double fi = 0; fi < 1; fi = round(fi + angleStep, 2))
			for (double theta = angleStep; theta < 1; theta = round(theta + angleStep, 2)) {

				if (((theta == 0.5) && (fi == 0)) || ((theta == 0.5) && (fi == 0.5)))
					if (fieldType.equals("lineal"))
						continue;

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
		for (double w = 0.01; w <= 2; w = (round(w + 0.01, 2))) {
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