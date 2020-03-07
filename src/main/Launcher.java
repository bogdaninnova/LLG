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

		double theta = Math.PI / 4;
		double phi = Math.PI / 4;
		double h = 0.3;
		double w = 1;

		Date date = new Date();
		CartesianCalculation c = new CartesianCalculation(
				new Anisotropy(theta, phi),
				new Elliptical(1, w, h),
				new Lineal(new Vector(0,0,1), 2, 0.5)
		);
		c.run(1000, 1000);
		System.out.println(new Date().getTime() - date.getTime());
		new Draw(c.getArray(),
				((Anisotropy) c.getField(Anisotropy.class)).getAxe(),
				0.4 * Math.PI, 0.4 * Math.PI, 0, "test").drawTraectory(true);

	}

	public static Object[] oneParticle(boolean isStochastic, String fieldType, double theta, double fi, double h, double w, String path, boolean isDraw) {
		Calculation c;
		switch (fieldType) {
			case "circular" :
				if (isStochastic)
					c = new StochasticCalculation(new Anisotropy(theta, fi), new Elliptical(1, w, h));
				else
					c = new CartesianCalculation(new Anisotropy(theta, fi), new Elliptical(1, w, h));
				break;
			case "lineal" :
				if (isStochastic)
					c = new StochasticCalculation(new Anisotropy(theta, fi), new Elliptical(1, w, h));
				else
					c = new CartesianCalculation(new Anisotropy(theta, fi), new Lineal( new Vector(1,0,0), w, h));
				break;
			case "elliptical" :
				if (isStochastic)
					c = new StochasticCalculation(new Anisotropy(theta, fi), new Elliptical(1, w, h));
				else
					c = new CartesianCalculation(new Anisotropy(theta, fi), new Elliptical(0.5, w, h));
				break;
			default:
				throw new IllegalArgumentException();
		}

		//c.run(0, 100);
		c.run();

		if (isDraw)
			new Draw(c.getArray(), c.getEasyAxe(),
					0.4 * Math.PI, 0.4 * Math.PI, 0, path).drawTraectory(true);

		return new Object[]{c.getEnergy(), c.getM_aver()};
	}


	public static void character (boolean isStochastic, double h, String fieldType, boolean isDraw) {

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
		oneIteration(isStochastic, h, 0, 0, fieldType, anis0Path, track, isDraw);

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
				oneIteration(isStochastic, h, angleTheta, angleFi, fieldType, anisPath, track, isDraw);
			}

		String anis1Path = path + "/h = " + h + ";theta=" + Math.PI + ";fi=" + 0.0;
		FolderEditor.createFolder(anis1Path);
		if (isDraw)
			FolderEditor.createFolder(anis1Path + "/" + track);
		oneIteration(isStochastic, h, Math.PI, 0, fieldType, anis1Path, track, isDraw);
	}

	public static void oneIteration(boolean isStochastic, double h, double angleTheta, double angleFi, String fieldType, String anisPath, String track, boolean isDraw) {
		ArrayList<Double> eList = new ArrayList<>();
		ArrayList<Vector> mList = new ArrayList<>();
		for (double w = 0.1; w <= 2; w = (round(w + 0.01, 2))) {
			System.out.println("Field Type = " + fieldType +"; h = " + h + "theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w);
			String trackName = anisPath + "/" + track + "/h = "+ h +", theta = " + angleTheta + ", fi = " + angleFi + ", w = " + w;
			Object[] result = oneParticle(isStochastic, fieldType, angleTheta, angleFi, h, w, trackName, isDraw);
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
