package main.fields;

import main.Vector;

public abstract class Field {
	
	private double dt = Math.pow(10, -8);
	
	public abstract Vector getValue(Vector M, double t);
	
	public Vector getDerivative(Vector M, double t) {
		Vector dF2 = getValue(M, t + dt);
		Vector dF1 = getValue(M, t);
		dF2 = dF2.plus(dF1.multiply(-1));
		return dF2.multiply(1 / dt);
	}

	public abstract Double getW();
	
}
