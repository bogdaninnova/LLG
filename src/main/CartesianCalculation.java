package main;

import main.fields.Anisotrophia;
import main.fields.Field;

import java.util.ArrayList;

public class CartesianCalculation extends Calculation {

	
	public Vector dM = new Vector();
	public Vector M;

	public ArrayList<Field> fields;
	private PeriodCounter pc = new PeriodCounter();
	public ArrayList<Double> energyList = new ArrayList<Double>();

	public CartesianCalculation(Field... fields) {
		setFields(fields);
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
	
	public void setFields(Field... fields) {
		this.fields = new ArrayList<Field>();
		for (Field field : fields)
			this.fields.add(field);

		if (isContainField(Anisotrophia.class))
			setBeginningLocation(new Vector(0, 0));
		else
			setBeginningLocation(new Vector(((Anisotrophia) getField(Anisotrophia.class)).getAxe()));
	}

	public boolean isContainField(Class fieldClass) {
		for (Field field : fields)
			if (field.getClass().equals(fieldClass))
				return true;
		return false;
	}

	public Field getField(Class fieldClass) {
		for (Field field : fields)
			if (field.getClass().equals(fieldClass))
				return field;
		return null;
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
		Vector MxH = M.crossProduct(getHeff(M, t));
		Vector a_MxHxH = M.crossProduct(MxH).multiply(ALPHA);

		return new Vector(
				(MxH.getX() + a_MxHxH.getX()),
				(MxH.getY() + a_MxHxH.getY()),
				(MxH.getZ() + a_MxHxH.getZ())).
						multiply(-1 / (1 + Math.pow(ALPHA, 2)));
	}

	public Vector getHeff(Vector M, double t) {
		Vector temp = new Vector();

		for (Field field : fields) {
			temp = temp.plus(field.getValue(M, t));
			//temp = temp.plus(field.getDerivative(M, t).multiply(-Constants.taoSigma)); //TODO
		}
		return temp;
	}

}
