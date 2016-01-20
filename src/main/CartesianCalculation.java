package main;

import main.fields.Anisotrophia;
import main.fields.EffectiveField;
import main.fields.Field;

import java.util.ArrayList;

public class CartesianCalculation extends Calculation {

	
	public Vector dM = new Vector();
	public Vector M;

	public EffectiveField fieldsList = new EffectiveField();

	private PeriodCounter pc = new PeriodCounter();
	public ArrayList<Double> energyList = new ArrayList<Double>();

	public CartesianCalculation(Field... fields) {
		updateFields(fields);
		update();
	}
	
	public double getEnergy() {
		return pc.getEnergy();
	}

	public Vector getM_aver() {
		return pc.getM_aver();
	}

	private void update() {
		pc.externalReset(this);
		array = new ArrayList<Vector>();
		t = 0;
	}
	
	public void updateFields(Field... fields) {
		for (Field field : fields)
			fieldsList.add(field);
		if (!fieldsList.isContain(Anisotrophia.class))
			setBeginningLocation(new Vector(0, 0));
		else
			setBeginningLocation(new Vector(((Anisotrophia) fieldsList.get(Anisotrophia.class)).getAxe()));

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
	
	public void run() {
		update();
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

	protected void wait(double waitingTime) {
		while (t < waitingTime) 
			iteration();
	}

	protected void iteration() {
		dM = getdM(M);
		M = M.plus(dM);
		t += dt;
	}
	
	private Vector getdM(Vector M) {
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

	private Vector LLG(double mx, double my, double mz, double t) {
		Vector M = new Vector(mx, my, mz);
		Vector MxH = M.crossProduct(fieldsList.getHeff(M, t));
		Vector a_MxHxH = M.crossProduct(MxH).multiply(ALPHA);

		return new Vector(
				(MxH.getX() + a_MxHxH.getX()),
				(MxH.getY() + a_MxHxH.getY()),
				(MxH.getZ() + a_MxHxH.getZ())).
						multiply(-1 / (1 + Math.pow(ALPHA, 2)));
	}

}
