package main.fields;

import main.Vector;

public class Lineal extends Field {

	private Vector direction;

	public Lineal(Vector direction, double w, double h) {
		setNewData(w, h, direction);
	}

	public Vector getValue(Vector M, double t) {
		return direction.multiply(h * Math.cos(w * t));
	}

	public Vector getDirection() {
		return direction;
	}

	public void setNewData(double w, double h, Vector direction) {
		this.w = w;
		this.h = h;
		this.direction = direction;
	}
	
}
