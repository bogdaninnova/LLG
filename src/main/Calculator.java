package main;

import main.fields.Anisotrophia;
import main.fields.EffectiveField;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Calculator {

	public static double t;
	public static double dt = Constants.dt;
	public Vector dM = new Vector();
	public Vector M;

	public List<Double> lsList = new ArrayList<Double>();
	private List<Double> lfList = new ArrayList<Double>();
	
	private int counter = 0;
	
	
	public static EffectiveField fieldsList = new EffectiveField();

	private LinkedList<Vector> array;
	private PeriodCounter pc = new PeriodCounter();
	public ArrayList<Double> energyList = new ArrayList<Double>();

	
	public Vector startVector = new Vector(0, 0, 1);

	public double w;




	public Calculator() {
		if (!fieldsList.isContain(Anisotrophia.class))
			setBeginningLocation(new Vector(0, 0));
		else
			setBeginningLocation(new Vector(((Anisotrophia) fieldsList.get(Anisotrophia.class)).getAxe()));
		update();
	}

	public double getEnergy() {
		return pc.getEnergy();
	}

	public Vector getM_aver() {
		return pc.getM_aver();
	}

	private void update() {
		pc.reset(w);
		array = new LinkedList<Vector>();
		t = 0;
		setBeginningLocation(startVector);
	}

	private void setBeginningLocation(Vector vector) {
		M = vector;
	}

	public void run(double waitingTime, double workingTime) {
		update();
		wait(waitingTime);
		while (true) {
			iteration();
			pc.update(this);
			array.add(new Vector(M));
			if (t >  waitingTime + workingTime)
				break;
		}
	}
	
	
//	private void orbitSeparationMethod(Vector M) {
//		Vector M1 = M.clone();
//		Vector M2 = Pertrubator.getNewPosition(M1);
//		M1 = M1.plus(getdM(M1));
//		M2 = M2.plus(getdM(M2));
//		lfList.add(Pertrubator.getLcurrent(M1, M2));
//		double summ = 0;
//		
//		for (Double a : lfList)
//			summ += a;
//		
//		lsList.add(summ / lfList.size() / dt);
//	}

	public void run() {
		update();
		setBeginningLocation(startVector);
		wait(300d);
		while (true) {
			iteration();
			
			pc.update(this);
			if (pc.isOver())
				break;
		}
		for (Vector vector : pc.list)
			array.add(vector);
	}



	private void wait(double waitingTime) {
		while (t < waitingTime) 
			iteration();
	}

	public LinkedList<Vector> getArray() {
		return array;
	}

	private void iteration() {
		dM = getdM(M);
		M = M.plus(dM);
		t += dt;
	}
	
	private static Vector getdM(Vector M) {
		Vector d1, d2, d3, d4;
		d1 = LLG(M.getX(), M.getY(), M.getZ(), t);
		d2 = LLG(M.getX()+dt/2*d1.getX(), M.getY()+dt/2*d1.getY(), M.getZ()+dt/2*d1.getZ(), t+dt/2);
		d3 = LLG(M.getX()+dt/2*d2.getX(), M.getY()+dt/2*d2.getY(), M.getZ()+dt/2*d2.getZ(), t+dt/2);
		d4 = LLG(M.getX()+dt/1*d3.getX(), M.getY()+dt/1*d3.getY(), M.getZ()+dt/1*d3.getZ(), t+dt/1);

		return new Vector(
				dt/6 * (d1.getX() + 2 * d2.getX() + 2 * d3.getX() + d4.getX()),
				dt/6 * (d1.getY() + 2 * d2.getY() + 2 * d3.getY() + d4.getY()),
				dt/6 * (d1.getZ() + 2 * d2.getZ() + 2 * d3.getZ() + d4.getZ()));
	}

	private static Vector LLG(double mx, double my, double mz, double t) {
		Vector M = new Vector(mx, my, mz);
		Vector MxH = M.crossProduct(fieldsList.getHeff(M, t));
		Vector a_MxHxH = M.crossProduct(MxH).multiply(Constants.alpha);

		return new Vector(
				(MxH.getX() + a_MxHxH.getX()),
				(MxH.getY() + a_MxHxH.getY()),
				(MxH.getZ() + a_MxHxH.getZ())).
						multiply(-1 / (1 + Math.pow(Constants.alpha, 2)));
	}

}
